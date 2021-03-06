package midi.player.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import midi.player.gui.main.MainPresenter;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MidiPianoApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MidiPianoAppFactory.class);
        MainPresenter mainPresenter = context.getBean(MainPresenter.class);
        mainPresenter.setPrimaryStage(stage);
        mainPresenter.showLogin();
        Scene scene = new Scene(mainPresenter.getView());
        stage.setTitle("Midi Piano");
        stage.setResizable(false);
        stage.setScene(scene);

        stage.show();
    }
}
