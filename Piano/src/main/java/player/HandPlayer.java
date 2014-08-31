package player;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.util.Duration;
import midiparser.mididata.events.Note;
import player.piano.PianoKey;

import java.util.List;

public class HandPlayer implements Player {

    private static final int COUNTDOWN_TIME = 3;
    private Timeline timeline;
    private Timeline countdownTimeline;
    private PianoLauncher launcher;
    private List<List<Note>> hand;

    public HandPlayer(PianoLauncher launcher, List<List<Note>> hand) {
        this.launcher = launcher;
        this.hand = hand;
        initializeCountdownTimeline();
        initializeTimeline();
    }

    @Override
    public void play() {
        if (timeline.getStatus().equals(Animation.Status.STOPPED)) {
            Platform.runLater(() -> hand.get(0).forEach(note -> launcher.getKey(note).preparePlay()));
        }
        countdownTimeline.setOnFinished(ev -> timeline.play());
        countdownTimeline.playFromStart();
    }

    @Override
    public void pause() {
        timeline.pause();
        MidiPlayer.getInstance().getPiano().allNotesOff();
    }

    @Override
    public void stop() {
        launcher.resetNotes();
        timeline.stop();
        MidiPlayer.getInstance().getPiano().allNotesOff();
    }

    private void initializeTimeline() {
        timeline = new Timeline();
        double multiplier = 1;
        for (int index = 0; index < hand.size(); index++ ) {
            final int finalIndex = index;
            List<Note> accord = hand.get(index);
            accord.forEach(note -> {
                PianoKey key = launcher.getKey(note);
                double time = multiplier * note.getTime() / 1000;
                KeyFrame startFrame = new KeyFrame(Duration.millis(time)
                        , ev -> {
                    key.play(note, multiplier);
                    if (finalIndex < hand.size() - 2) {
                        hand.get(finalIndex + 1).forEach(nextNote -> launcher.getKey(nextNote).preparePlay());
                    }
                });
                timeline.getKeyFrames().addAll(startFrame);
            });
        }

        BooleanBinding timelineRunning = timeline.statusProperty().isEqualTo(Animation.Status.RUNNING);
        BooleanBinding countdownTimelineRunning = countdownTimeline.statusProperty().isEqualTo(Animation.Status.RUNNING);
        launcher.getStart().disableProperty().bind(timelineRunning.or(countdownTimelineRunning));
        launcher.getPause().disableProperty().bind(timelineRunning.not().or(countdownTimelineRunning));
        launcher.getStop().disableProperty().bind(timeline.statusProperty().isEqualTo(Animation.Status.STOPPED).or(countdownTimelineRunning));
    }

    private void initializeCountdownTimeline() {
        countdownTimeline = new Timeline();
        IntegerProperty countdown = new SimpleIntegerProperty(COUNTDOWN_TIME);
        Label countdownLabel = launcher.getCountdownLabel();
        countdownLabel.textProperty().bind(Bindings.format("%s", countdown));
        KeyFrame beginTimer = new KeyFrame(Duration.millis(0)
                , new KeyValue(countdown, COUNTDOWN_TIME)
                , new KeyValue(countdownLabel.visibleProperty(), true));
        KeyFrame endTimer = new KeyFrame(Duration.millis(COUNTDOWN_TIME * 1000)
                , new KeyValue(countdown, 0)
                , new KeyValue(countdownLabel.visibleProperty(), false));
        countdownTimeline.getKeyFrames().addAll(beginTimer, endTimer);
    }
}
