package midi.player.gui.chooser;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import midi.common.service.Midi;
import midi.player.gui.main.MainPresenter;
import midi.player.gui.song.SongPresenter;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChooserPresenter {

    @FXML
    private AnchorPane root;
    @FXML
    private GridPane songsPane;

    @Inject private MainPresenter mainPresenter;

    private List<SongPresenter> songsPresenters = new ArrayList<>();

    public Node getView() {
        return root;
    }

    public void setCollection(Iterable<Midi> midis) {
        int index = 0;
        for (Midi midi : midis) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.load(getClass().getResourceAsStream("/fxml/Song.fxml"));
                SongPresenter songPresenter = loader.getController();
                songPresenter.setMidi(midi);
                songPresenter.setMainPresenter(mainPresenter);
                songsPane.add(songPresenter.getView(), 1, index++);
                songsPresenters.add(songPresenter);
            } catch (IOException e) {
                throw new RuntimeException("Unable to load FXML file '/fxml/Song.fxml'", e);
            }
        }
    }
}