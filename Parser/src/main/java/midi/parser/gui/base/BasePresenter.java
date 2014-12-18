package midi.parser.gui.base;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import midi.common.security.SecurityService;
import midi.common.service.Midi;
import midi.parser.gui.main.MainPresenter;
import midi.parser.gui.song.SongPresenter;

import javax.inject.Inject;

public class BasePresenter {

    @FXML private BorderPane root;
    @FXML private ListView<Midi> songsList;
    @FXML private HBox editSongHBox;

    @Inject private SongPresenter songPresenter;
    @Inject private MainPresenter mainPresenter;
    @Inject SecurityService securityService;

    public Node getView() {
        return root;
    }

    public void setMidis(Iterable<Midi> midis) {
        songsList.getItems().clear();
        midis.forEach(midi -> songsList.getItems().add(midi));
        if (songsList.getItems().size() != 0) {
            songsList.getSelectionModel().select(0);
            songPresenter.setMidi(midis.iterator().next());
            editSongHBox.getChildren().setAll(songPresenter.getView());
        }
    }

    @FXML
    private void handleAdd(ActionEvent event) {
        mainPresenter.showParser();
    }

    @FXML
    private void handleLogout(ActionEvent actionEvent) {
        securityService.logout();
        mainPresenter.showLogin();
    }

    @FXML
    private void handleExit(ActionEvent event) {
        securityService.logout();
        Platform.exit();
    }

    @FXML
    private void initialize() {
        songsList.setCellFactory(new Callback<ListView<Midi>, ListCell<Midi>>() {
            @Override
            public ListCell<Midi> call(ListView<Midi> param) {
                final ListCell<Midi> cell = new ListCell<Midi>()
                {
                    protected void updateItem(Midi midi, boolean empty)
                    {
                        super.updateItem(midi, empty);
                        if (!empty)
                        {
                            setText(midi.getName());
                        }
                    }
                };
                cell.setOnMouseClicked(event -> {
                    Midi midi = cell.getItem();
                    if (midi != null) {
                        songPresenter.setMidi(midi);
                        editSongHBox.getChildren().setAll(songPresenter.getView());
                    }
                });
                return cell;
            }
        });
    }
}
