package midi.player.gui.piano;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import midi.common.service.Midi;
import midi.player.gui.keys.KeysPresenter;

import javax.inject.Inject;

public class PianoPresenter {

    @FXML
    private VBox root;

    @FXML
    private StackPane pianoStack;

    @Inject private KeysPresenter keysPresenter;

    public Node getView() {
        return root;
    }

    public void setMidi(Midi midi) {
        initializeKeys();
        System.out.println(midi.getName());
    }

    private void initializeKeys() {
        keysPresenter.setMode(true);
        pianoStack.getChildren().add(0, keysPresenter.getView());
    }

    @FXML
    private void handlePlay() {
    }

    @FXML
    private void handlePause() {
    }

    @FXML
    private void handleStop() {
    }

    @FXML
    private void toggleLeftHand() {
    }

    @FXML
    private void toggleRightHand() {
    }

    @FXML
    private void toggleLeftSound() {
    }

    @FXML
    private void toggleRightSound() {
    }

    @FXML
    private void handleOpen() {
    }

}
