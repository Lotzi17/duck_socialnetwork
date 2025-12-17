package lab2_map.domain;

import java.time.LocalDateTime;

public class Message {
    private Long id;
    private Long fromUserId;
    private Long toUserId;
    private String content;
    private LocalDateTime date;

    public Message(Long id, Long fromUserId, Long toUserId,
                   String content, LocalDateTime date) {
        this.id = id;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.content = content;
        this.date = date;
    }

    public Long getId() { return id; }
    public Long getFromUserId() { return fromUserId; }
    public Long getToUserId() { return toUserId; }
    public String getContent() { return content; }
    public LocalDateTime getDate() { return date; }
}
