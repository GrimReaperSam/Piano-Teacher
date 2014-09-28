package player.components;

import javafx.animation.FillTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import midiparser.mididata.events.Note;
import player.PianoController;
import player.piano.PianoKey;

import java.util.ArrayList;
import java.util.List;

public class GraphicComponent extends BaseGraphicComponent implements Component{

    private PianoController contoller;
    private List<Note> playing = new ArrayList<>();

    public GraphicComponent(PianoController contoller) {
        this.contoller = contoller;
    }

    @Override
    public void play(Note note) {
        PianoKey key = getKey(note);
        key.resetStyle();

        FillTransition transition = new FillTransition();
        transition.setShape(key.getRectangle());
        transition.setToValue(Color.DARKGREEN);
        transition.setDuration(Duration.millis(note.getDuration() / 2000));
        transition.play();

        key.setPlaying(true);
        playing.add(note);
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
    }

    private PianoKey getKey(Note note) {
        return contoller.getKey(note);
    }
}
