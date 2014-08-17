package midiparser.mididata;

import midiparser.mididata.events.Event;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Track {

    @XmlElementRef(name = "event")
    @XmlElementWrapper(name="events")
    private List<Event> events;

    public List<Event> getEvents() {
        if (events == null) {
            events = new ArrayList<>();
        }
        return events;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        events.stream().forEach((event) -> {
            sb.append(event);
            sb.append('\n');
        });
        return sb.toString();
    }
}
