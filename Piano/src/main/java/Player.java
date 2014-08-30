import javafx.animation.FillTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import midiparser.mididata.MIDI;
import midiparser.mididata.Track;
import midiparser.mididata.events.Event;
import midiparser.mididata.events.Note;
import piano.view.PianoKey;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import java.util.ArrayList;
import java.util.List;

public class Player {

    private MIDI midi;
    List<List<Note>> notes;
    private static Synthesizer synthesizer;
    private static MidiChannel channel;
    private Timeline timeline;
    private PianoLauncher launcher;

    public Player(PianoLauncher launcher, MIDI midi) {
        this.midi = midi;
        this.launcher = launcher;
        open();
        organizeNotes();
        notes.forEach(accord -> {
            System.out.println("Accord");
            accord.forEach(note -> System.out.print(note.getKey() + " "));
            System.out.println("");
        });
        prepareTimeline();
    }

    private void organizeNotes() {
        Track track = midi.getTracks().get(0);
        notes = new ArrayList<>();
        notes.add(new ArrayList<>());
        long ticks = 0;
        List<Event> events = track.getEvents();
        for (int i=0; i< events.size(); i++) {
            Event event = events.get(i);
            if (Note.class.isAssignableFrom(event.getClass())) {
                Note note = (Note) event;
                if (note.getTicks() > ticks) {
                    List<Note> accord = new ArrayList<>();
                    accord.add(note);
                    ticks = note.getTicks();
                    notes.add(accord);
                } else {
                    notes.get(notes.size()-1).add(note);
                }
            }
        }
    }

    private void prepareTimeline() {
        timeline = new Timeline();
        double multiplier = 1;
        for (int index = 0; index < notes.size(); index++ ) {
            final int finalIndex = index;
            List<Note> accord = notes.get(index);
            accord.forEach(note -> {
                PianoKey key = launcher.getKey(note.getValue());
                Paint fill = launcher.isWhite(note.getValue())? Color.WHITE: Color.web("#252525");
                double time = multiplier * note.getTime() / 1000 + 1000;
                KeyFrame startFrame = new KeyFrame(Duration.millis(time)
                        , ev -> {
                    channel.noteOn(note.getValue(), note.getVolume());
                    FillTransition transition = new FillTransition(Duration.millis(multiplier * note.getDuration() / 2000), key);
                    transition.setToValue(Color.DARKGREEN);
                    transition.setOnFinished(e -> {
                        key.setFill(fill);
                        channel.noteOff(note.getValue());
                    });
                    transition.play();

                    if (finalIndex < notes.size() - 2) {
                        notes.get(finalIndex + 1).forEach(nextNote -> launcher.getKey(nextNote.getValue()).setFill(Color.ORANGERED));
                    }
                });
                timeline.getKeyFrames().addAll(startFrame);
            });
        }
    }

    private void open() {
        try {
            if (synthesizer == null) {
                if ((synthesizer = MidiSystem.getSynthesizer()) == null) {
                    System.out.println("getSynthesizer() failed!");
                    return;
                }
            }
            synthesizer.open();
            if (channel == null) {
                channel = synthesizer.getChannels()[0];
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void close() {
        if (synthesizer != null) {
            synthesizer.close();
        }
        synthesizer = null;
        channel = null;
    }

    public void play() {
        timeline.playFromStart();
    }

    public void pause() {
        timeline.pause();
        channel.allNotesOff();
    }

    public void resume() {
        timeline.play();
    }

    public Synthesizer getSynthesizer() {
        return synthesizer;
    }
}
