package midiparser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MidiParserLauncher extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        primaryStage.setTitle("Midi Parser");
        primaryStage.getIcons().add(new Image("/images/piano.png"));
        primaryStage.setResizable(false);

        FXMLLoader loader = new FXMLLoader(MidiParserLauncher.class.getResource("/view/RootLayout.fxml"));
        VBox rootLayout = loader.load();
        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.show();

        MidiInfoController controller = loader.getController();
        controller.setLauncher(this);
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
