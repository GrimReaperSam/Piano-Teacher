package midiparser.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import midiparser.ErrorController;
import midiparser.MidiParserLauncher;

public class Dialogs {

    public static void errorDialog(String error) {
        dialog("Error", error);
    }

    public static void infoDialog(String info) {
        dialog("Information", info);
    }

    public static void dialog(String title, String label) {
        try {
            FXMLLoader loader = new FXMLLoader(MidiParserLauncher.class.getResource("view/DialogLayout.fxml"));
            VBox overviewPage = loader.load();
            Scene scene = new Scene(overviewPage);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();

            // Give the controller access to the main app
            ErrorController controller = loader.getController();
            controller.setStage(stage);
            controller.getErrorLabel().setText(label);
        } catch (Exception e) {
        }
    }
}
