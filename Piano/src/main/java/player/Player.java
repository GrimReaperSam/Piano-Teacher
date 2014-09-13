package player;

import javafx.animation.Timeline;

public interface Player {

    Timeline getTimeline();

    void resetTimeline();

    void preparePlay();

    void play();

    void fastPlay();

    void pause();

    void stop();
}
