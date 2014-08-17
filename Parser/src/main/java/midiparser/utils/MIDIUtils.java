package midiparser.utils;

public class MIDIUtils {

    private static final String[] KEY_NAMES = { "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" };

    public static String getKey(int keyNumber) {
        int nNote = keyNumber % 12;
        int nOctave = keyNumber / 12;
        return KEY_NAMES[nNote] + (nOctave - 1);
    }

}
