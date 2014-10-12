package midi.midiparser.mididata.events;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class InstrumentName extends Event{

    private String name;

    public InstrumentName(String name, long ticks, double time) {
        super(ticks, time);
        this.name = name;
    }

    protected InstrumentName(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return super.toString() + "Instrument Name: " + name;
    }
}
