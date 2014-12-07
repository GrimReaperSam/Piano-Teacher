package midi.player.engine.listeners;

import javafx.animation.Animation;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.Duration;
import midi.player.engine.Player;

import java.util.List;

public class MultiplierListener implements ChangeListener<Number> {

    private final List<Player> players;

    public MultiplierListener(List<Player> players) {
        this.players = players;
    }

    @Override
    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        players.forEach((player) -> {
            double timeMultipler = newValue.doubleValue() / oldValue.doubleValue();
            Timeline timeline = player.getTimeline();
            Animation.Status previousStatus = timeline.getStatus();
            Duration current = timeline.getCurrentTime();
            player.refresh();
            player.getTimeline().jumpTo(current.multiply(timeMultipler));
            if (previousStatus.equals(Animation.Status.RUNNING)) {
                player.play();
            }
        });
    }
}
