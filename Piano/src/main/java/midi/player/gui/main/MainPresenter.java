package midi.player.gui.main;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

public class MainPresenter {

    @FXML private Parent root;
    @FXML private BorderPane contentArea;

    public Parent getView() {
        return root;
    }

}