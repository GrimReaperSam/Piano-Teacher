package player.piano;

import javafx.scene.paint.Color;

public class WhiteKey extends  PianoKey{

    private static final int HEIGHT = 180;

    public WhiteKey(int x, int y, int width) {
        super(x, y, width, HEIGHT);
    }

    @Override
    public void resetStyle() {
        super.resetStyle();
        getRectangle().setFill(Color.WHITE);
    }
}
