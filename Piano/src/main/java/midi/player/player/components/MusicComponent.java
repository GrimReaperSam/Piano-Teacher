package midi.player.player.components;


import midi.common.data.events.Note;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;

public class MusicComponent extends BaseMusicComponent {

    private Synthesizer synthesizer;
    private MidiChannel channel;

    public MusicComponent() {
        initializeMidiSynthesizer();
    }

    @Override
    public void play(Note note) {
        channel.noteOn(note.getValue(), getSound() ? note.getVolume() : 0);
    }

    @Override
    public void stop(Note note) {
        channel.noteOff(note.getValue());
    }

    @Override
    public void clear() {
        channel.allNotesOff();
    }

    private void initializeMidiSynthesizer() {
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
}
