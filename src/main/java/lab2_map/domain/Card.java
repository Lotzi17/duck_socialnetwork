package lab2_map.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a flock of ducks.
 */
public class Card<T extends Duck> extends Entity<Long> {
    private String name;
    private List<T> members = new ArrayList<>();

    public Card(Long id, String name) {
        super.setId(id);
        this.name = name;
    }

    public void addDuck(T duck) {
        members.add(duck);
        duck.setCard((Card<Duck>) this);
    }

    public void removeDuck(T duck) {
        members.remove(duck);
        duck.setCard(null);
    }
    /**
     * Calculates the average performance of the flock based on its members' speed and endurance.
     */

    public double getAveragePerformance() {
        if (members.isEmpty()) return 0;
        double total = 0;
        for (T d : members) {
            total += (d.getSpeed() + d.getEndurance()) / 2.0;
        }
        return total / members.size();
    }

    public List<T> getMembers() { return members; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return "Card: " + name + " (avg performance=" + getAveragePerformance() + ")";
    }
}
