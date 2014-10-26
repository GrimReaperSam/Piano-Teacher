package midi.parser.gui.main;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import midi.common.service.Midi;
import midi.parser.gui.base.BasePresenter;
import midi.parser.gui.dialog.DialogPresenter;
import midi.parser.gui.parser.ParserPresenter;

import javax.inject.Inject;

public class MainPresenter {

    @FXML private Parent root;
    @FXML private BorderPane contentArea;

    @Inject private BasePresenter basePresenter;
    @Inject private ParserPresenter parserPresenter;
    @Inject private DialogPresenter dialogPresenter;

    public Parent getView() {
        return root;
    }

    public void showParser() {
        parserPresenter.clear();
        contentArea.setCenter(parserPresenter.getView());
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
        dialogPresenter.getDialog().setTitle(title);
        dialogPresenter.setText(text);
        dialogPresenter.getDialog().show();
    }

}