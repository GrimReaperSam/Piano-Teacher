package midi.parser.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import midi.common.security.SecurityService;
import midi.common.service.MidiService;
import midi.parser.gui.base.BasePresenter;
import midi.parser.gui.dialog.DialogPresenter;
import midi.parser.gui.login.LoginPresenter;
import midi.parser.gui.main.MainPresenter;
import midi.parser.gui.parser.ParserPresenter;
import midi.parser.gui.song.SongPresenter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.remoting.httpinvoker.CommonsHttpInvokerRequestExecutor;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.remoting.httpinvoker.HttpInvokerRequestExecutor;

import java.io.IOException;

@Configuration
public class MidiParserAppFactory {

    @Bean
    public MainPresenter mainPresenter() {
        return loadPresenter("/fxml/Main.fxml");
    }

    @Bean
    public ParserPresenter parserPresenter() {
        return loadPresenter("/fxml/Parser.fxml");
    }

    @Bean
    @Scope(value = "prototype")
    public SongPresenter songPresenter() {
        return loadPresenter("/fxml/Song.fxml");
    }

    @Bean
    public BasePresenter basePresenter() { return loadPresenter("/fxml/Base.fxml"); }

    @Bean public LoginPresenter loginPresenter() {
        return loadPresenter("/fxml/Login.fxml");
    }

    @Bean
    public DialogPresenter dialogPresenter() {
        FXMLLoader dialogLoader = loader("/fxml/Dialog.fxml");
        Stage stage = new Stage();
        stage.setScene(new Scene(dialogLoader.getRoot()));
        DialogPresenter dialogPresenter = dialogLoader.getController();
        dialogPresenter.setDialog(stage);
        return dialogPresenter;
    }

    @Bean
    public MidiService midiService() {
        return createService("midi.service", MidiService.class);
    }

    @Bean
    public SecurityService securityService() {
        return createService("security.service", SecurityService.class);
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
