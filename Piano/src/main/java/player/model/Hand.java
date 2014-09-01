package player.model;

import java.util.ArrayList;
import java.util.List;

public class Hand {

    public List<Accord> accords;

    public Hand() {
    }

    public List<Accord> getAccords() {
        if (accords == null) {
            accords = new ArrayList<>();
        }
        return accords;
    }

    public void add(Accord accord) {
        getAccords().add(accord);
    }

    public Accord get(int index) {
        return getAccords().get(index);
    }

    public int size() {
        return getAccords().size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hand hand = (Hand) o;

        return !(accords != null ? !accords.equals(hand.accords) : hand.accords != null);

    }

    @Override
    public int hashCode() {
        return accords != null ? accords.hashCode() : 0;
    }
}
