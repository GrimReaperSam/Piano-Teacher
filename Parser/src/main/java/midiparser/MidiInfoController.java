package midiparser;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleListProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import midiparser.mididata.MIDI;
import midiparser.model.MidiInfo;
import midiparser.parser.MidiParser;
import midiparser.utils.Dialogs;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

public class MidiInfoController {

    public static final String MIDI_SAVE_DIRECTORY = "MIDI_SAVEDIR";
    private static final String OUTPUT_SAVE_DIRECTORY = "OUTPUT_SAVEDIR";
    public static final String XML_RESULT_FOLDER = "results";
    public static final String XML_FORMAT = ".xml";
    private static final String TXT_RESULT_FOLDER = "textResults";
    public static final String TXT_FORMAT = ".txt";

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

    @FXML
    private Label batchNoticeLabel;

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
            info.getMidiFiles().addAll(files);
        }
    }

    @FXML
    private void handleOutputFileButton(ActionEvent event) {
        Preferences userPrefs = Preferences.userRoot().node(getClass().getName());
        File initialDirectory = new File (userPrefs.get(OUTPUT_SAVE_DIRECTORY, new File(".").getAbsolutePath()));

        File chosenFile;
        if (info.getMidiFiles().size() == 1) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(initialDirectory);
            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extensionFilter);
            chosenFile = fileChooser.showSaveDialog(launcher.getPrimaryStage());
        } else {
            DirectoryChooser dirChooser = new DirectoryChooser();
            dirChooser.setInitialDirectory(initialDirectory);
            chosenFile = dirChooser.showDialog(launcher.getPrimaryStage());
        }
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
        save(info.getMidiFiles());
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
        batchNoticeLabel.visibleProperty().bind(new SimpleListProperty<>(info.getMidiFiles()).sizeProperty().greaterThan(1));
    }

    private MIDI parse(File midiFile) {
        try {
            return new MidiParser().parse(midiFile, info.getMultiplier());
        } catch (Exception e) { //Should never be here unless file was modified
            Dialogs.errorDialog("Specified path does not lead to a midi file, please make sure you have the correct path.");
            return null;
        }
    }

    private void save(List<File> midiFiles) {
        boolean isBatch = midiFiles.size() > 1;
        midiFiles.stream().map(this::parse).forEach((midi) -> {
            String midiName = getFileName(midi);
            try {
                Path path = Paths.get(String.format("%s/%s%s", XML_RESULT_FOLDER, midiName, XML_FORMAT));
                Files.createDirectories(path.getParent());

                Files.deleteIfExists(path);
                Path midiPath = Files.createFile(path);

                JAXBContext context = JAXBContext.newInstance(MIDI.class);
                Marshaller jxbM = context.createMarshaller();

                jxbM.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                jxbM.marshal(midi, midiPath.toFile());

                //This covers all cases
                if(info.isTextOutput()) { //no output just skip
                    PrintWriter printer;
                    File output = info.getOutput();
                    if (output != null && !isBatch) { //Single file output specified, save in file
                        printer = new PrintWriter(output);
                    } else {
                        if (output == null) {  //No save information given, will use default 'textResults'
                            Path textResultsPath = Paths.get(TXT_RESULT_FOLDER);
                            if (Files.notExists(textResultsPath)) {
                                textResultsPath = Files.createDirectory(textResultsPath);
                            }
                            output = textResultsPath.toFile();
                        } // else save information give, will use the given output
                        File file = new File(output, midiName + TXT_FORMAT);
                        printer = new PrintWriter(file);
                    }
                    printer.println(midi);
                    printer.close();
                }
            } catch (Exception ignored) {
            }
        });
    }

    private String getFileName(MIDI midi) {
        return midi.getFileName().split("\\.")[0] + (midi.getMultiplier() == 1? "": midi.getMultiplier());
    }

}
