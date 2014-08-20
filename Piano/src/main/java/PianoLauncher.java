import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import piano.view.BlackKey;
import piano.view.PianoKey;
import piano.view.WhiteKey;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import java.util.Arrays;
import java.util.List;

public class PianoLauncher extends Application {

    private static final int WHITE_NUMBER = 52;
    private static final List<Integer> BLACK_INDICES = Arrays.asList(0, 1, 3, 4, 5);

    private Stage primaryStage;
    private Pane rootLayout;

    @Override
    public void start(Stage stage) throws Exception {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        rootLayout = new Pane();
        Scene scene = new Scene(rootLayout);
        scene.setFill(Color.BLACK);
        scene.getStylesheets().add("pianostyles.css");

        primaryStage = stage;
        primaryStage.setTitle("Piano");
        primaryStage.setResizable(false);
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(500);

        int keyWidth = (int) bounds.getWidth() / WHITE_NUMBER;
        int remainingWidth = (int) (bounds.getWidth() % WHITE_NUMBER);
        int rightMargin = remainingWidth / 4;

        int note = 21;
        WhiteKey whiteKey;
        for (int i = 0; i < WHITE_NUMBER; i++) {
            whiteKey = new WhiteKey(rightMargin + i*keyWidth, 1, keyWidth);
            whiteKey.setNote(note);
            rootLayout.getChildren().add(whiteKey);
            if (BLACK_INDICES.contains((i+5) % 7)) {
                note += 2;
            } else {
                note += 1;
            }
        }

        note = 22;
        BlackKey blackKey;
        for (int i = 0; i < WHITE_NUMBER-1; i++) {
            if (BLACK_INDICES.contains((i+5) % 7)) {
                blackKey = new BlackKey(rightMargin + i * keyWidth + keyWidth/2 + 4, 1, keyWidth - 8);
                blackKey.setNote(note);
                rootLayout.getChildren().add(blackKey);
                if (BLACK_INDICES.contains((i+6) % 7)) {
                    note += 2;
                } else {
                    note += 3;
                }
            }
        }

        primaryStage.setScene(scene);
        primaryStage.show();

        Synthesizer synth = MidiSystem.getSynthesizer();
        synth.open();
        MidiChannel piano = synth.getChannels()[0];
        int c =0;
        for (Node node: rootLayout.getChildren()) {
            c++;
            PianoKey key = (PianoKey) node;
            piano.noteOn(key.getNote(), 100);
            Thread.sleep(200);
            piano.noteOff(key.getNote());
        }
        System.out.println(c);
        synth.close();

    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }

}
