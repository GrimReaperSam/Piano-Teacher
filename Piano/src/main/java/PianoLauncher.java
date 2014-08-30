import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import midiparser.mididata.MIDI;
import piano.view.BlackKey;
import piano.view.PianoKey;
import piano.view.WhiteKey;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PianoLauncher extends Application {

    private static final int WHITE_NUMBER = 52;
    private static final List<Integer> BLACK_INDICES = Arrays.asList(0, 1, 3, 4, 5);
    private static List<Integer> WHITE_NOTES = new ArrayList<>();
    private static List<Integer> BLACK_NOTES = new ArrayList<>();

    private Stage primaryStage;
    private Pane rootLayout;
    private Group whiteGroup;
    private Group blackGroup;
    private Player player;

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
        primaryStage.setHeight(500);

        int keyWidth = (int) bounds.getWidth() / WHITE_NUMBER;
        int remainingWidth = (int) (bounds.getWidth() % WHITE_NUMBER);
        int rightMargin = remainingWidth / 3;

        int noteIndex = 21;
        whiteGroup = new Group();
        blackGroup = new Group();
        WhiteKey whiteKey;
        BlackKey blackKey;
        for (int i = 0; i < WHITE_NUMBER; i++) {
            whiteKey = new WhiteKey(rightMargin + i * keyWidth, 1, keyWidth);
            whiteKey.setNote(noteIndex);
            WHITE_NOTES.add(noteIndex);
            whiteGroup.getChildren().add(whiteKey);
            if (BLACK_INDICES.contains((i + 5) % 7)) {
                if (i == WHITE_NUMBER - 1) {
                    continue;
                }
                noteIndex += 1;
                blackKey = new BlackKey(rightMargin + i * keyWidth + keyWidth / 2 + 4, 1, keyWidth - 8);
                blackKey.setNote(noteIndex);
                BLACK_NOTES.add(noteIndex);
                blackGroup.getChildren().add(blackKey);
            }
            noteIndex += 1;
        }
        rootLayout.getChildren().addAll(whiteGroup, blackGroup);

        primaryStage.setScene(scene);
        primaryStage.show();

        JAXBContext context = JAXBContext.newInstance(MIDI.class);
        Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
        MIDI midi = (MIDI) jaxbUnmarshaller.unmarshal(new File(PianoLauncher.class.getResource("Mozart - Turkish March.xml").toURI()));

        player = new Player(this, midi);

        VBox box = new VBox();
        Button play = new Button("Start");
        play.setOnAction(event -> player.play());

        Button pause = new Button("Pause");
        pause.setOnAction(event -> player.pause());

        Button resume = new Button("Resume");
        resume.setOnAction(event -> player.resume());
        box.getChildren().addAll(play, pause, resume);

        rootLayout.getChildren().add(box);
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    public PianoKey getKey(int note) {
        int index = WHITE_NOTES.indexOf(note);
        if (index != -1) {
            return (PianoKey) whiteGroup.getChildren().get(index);
        } else {
            index = BLACK_NOTES.indexOf(note);
            return (PianoKey) blackGroup.getChildren().get(index);
        }
    }

    public boolean isWhite(int note) {
        return WHITE_NOTES.indexOf(note) != -1;
    }

}
