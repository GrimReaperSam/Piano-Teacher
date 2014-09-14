package player;

import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import keysgenerator.KeysGenerator;
import keysgenerator.KeysResult;
import midiparser.mididata.events.Note;
import player.model.MidiFile;
import player.piano.PianoKey;

import java.util.HashMap;
import java.util.Map;

public class PianoController {

    private Map<Integer, PianoKey> whiteNotes = new HashMap<>();
    private Map<Integer, PianoKey> blackNotes = new HashMap<>();
    private Player rightPlayer;
    private Player leftPlayer;

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
    private Timeline countdownTimeline;

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

    public PianoKey getKey(Note note) {
        int index = note.getValue();
        return whiteNotes.containsKey(index) ? whiteNotes.get(index) : blackNotes.get(index);
    }

    @FXML
    private void handlePlay() {
        rightPlayer.preparePlay();
        leftPlayer.preparePlay();
        countdownTimeline.setOnFinished(ev -> {
            rightPlayer.play();
            leftPlayer.play();
        });
        countdownTimeline.playFromStart();
    }

    @FXML
    private void handlePause() {
        rightPlayer.pause();
        leftPlayer.pause();
    }

    @FXML
    private void handleStop() {
        rightPlayer.stop();
        leftPlayer.stop();
    }

    @FXML
    private void toggleLeftHand() {
        toggleHand(leftPlayer);
    }

    @FXML
    private void toggleRightHand() {
        toggleHand(rightPlayer);
    }

    @FXML
    private void toggleLeftSound() {
        leftPlayer.toggleSound();
    }

    @FXML
    private void toggleRightSound() {
        rightPlayer.toggleSound();
    }

    private void toggleHand(Player player) {
        if (player.getTimeline().getStatus().equals(Status.RUNNING)) {
            player.pause();
        } else {
            Duration currentTime = getOther(player).getTimeline().getCurrentTime();
            player.getTimeline().jumpTo(currentTime);
            player.play();
        }
    }

    @FXML
    private void initialize() {
        KeysResult result = KeysGenerator.newInstance()
//                                            .whiteNumber(36)
                                            .whiteNumber(52)
//                                            .startNote(48)
                                            .startNote(21)
                                            .blackOffset(5)
                                            .generate();
        whiteNotes = result.getWhiteNotes();
        blackNotes = result.getBlackNotes();
        keys.getChildren().addAll(result.getWhiteGroup(), result.getBlackGroup());

        countdown.prefWidthProperty().bind(keys.widthProperty());
        countdown.prefHeightProperty().bind(keys.heightProperty());

        try {
            midi = new MidiFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (midi.getRightHand() != null) {
            rightPlayer = new HandPlayer(this, midi.getRightHand());
        }
        if (midi.getLeftHand() != null) {
            leftPlayer = new HandPlayer(this, midi.getLeftHand());
        }

        initializeCountdownTimeline();
    }

    private Player getOther(Player player) {
        return player.equals(rightPlayer) ? leftPlayer : rightPlayer;
    }

    private void initializeCountdownTimeline() {
        countdownTimeline = new Timeline();
        double COUNTDOWN_TIME = midi.getCountdown() / 1000;
        IntegerProperty countdownProperty = new SimpleIntegerProperty();
        countdown.textProperty().bind(countdownProperty.asString());
        KeyFrame beginTimer = new KeyFrame(Duration.millis(0)
                , new KeyValue(countdownProperty, 1)
                , new KeyValue(countdown.visibleProperty(), true));

        KeyFrame endTimer = new KeyFrame(Duration.millis(COUNTDOWN_TIME)
                , new KeyValue(countdownProperty, 4)
                , new KeyValue(countdown.visibleProperty(), false));

        countdownTimeline.getKeyFrames().addAll(beginTimer, endTimer);
    }

}