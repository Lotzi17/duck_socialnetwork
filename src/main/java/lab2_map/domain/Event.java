package lab2_map.domain;

import java.util.ArrayList;
import java.util.List;

public class Event extends Entity<Long> implements Observable {
    protected String name;
    protected List<Observer> subscribers = new ArrayList<>();

    public Event(Long id, String name) {
        super.setId(id);
        this.name = name;
    }

    public String getName() { return name; }

    @Override
    public void subscribe(Observer observer) {
        if (!subscribers.contains(observer)) {
            subscribers.add(observer);
            System.out.println(((observer instanceof User u) ? u.getUsername() : "Observer")
                    + " subscribed to event: " + name);
        }
    }

    @Override
    public void unsubscribe(Observer observer) {
        subscribers.remove(observer);
        System.out.println("Observer unsubscribed from event: " + name);
    }

    @Override
    public void notifySubscribers(String message) {
        System.out.println("Notifying " + subscribers.size() + " subscribers about event '" + name + "'");
        for (Observer o : subscribers) {
            o.update("[Event: " + name + "] " + message);
        }
    }

    @Override
    public String toString() {
        return "Event: " + name + " (" + subscribers.size() + " subscribers)";
    }
}
