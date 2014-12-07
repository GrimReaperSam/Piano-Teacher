package midi.player.player.listeners.timelinelisteners;

import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Slider;
import javafx.util.Duration;


public class TimelineChangedListener extends BaseTimelineListener implements ChangeListener<Duration> {

    public TimelineChangedListener(Timeline timeline, Slider progressBar) {
        super(timeline, progressBar);
    }

    @Override
    public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
        updateProgress();
    }
}
