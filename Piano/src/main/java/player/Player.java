package player;

import javafx.animation.Timeline;

public interface Player {

    Timeline getTimeline();

    void play();

    void pause();

    void stop();
}
