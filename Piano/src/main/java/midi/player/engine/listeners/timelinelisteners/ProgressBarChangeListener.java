package midi.player.engine.listeners.timelinelisteners;

import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.control.Slider;

public class ProgressBarChangeListener extends BaseTimelineListener implements InvalidationListener {

    public ProgressBarChangeListener(Timeline timeline, Slider progressBar) {
        super(timeline, progressBar);
    }

    @Override
    public void invalidated(Observable observable) {
    }
}
