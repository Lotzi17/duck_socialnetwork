package lab2_map.domain;

import java.time.LocalDateTime;

public class ReplyMessage extends Message {

    private final Message repliedMessage;

    public ReplyMessage(Long id,
                        Long fromUserId,
                        Long toUserId,
                        String content,
                        LocalDateTime date,
                        Message repliedMessage) {
        super(id, fromUserId, toUserId, content, date);
        this.repliedMessage = repliedMessage;
    }

    public Message getRepliedMessage() {
        return repliedMessage;
    }
}
