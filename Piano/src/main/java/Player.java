import javafx.animation.FillTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import midiparser.mididata.MIDI;
import midiparser.mididata.events.Event;
import midiparser.mididata.events.Note;
import piano.view.PianoKey;

public class Player {

    private MIDI midi;
    private Timeline timeline;
    private PianoLauncher launcher;

    public Player(MIDI midi) {
        this.midi = midi;
        this.timeline = new Timeline();
    }

    public void setLauncher(PianoLauncher launcher) {
        this.launcher = launcher;
    }

    public void play() {
        Task task = new Task<Void>() {
            @Override
            protected Void call() throws  Exception {
                int count = 0;
                for (midiparser.mididata.Track midiTrack: midi.getTracks()) {
                    count++;
                    if (count > 1) {
                        break;
                    }
                    final int multiplier = 1;
                    for (Event event : midiTrack.getEvents())
                        if (event.getClass().isAssignableFrom(Note.class)) {
                            Note note = (Note) event;
                            PianoKey key = launcher.getKey(note.getValue());
                            Paint fill = key.getFill();

                            KeyFrame startFrame = new KeyFrame(Duration.millis(multiplier * note.getTime() / 1000)
                                    , ev -> {
                                FillTransition transition = new FillTransition(Duration.millis( multiplier * note.getDuration() / 2000), key);
                                transition.setToValue(Color.DARKGREEN);
                                transition.setCycleCount(2);
                                transition.setAutoReverse(true);
                                transition.play();
                                key.noteOn(note.getVolume());
                            });
                            KeyFrame endFrame = new KeyFrame(Duration.millis(multiplier * (note.getTime() + note.getDuration()) / 1000)
                                    , ev -> {
                                key.setFill(fill);
                                key.noteOff();
                            });
                            timeline.getKeyFrames().addAll(startFrame, endFrame);
                        }
                }
                timeline.playFromStart();
                return null;
            }
        };
        new Thread(task).start();
    }

    public void pause() {
        timeline.pause();
    }

    public void resume() {
        timeline.play();
    }
}
