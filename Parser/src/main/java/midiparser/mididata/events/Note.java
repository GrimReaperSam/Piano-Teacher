package midiparser.mididata.events;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Note extends Event {

    private String key;
    private int volume;
    private double duration;

    private Note(NoteBuilder b) {
        super(b.ticks, b.time);
        this.key = b.key;
        this.volume = b.volume;
        this.duration = b.duration;
    }

    protected Note(){
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public long getDuration() {
        return (long) (duration);
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public static class NoteBuilder {
        private String key;
        private long ticks;
        private double time;
        private double duration;
        private int volume;

        public NoteBuilder(String key) {
            this.key = key;
        }

        public NoteBuilder ticks(long ticks) {
            this.ticks = ticks;
            return this;
        }

        public long getTicks() {
            return ticks;
        }
        public NoteBuilder time(double time) {
            this.time = time;
            return this;
        }

        public NoteBuilder duration(double duration) {
            this.duration = duration;
            return this;
        }

        public NoteBuilder volume(int volume) {
            this.volume = volume;
            return this;
        }

        public Note build() {
            return new Note(this);
        }
    }

    @Override
    public String toString() {
        return super.toString() + "Note:" + key + " Duration:" + duration / 1000 + "ms Volume:" + volume;
    }
}
