package midi.common.data.events;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ControlChange extends Event {

    private int control;
    private int value;

    public ControlChange( int control, int value, long ticks, double time) {
        super(ticks, time);
        this.control = control;
        this.value = value;
    }

    protected ControlChange(){}

    public int getControl() {
        return control;
    }

    public void setControl(int control) {
        this.control = control;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return super.toString() + "Control change " + control + " value: " + value;
    }
}
