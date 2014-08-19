import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import piano.view.BlackKey;
import piano.view.WhiteKey;

public class PianoLauncher extends Application {

    private Stage primaryStage;
    private Pane rootLayout;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        primaryStage.setTitle("Piano");
        primaryStage.setResizable(false);
        primaryStage.setWidth(300);
        primaryStage.setHeight(182);

        rootLayout = new Pane();
        rootLayout.getChildren().add(new WhiteKey(1,1));
        rootLayout.getChildren().add(new WhiteKey(31,1));
        rootLayout.getChildren().add(new WhiteKey(61,1));
        rootLayout.getChildren().add(new WhiteKey(91,1));
        rootLayout.getChildren().add(new WhiteKey(121,1));
        WhiteKey white = new WhiteKey(151, 1);
        white.setFill(Color.GREEN);
        rootLayout.getChildren().add(white);
        rootLayout.getChildren().add(new WhiteKey(181,1));
        rootLayout.getChildren().add(new WhiteKey(211,1));
        white = new WhiteKey(241, 1);
        white.setFill(Color.DARKORANGE);
        rootLayout.getChildren().add(white);
        rootLayout.getChildren().add(new WhiteKey(271,1));

        rootLayout.getChildren().add(new BlackKey(19,1));
        rootLayout.getChildren().add(new BlackKey(49,1));
        rootLayout.getChildren().add(new BlackKey(109,1));
        BlackKey black = new BlackKey(139,1);
        black.setFill(Color.YELLOW);
        rootLayout.getChildren().add(black);
        rootLayout.getChildren().add(new BlackKey(169,1));
        rootLayout.getChildren().add(new BlackKey(229,1));
        rootLayout.getChildren().add(new BlackKey(259,1));

        Scene scene = new Scene(rootLayout);
        scene.setFill(Color.BLACK);
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }

}
