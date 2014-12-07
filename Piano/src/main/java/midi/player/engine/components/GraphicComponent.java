package midi.player.engine.components;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import midi.common.data.events.Note;
import midi.player.gui.keys.Key.PianoKey;
import midi.player.gui.piano.PianoPresenter;

import java.util.ArrayList;
import java.util.List;

public class GraphicComponent extends BaseGraphicComponent implements Component {

    private PianoPresenter presenter;
    private List<Note> playing = new ArrayList<>();

    public GraphicComponent(PianoPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void play(Note note) {
        PianoKey key = getKey(note);
        key.resetStyle();
        key.getRectangle().setFill(Color.DARKGREEN);

        key.setPlaying(true);
        if (!playing.contains(note)) {
            playing.add(note);
        }
    }

    @Override
    public void stop(Note note) {
        PianoKey key = getKey(note);
        key.resetStyle();
        key.setPlaying(false);
        playing.remove(note);
    }

    @Override
    public void clear() {
        playing.forEach((note) -> getKey(note).resetStyle());
    }

    @Override
    public void playNext(Note note) {
        PianoKey key = getKey(note);
        Rectangle rectangle = key.getRectangle();
        if (key.isPlaying()) {
            rectangle.setStroke(Color.RED);
        } else {
            key.resetStyle();
            rectangle.setFill(Color.DARKORANGE);
        }
        if (!playing.contains(note)) {
            playing.add(note);
        }
    }

    private PianoKey getKey(Note note) {
        return presenter.getKey(note);
    }
}
