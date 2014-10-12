package midi.common.data.events;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class EndOfTrack extends Event{

    public EndOfTrack(long ticks, double time) {
        super(ticks, time);
    }

    protected EndOfTrack(){}

    @Override
    public String toString() {
        return super.toString() + "End of Track";
    }
}
