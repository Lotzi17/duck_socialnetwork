package lab2_map.domain;

import java.util.*;

public class RaceEvent extends Event {
    private List<Duck> participants;
    private List<Double> buoysDistances;
    private int M;

    public RaceEvent(Long id, String name, List<Duck> allDucks, List<Double> distances, int M) {
        super(id, name);
        this.M = M;
        this.buoysDistances = distances;
        this.participants = selectParticipants(allDucks);
    }

    private List<Duck> selectParticipants(List<Duck> allDucks) {
        List<Duck> swimmers = new ArrayList<>();
        for (Duck d : allDucks) {
            if (d.getType() == UserType.SWIMMING || d.getType() == UserType.FLYING_AND_SWIMMING) {
                swimmers.add(d);
            }
        }
        swimmers.sort(Comparator.comparingDouble(Duck::getEndurance).reversed());
        return swimmers.subList(0, Math.min(M, swimmers.size()));
    }

    public void simulateRace() {
        System.out.println("Starting Race Event: " + name);
        notifySubscribers("Race has started!");
        for (int i = 0; i < participants.size(); i++) {
            Duck duck = participants.get(i);
            double time = calculateRaceTime(duck);
            System.out.printf("Duck %s on lane %d: t = %.3f s%n", duck.getUsername(), i + 1, time);
        }
        notifySubscribers("Race has finished!");
    }

    private double calculateRaceTime(Duck duck) {
        double maxTime = 0;
        for (double d : buoysDistances) {
            double t = 2 * d / duck.getSpeed();
            if (t > maxTime) maxTime = t;
        }
        return maxTime;
    }

    public int getMaxParticipants() {
        return M;
    }

    public List<Double> getDistances() {
        return buoysDistances;
    }

    public List<Duck> getAllDucks() {
        List<Duck> combined = new ArrayList<>(participants);
        return combined;
    }

    public List<Duck> getParticipants() {
        return participants;
    }

}
