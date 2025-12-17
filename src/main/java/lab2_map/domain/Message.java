package lab2_map.domain;

import java.time.LocalDateTime;

/**
 * Represents a message exchanged between two users.
 */
public class Message extends Entity<Long> {
    private static long idCounter = 0;
    private User sender;
    private User receiver;
    private String content;
    private LocalDateTime timestamp;

    public Message(User sender, User receiver, String content) {
        this.setId(++idCounter);
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public User getSender() { return sender; }
    public User getReceiver() { return receiver; }
    public String getContent() { return content; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return "[" + timestamp + "] " + sender.getUsername() +
                " → " + receiver.getUsername() + ": " + content;
    }
}
