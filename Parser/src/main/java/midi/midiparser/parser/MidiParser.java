package midi.midiparser.parser;

import midi.common.data.ParsedMidi;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;
import java.io.File;
import java.io.IOException;

public class MidiParser {

    private final ParsedMidi midi;

    public MidiParser() {
        midi = new ParsedMidi();
    }

    public ParsedMidi parse(File file,double multiplier) throws InvalidMidiDataException, IOException {
        midi.setFileName(file.getName().split("\\.")[0]);
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
            midi.common.data.Track midiTrack = new TrackParser(midi).parse(track);
            if (!midiTrack.getNotes().isEmpty()) {
                midi.getTracks().add(midiTrack);
            }
        }
        return midi;
    }

}
