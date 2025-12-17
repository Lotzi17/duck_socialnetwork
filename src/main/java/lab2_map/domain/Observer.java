package lab2_map.domain;

/**
 * Observer interface.
 * Implemented by users who want to receive event notifications.
 */
public interface Observer {
    /**
     * Called when an observable object (like an Event)
     * sends a notification to all its observers.
     * @param message notification text
     */
    void update(String message);
}
