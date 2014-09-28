package player.components;

import midiparser.mididata.events.Note;

public interface Component {

    void play(Note note);

    void stop(Note note);

    void clear();
}
