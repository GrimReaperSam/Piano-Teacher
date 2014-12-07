package midi.player.gui.piano;

import javafx.animation.Animation;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import midi.common.data.events.Note;
import midi.common.service.Midi;
import midi.player.engine.HandPlayer;
import midi.player.engine.Player;
import midi.player.engine.midiinfo.MidiFile;
import midi.player.engine.timelines.CountdownGenerator;
import midi.player.gui.keys.Key.PianoKey;
import midi.player.gui.keys.KeysPresenter;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class PianoPresenter {

    @FXML private VBox root;
    @FXML private StackPane pianoStack;

    private MidiFile midi;
    private Player rightPlayer;
    private Player leftPlayer;
    private List<Player> players;
    private Timeline countdownTimeline;
    private double currentMultiplier;
    private boolean useFull = true;

    @FXML private Label countdown;
    @FXML private Button play;
    @FXML private Button pause;
    @FXML private Button stop;
    @FXML private Slider progressBar;
    @FXML private GridPane handsPane;
    @FXML private Slider multiplierSlider;

    @Inject private KeysPresenter keysPresenter;

    public Node getView() {
        return root;
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
        return keysPresenter.getKey(note);
    }

    public void setMidi(Midi unparsedMidi) {
        midi = new MidiFile(unparsedMidi.getData());
        keysPresenter.setMode(useFull);
        pianoStack.getChildren().add(0, keysPresenter.getView());
        countdownTimeline = new CountdownGenerator().createCountdown(midi, countdown);
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
        handsPane.visibleProperty().bind(new SimpleBooleanProperty(useFull));
        countdown.prefWidthProperty().bind(pianoStack.widthProperty());
        countdown.prefHeightProperty().bind(pianoStack.heightProperty());
        multiplierSlider.valueChangingProperty().addListener(new MultiplierListener());
        currentMultiplier = multiplierSlider.getValue();
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

    private void toggleHand(Player player) {
        if (player.getTimeline().getStatus().equals(Animation.Status.RUNNING)) {
            player.pause();
        } else {
            Duration currentTime = getOther(player).getTimeline().getCurrentTime();
            player.getTimeline().jumpTo(currentTime);
            player.play();
        }
    }

    private Player getOther(Player player) {
        return player.equals(rightPlayer) ? leftPlayer : rightPlayer;
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
