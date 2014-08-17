import midiparser.mididata.MIDI;
import midiparser.model.MidiInfo;
import midiparser.parser.MidiParser;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;

public class ParserTest {


    @Test
    public void testTurkishMarch() throws JAXBException, URISyntaxException {
        testFile("Mozart - Turkish March");
    }
    @Test
    public void testDrifting() throws JAXBException, URISyntaxException {
        testFile("Andy Mckee - Drifting (Pro)");
    }
    @Test
    public void testRiverFlowsInYou() throws JAXBException, URISyntaxException {
        testFile("Yiruma-River_Flows_In_You_(ver_2)_Guitar_Pro_Tab");
    }

    private void testFile(String midiFileName) throws JAXBException, URISyntaxException {
        MidiInfo info = new MidiInfo();
        info.setMidi(new File(ParserTest.class.getResource(midiFileName + ".mid").toURI()));
        MidiParser parser = new MidiParser();
        MIDI result = parser.parse(info);

        JAXBContext context = JAXBContext.newInstance(MIDI.class);
        Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
        MIDI expected = (MIDI) jaxbUnmarshaller.unmarshal(new File(ParserTest.class.getResource(midiFileName + ".xml").toURI()));

        assertEquals(result.toString(), expected.toString());
    }

}
