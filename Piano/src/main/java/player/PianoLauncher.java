package player;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PianoLauncher extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(PianoLauncher.class.getResource("../view/RootLayout.fxml"));
        VBox rootLayout = loader.load();

        Scene scene = new Scene(rootLayout);
        stage.setTitle("Piano");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }

}
