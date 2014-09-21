package midiparser.model;

import java.io.File;
import java.util.List;

public class MidiInfo {

    private File output;
    private boolean textOutput;
    private double  multiplier;
    private List<File> midiFiles;

    public MidiInfo() {
        multiplier = 1;
    }


    public File getOutput() {
        return output;
    }

    public void setOutput(File output) {
        this.output = output;
    }

    public boolean isTextOutput() {
        return textOutput;
    }

    public void setTextOutput(boolean textOutput) {
        this.textOutput = textOutput;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }

    public void setMidiFiles(List<File> midiFiles) {
        this.midiFiles = midiFiles;
    }

    public List<File> getMidiFiles() {
        return midiFiles;
    }
}
