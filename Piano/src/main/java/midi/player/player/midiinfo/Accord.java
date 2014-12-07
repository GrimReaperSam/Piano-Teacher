package midi.player.player.midiinfo;


import midi.common.data.events.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Accord {

    public List<Note> notes;

    public Accord() {
    }

    public List<Note> getNotes() {
        if (notes == null) {
            notes = new ArrayList<>();
        }
        return notes;
    }

    public void add(Note note) {
        getNotes().add(note);
    }

    public void forEach(Consumer<? super Note> action) {
        getNotes().forEach(action);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Accord accord = (Accord) o;

        return !(notes != null ? !notes.equals(accord.notes) : accord.notes != null);

    }

    @Override
    public int hashCode() {
        return notes != null ? notes.hashCode() : 0;
    }
}
