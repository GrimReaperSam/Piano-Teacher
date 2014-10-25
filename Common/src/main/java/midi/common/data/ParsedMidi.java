package midi.common.data;

import midi.common.util.DateUtils;

import javax.sound.midi.Sequence;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ParsedMidi {

    private String fileName;
    private int type;
    private long ticks;
    private long microseconds;

    private int resolution;
    private float divisionType;

    private float microsecondsPerBeat= 500000; //Only used in PPQ mode

    private int trackCount;

    @XmlElement(name = "track")
    @XmlElementWrapper(name = "tracks")
    private List<Track> tracks;

    public ParsedMidi() {
    }

    public List<Track> getTracks() {
        if (tracks == null) {
            tracks = new ArrayList<>();
        }
        return tracks;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getTicks() {
        return ticks;
    }

    public void setTicks(long ticks) {
        this.ticks = ticks;
    }

    public long getMicroseconds() {
        return microseconds;
    }

    public void setMicroseconds(long microseconds) {
        this.microseconds = microseconds;
    }

    public int getTrackCount() {
        return trackCount;
    }

    public void setTrackCount(int trackCount) {
        this.trackCount = trackCount;
    }

    public int getResolution() {
        return resolution;
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    public float getDivisionType() {
        return divisionType;
    }

    public void setDivisionType(float divisionType) {
        this.divisionType = divisionType;
    }

    public double toMicros(long ticks) {
        float originalTime;
        if (getDivisionType()== Sequence.PPQ) {
            originalTime = ticks * microsecondsPerBeat;
        } else {
            float microsecondsPerTick = 1000000.0f / (divisionType * resolution);
            originalTime =  ticks * microsecondsPerTick;
        }
        return originalTime;
    }

    public void updateMPB(int microsecondPerTick) {
        if (getDivisionType() == Sequence.PPQ) {
            microsecondsPerBeat = microsecondPerTick / getResolution();
        } else {
            throw new IllegalStateException("Cannot update MPB in SMTP mode");
        }
    }

    public float getMicrosecondsPerBeat() {
        return microsecondsPerBeat;
    }

//    public int getBPM() {
//        if (getDivisionType() == Sequence.PPQ) {
//            float bpm = 60000000 / (microsecondsPerBeat * getResolution());
//            return (int) (Math.round(bpm * 100.0f) / 100.0f);
//        } else {
//            throw new IllegalStateException("Cannot update MPB in SMTP mode");
//        }
//    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("File: ").append(fileName).append("\n");
        sb.append("MIDI file format: ").append(type).append("\n");
        sb.append("Length: ").append(ticks).append(" ticks\n");
        sb.append("Duration: ").append(DateUtils.toMinSec(microseconds)).append(" microseconds\n");
        sb.append("Number of tracks: ").append(trackCount).append("\n");
        for (int index=0; index<tracks.size(); index++) {
            sb.append("---------------------------------------------------------------------------\n");
            sb.append("Track ").append(index).append(":\n");
            sb.append("---------------------------------------------------------------------------\n");
            sb.append(tracks.get(index));
        }
        return sb.toString();
    }

}
