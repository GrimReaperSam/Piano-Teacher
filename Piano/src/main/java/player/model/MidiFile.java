package player.model;

import midiparser.mididata.MIDI;
import midiparser.mididata.Track;
import midiparser.mididata.events.Note;
import player.PianoController;

import javax.xml.bind.JAXBContext;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class MidiFile {

    private Hand rightHand;
    private Hand leftHand;
    private double countdown;
    private double multiplier = 1;

    public MidiFile() throws Exception {
        File file = new File(PianoController.class.getResource("../music/beethoven_opus10_1.xml").toURI());
        initMidi(file);
    }

    private void initMidi(File file) throws Exception {
        MIDI midi = (MIDI) JAXBContext.newInstance(MIDI.class).createUnmarshaller().unmarshal(file);
        rightHand = getHand(midi, 0);
        leftHand = getHand(midi, 1);
        countdown = 4 * midi.getMicrosecondsPerBeat();
    }

    public List<Hand> getHands() {
        return Arrays.asList(rightHand, rightHand);
    }

    public double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }

    public Hand getRightHand() {
        return rightHand;
    }

    public Hand getLeftHand() {
        return leftHand;
    }

    public double getCountdown() {
        return countdown;
    }

    public void setCountdown(double countdown) {
        this.countdown = countdown;
    }

    private Hand getHand(MIDI midi, int trackIndex) {
        if(midi.getTracks().size() <= trackIndex) {
            return null;
        }
        Track track = midi.getTracks().get(trackIndex);
        Hand hand = new Hand();
        hand.add(new Accord());
        long ticks = 0;
        for (Note note : track.getNotes()) {
            if (note.getTicks() > ticks) {
                Accord accord = new Accord();
                accord.add(note);
                ticks = note.getTicks();
                hand.add(accord);
            } else {
                hand.get(hand.size() - 1).add(note);
            }
        }
        return hand;
    }

}
