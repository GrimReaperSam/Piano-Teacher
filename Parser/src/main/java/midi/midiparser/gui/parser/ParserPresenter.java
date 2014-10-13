package midi.midiparser.gui.parser;

import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleListProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import midi.common.data.MIDI;
import midi.common.service.Midi;
import midi.common.service.MidiService;
import midi.midiparser.gui.main.MainPresenter;
import midi.midiparser.model.MidiInfo;
import midi.midiparser.parser.MidiParser;

import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

public class ParserPresenter {

    private static final String MIDI_SAVE_DIRECTORY = "MIDI_SAVEDIR";
    private static final String OUTPUT_SAVE_DIRECTORY = "OUTPUT_SAVEDIR";
    private static final String XML_RESULT_FOLDER = "results";
    private static final String XML_FORMAT = ".xml";
    private static final String TXT_RESULT_FOLDER = "textResults";
    private static final String TXT_FORMAT = ".txt";

    @FXML private Node root;
    @FXML private TextField midiFile;
    @FXML private CheckBox txtCheckbox;
    @FXML private HBox outputHbox;
    @FXML private TextField outputFile;
    @FXML private Slider multiplier;
    @FXML private Label tempoLabel;
    @FXML private Button parseButton;
    @FXML private Label batchNoticeLabel;

    @Inject private MidiService midiService;
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
        mainPresenter.showInfo("Parsing complete");
    }

    @FXML
    private void handleExit(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void handleClear(ActionEvent event) {
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

    private MIDI parse(File midiFile) {
        try {
            return new MidiParser().parse(midiFile, midiInfo.getMultiplier());
        } catch (Exception e) { //Should never be here unless file was modified
            mainPresenter.showError("Specified path does not lead to a midi file, please make sure you have the correct path.");
            return null;
        }
    }

    /**
     * Given the list of midiFiles in the MidiInfo model, it will parse each file, and save an xml result along a txt one if required
     */
    private void save() {
        boolean isBatch = midiInfo.getMidiFiles().size() > 1;
        midiInfo.getMidiFiles().stream().map(this::parse).forEach((midi) -> {
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

                StringWriter sw = new StringWriter();
                jxbM.marshal(midi, sw);
                midiService.updateMidi(new Midi(null, midiName, sw.toString()));

                //This covers all cases
                if(midiInfo.isTextOutput()) { //no output just skip
                    PrintWriter printer;
                    File output = midiInfo.getOutput();
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
                        File file = new File(output, String.format("%s%s", midiName, TXT_FORMAT));
                        printer = new PrintWriter(file);
                    }
                    printer.println(midi);
                    printer.close();
                }
            } catch (Exception ignored) {
            }
        });
    }

    /**
     * @param midi the requested song
     * @return "songName" or "songName0.3"
     */
    private String getFileName(MIDI midi) {
        return midi.getFileName().split("\\.")[0] + (midi.getMultiplier() == 1? "": midi.getMultiplier());
    }
}
