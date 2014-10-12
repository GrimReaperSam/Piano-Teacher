package midi.common.data.events;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ChannelPrefix extends Event{

    private int prefix;

    public ChannelPrefix(int prefix, long ticks, double time) {
        super(ticks, time);
        this.prefix = prefix;
    }

    protected ChannelPrefix(){}

    public int getPrefix() {
        return prefix;
    }

    public void setPrefix(int prefix) {
        this.prefix = prefix;
    }

    @Override
    public String toString() {
        return super.toString() + "MIDI Channel Prefix: " + prefix;
    }
}
