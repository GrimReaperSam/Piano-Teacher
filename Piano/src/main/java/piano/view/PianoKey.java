package piano.view;

import javafx.scene.shape.Rectangle;

public class PianoKey extends Rectangle {

    private int note;

    public PianoKey(int x, int y, int w, int h) {
        super(x, y, w, h);
        getStyleClass().add("piano-key");
    }

    public void setNote(int note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Note: " + note;
    }
}