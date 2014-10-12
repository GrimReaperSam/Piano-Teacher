package midi.midiparser.gui;

import javafx.fxml.FXMLLoader;
import midi.midiparser.gui.main.MainPresenter;
import midi.midiparser.gui.parser.ParserPresenter;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

public class MidiParserAppFactory {

    @Bean
    public MainPresenter mainPresenter() {
        return loadPresenter("/fxml/Main.fxml");
    }

    @Bean
    public ParserPresenter parserPresenter() {
        return loadPresenter("/fxml/Parser.fxml");
    }

    private <T> T loadPresenter(String fxmlFile)
    {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.load(getClass().getResourceAsStream(fxmlFile));
            return (T) loader.getController();
        } catch (IOException e) {
            throw new RuntimeException(String.format("Unable to load FXML file '%s'", fxmlFile), e);
        }
    }
}
