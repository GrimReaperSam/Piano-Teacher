package midiparser.parser;

import midiparser.mididata.MIDI;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;
import java.io.File;

public class MidiParser {

    private final MIDI midi;

    public MidiParser() {
        midi = new MIDI();
    }

    public MIDI parse(File file,double multiplier) throws Exception {
        midi.setFileName(file.getName());
        midi.setType(MidiSystem.getMidiFileFormat(file).getType());

        Sequence sequence = MidiSystem.getSequence(file);
        midi.setMultiplier(multiplier);
        midi.setTicks(sequence.getTickLength());
        midi.setMicroseconds((long) (sequence.getMicrosecondLength() / multiplier));

        midi.setResolution(sequence.getResolution());
        midi.setDivisionType(sequence.getDivisionType());

        Track[] tracks = sequence.getTracks();
        midi.setTrackCount(tracks.length);
        for (Track track: tracks) {
            midiparser.mididata.Track midiTrack = new TrackParser(midi).parse(track);
            if (!midiTrack.getNotes().isEmpty()) {
                midi.getTracks().add(midiTrack);
            }
        }
        return midi;
    }

}
