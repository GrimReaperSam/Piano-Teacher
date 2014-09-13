package player;

import javafx.animation.Timeline;

public interface Player {

    Timeline getTimeline();

    void resetTimeline();

    void play();

    void fastPlay();

    void pause();

    void stop();
}
