package midi.player.gui.main;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import midi.common.service.Midi;
import midi.player.gui.chooser.ChooserPresenter;
import midi.player.gui.piano.PianoPresenter;

import javax.inject.Inject;

public class MainPresenter {

    @FXML private Parent root;
    @FXML private BorderPane contentArea;

    private Stage primaryStage;

    @Inject private PianoPresenter pianoPresenter;
    @Inject private ChooserPresenter chooserPresenter;

    public Parent getView() {
        return root;
    }

    public void showPiano(Midi midi) {
        pianoPresenter.setMidi(midi);
        contentArea.setCenter(pianoPresenter.getView());
        primaryStage.sizeToScene();
        pianoPresenter.configureLabel();
    }

    public void showChooser(Iterable<Midi> midis) {
        chooserPresenter.setCollection(midis);
        contentArea.setCenter(chooserPresenter.getView());
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}