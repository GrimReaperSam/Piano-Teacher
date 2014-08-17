package midiparser.parser;

import midiparser.mididata.MIDI;
import midiparser.mididata.Track;
import midiparser.mididata.events.*;
import midiparser.mididata.events.Note.NoteBuilder;
import midiparser.utils.MIDIUtils;

import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import java.util.Collections;
import java.util.HashMap;

public class TrackParser {

//    private static final String[] sm_astrKeySignatures = { "Cb", "Gb", "Db", "Ab", "Eb", "Bb", "F", "C", "G", "D", "A", "E", "B", "F#", "C#" };

    private final Track track;

    private HashMap<String, NoteBuilder> notes;
    private MIDI midi;

    public TrackParser(MIDI midi) {
        this.midi = midi;
        this.notes = new HashMap<>();
        this.track = new Track();
    }

    public Track parse(javax.sound.midi.Track original) {
        for (int event = 0; event < original.size(); event++) {
            MidiEvent midiEvent = original.get(event);
            parse(midiEvent.getMessage(), midiEvent.getTick());
        }
        Collections.sort(track.getEvents(), (Event e1, Event e2) ->
            new Long(e1.getTicks()).compareTo(e2.getTicks())
        );
        return track;
    }

    public void parse(MidiMessage message, long timeStamp) {
        if (message instanceof ShortMessage) {
            decodeMessage((ShortMessage) message, timeStamp);
        } else if (message instanceof MetaMessage) {
            decodeMessage((MetaMessage) message, timeStamp);
        }
    }

    private void fireNoteOn(ShortMessage message, long timeStamp) {
        String key = MIDIUtils.getKey(message.getData1());
        NoteBuilder nb = new NoteBuilder(key).ticks(timeStamp).time(midi.toMicros(timeStamp)).volume(message.getData2());
        notes.put(key, nb);
    }

    private void fireNoteOff(ShortMessage message, long timeStamp) {
        String keyName = MIDIUtils.getKey(message.getData1());
        if (notes.containsKey(keyName)) {
            NoteBuilder nb = notes.remove(keyName);
            nb.duration(midi.toMicros(timeStamp - nb.getTicks()));
            Note note = nb.build();
            track.getEvents().add(note);
        }
    }

    private void firePolyphonicKeyPressure(ShortMessage message, long timeStamp) {
        String keyName = MIDIUtils.getKey(message.getData1());
        int pressure = message.getData2();
        PolyKeyPressure pkp = new PolyKeyPressure(keyName, pressure, timeStamp, midi.toMicros(timeStamp));
        track.getEvents().add(pkp);
    }

    private void fireControlChange(ShortMessage message, long timeStamp) {
        int control = message.getData1();
        int value = message.getData2();
        ControlChange cc = new ControlChange(control, value, timeStamp, midi.toMicros(timeStamp));
        track.getEvents().add(cc);
    }

    private void fireProgramChange(ShortMessage message, long timeStamp) {
        int program = message.getData1();
        ProgramChange pc = new ProgramChange(program, timeStamp, midi.toMicros(timeStamp));
        track.getEvents().add(pc);

    }

    private void fireKeyPressure(ShortMessage message, long timeStamp) {
        String keyName = MIDIUtils.getKey(message.getData1());
        int pressure = message.getData2();
        KeyPressure kp = new KeyPressure(keyName, pressure, timeStamp, midi.toMicros(timeStamp));
        track.getEvents().add(kp);
    }

    private void firePitchWheel(ShortMessage message, long timeStamp) {
        int wheel = (message.getData1() & 0x7F) | ((message.getData2() & 0x7F) << 7);
        PitchWheel pw = new PitchWheel(wheel, timeStamp, midi.toMicros(timeStamp));
        track.getEvents().add(pw);
    }

    private void fireTempo(int tempo, long timeStamp) {
        midi.updateMPB(tempo);
        Tempo tEvent = new Tempo(tempo, midi.getBPM(), timeStamp, midi.toMicros(timeStamp));
        track.getEvents().add(tEvent);
    }

    private void fireTrackName(String name, long timeStamp) {
        TrackName tn = new TrackName(name, timeStamp, midi.toMicros(timeStamp));
        track.getEvents().add(tn);
    }

