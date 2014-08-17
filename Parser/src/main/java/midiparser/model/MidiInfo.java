package midiparser.model;

import java.io.File;

public class MidiInfo {

    private File midi;
    private File output;
    private boolean textOutput;
    private double  multiplier;

    public MidiInfo() {
        multiplier = 1;
    }

    public File getMidi() {
        return midi;
    }

    public void setMidi(File midi) {
        this.midi = midi;
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


}
