package midiparser.mididata.events;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PitchWheel extends Event{

    private int wheel;

    public PitchWheel(int wheel, long ticks, double time) {
        super(ticks, time);
        this.wheel = wheel;
    }

    protected PitchWheel(){}

    public int getWheel() {
        return wheel;
    }

    public void setWheel(int wheel) {
        this.wheel = wheel;
    }

    @Override
    public String toString() {
        return super.toString() + "Pitch wheel change " + wheel;
    }
}
