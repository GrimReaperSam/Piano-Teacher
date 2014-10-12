package player.components;


import midi.common.data.events.Note;

public abstract class BaseGraphicComponent implements Component {

    public abstract void playNext(Note note);
}

