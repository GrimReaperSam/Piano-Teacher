package midi.midiparser.gui.parser;

import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
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
import java.util.stream.Stream;

public class ParserPresenter {

    private static final String MIDI_SAVE_DIRECTORY = "MIDI_SAVEDIR";

    @FXML private Node root;
    @FXML private Button parseButton;

    @Inject private MainPresenter mainPresenter;
    @Inject private MidiService midiService;

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
            midiInfo.getMidiFiles().addAll(files);
        }
    }

    @FXML
    private void handleParse(ActionEvent event) {
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
            } else if (newValue.equals(Worker.State.FAILED)) {
                mainPresenter.showBase(midiService.getAll());
            }
        });
        new Thread(saveTask).start();
    }

    private ParsedMidi parse(File midiFile) {
        try {
            return new MidiParser().parse(midiFile, midiInfo.getMultiplier());
        } catch (Exception e) { //Should never be here unless file was modified
            mainPresenter.showError("Specified path does not lead to a midi file, please make sure you have the correct path.");
            return null;
        }
    }

}
