package midiparser;

import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import midiparser.mididata.MIDI;
import midiparser.model.MidiInfo;
import midiparser.parser.MidiParser;
import midiparser.utils.Dialogs;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

public class MidiInfoController {

    public static final String MIDI_SAVE_DIRECTORY = "MIDI_SAVEDIR";
    private static final String OUTPUT_SAVE_DIRECTORY = "OUTPUT_SAVEDIR";

    @FXML
    private TextField midiFile;

    @FXML
    private CheckBox txtCheckbox;

    @FXML
    private HBox outputHbox;

    @FXML
    private TextField outputFile;

    @FXML
    private Slider multiplier;

    @FXML
    private Label tempoLabel;

    @FXML
    private Button parseButton;

    private MidiParserLauncher launcher;
    private MidiInfo info = new MidiInfo();

    public MidiInfoController() {
    }

    public void setLauncher(MidiParserLauncher launcher) {
        this.launcher = launcher;
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
            info.setMidiFiles(files);
        }
    }

    @FXML
    private void handleOutputFileButton(ActionEvent event) {
        Preferences userPrefs = Preferences.userRoot().node(getClass().getName());
        File initialDirectory = new File (userPrefs.get(OUTPUT_SAVE_DIRECTORY, new File(".").getAbsolutePath()));

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(initialDirectory);
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extensionFilter);

        File chosenFile = fileChooser.showSaveDialog(launcher.getPrimaryStage());
        if (chosenFile != null) {
            userPrefs.put(OUTPUT_SAVE_DIRECTORY,  chosenFile.getParent());
            info.setOutput(chosenFile);
            outputFile.setText(chosenFile.getPath());
        }
    }

    @FXML
    private void handleParse(ActionEvent event) {
        info.setTextOutput(txtCheckbox.isSelected());
        info.setMultiplier(Math.floor(multiplier.getValue() * 10) / 10);
        info.getMidiFiles().forEach(this::parse);
        Dialogs.infoDialog("Parsing complete");
    }

    @FXML
    private void handleExit(ActionEvent event) {
        launcher.getPrimaryStage().close();
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
    }

    private void parse(File midiFile) {
        try {
            if (!midiFile.exists()) {
                Dialogs.errorDialog("Specified path does not lead to a midi file, please make sure you have the correct path.");
                return;
            }

            MIDI midi = new MidiParser().parse(midiFile, info.getMultiplier());
            String midiName = getFileName(midi);
            if(info.isTextOutput()) {
                PrintWriter printer;
                File output = info.getOutput();
                if (info.getOutput() != null) {
                    printer = new PrintWriter(output);
                } else {
                    printer = new PrintWriter(midiName + ".txt");
                }
                printer.println(midi);
                printer.close();
            }
            File dir = new File("results");
            if (!dir.exists()) {
                dir.mkdir();
            }
            File xmlFile = new File("results/" + midiName + ".xml");
            JAXBContext context = JAXBContext.newInstance(MIDI.class);
            Marshaller jxbM = context.createMarshaller();

            jxbM.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jxbM.marshal(midi, xmlFile);
        } catch (Exception e) {
        }
    }

    private String getFileName(MIDI midi) {
        return midi.getFileName().split("\\.")[0] + (midi.getMultiplier() == 1? "": midi.getMultiplier());
    }

}
