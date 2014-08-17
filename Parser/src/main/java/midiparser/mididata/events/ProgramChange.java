package midiparser.mididata.events;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ProgramChange extends Event {

    private int program; //TODO list of programs (name)

    public ProgramChange(int program, long ticks, double time) {
        super(ticks, time);
        this.program = program;
    }

    protected ProgramChange(){}

    public int getProgram() {
        return program;
    }

    public void setProgram(int program) {
        this.program = program;
    }

    @Override
    public String toString() {
        return super.toString() + "Program change: " + program;
    }
}
