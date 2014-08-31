package player.model;

import midiparser.mididata.MIDI;
import midiparser.mididata.Track;
import midiparser.mididata.events.Event;
import midiparser.mididata.events.Note;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MidiFile {

    private MIDI midi;
    private List<List<Note>> rightHand;
    private List<List<Note>> leftHand;
    private double multiplier = 1;

    public MidiFile(File file) throws Exception {
        initMidi(file);
    }

    private void initMidi(File file) throws Exception {
        JAXBContext context = JAXBContext.newInstance(MIDI.class);
        Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
        midi = (MIDI) jaxbUnmarshaller.unmarshal(file);
        rightHand = getHand(midi, 0);
        leftHand = getHand(midi, 1);
    }

    private List<List<Note>> getHand(MIDI midi, int trackIndex) {
        if(midi.getTracks().size() <= trackIndex) {
            return null;
        }
        Track track = midi.getTracks().get(trackIndex);
        List<List<Note>> notes = new ArrayList<>();
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
        return notes;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }

    public List<List<Note>> getRightHand() {
        return rightHand;
    }

    public List<List<Note>> getLeftHand() {
        return leftHand;
    }
}
