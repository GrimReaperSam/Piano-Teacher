package midi.player.engine.timelines;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.util.Duration;
import midi.player.engine.midiinfo.MidiFile;

public class CountdownGenerator {

    public CountdownGenerator() {}

    public Timeline createCountdown(MidiFile midi, Label countdown) {
        Timeline timeline = new Timeline();

        double COUNTDOWN_TIME = midi.getCountdown() / 1000;
        int measure = midi.getMeasure() != 0 ? midi.getMeasure() : 4;
        IntegerProperty countdownProperty = new SimpleIntegerProperty();
        countdown.textProperty().bind(countdownProperty.asString());

        KeyFrame beginTimer = new KeyFrame(Duration.millis(0)
                , new KeyValue(countdownProperty, 1)
                , new KeyValue(countdown.visibleProperty(), true));

        KeyFrame endTimer = new KeyFrame(Duration.millis(COUNTDOWN_TIME)
                , new KeyValue(countdownProperty, measure)
                , new KeyValue(countdown.visibleProperty(), false));

        timeline.getKeyFrames().addAll(beginTimer, endTimer);
        return timeline;
    }
}
