package midi.midiparser.gui.parser;

import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleListProperty;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import midi.common.data.ParsedMidi;
import midi.common.service.Midi;
import midi.common.service.MidiBuilder;
import midi.common.service.MidiService;
import midi.midiparser.gui.main.MainPresenter;
import midi.midiparser.model.MidiInfo;
import midi.midiparser.parser.MidiParser;

import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.StringWriter;
import java.util.List;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParserPresenter {

    private static final String MIDI_SAVE_DIRECTORY = "MIDI_SAVEDIR";
    private static final String OUTPUT_SAVE_DIRECTORY = "OUTPUT_SAVEDIR";

    @FXML private Node root;
    @FXML private TextField midiFile;
    @FXML private CheckBox txtCheckbox;
    @FXML private HBox outputHbox;
    @FXML private TextField outputFile;
    @FXML private Slider multiplier;
    @FXML private Label tempoLabel;
    @FXML private Button parseButton;
    @FXML private Label batchNoticeLabel;

    @Inject private MainPresenter mainPresenter;

    private MidiInfo midiInfo = new MidiInfo();

    public Node getView() {
        return root;
    }

    @FXML
    private void handleMidiFileButton(ActionEvent event) {
        Preferences userPrefs = Preferences.userRoot().node(getClass().getName());
        File initialDirectory = new File (userPrefs.get(MIDI_SAVE_DIRECTORY, new File(".").getAbsolutePath()));

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("MIDI files (*.mid)", "*.mid");
        fileChooser.getExtensionFilters().add(extensionFilter);
        fileChooser.setInitialDirectory(initialDirectory);

        List<File> files = fileChooser.showOpenMultipleDialog(null);
        if (files != null) {
            userPrefs.put(MIDI_SAVE_DIRECTORY, files.get(0).getParent());
            if (files.size() == 1) {
                midiFile.setText(files.get(0).getPath());
            } else {
                String fileName = files.stream().map((file) -> file.getName().split("\\.")[0]).collect(Collectors.joining(", "));
                midiFile.setText(fileName);
            }
            midiInfo.getMidiFiles().addAll(files);
        }
    }

    @FXML
    private void handleOutputFileButton(ActionEvent event) {
        Preferences userPrefs = Preferences.userRoot().node(getClass().getName());
        File initialDirectory = new File (userPrefs.get(OUTPUT_SAVE_DIRECTORY, new File(".").getAbsolutePath()));

        File chosenFile;
        if (midiInfo.getMidiFiles().size() == 1) { //Single Midi file to save, open a save file dialog
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(initialDirectory);
            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extensionFilter);
            chosenFile = fileChooser.showSaveDialog(root.getScene().getWindow());
        } else { //Multiple Midi files to save, open a save folder dialog
            DirectoryChooser dirChooser = new DirectoryChooser();
            dirChooser.setInitialDirectory(initialDirectory);
            chosenFile = dirChooser.showDialog(root.getScene().getWindow());
        }
        if (chosenFile != null) {
            userPrefs.put(OUTPUT_SAVE_DIRECTORY,  chosenFile.getParent());
            midiInfo.setOutput(chosenFile);
            outputFile.setText(chosenFile.getPath());
        }
    }

    @FXML
    private void handleParse(ActionEvent event) {
        midiInfo.setTextOutput(txtCheckbox.isSelected());
        midiInfo.setMultiplier(Math.floor(multiplier.getValue() * 10) / 10);
        save();
    }

    @FXML
    private void handleExit(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void handleClear(ActionEvent event) {
        clear();
    }

    public void clear() {
        midiFile.clear();
        outputFile.clear();
        multiplier.setValue(1);
    }

    @FXML
    private void initialize() {
        BooleanBinding isEmpty = midiFile.textProperty().isEmpty();
        txtCheckbox.disableProperty().bind(isEmpty);
        multiplier.disableProperty().bind(isEmpty);
        parseButton.disableProperty().bind(isEmpty);
        tempoLabel.disableProperty().bind(isEmpty);
        outputHbox.visibleProperty().bind(txtCheckbox.selectedProperty());
        outputHbox.managedProperty().bind(txtCheckbox.selectedProperty());
        batchNoticeLabel.visibleProperty().bind(new SimpleListProperty<>(midiInfo.getMidiFiles()).sizeProperty().greaterThan(1));
    }

    private ParsedMidi parse(File midiFile) {
        try {
            return new MidiParser().parse(midiFile, midiInfo.getMultiplier());
        } catch (Exception e) { //Should never be here unless file was modified
            mainPresenter.showError("Specified path does not lead to a midi file, please make sure you have the correct path.");
            return null;
        }
    }

    @Inject private MidiService midiService;
    /**
     * Given the list of midiFiles in the MidiInfo model, it will parse each file, and save an xml result along a txt one if required
     */
    private void save() {
        Stream<ParsedMidi> midiStream = midiInfo.getMidiFiles().stream().map(this::parse);
        Midi[] newSongs = midiStream.map(midi -> {
            try {
                StringWriter sw = new StringWriter();
                JAXBContext.newInstance(ParsedMidi.class).createMarshaller().marshal(midi, sw);
                return MidiBuilder.newInstance()
                        .setName(midi.getFileName())
                        .setLength(midi.getMicroseconds())
                        .setData(sw.toString())
                        .createMidi();
            } catch (JAXBException e) {
                mainPresenter.showError("Unable to create XML parser, make sure all your classes properly obey the annotation format!");
                return null;
            }
        }).toArray(Midi[]::new);
        final Task<Void> saveTask = new Task<Void>()
        {
            protected Void call() throws Exception
            {
                midiService.addAll(newSongs);
                return null;
            }
        };
        saveTask.stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(Worker.State.SUCCEEDED)) {
                mainPresenter.showBase(midiService.getAll());
            }
        });
        new Thread(saveTask).start();
    }
    //Cbkake12
}
