package midi.player.gui.keys;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import midi.common.data.events.Note;
import midi.player.gui.keys.Key.PianoKey;

import javax.inject.Inject;
import java.util.Map;

public class KeysPresenter {

    @FXML private Pane root;

    @Inject private Keys halfPiano;
    @Inject private Keys fullPiano;

    private Map<Integer, PianoKey> whiteNotes;
    private Map<Integer, PianoKey> blackNotes;

    public Pane getView() {
        return root;
    }

    public void setMode(boolean full) {
        Keys result = full ? fullPiano: halfPiano;
        whiteNotes = result.getWhiteNotes();
        blackNotes = result.getBlackNotes();
        root.getChildren().addAll(result.getWhiteGroup(), result.getBlackGroup());
    }

    public PianoKey getKey(Note note) {
        int index = note.getValue();
        return whiteNotes.containsKey(index) ? whiteNotes.get(index) : blackNotes.get(index);
    }

}
