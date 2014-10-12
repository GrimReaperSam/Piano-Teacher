package midi.midiparser.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;

public class MidiInfo {

    private File output;
    private boolean textOutput;
    private double  multiplier;
    private ObservableList<File> midiFiles;

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

    public ObservableList<File> getMidiFiles() {
        if (midiFiles == null) {
            midiFiles = FXCollections.observableArrayList();
        }
        return midiFiles;
    }
}
