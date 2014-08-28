package piano.view;

import javafx.scene.shape.Rectangle;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;

public class PianoKey extends Rectangle {

    private int note;
    private static Synthesizer synthesizer;
    private static MidiChannel channel;

    public PianoKey(int x, int y, int w, int h) {
        super(x, y, w, h);
        getStyleClass().add("piano-key");
        open();
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

    public void noteOn(int velocity) {
        channel.noteOn(note, velocity);
    }

    public void noteOff() {
        channel.noteOff(note);
    }

    public void setNote(int note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Note: " + note;
    }
}