package midi.midiparser.mididata.events;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PolyKeyPressure extends Event {

    private String key;
    private int pressure;

    public PolyKeyPressure(String key, int pressure, long ticks, double time) {
        super(ticks, time);
        this.key = key;
        this.pressure = pressure;
    }

    protected PolyKeyPressure(){}

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return super.toString() + "Polyphonic key pressure " + key + " pressure: " + pressure;
    }
}
