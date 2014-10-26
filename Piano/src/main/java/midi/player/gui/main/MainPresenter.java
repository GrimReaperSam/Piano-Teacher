package midi.player.gui.main;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import midi.common.service.Midi;
import midi.player.gui.piano.PianoPresenter;

import javax.inject.Inject;

public class MainPresenter {

    @FXML private Parent root;
    @FXML private BorderPane contentArea;

    @Inject
    private PianoPresenter pianoPresenter;

    public Parent getView() {
        return root;
    }

    public void showPiano(Midi midi) {
        pianoPresenter.setMidi(midi);
        contentArea.setCenter(pianoPresenter.getView());
    }
}