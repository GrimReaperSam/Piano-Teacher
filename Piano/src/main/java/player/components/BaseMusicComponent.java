package player.components;

public abstract class BaseMusicComponent implements Component {

    private boolean sound = true;

    public boolean getSound() {
        return sound;
    }
    public void toggleSound() {
        sound = !sound;
    }
}
