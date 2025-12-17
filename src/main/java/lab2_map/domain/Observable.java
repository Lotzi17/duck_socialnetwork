package lab2_map.domain;

/**
 * Observable interface for the Observer design pattern.
 */
public interface Observable {

    /**
     * Adds an observer to the subscriber list.
     * @param observer the observer to subscribe
     */
    void subscribe(Observer observer);

    /**
     * Removes an observer from the subscriber list.
     * @param observer the observer to unsubscribe
     */
    void unsubscribe(Observer observer);

    /**
     * Notifies all subscribed observers with a message.
     * @param message the message to be sent to observers
     */
    void notifySubscribers(String message);
}
