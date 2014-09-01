package player;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PianoLauncher extends Application {

    private Stage primaryStage;
    private VBox rootLayout;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        FXMLLoader loader = new FXMLLoader(PianoLauncher.class.getResource("../view/RootLayout.fxml"));
        rootLayout = loader.load();

        PianoController controller = loader.getController();
        controller.setLauncher(this);
        Task<Void> setup = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                controller.setup();
                return null;
            }
        };
        new Thread(setup).start();

        Scene scene = new Scene(rootLayout);
        primaryStage.setTitle("Piano");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }

}
