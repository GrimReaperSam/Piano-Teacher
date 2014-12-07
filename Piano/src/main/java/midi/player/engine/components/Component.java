package midi.player.engine.components;


import midi.common.data.events.Note;

public interface Component {

    void play(Note note);

    void stop(Note note);

    void clear();
}
