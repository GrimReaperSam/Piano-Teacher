package midiparser.parser;

import midiparser.mididata.MIDI;
import midiparser.model.MidiInfo;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;
import java.io.File;

public class MidiParser {

    private final MIDI midi;

    public MidiParser() {
        midi = new MIDI();
    }

    public MIDI parse(MidiInfo info) {
        try {
            File file = info.getMidi();
            midi.setFileName(file.getName());
            midi.setType(MidiSystem.getMidiFileFormat(file).getType());

            Sequence sequence = MidiSystem.getSequence(file);
            midi.setMultiplier(info.getMultiplier());
            midi.setTicks(sequence.getTickLength());
            midi.setMicroseconds((long) (sequence.getMicrosecondLength() /info.getMultiplier()));

            midi.setResolution(sequence.getResolution());
            midi.setDivisionType(sequence.getDivisionType());

            Track[] tracks = sequence.getTracks();
            midi.setTrackCount(tracks.length);
            for (Track track: tracks) {
                midi.getTracks().add(new TrackParser(midi).parse(track));
            }
            return midi;
        }catch (Exception e) {
            throw new IllegalStateException("Unable to find midi file");
        }
    }

}
