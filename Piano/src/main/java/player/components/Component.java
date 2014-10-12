package player.components;


import midi.midiparser.mididata.events.Note;

public interface Component {

    void play(Note note);

    void stop(Note note);

    void clear();
}
