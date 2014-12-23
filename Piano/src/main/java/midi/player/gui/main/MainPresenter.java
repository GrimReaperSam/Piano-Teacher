package midi.player.gui.main;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import midi.common.service.Midi;
import midi.player.gui.chooser.ChooserPresenter;
import midi.player.gui.login.LoginPresenter;
import midi.player.gui.piano.PianoPresenter;
import midi.player.gui.register.RegisterPresenter;

import javax.inject.Inject;

public class MainPresenter {

    @FXML private Parent root;
    @FXML private BorderPane contentArea;

    private Stage primaryStage;

    @Inject private PianoPresenter pianoPresenter;
    @Inject private ChooserPresenter chooserPresenter;
    @Inject private LoginPresenter loginPresenter;
    @Inject private RegisterPresenter registerPresenter;

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

    public void showLogin() {
        contentArea.setCenter(loginPresenter.getView());
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showRegister() {
        contentArea.setCenter(registerPresenter.getView());
    }
}