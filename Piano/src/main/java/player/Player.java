package player;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.util.Duration;
import midiparser.mididata.MIDI;
import midiparser.mididata.Track;
import midiparser.mididata.events.Event;
import midiparser.mididata.events.Note;
import player.piano.PianoKey;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private static final int COUNTDOWN_TIME = 3;
    private MIDI midi;
    List<List<Note>> notes;
    private Timeline timeline;
    private PianoLauncher launcher;

    public Player(PianoLauncher launcher, MIDI midi) {
        this.midi = midi;
        this.launcher = launcher;
        organizeNotes();
        prepareTimeline();
        launcher.getStart().disableProperty().bind(timeline.statusProperty().isEqualTo(Animation.Status.RUNNING));
        launcher.getResume().disableProperty().bind(timeline.statusProperty().isEqualTo(Animation.Status.RUNNING));
        launcher.getPause().disableProperty().bind(timeline.statusProperty().isNotEqualTo(Animation.Status.RUNNING));
    }

    private void organizeNotes() {
        Track track = midi.getTracks().get(0);
        notes = new ArrayList<>();
        notes.add(new ArrayList<>());
        long ticks = 0;
        for (Event event : track.getEvents()) {
            if (Note.class.isAssignableFrom(event.getClass())) {
                Note note = (Note) event;
                if (note.getTicks() > ticks) {
                    List<Note> accord = new ArrayList<>();
                    accord.add(note);
                    ticks = note.getTicks();
                    notes.add(accord);
                } else {
                    notes.get(notes.size() - 1).add(note);
                }
            }
        }
    }

    private void prepareTimeline() {
        timeline = new Timeline();
        double multiplier = 4;
        IntegerProperty countdown = new SimpleIntegerProperty(COUNTDOWN_TIME);
        Label countdownLabel = launcher.getCountdownLabel();
        countdownLabel.textProperty().bind(Bindings.format("%s", countdown));
        KeyFrame initialFrame = new KeyFrame(Duration.millis(0)
            , ev -> notes.get(0).forEach(note -> launcher.getKey(note).preparePlay())
            , new KeyValue(countdown, COUNTDOWN_TIME)
            , new KeyValue(countdownLabel.visibleProperty(), true));
        KeyFrame endTimer = new KeyFrame(Duration.millis(COUNTDOWN_TIME * 1000)
            , new KeyValue(countdown, 0)
            , new KeyValue(countdownLabel.visibleProperty(), false));
        for (int index = 0; index < notes.size(); index++ ) {
            final int finalIndex = index;
            List<Note> accord = notes.get(index);
            accord.forEach(note -> {
                PianoKey key = launcher.getKey(note);
                double time = multiplier * note.getTime() / 1000 + COUNTDOWN_TIME * 1000;
                KeyFrame startFrame = new KeyFrame(Duration.millis(time)
                    , ev -> {
                        key.play(note, multiplier);
                        if (finalIndex < notes.size() - 2) {
                            notes.get(finalIndex + 1).forEach(nextNote -> launcher.getKey(nextNote).preparePlay());
                        }
                });
                timeline.getKeyFrames().addAll(startFrame);
            });
        }
        timeline.getKeyFrames().addAll(initialFrame, endTimer);
    }

    public void play() {
        timeline.playFromStart();
    }

    public void pause() {
        timeline.pause();
        MidiPlayer.getInstance().getPiano().allNotesOff();
    }

    public void resume() {
        timeline.play();
    }

}
