package midi.midiparser.mididata.events;

import midi.midiparser.utils.DateUtils;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement
@XmlSeeAlso({ChannelPrefix.class, ControlChange.class, EndOfTrack.class, InstrumentName.class,
KeyPressure.class, Note.class, PitchWheel.class, PolyKeyPressure.class, ProgramChange.class,
Tempo.class, TrackName.class})
public abstract class Event {

    private long ticks;
    private double time;

    public Event(long ticks, double time) {
        this.ticks = ticks;
        this.time = time;
    }
    protected Event(){}

    public long getTicks() {
        return ticks;
    }

    public void setTicks(long ticks) {
        this.ticks = ticks;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }
    @Override
    public String toString() {
        return String.format("tick %d, at %s: ", ticks, DateUtils.toMinSec(time));
    }

}
