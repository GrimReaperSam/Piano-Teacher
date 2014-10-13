package midi.midiparser.gui;

import javafx.fxml.FXMLLoader;
import midi.common.service.MidiService;
import midi.midiparser.gui.main.MainPresenter;
import midi.midiparser.gui.parser.ParserPresenter;
import org.springframework.context.annotation.Bean;
import org.springframework.remoting.httpinvoker.CommonsHttpInvokerRequestExecutor;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.remoting.httpinvoker.HttpInvokerRequestExecutor;

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

    @Bean
    public HttpInvokerRequestExecutor httpInvokerRequestExecutor()
    {
        return new CommonsHttpInvokerRequestExecutor();
    }

    @Bean
    public MidiService midiService() {
        return createService("midi.service", MidiService.class);
    }

    protected <T> T createService(String endPoint, Class serviceInterface)
    {
        HttpInvokerProxyFactoryBean factory = new HttpInvokerProxyFactoryBean();
        String serverUrl = String.format("http://localhost:8080/%s", endPoint);
        factory.setServiceUrl(serverUrl);
        factory.setServiceInterface(serviceInterface);
        factory.setHttpInvokerRequestExecutor(httpInvokerRequestExecutor());
        factory.afterPropertiesSet();
        return (T) factory.getObject();
    }
}
