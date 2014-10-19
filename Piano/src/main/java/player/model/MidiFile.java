package player.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import midi.common.data.ParsedMidi;
import midi.common.data.Track;
import midi.common.data.events.Note;
import player.PianoController;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.File;

public class MidiFile {

    private File file;
    private Hand rightHand;
    private Hand leftHand;
    private ObservableList<Hand> hands = FXCollections.observableArrayList();
    private double countdown;
    private double multiplier = 1;
    private int measure;

    public MidiFile() {
        try {
            file = new File(PianoController.class.getResource("../music/beethoven_opus10_1.xml").toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
        initMidi();
    }

    public void setFile(File file) {
        this.file = file;
        initMidi();
    }

    public ObservableList<Hand> getHands() {
        return hands;
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

    public int getMeasure() {
        return measure;
    }

    private Hand getHand(ParsedMidi midi, int trackIndex) {
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

    private void initMidi() {
        try {
            ParsedMidi midi = (ParsedMidi) JAXBContext.newInstance(ParsedMidi.class).createUnmarshaller().unmarshal(file);
            rightHand = getHand(midi, 0);
            leftHand = getHand(midi, 1);
            hands.clear();
            if (rightHand != null) {
                hands.add(rightHand);
            }
            if (leftHand != null) {
                hands.add(leftHand);
            }
            countdown = 4 * midi.getMicrosecondsPerBeat();
            measure = midi.getTracks().get(0).getTimeSignature();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
