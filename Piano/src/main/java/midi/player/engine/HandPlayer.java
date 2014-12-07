package midi.player.engine;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.Slider;
import javafx.util.Duration;
import midi.common.data.events.Note;
import midi.player.engine.components.BaseGraphicComponent;
import midi.player.engine.components.BaseMusicComponent;
import midi.player.engine.components.GraphicComponent;
import midi.player.engine.components.MusicComponent;
import midi.player.engine.listeners.timelinelisteners.TimelineChangedListener;
import midi.player.engine.midiinfo.Accord;
import midi.player.engine.midiinfo.Hand;
import midi.player.gui.piano.PianoPresenter;

public class HandPlayer implements Player {


    private PianoPresenter presenter;
    private Hand hand;
    private Timeline timeline;
    BaseMusicComponent music;
    BaseGraphicComponent graphic;

    public HandPlayer(PianoPresenter presenter, Hand hand) {
        this.presenter = presenter;
        this.hand = hand;
        music = MusicComponent.getInstance();
        graphic = new GraphicComponent(presenter);
        resetTimeline();
    }

    @Override
    public Timeline getTimeline() {
        return timeline;
    }

    @Override
    public void preparePlay() {
        if (timeline.getStatus().equals(Animation.Status.STOPPED)) {
            Platform.runLater(() -> hand.get(0).forEach(graphic::playNext));
        }
    }

    @Override
    public void play() {
        timeline.play();
    }

    @Override
    public void pause() {
        timeline.pause();
        resetNotes();
    }

    @Override
    public void stop() {
        timeline.stop();
        resetNotes();
    }

    @Override
    public void refresh() {
        stop();
        resetTimeline();
    }


    @Override
    public void toggleSound() {
        music.toggleSound();
    }

    public void resetNotes() {
        music.clear();
        graphic.clear();
    }

    public void resetTimeline() {
        Duration currentTime = Duration.millis(0);
        if (timeline != null) {
            currentTime = timeline.getCurrentTime();
        }
        timeline = new Timeline();

        double multiplier = presenter.getMultiplierSlider().getValue();
        for (int index = 0; index < hand.size(); index++) {
            final int finalIndex = index;
            Accord accord = hand.get(index);
            accord.forEach(note -> {
                Note adjustedNote = note.multiply(multiplier);
                double startTime = adjustedNote.getTime() / 1000;
                double endTime = startTime + adjustedNote.getDuration() / 1000;
                KeyFrame startFrame = new KeyFrame(Duration.millis(startTime)
                        , ev -> {
                    music.play(adjustedNote);
                    graphic.play(adjustedNote);
                    if (finalIndex < hand.size() - 2) {
                        hand.get(finalIndex + 1).forEach(graphic::playNext);
                    }
                });
                KeyFrame endFrame= new KeyFrame(Duration.millis(endTime)
                        , ev -> {
                    music.stop(adjustedNote);
                    graphic.stop(adjustedNote);
                });
                timeline.getKeyFrames().addAll(startFrame, endFrame);
            });
        }
        timeline.setOnFinished(event -> resetNotes());

        BooleanBinding timelineRunning = timeline.statusProperty().isEqualTo(Animation.Status.RUNNING);
        presenter.getStart().disableProperty().bind(timelineRunning);
        presenter.getPause().disableProperty().bind(timelineRunning.not());
        presenter.getStop().disableProperty().bind(timeline.statusProperty().isEqualTo(Animation.Status.STOPPED));

        timeline.jumpTo(currentTime);
        Slider progressBar = presenter.getProgressBar();
        timeline.currentTimeProperty().addListener(new TimelineChangedListener(timeline, progressBar));
        progressBar.valueProperty().addListener(ov -> {
            if (progressBar.isValueChanging()) {
                // multiply duration by percentage calculated by slider position
                Duration totalDuration = timeline.getTotalDuration();
                if (totalDuration != null) {
                    Animation.Status previousStatus = timeline.getStatus();
                    timeline.pause();
                    resetNotes();
                    timeline.jumpTo(totalDuration.multiply(progressBar.getValue() / 100.0));
                    progressBar.pressedProperty().addListener((observable, oldValue, newValue) -> {
                        if (previousStatus.equals(Animation.Status.RUNNING)) {
                            play();
                        }
                    });
                }
                updateValues();
            }
        });

    }

    private void updateValues() {
        Slider progressBar = presenter.getProgressBar();
        if (progressBar != null) {
            Platform.runLater(() -> {
                Duration currentTime = timeline.getCurrentTime();
                Duration totalDuration = timeline.getTotalDuration();
                progressBar.setDisable(totalDuration.isUnknown());
                if (!progressBar.isDisabled() && totalDuration.greaterThan(Duration.ZERO) && !progressBar.isValueChanging()) {
                    progressBar.setValue(currentTime.divide(totalDuration.toMillis()).toMillis() * 100.0);
                }
            });
        }
    }

}
