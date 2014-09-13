package player;

import javafx.animation.Animation.Status;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import midiparser.mididata.events.Note;
import player.model.Hand;
import player.model.MidiFile;
import player.piano.BlackKey;
import player.piano.PianoKey;
import player.piano.WhiteKey;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PianoController {

    private static final int WHITE_NUMBER = 52;
    private static final List<Integer> BLACK_INDICES = Arrays.asList(0, 1, 3, 4, 5);
    private Map<Integer, PianoKey> whiteNotes = new HashMap<>();
    private Map<Integer, PianoKey> blackNotes = new HashMap<>();
    private Player player;

    @FXML
    private Pane keys;

    @FXML
    private Label countdown;

    @FXML
    private Button play;

    @FXML
    private Button pause;

    @FXML
    private Button stop;

    @FXML
    private Slider progressBar;

    private MidiFile midi;

    public PianoController() {
    }

    public Button getStart() {
        return play;
    }

    public Button getPause() {
        return pause;
    }

    public Button getStop() {
        return stop;
    }

    public Slider getProgressBar() {return progressBar;}

    public Label getCountdownLabel() {
        return countdown;
    }

    public PianoKey getKey(Note note) {
        int index = note.getValue();
        return whiteNotes.containsKey(index) ? whiteNotes.get(index) : blackNotes.get(index);
    }

    public void resetNotes() {
        Platform.runLater(() -> {
            whiteNotes.forEach((key, note) -> note.resetStyle());
            blackNotes.forEach((key, note) -> note.resetStyle());
        });
    }

    public void setup() {
        try {
            midi = new MidiFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        player = new MidiTimeline(this, midi);
    }

    @FXML
    private void handlePlay() {
        player.play();
    }

    @FXML
    private void handlePause() {
        player.pause();
    }

    @FXML
    private void handleStop() {
        player.stop();
    }

    @FXML
    private void toggleLeftHand() {
        toggleHand(midi.getLeftHand());
    }

    @FXML
    private void toggleRightHand() {
        toggleHand(midi.getRightHand());
    }

    private void toggleHand(Hand hand) {
        boolean isRunning = player.getTimeline().getStatus().equals(Status.RUNNING);
        player.pause();
        resetNotes();
        ObservableList<Hand> hands = midi.getHands();
        if (hands.contains(hand)) {
            hands.remove(hand);
        } else {
            hands.add(hand);
        }
        player.resetTimeline();
        if (isRunning) {
            player.fastPlay();
        }
    }

    @FXML
    private void initialize() {
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
        keys.getChildren().addAll(whiteGroup, blackGroup);

        countdown.prefWidthProperty().bind(keys.widthProperty());
        countdown.prefHeightProperty().bind(keys.heightProperty());
    }

}
