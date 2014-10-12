package midi.midiparser.mididata.events;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Tempo extends Event{

    private int tempo;
    private int bpm;

    public Tempo(int tempo, int bpm, long ticks, double time) {
        super(ticks, time);
        this.tempo = tempo;
        this.bpm = bpm;
    }
    protected Tempo(){}
    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public int getBPM() {
        return bpm;
    }

    public void setBPM(int bpm) {
        this.bpm = bpm;
    }

    @Override
    public String toString() {
        return super.toString() + "Set Tempo: " + bpm + " bpm";
    }
}
