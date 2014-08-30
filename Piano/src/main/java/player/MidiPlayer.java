package player;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;

public class MidiPlayer {

    private Synthesizer synthesizer;
    private MidiChannel pianoChannel;

    public static MidiPlayer player;
    static {
        player = new MidiPlayer();
        player.open();
    }

    public static MidiPlayer getInstance() {
        return player;
    }

    public MidiChannel getPiano() {
        return pianoChannel;
    }

    private MidiPlayer() {
    }

    public void open() {
        try {
            if (synthesizer == null) {
                if ((synthesizer = MidiSystem.getSynthesizer()) == null) {
                    System.out.println("getSynthesizer() failed!");
                    return;
                }
            }
            synthesizer.open();
            if (pianoChannel == null) {
                pianoChannel = synthesizer.getChannels()[0];
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
        pianoChannel = null;
    }
}
