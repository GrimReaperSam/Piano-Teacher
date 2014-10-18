package midi.midiparser.gui.base;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.util.Callback;
import midi.common.service.Midi;
import midi.midiparser.gui.main.MainPresenter;
import midi.midiparser.gui.song.SongPresenter;

import javax.inject.Inject;

public class BasePresenter {

    @FXML private Node root;
    @FXML private ListView<Midi> songsList;
    @FXML private ScrollPane songView;

    @Inject private SongPresenter songPresenter;
    @Inject private MainPresenter mainPresenter;

    public Node getView() {
        return root;
    }

    public void setMidis(Iterable<Midi> midis) {
        midis.forEach(midi -> songsList.getItems().add(midi));
        if (songsList.getItems().size() != 0) {
            songsList.getSelectionModel().select(0);
            songPresenter.setMidi(midis.iterator().next());
            songView.setContent(songPresenter.getView());
        }
    }

    @FXML
    private void handleAdd(ActionEvent event) {
        mainPresenter.showParser();
    }

    @FXML
    private void handleSave(ActionEvent event) {}

    @FXML
    private void handleExit(ActionEvent event) {
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
                        songView.setContent(songPresenter.getView());
                    }
                });
                return cell;
            }
        });
    }
}
