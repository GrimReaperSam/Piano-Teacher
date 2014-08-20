package piano.view;

public class WhiteKey extends PianoKey {

    public static final int HEIGHT = 180;

    public WhiteKey(int x, int y, int width) {
        super(x, y, width, HEIGHT);
        getStyleClass().add("white-key");
    }
}
