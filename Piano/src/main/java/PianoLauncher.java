import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import piano.view.BlackKey;
import piano.view.WhiteKey;

public class PianoLauncher extends Application {

    private static final int WHITE_NUMBER = 52;
    private static final int[] BLACK_INDICES = {0, 1, 3, 4, 5};

    private Stage primaryStage;
    private Pane rootLayout;

    @Override
    public void start(Stage stage) throws Exception {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        rootLayout = new Pane();
        Scene scene = new Scene(rootLayout);
        scene.setFill(Color.BLACK);
        scene.getStylesheets().add("pianostyles.css");

        primaryStage = stage;
        primaryStage.setTitle("Piano");
        primaryStage.setResizable(false);
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        int keyWidth = (int) bounds.getWidth() / WHITE_NUMBER;
        int remainingWidth = (int) (bounds.getWidth() % WHITE_NUMBER);
        int rightMargin = remainingWidth / 4;

        for (int i = 0; i < WHITE_NUMBER; i++) {
            rootLayout.getChildren().add(new WhiteKey(rightMargin + i*keyWidth, 1, keyWidth));
        }

        for (int i = 0; i < WHITE_NUMBER-1; i++) {
            for (int index: BLACK_INDICES) {
                if ((i+5) % 7 == index) {
                    int xPos = rightMargin + i * keyWidth + keyWidth/2 + 4;
                    int bWidth = keyWidth - 8;
                    rootLayout.getChildren().add(new BlackKey(xPos, 1, bWidth));
                }
            }
        }

//        rootLayout.getChildren().add(new BlackKey(19,1));
//        rootLayout.getChildren().add(new BlackKey(49,1));
//        rootLayout.getChildren().add(new BlackKey(109,1));
//        BlackKey black = new BlackKey(139,1);
//        black.setFill(Color.YELLOW);
//        rootLayout.getChildren().add(black);
//        rootLayout.getChildren().add(new BlackKey(169,1));
//        rootLayout.getChildren().add(new BlackKey(229,1));
//        rootLayout.getChildren().add(new BlackKey(259,1));

        primaryStage.setScene(scene);
        primaryStage.show();


    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }

}
