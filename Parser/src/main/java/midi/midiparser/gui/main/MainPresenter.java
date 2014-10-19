package midi.midiparser.gui.main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import midi.common.data.ParsedMidi;
import midi.common.service.Midi;
import midi.common.service.MidiBuilder;
import midi.common.service.MidiService;
import midi.midiparser.gui.base.BasePresenter;
import midi.midiparser.gui.dialog.DialogPresenter;
import midi.midiparser.gui.parser.ParserPresenter;
import midi.midiparser.gui.song.SongPresenter;

import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

public class MainPresenter {

    @FXML private Parent root;
    @FXML private BorderPane contentArea;

    @Inject private BasePresenter basePresenter;
    @Inject private ParserPresenter parserPresenter;
    @Inject private SongPresenter songPresenter;
    @Inject private FXMLLoader dialogLoader;
    @Inject private MidiService midiService;

    public Parent getView() {
        return root;
    }

    public void showParser() {
        contentArea.setCenter(parserPresenter.getView());
    }

    public void showSong(ParsedMidi midi) {
        StringWriter sw = new StringWriter();
        try {
            JAXBContext context = JAXBContext.newInstance(ParsedMidi.class);
            Marshaller jxbM = context.createMarshaller();
            jxbM.marshal(midi, sw);
        } catch (JAXBException e) {
            showError("Unable to create XML parser, make sure all your classes properly obey the annotation format!");
            return;
        }
        Midi newInstance = MidiBuilder.newInstance()
                .setName(midi.getFileName())
                .setLength(midi.getMicroseconds())
                .setData(sw.toString())
                .createMidi();
        midiService.updateMidi(newInstance);
//        songPresenter.setMidi(newInstance);
//        contentArea.setCenter(songPresenter.getView());
    }

    public void showBase(Iterable<Midi> midis) {
        basePresenter.setMidis(midis);
        contentArea.setCenter(basePresenter.getView());
    }

    public void showError(String text) {
        showDialog("Error", text);
    }

    public void showInfo(String info) {
        showDialog("Information", info);
    }

    private void showDialog(String title, String text) {
            Stage stage = new Stage();
            stage.setScene(new Scene(dialogLoader.getRoot()));
            stage.setTitle(title);
            stage.show();

            DialogPresenter dialogPresenter = dialogLoader.getController();
            dialogPresenter.setDialog(stage);
            dialogPresenter.setText(text);
    }

}