package piano.view;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PianoKey extends Rectangle {

    private int note = 1;

    public PianoKey(int x, int y, int w, int h) {
        super(x, y, w, h);
        setStroke(Color.BLACK);
    }

    public void setNote(int note) {
        this.note = note;
    }

    public int getNote() {
        return note;
    }
}