package midi.player.gui.keys;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import midi.player.gui.keys.Key.PianoKey;

import javax.inject.Inject;
import java.util.Map;

public class KeysPresenter {

    @FXML private Pane root;

    @Inject private Keys halfPiano;
    @Inject private Keys fullPiano;

    private Map<Integer, PianoKey> whiteNotes;
    private Map<Integer, PianoKey> blackNotes;

    public Node getView() {
        return root;
    }

    public void setMode(boolean full) {
        Keys result = full ? fullPiano: halfPiano;
        whiteNotes = result.getWhiteNotes();
        blackNotes = result.getBlackNotes();
        root.getChildren().addAll(result.getWhiteGroup(), result.getBlackGroup());
    }

}
