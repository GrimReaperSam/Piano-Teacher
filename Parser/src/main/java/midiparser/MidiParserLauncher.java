package midiparser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import midiparser.mididata.MIDI;
import midiparser.model.MidiInfo;
import midiparser.parser.MidiParser;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.PrintWriter;

public class MidiParserLauncher extends Application {

    private Stage primaryStage;
    private VBox rootLayout;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        primaryStage.setTitle("Midi Parser");
        primaryStage.getIcons().add(new Image("/images/piano.png"));
        primaryStage.setResizable(false);

        FXMLLoader loader = new FXMLLoader(MidiParserLauncher.class.getResource("/view/RootLayout.fxml"));
        rootLayout = loader.load();
        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.show();

        MidiInfoController controller = loader.getController();
        controller.setLauncher(this);
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void parse(MidiInfo info) {
        MIDI midi = new MidiParser().parse(info);
        String midiName = getFileName(midi);
        try {
            if(info.isTextOutput()) {
                PrintWriter printer;
                File output = info.getOutput();
                if (info.getOutput() != null) {
                    printer = new PrintWriter(output);
                } else {
                    printer = new PrintWriter(midiName + ".txt");
                }
                printer.println(midi);
                printer.close();
            }
            File dir = new File("results");
            if (!dir.exists()) {
                dir.mkdir();
            }
            File xmlFile = new File("results/" + midiName + ".xml");
            JAXBContext context = JAXBContext.newInstance(MIDI.class);
            Marshaller jxbM = context.createMarshaller();

            jxbM.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jxbM.marshal(midi, xmlFile);
        } catch (Exception e) {
        }
    }

    private String getFileName(MIDI midi) {
        return midi.getFileName().split("\\.")[0] + (midi.getMultiplier() == 1? "": midi.getMultiplier());
    }
}
