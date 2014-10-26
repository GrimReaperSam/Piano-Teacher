package midi.player.gui.piano;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import midi.common.service.Midi;
import midi.player.gui.main.MainPresenter;

import javax.inject.Inject;

public class PianoPresenter {

    @FXML
    private VBox root;

    @Inject private MainPresenter mainPresenter;

    public Node getView() {
        return root;
    }

    public void setMidi(Midi midi) {
        System.out.println(midi.getName());
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
