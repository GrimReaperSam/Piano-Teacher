package midi.player.gui.song;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import midi.common.service.Difficulty;
import midi.common.service.Midi;
import midi.common.util.DateUtils;
import midi.player.gui.main.MainPresenter;

public class SongPresenter {

    @FXML private Node root;
    @FXML private Label songNameLabel;
    @FXML private Label composerLabel;
    @FXML private Label genreLabel;
    @FXML private Label lengthLabel;
    @FXML private Label albumLabel;
    @FXML private Label difficultyLabel;
    @FXML private Label yearLabel;

    private Midi midi;
    private MainPresenter mainPresenter;

    public Node getView() {
        return root;
    }

    public void setMidi(Midi midi) {
        this.midi = midi;
        songNameLabel.setText(midi.getName());
        composerLabel.setText(midi.getComposer());
        genreLabel.setText(midi.getGenre());
        albumLabel.setText(midi.getAlbum());
        lengthLabel.setText(DateUtils.toMinSec(midi.getLength()));
        Difficulty difficulty = midi.getDifficulty();
        if (difficulty != null) {
            difficultyLabel.setText(difficulty.toString());
        }
        yearLabel.setText(midi.getYear());
    }

    @FXML
    private void initialize() {
        root.setOnMouseClicked(event -> mainPresenter.showPiano(midi));
    }

    public void setMainPresenter(MainPresenter mainPresenter) {
        this.mainPresenter = mainPresenter;
    }
}
