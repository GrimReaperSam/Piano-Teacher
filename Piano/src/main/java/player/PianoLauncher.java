package player;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import midiparser.mididata.events.Note;
import player.model.MidiFile;
import player.piano.BlackKey;
import player.piano.PianoKey;
import player.piano.WhiteKey;

import java.io.File;
import java.util.*;

public class PianoLauncher extends Application {

    private static final int WHITE_NUMBER = 52;
    private static final List<Integer> BLACK_INDICES = Arrays.asList(0, 1, 3, 4, 5);
    private Map<Integer, PianoKey> whiteNotes = new HashMap<>();
    private Map<Integer, PianoKey> blackNotes = new HashMap<>();

    private Stage primaryStage;
    private VBox rootLayout;
    private List<Player> players;
    private Button start;
    private Button pause;
    private Button stop;
    private Label countdownLabel;

    @Override
    public void start(Stage stage) throws Exception {
        players = new ArrayList<>();
        rootLayout = new VBox();
        Scene scene = new Scene(rootLayout);
        scene.setFill(Color.BLACK);
        scene.getStylesheets().add("pianostyles.css");

        createPiano();

        HBox box = new HBox();
        start = new Button();
        start.getStyleClass().addAll("image-button", "play-button");
        start.setOnAction(event -> players.forEach(Player::play));

        pause = new Button();
        pause.getStyleClass().addAll("image-button", "pause-button");
        pause.setOnAction(event -> players.forEach(Player::pause));
        pause.setDisable(true);

        stop = new Button();
        stop.getStyleClass().addAll("image-button", "stop-button");
        stop.setOnAction(event -> players.forEach(Player::stop));
        stop.setDisable(true);

        box.getChildren().addAll(start, pause, stop);
        box.getStyleClass().add("play-hbox");

        rootLayout.getChildren().add(box);

        primaryStage = stage;
        primaryStage.setTitle("Piano");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();

        MidiFile file = new MidiFile(new File(PianoLauncher.class.getResource("../Mozart - Turkish March.xml").toURI()));
        if (file.getRightHand() != null) {
            Player rightHand = new HandPlayer(this, file.getRightHand());
            players.add(rightHand);
        }
        if (file.getLeftHand() != null) {
            Player leftHand = new HandPlayer(this, file.getLeftHand());
            players.add(leftHand);
        }
    }

    private void createPiano() {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        int keyWidth = (int) bounds.getWidth() / WHITE_NUMBER;

        int noteIndex = 21;
        Group whiteGroup = new Group();
        Group blackGroup = new Group();
        WhiteKey whiteKey;
        BlackKey blackKey;
        for (int i = 0; i < WHITE_NUMBER; i++) {
            whiteKey = new WhiteKey(i * keyWidth, 1, keyWidth);
            whiteKey.setNote(noteIndex);
            whiteNotes.put(noteIndex, whiteKey);
            whiteGroup.getChildren().add(whiteKey.getRectangle());
            if (BLACK_INDICES.contains((i + 5) % 7)) {
                if (i == WHITE_NUMBER - 1) {
                    continue;
                }
                noteIndex += 1;
                blackKey = new BlackKey(i * keyWidth + keyWidth / 2 + 4, 1, keyWidth - 8);
                blackKey.setNote(noteIndex);
                blackNotes.put(noteIndex, blackKey);
                blackGroup.getChildren().add(blackKey.getRectangle());
            }
            noteIndex += 1;
        }
        Pane pianoGroup = new Pane();
        pianoGroup.getChildren().addAll(whiteGroup, blackGroup);

        countdownLabel = new Label();
        countdownLabel.setVisible(false);
        countdownLabel.prefWidthProperty().bind(pianoGroup.widthProperty());
        countdownLabel.prefHeightProperty().bind(pianoGroup.heightProperty());
        countdownLabel.getStyleClass().add("countdown-label");

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(pianoGroup, countdownLabel);

        rootLayout.getChildren().addAll(stackPane);
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    public PianoKey getKey(Note note) {
        int index = note.getValue();
        return whiteNotes.containsKey(index) ? whiteNotes.get(index) : blackNotes.get(index);
    }

    public Button getStart() {
        return start;
    }

    public Button getPause() {
        return pause;
    }

    public Button getStop() {
        return stop;
    }

    public Label getCountdownLabel() {
        return countdownLabel;
    }

    public void resetNotes() {
        Platform.runLater(() -> {
            whiteNotes.forEach((key, note) -> note.resetStyle());
            blackNotes.forEach((key, note) -> note.resetStyle());
        });
    }
}
