package midi.player.gui.keys;

import javafx.scene.Group;
import player.piano.PianoKey;

import java.util.HashMap;
import java.util.Map;

public class Keys {

    private Map<Integer, PianoKey> whiteNotes = new HashMap<>();
    private Map<Integer, PianoKey> blackNotes = new HashMap<>();
    private Group whiteGroup = new Group();
    private Group blackGroup = new Group();

    public Map<Integer, PianoKey> getWhiteNotes() {
        return whiteNotes;
    }

    public Map<Integer, PianoKey> getBlackNotes() {
        return blackNotes;
    }

    public Group getWhiteGroup() {
        return whiteGroup;
    }

    public Group getBlackGroup() {
        return blackGroup;
    }
}
