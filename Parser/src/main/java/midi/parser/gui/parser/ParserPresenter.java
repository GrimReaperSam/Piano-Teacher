package midi.parser.gui.parser;

import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import midi.common.data.ParsedMidi;
import midi.common.service.Midi;
import midi.common.service.MidiBuilder;
import midi.common.service.MidiService;
import midi.parser.gui.main.MainPresenter;
import midi.parser.gui.song.SongPresenter;
import midi.parser.parser.MidiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class ParserPresenter {

    private static final String MIDI_SAVE_DIRECTORY = "MIDI_SAVEDIR";

    @FXML private Node root;
    @FXML private GridPane songsPane;

    private List<SongPresenter> songsPresenters = new ArrayList<>();

    @Inject private MainPresenter mainPresenter;
    @Inject private MidiService midiService;
    @Autowired private ApplicationContext appContext;

    public Node getView() {
        return root;
    }

    @FXML
    private void handleSelectMidis(ActionEvent event) {
        Preferences userPrefs = Preferences.userRoot().node(getClass().getName());
        File initialDirectory = new File (userPrefs.get(MIDI_SAVE_DIRECTORY, new File(".").getAbsolutePath()));

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("MIDI files (*.mid)", "*.mid");
        fileChooser.getExtensionFilters().add(extensionFilter);
        fileChooser.setInitialDirectory(initialDirectory);

        List<File> files = fileChooser.showOpenMultipleDialog(root.getScene().getWindow());
        if (files != null) {
            userPrefs.put(MIDI_SAVE_DIRECTORY, files.get(0).getParent());
            Midi[] newMidis = files.stream().map(this::parse).map(midi -> {
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
            for (int i=0; i< newMidis.length; i++) {
                SongPresenter songPresenter = appContext.getBean(SongPresenter.class);
                songPresenter.setMidi(newMidis[i]);
                songsPane.add(songPresenter.getView(), 0, i);
                songsPresenters.add(songPresenter);
            }
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        final Task<Void> saveTask = new Task<Void>()
        {
            protected Void call() throws Exception
            {
                midiService.addAll(songsPresenters.stream().map(SongPresenter::getMidi).toArray(Midi[]::new));
                return null;
            }
        };
        saveTask.stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(Worker.State.SUCCEEDED)) {
                mainPresenter.showBase(midiService.getAll());
            } else if (newValue.equals(Worker.State.FAILED)) {
                mainPresenter.showBase(midiService.getAll());
                mainPresenter.showError(saveTask.getException().toString());
            }
        });
        new Thread(saveTask).start();
    }

    private ParsedMidi parse(File midiFile) {
        try {
            return new MidiParser().parse(midiFile);
        } catch (Exception e) { //Should never be here unless file was modified
            mainPresenter.showError("Specified path does not lead to a midi file, please make sure you have the correct path.");
            return null;
        }
    }

    public void clear() {
        songsPane.getChildren().clear();
    }
}