    private void fireInstrumentName(String name, long timeStamp) {
        InstrumentName in = new InstrumentName(name, timeStamp, midi.toMicros(timeStamp));
        track.getEvents().add(in);
    }

    private void fireChannelPrefix(int prefix, long timeStamp) {
        ChannelPrefix cp = new ChannelPrefix(prefix, timeStamp, midi.toMicros(timeStamp));
        track.getEvents().add(cp);
    }

    private void fireEndOfTrack(long timeStamp) {
        EndOfTrack eot = new EndOfTrack(timeStamp, midi.toMicros(timeStamp));
        track.getEvents().add(eot);
    }

    private void decodeMessage(ShortMessage message, long timeStamp) {
        switch (message.getCommand()) {
        case 0x80:
            fireNoteOff(message, timeStamp);
            break;

        case 0x90:
            if (message.getData2() == 0) {
                fireNoteOff(message, timeStamp);
            }
            fireNoteOn(message, timeStamp);
            break;

        case 0xa0:
            firePolyphonicKeyPressure(message, timeStamp);
            break;

        case 0xb0:
            fireControlChange(message, timeStamp);
            break;

        case 0xc0:
            fireProgramChange(message, timeStamp);
            break;

        case 0xd0:
            fireKeyPressure(message, timeStamp);
            break;

        case 0xe0:
            firePitchWheel(message, timeStamp);
            break;

        default:
            throw new IllegalStateException(String.format("Unsupport message: %s %s", message.getData1(), message.getData2()));
        }
    }

    private void decodeMessage(MetaMessage message, long timeStamp) {
        byte[] abData = message.getData();
        switch (message.getType()) {
//        case 0:
//            int nSequenceNumber;
//            if (abData.length == 0) {
//                nSequenceNumber = 0;
//            } else {
//                nSequenceNumber = ((abData[0] & 0xFF) << 8) | (abData[1] & 0xFF);
//            }
//            strMessage = "Sequence Number: " + nSequenceNumber;
//            break;
//
//        case 1:
//            String strText = new String(abData);
//            strMessage = "Text Event: " + strText;
//            break;
//
//        case 2:
//            String strCopyrightText = new String(abData);
//            strMessage = "Copyright Notice: " + strCopyrightText;
//            break;

        case 3:
            fireTrackName(new String(abData), timeStamp);
            break;

        case 4:
            fireInstrumentName(new String(abData), timeStamp);
            break;

//        case 5:
//            String strLyrics = new String(abData);
//            if (strLyrics.equals("\r\n")) {
//                strLyrics = "\\n";
//            }
//            strMessage = "Lyric: " + strLyrics;
//            break;
//
//        case 6:
//            String strMarkerText = new String(abData);
//            strMessage = "Marker: " + strMarkerText;
//            break;
//
//        case 7:
//            String strCuePointText = new String(abData);
//            strMessage = "Cue Point: " + strCuePointText;
//            break;

        case 0x20:
            fireChannelPrefix(abData[0] & 0xFF, timeStamp);
            break;

        case 0x2F:
            fireEndOfTrack(timeStamp);
            break;

        case 0x51:
            int nTempo = ((abData[0] & 0xFF) << 16) | ((abData[1] & 0xFF) << 8) | (abData[2] & 0xFF); // tempo
            fireTempo(nTempo, timeStamp);
            break;

//        case 0x54:
//            // print("data array length: " + abData.length);
//            strMessage = "SMTPE Offset: " + (abData[0] & 0xFF) + ":" + (abData[1] & 0xFF) + ":" + (abData[2] & 0xFF) + "." + (abData[3] & 0xFF) + "." + (abData[4] & 0xFF);
//            break;
//
//        case 0x58:
//            strMessage = "Time Signature: " + (abData[0] & 0xFF) + "/" + (1 << (abData[1] & 0xFF)) + ", MIDI clocks per metronome tick: " + (abData[2] & 0xFF) + ", 1/32 per 24 MIDI clocks: "
//                    + (abData[3] & 0xFF);
//            break;
//
//        case 0x59:
//            String strGender = (abData[1] == 1) ? "minor" : "major";
//            strMessage = "Key Signature: " + sm_astrKeySignatures[abData[0] + 7] + " " + strGender;
//            break;

        default:
            break;
        }
    }

}
