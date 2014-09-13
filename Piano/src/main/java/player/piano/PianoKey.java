package player.piano;

import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.animation.FillTransition;
import javafx.animation.StrokeTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import midiparser.mididata.events.Note;
import player.MidiPlayerComponent;

public class PianoKey {

    private int note;
    private Rectangle rectangle;
    private FillTransition transition;
    private MidiPlayerComponent player = MidiPlayerComponent.getInstance();
    private StrokeTransition stroke;

    public PianoKey(int x, int y, int w, int h) {
        rectangle = new Rectangle(x, y, w, h);
        transition = new FillTransition();
        transition.setShape(rectangle);
        transition.setToValue(Color.DARKGREEN);
        transition.setOnFinished(e -> noteOff());

        stroke = new StrokeTransition(transition.getDuration().divide(2), rectangle);
        stroke.setToValue(Color.WHITE);
        stroke.setCycleCount(4);
        stroke.setOnFinished(e -> rectangle.setStrokeWidth(1));

        resetStyle();
    }

    public void resetStyle() {
        rectangle.getStyleClass().setAll("piano-key");
        if (stroke.getStatus().equals(Status.RUNNING)) {
            stroke.stop();
        }
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Note: " + note;
    }

    public void play(Note note, double multiplier) {
        noteOn(note.getVolume());
        transition.setDuration(Duration.millis(multiplier * note.getDuration() / 2000));
        transition.play();
    }

    public void preparePlay() {
        if (transition != null && transition.getStatus().equals(Animation.Status.RUNNING)) {
            rectangle.setStrokeWidth(1);
            stroke.play();
        } else {
            rectangle.getStyleClass().add("prepare-key");
        }
    }

    private void noteOn(int velocity) {
        player.getPiano().noteOn(note, velocity);
    }

    private void noteOff() {
        player.getPiano().noteOff(note);
        resetStyle();
    }

}
