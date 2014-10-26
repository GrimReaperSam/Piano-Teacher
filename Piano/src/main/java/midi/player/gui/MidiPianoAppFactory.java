package midi.player.gui;

import javafx.fxml.FXMLLoader;
import midi.common.service.MidiService;
import midi.player.gui.main.MainPresenter;
import midi.player.gui.piano.PianoPresenter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.httpinvoker.CommonsHttpInvokerRequestExecutor;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.remoting.httpinvoker.HttpInvokerRequestExecutor;

import java.io.IOException;

@Configuration
public class MidiPianoAppFactory {

    @Bean
    public MainPresenter mainPresenter() {
        return loadPresenter("/fxml/Main.fxml");
    }

    @Bean
    public PianoPresenter pianoPresenter() {
        return loadPresenter("/fxml/Piano.fxml");
    }

    @Bean
    public MidiService midiService() {
        return createService("midi.service", MidiService.class);
    }

    @Bean
    public HttpInvokerRequestExecutor httpInvokerRequestExecutor()
    {
        return new CommonsHttpInvokerRequestExecutor();
    }

    private <T> T createService(String endPoint, Class serviceInterface) {
        HttpInvokerProxyFactoryBean factory = new HttpInvokerProxyFactoryBean();
        String serverUrl = String.format("http://localhost:8080/%s", endPoint);
        factory.setServiceUrl(serverUrl);
        factory.setServiceInterface(serviceInterface);
        factory.setHttpInvokerRequestExecutor(httpInvokerRequestExecutor());
        factory.afterPropertiesSet();
        return (T) factory.getObject();
    }

    private <T> T loadPresenter(String fxmlFile) {
        return (T) loader(fxmlFile).getController();
    }

    private FXMLLoader loader(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.load(getClass().getResourceAsStream(fxmlFile));
            return loader;
        } catch (IOException e) {
            throw new RuntimeException(String.format("Unable to load FXML file '%s'", fxmlFile), e);
        }
    }
}
