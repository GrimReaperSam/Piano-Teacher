package piano.view;

import javafx.scene.paint.Color;

public class BlackKey extends PianoKey {

    public static final int BLACK_WIDTH = 20;
    public static final int BLACK_HEIGHT = 110;

    public BlackKey(int x, int y) {
        super(x, y, BLACK_WIDTH, BLACK_HEIGHT);
        setFill(Color.BLACK);
    }
}
