package midiparser;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ErrorController {
    @FXML
    private Label errorLabel;

    private Stage stage;


    public Label getErrorLabel() {
        return errorLabel;
    }

    public ErrorController() {}

    @FXML
    private void handleAccept(ActionEvent event) {
        stage.close();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
