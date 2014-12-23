package midi.parser.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import midi.parser.gui.main.MainPresenter;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MidiParserApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MidiParserAppFactory.class);
        MainPresenter mainPresenter = context.getBean(MainPresenter.class);
        mainPresenter.setPrimaryStage(stage);
        mainPresenter.showLogin();
        Scene scene = new Scene(mainPresenter.getView());
        stage.setTitle("Midi Parser");
        stage.getIcons().add(new Image(getClass().getResource("/images/piano.png").openStream()));
        stage.setResizable(false);
        stage.setScene(scene);

        stage.show();
    }
}
