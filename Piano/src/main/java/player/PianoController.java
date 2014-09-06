package player;

import javafx.animation.Animation.Status;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.util.Duration;
import midiparser.mididata.events.Note;
import player.model.Hand;
import player.model.MidiFile;
import player.piano.BlackKey;
import player.piano.PianoKey;
import player.piano.WhiteKey;

import java.io.File;
import java.util.*;

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
            midi = new MidiFile(new File(PianoController.class.getResource("../Mozart - Turkish March.xml").toURI()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Hand> hands = new ArrayList<>();
        if (midi.getRightHand() != null) {
            hands.add(midi.getRightHand());
        }
        if (midi.getLeftHand() != null) {
            hands.add(midi.getLeftHand());
        }
        player = new MidiTimeline(this, hands);

        player.getTimeline().currentTimeProperty().addListener((observable, oldValue, newValue) -> updateValues());
        progressBar.valueProperty().addListener(ov -> {
            if (progressBar.isValueChanging()) {
                // multiply duration by percentage calculated by slider position
                Timeline timeline = player.getTimeline();
                Duration totalDuration = timeline.getTotalDuration();
                if (totalDuration != null) {
                    Status previousStatus = timeline.getStatus();
                    timeline.pause();
                    timeline.jumpTo(totalDuration.multiply(progressBar.getValue() / 100.0));
                    resetNotes();
                    if (previousStatus.equals(Status.RUNNING)) {
                        timeline.play();
                    }
                }
                updateValues();
            }
        });
    }

    private void updateValues() {
        if (progressBar != null) {
            Platform.runLater(() -> {
                Timeline timeline = player.getTimeline();
                Duration currentTime = timeline.getCurrentTime();
                Duration totalDuration = timeline.getTotalDuration();
                progressBar.setDisable(totalDuration.isUnknown());
                if (!progressBar.isDisabled() && totalDuration.greaterThan(Duration.ZERO) && !progressBar.isValueChanging()) {
                    progressBar.setValue(currentTime.divide(totalDuration.toMillis()).toMillis() * 100.0);
                }
            });
        }
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