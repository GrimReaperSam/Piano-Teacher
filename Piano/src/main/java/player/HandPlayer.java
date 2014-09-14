package player;

import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.Slider;
import javafx.util.Duration;
import player.model.Accord;
import player.model.Hand;
import player.piano.PianoKey;

import java.util.ArrayList;
import java.util.List;

public class HandPlayer implements Player {

    private PianoController controller;
    private Hand hand;
    private Timeline timeline;
    private boolean sound = true;
    private List<PianoKey> currentlyModified = new ArrayList<>();

    public HandPlayer(PianoController controller, Hand hand) {
        this.controller = controller;
        this.hand = hand;
        resetTimeline();
    }

    @Override
    public Timeline getTimeline() {
        return timeline;
    }

    @Override
    public void preparePlay() {
        if (timeline.getStatus().equals(Animation.Status.STOPPED)) {
            Platform.runLater(() -> hand.get(0).forEach(note -> controller.getKey(note).preparePlay()));
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

    public void resetNotes() {
        if (!currentlyModified.isEmpty()) {
            currentlyModified.forEach((key) -> {
                key.resetStyle();
                MidiPlayerComponent.getInstance().getPiano().noteOff(key.getNote());
            });
        }
    }

    public void resetTimeline() {
        Duration currentTime = Duration.millis(0);
        if (timeline != null) {
            currentTime = timeline.getCurrentTime();
        }
        timeline = new Timeline();

        double multiplier = 1;
        for (int index = 0; index < hand.size(); index++) {
            final int finalIndex = index;
            Accord accord = hand.get(index);
            accord.forEach(note -> {
                PianoKey key = controller.getKey(note);
                double time = multiplier * note.getTime() / 1000;
                KeyFrame noteFrame = new KeyFrame(Duration.millis(time)
                        , ev -> {
                    key.play(note, multiplier, sound);
                    currentlyModified.remove(key);
                    if (finalIndex < hand.size() - 2) {
                        hand.get(finalIndex + 1).forEach(nextNote -> {
                            PianoKey nextKey = controller.getKey(nextNote);
                            if (!currentlyModified.contains(nextKey)) {
                                currentlyModified.add(nextKey);
                            }
                            nextKey.preparePlay();
                        });
                    }
                });
                timeline.getKeyFrames().addAll(noteFrame);
            });
        }
        timeline.setOnFinished(event -> resetNotes());

        BooleanBinding timelineRunning = timeline.statusProperty().isEqualTo(Animation.Status.RUNNING);
        controller.getStart().disableProperty().bind(timelineRunning);
        controller.getPause().disableProperty().bind(timelineRunning.not());
        controller.getStop().disableProperty().bind(timeline.statusProperty().isEqualTo(Animation.Status.STOPPED));

        timeline.jumpTo(currentTime);
        timeline.currentTimeProperty().addListener((observable, oldValue, newValue) -> updateValues());
        Slider progressBar = controller.getProgressBar();
        progressBar.valueProperty().addListener(ov -> {
            if (progressBar.isValueChanging()) {
                // multiply duration by percentage calculated by slider position
                Duration totalDuration = timeline.getTotalDuration();
                if (totalDuration != null) {
                    Status previousStatus = timeline.getStatus();
                    timeline.pause();
                    resetNotes();
                    timeline.jumpTo(totalDuration.multiply(progressBar.getValue() / 100.0));
                    progressBar.pressedProperty().addListener((observable, oldValue, newValue) -> {
                        if (previousStatus.equals(Status.RUNNING)) {
                            play();
                        }
                    });
                }
                updateValues();
            }
        });

    }

    private void updateValues() {
        Slider progressBar = controller.getProgressBar();
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

    public void toggleSound() {
        sound = !sound;
    }
}