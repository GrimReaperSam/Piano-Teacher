package midiparser.mididata;

import midiparser.mididata.events.Note;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Track {

    @XmlElementRef(name = "note")
    @XmlElementWrapper(name="notes")
    private List<Note> notes;
    private int timeSignature;

    public List<Note> getNotes() {
        if (notes == null) {
            notes = new ArrayList<>();
        }
        return notes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        notes.stream().forEach((note) -> {
            sb.append(note);
            sb.append('\n');
        });
        return sb.toString();
    }

    public void setTimeSignature(int timeSignature) {
        this.timeSignature = timeSignature;
    }

    public int getTimeSignature() {
        return timeSignature;
    }
}
