package midi.player.gui.song;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import midi.common.service.Difficulty;
import midi.common.service.Midi;
import midi.common.service.MidiBuilder;
import midi.common.util.DateUtils;
import midi.player.gui.main.MainPresenter;

public class SongPresenter {

    @FXML private Node root;
    @FXML private TextField songNameTextField;
    @FXML private TextField composerTextField;
    @FXML private TextField genreTextField;
    @FXML private TextField albumTextField;
    @FXML private Label lengthLabel;
    @FXML private ComboBox<Difficulty> difficultyComboBox;
    @FXML private TextField yearTextField;

    private Midi midi;
    private MainPresenter mainPresenter;

    public Node getView() {
        return root;
    }

    public void setMidi(Midi midi) {
        this.midi = midi;
        songNameTextField.setText(midi.getName());
        composerTextField.setText(midi.getComposer());
        genreTextField.setText(midi.getGenre());
        albumTextField.setText(midi.getAlbum());
        lengthLabel.setText(DateUtils.toMinSec(midi.getLength()));
        Difficulty difficulty = Difficulty.fromInt(midi.getDifficulty());
        if (difficulty != null) {
            difficultyComboBox.setValue(difficulty);
        }
        yearTextField.setText(midi.getYear());
    }

    public Midi getMidi() {
        return MidiBuilder.newInstance()
                .setData(midi.getData())
                .setName(songNameTextField.getText())
                .setComposer(composerTextField.getText())
                .setGenre(genreTextField.getText())
                .setAlbum(albumTextField.getText())
                .setLength(midi.getLength())
                .setDifficulty(difficultyComboBox.getValue().getValue())
                .setYear(yearTextField.getText())
                .createMidi();
    }

    @FXML
    private void initialize() {
        difficultyComboBox.getItems().addAll(Difficulty.values());
        difficultyComboBox.setValue(Difficulty.NORMAL);

        root.setOnMouseClicked(event -> mainPresenter.showPiano(midi));
    }

    public void setMainPresenter(MainPresenter mainPresenter) {
        this.mainPresenter = mainPresenter;
    }
}
