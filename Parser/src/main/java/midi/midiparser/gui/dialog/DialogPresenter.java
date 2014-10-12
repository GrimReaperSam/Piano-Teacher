package midi.midiparser.gui.dialog;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class DialogPresenter {

    @FXML private Label errorLabel;

    private Stage dialog;

    public void setText(String text) {
        errorLabel.setText(text);
    }

    @FXML
    private void handleAccept(ActionEvent event) {
        dialog.close();
    }

    public void setDialog(Stage dialog) {
        this.dialog = dialog;
    }
}
