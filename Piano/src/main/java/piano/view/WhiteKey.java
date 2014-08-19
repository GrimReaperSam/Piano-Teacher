package piano.view;

import javafx.scene.paint.Color;

public class WhiteKey extends PianoKey {

    public static final int WHITE_WIDTH = 28;
    public static final int WHITE_HEIGHT = 180;

    public WhiteKey(int x, int y) {
        super(x, y, WHITE_WIDTH, WHITE_HEIGHT);
        setFill(Color.WHITE);
    }
}
