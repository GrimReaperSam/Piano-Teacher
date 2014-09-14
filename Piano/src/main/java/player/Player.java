package player;

import javafx.animation.Timeline;

public interface Player {

    Timeline getTimeline();

    void preparePlay();

    void play();

    void pause();

    void stop();

    void toggleSound();
}
