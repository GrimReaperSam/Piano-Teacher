package midi.player.player.components;


import midi.common.data.events.Note;

public interface Component {

    void play(Note note);

    void stop(Note note);

    void clear();
}
