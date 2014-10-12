package midi.midiparser.gui.main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import midi.midiparser.gui.dialog.DialogPresenter;
import midi.midiparser.gui.parser.ParserPresenter;

import javax.inject.Inject;

public class MainPresenter {

    @FXML private Parent root;
    @FXML private BorderPane contentArea;

    @Inject private ParserPresenter parserPresenter;

    public Parent getView() {
        return root;
    }

    public void showParser() {
        contentArea.setCenter(parserPresenter.getView());
    }

    public void showError(String text) {
        showDialog("Error", text);
    }

    public void showInfo(String info) {
        showDialog("Information", info);
    }

    private void showDialog(String title, String text) {
        try {
            FXMLLoader loader = new FXMLLoader();
            VBox overviewPage = loader.load(getClass().getResourceAsStream("/fxml/Dialog.fxml"));
            Scene scene = new Scene(overviewPage);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();

            DialogPresenter dialogPresenter = loader.getController();
            dialogPresenter.setDialog(stage);
            dialogPresenter.setText(text);
        } catch (Exception e) {
            throw new RuntimeException("Unable to load FXML file '/fxml/Dialog.fxml'", e);
        }
    }

}