package player.piano;

public class BlackKey extends PianoKey {

    private static final int HEIGHT = 110;


    public BlackKey(int x, int y, int width) {
        super(x, y, width, HEIGHT);
    }

    @Override
    public void resetFill() {
        getRectangle().setFill(BLACK);
    }

}
