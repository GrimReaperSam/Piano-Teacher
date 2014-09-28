package player.piano;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PianoKey {

    private int note;
    private Rectangle rectangle;
    private boolean isPlaying = false;
    protected static Color BLACK = Color.web("#252525");

    public PianoKey(int x, int y, int w, int h) {
        rectangle = new Rectangle(x, y, w, h);
        resetStyle();
    }

    public void resetStyle() {
        rectangle.setStroke(BLACK);
    }

    public void addStyle(String style) {
        rectangle.getStyleClass().add(style);
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

    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    @Override
    public String toString() {
        return "Note: " + note;
    }
}
