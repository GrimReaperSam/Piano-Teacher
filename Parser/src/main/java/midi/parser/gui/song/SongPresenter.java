package midi.parser.gui.song;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import midi.common.service.Difficulty;
import midi.common.service.Midi;
import midi.common.service.MidiBuilder;
import midi.common.service.MidiService;
import midi.common.util.DateUtils;

import javax.inject.Inject;

public class SongPresenter {

    @FXML private Node root;
    @FXML private TextField songNameTextField;
    @FXML private TextField composerTextField;
    @FXML private TextField genreTextField;
    @FXML private TextField albumTextField;
    @FXML private Label lengthLabel;
    @FXML private ComboBox<Difficulty> difficultyComboBox;
    @FXML private TextField yearTextField;
    @FXML private Label existsLabel;

    private Midi midi;
    private boolean exists;

    @Inject private MidiService midiService;

    public Node getView() {
        return root;
    }

    public void setMidi(Midi midi) {
        setMidi(midi, false);
    }

    public void setMidi(Midi midi, boolean add) {
        this.midi = midi;
        String name = midi.getName();
        if (add) {
            songNameTextField.textProperty().addListener(observable -> {
                exists = midiService.existsByName(name);
                existsLabel.setVisible(exists);
            });
        }
        songNameTextField.setText(name);
        composerTextField.setText(midi.getComposer());
        genreTextField.setText(midi.getGenre());
        albumTextField.setText(midi.getAlbum());
        lengthLabel.setText(DateUtils.toMinSec(midi.getLength()));
        Difficulty difficulty = midi.getDifficulty();
        difficultyComboBox.setValue(difficulty != null ? difficulty: Difficulty.NORMAL);
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
                .setDifficulty(difficultyComboBox.getValue())
                .setYear(yearTextField.getText())
                .createMidi();
    }

    public boolean isValid() {
        return !exists;
    }

    @FXML
    private void initialize() {
        difficultyComboBox.getItems().addAll(Difficulty.values());
        difficultyComboBox.setValue(Difficulty.NORMAL);
    }
}
