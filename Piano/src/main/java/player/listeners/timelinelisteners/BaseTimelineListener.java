package player.listeners.timelinelisteners;

import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Slider;
import javafx.util.Duration;

public abstract class BaseTimelineListener {

    protected Timeline timeline;
    protected Slider progressBar;

    public BaseTimelineListener(Timeline timeline, Slider progressBar) {
        this.timeline = timeline;
        this.progressBar = progressBar;
    }

    protected void updateProgress() {
        if (progressBar != null) {
            Platform.runLater(() -> {
                Duration currentTime = timeline.getCurrentTime();
                Duration totalDuration = timeline.getTotalDuration();
                progressBar.setDisable(totalDuration.isUnknown());
                if (!progressBar.isDisabled() && totalDuration.greaterThan(Duration.ZERO) && !progressBar.isValueChanging()) {
                    progressBar.setValue(currentTime.divide(totalDuration.toMillis()).toMillis() * 100.0);
                }
            });
        }
    }
}
