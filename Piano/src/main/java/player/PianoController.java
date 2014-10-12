package player;

import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import midi.midiparser.mididata.events.Note;
import player.keysgenerator.Piano;
import player.keysgenerator.PianoGenerator;
import player.model.MidiFile;
import player.piano.PianoKey;
import timelines.CountdownGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PianoController {

    private Map<Integer, PianoKey> whiteNotes = new HashMap<>();
    private Map<Integer, PianoKey> blackNotes = new HashMap<>();
    private Player rightPlayer;
    private Player leftPlayer;
    private List<Player> players;
    private MidiFile midi  = new MidiFile();
    private Timeline countdownTimeline;
    private double currentMultiplier;
    private boolean useFull = true;

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

    @FXML
    private GridPane handsPane;
    @FXML
    private Slider multiplierSlider;

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

    public Slider getMultiplierSlider() {return multiplierSlider;}

    public PianoKey getKey(Note note) {
        int index = note.getValue();
        return whiteNotes.containsKey(index) ? whiteNotes.get(index) : blackNotes.get(index);
    }

    @FXML
    private void handlePlay() {
        players.forEach(Player::preparePlay);
        countdownTimeline.setOnFinished(ev -> players.forEach(Player::play));
        countdownTimeline.playFromStart();
    }

    @FXML
    private void handlePause() {
        players.forEach(Player::pause);
    }

    @FXML
    private void handleStop() {
        players.forEach(Player::stop);
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

    @FXML
    private void handleOpen() {
        players.forEach(Player::stop);

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extensionFilter);

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            midi.setFile(file);
            players.clear();
            if (midi.getRightHand() != null) {
                rightPlayer = new HandPlayer(this, midi.getRightHand());
                players.add(rightPlayer);
            }
            if (useFull) {
                if (midi.getLeftHand() != null) {
                    leftPlayer = new HandPlayer(this, midi.getLeftHand());
                    players.add(leftPlayer);
                }
            }
        }
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
        Piano halfPiano = PianoGenerator.newInstance()
                                            .whiteNumber(36)
                                            .startNote(48)
                                            .generate();
        Piano fullPiano = PianoGenerator.newInstance()
                                            .whiteNumber(52)
                                            .startNote(21)
                                            .blackOffset(5)
                                            .generate();
        Piano result = useFull ? fullPiano : halfPiano;
        whiteNotes = result.getWhiteNotes();
        blackNotes = result.getBlackNotes();
        keys.getChildren().addAll(result.getWhiteGroup(), result.getBlackGroup());
        handsPane.visibleProperty().bind(new SimpleBooleanProperty(useFull));

        countdown.prefWidthProperty().bind(keys.widthProperty());
        countdown.prefHeightProperty().bind(keys.heightProperty());

        midi = new MidiFile();
        players = new ArrayList<>();
        if (midi.getRightHand() != null) {
            rightPlayer = new HandPlayer(this, midi.getRightHand());
            players.add(rightPlayer);
        }
        if (useFull) {
            if (midi.getLeftHand() != null) {
                leftPlayer = new HandPlayer(this, midi.getLeftHand());
                players.add(leftPlayer);
            }
        }

        multiplierSlider.valueChangingProperty().addListener(new MultiplierListener());
        currentMultiplier = multiplierSlider.getValue();
        initializeCountdownTimeline();
    }

    private Player getOther(Player player) {
        return player.equals(rightPlayer) ? leftPlayer : rightPlayer;
    }

    private void initializeCountdownTimeline() {
        countdownTimeline = new CountdownGenerator().createCountdown(midi, countdown);
    }


    /**
     * This listener will activate when the tempo is changed, and recalculates the timeline + creates a countdown
     */
    private class MultiplierListener implements ChangeListener<Boolean> {

        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean wasChanging, Boolean changing) {
            if (!changing) {
                players.forEach((player) -> {
                    double newValue = multiplierSlider.getValue();
                    double timeMultipler = newValue / currentMultiplier;
                    currentMultiplier = newValue;
                    Timeline timeline = player.getTimeline();
                    Animation.Status previousStatus = timeline.getStatus();
                    Duration current = timeline.getCurrentTime();
                    player.refresh();
                    player.getTimeline().jumpTo(current.multiply(timeMultipler));
                    if (previousStatus.equals(Animation.Status.RUNNING)) {
                        countdownTimeline.setOnFinished(ev -> players.forEach(Player::play));
                        countdownTimeline.playFromStart();
                    }
                });
            }
        }
    }

}