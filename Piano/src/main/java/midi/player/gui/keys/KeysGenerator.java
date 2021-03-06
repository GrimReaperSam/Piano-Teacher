package midi.player.gui.keys;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import midi.player.gui.keys.Key.BlackKey;
import midi.player.gui.keys.Key.WhiteKey;

import java.util.Arrays;
import java.util.List;

public class KeysGenerator {

    private static final List<Integer> BLACK_INDICES = Arrays.asList(0, 1, 3, 4, 5);

    private int whiteNumber;
    private int startNote;
    private int blackOffset;

    public static KeysGenerator newInstance() {
        return new KeysGenerator();
    }

    private KeysGenerator() {}

    public KeysGenerator whiteNumber(int whiteNumber) {
        this.whiteNumber = whiteNumber;
        return this;
    }

    public KeysGenerator startNote(int startNote) {
        this.startNote = startNote;
        return this;
    }

    public KeysGenerator blackOffset(int blackOffset) {
        this.blackOffset = blackOffset;
        return this;
    }

    public Keys generate() {
        Keys result = new Keys();
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        int keyWidth = (int) bounds.getWidth() / whiteNumber;

        int noteIndex = startNote;
        WhiteKey whiteKey;
        BlackKey blackKey;
        for (int i = 0; i < whiteNumber; i++) {
            whiteKey = new WhiteKey(i * keyWidth, 1, keyWidth);
            whiteKey.setNote(noteIndex);
            result.getWhiteNotes().put(noteIndex, whiteKey);
            result.getWhiteGroup().getChildren().add(whiteKey.getRectangle());
            if (BLACK_INDICES.contains((i + blackOffset) % 7)) {
                if (i == whiteNumber - 1) {
                    continue;
                }
                noteIndex += 1;
                blackKey = new BlackKey(i * keyWidth + keyWidth / 2 + 4, 1, keyWidth - 8);
                blackKey.setNote(noteIndex);
                result.getBlackNotes().put(noteIndex, blackKey);
                result.getBlackGroup().getChildren().add(blackKey.getRectangle());
            }
            noteIndex += 1;
        }
        return result;
    }

}
