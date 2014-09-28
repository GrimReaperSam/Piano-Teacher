package player.components;

import midiparser.mididata.events.Note;

public abstract class BaseGraphicComponent implements Component {

    public abstract void playNext(Note note);
}

