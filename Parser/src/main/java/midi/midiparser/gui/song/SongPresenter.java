package midi.midiparser.gui.song;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
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
import midi.midiparser.gui.main.MainPresenter;

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


    @Inject private MidiService midiService;
    @Inject private MainPresenter mainPresenter;

    private Midi midi;

    public Node getView() {
        return root;
    }

    public void setMidi(Midi midi) {
        this.midi = midi;
        songNameTextField.setText(midi.getName());
        lengthLabel.setText(DateUtils.toMinSec(midi.getLength()));
    }

    private void handleSave(ActionEvent event) {
//        String midiName = midi.getFileName();
            //This covers all cases
//            if(midiInfo.isTextOutput()) { //no output just skip
//                PrintWriter printer;
//                File output = midiInfo.getOutput();
//                if (output != null && !isBatch) { //Single file output specified, save in file
//                    printer = new PrintWriter(output);
//                } else {
//                    if (output == null) {  //No save information given, will use default 'textResults'
//                        Path textResultsPath = Paths.get(TXT_RESULT_FOLDER);
//                        if (Files.notExists(textResultsPath)) {
//                            textResultsPath = Files.createDirectory(textResultsPath);
//                        }
//                        output = textResultsPath.toFile();
//                    } // else save information give, will use the given output
//                    File file = new File(output, String.format("%s%s", midiName, TXT_FORMAT));
//                    printer = new PrintWriter(file);
//                }
//                printer.println(midi);
//                printer.close();
//            }

        Midi toSave = MidiBuilder.newInstance()
                .setData(midi.getData())
                .setName(songNameTextField.getText())
                .setComposer(composerTextField.getText())
                .setGenre(genreTextField.getText())
                .setAlbum(albumTextField.getText())
                .setLength(midi.getLength())
                .setDifficulty(difficultyComboBox.getValue().getValue())
                .setYear(yearTextField.getText())
                .createMidi();

        final Task<Midi> saveTask = new Task<Midi>()
        {
            protected Midi call() throws Exception
            {
               return midiService.updateMidi(toSave);
            }
        };
        new Thread(saveTask).start();
    }

    @FXML
    private void initialize() {
        difficultyComboBox.getItems().addAll(Difficulty.values());
        difficultyComboBox.setValue(Difficulty.NORMAL);
    }
}
