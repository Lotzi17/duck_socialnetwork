package lab2_map.repo;
import java.util.List;
import lab2_map.domain.Message;

public interface MessageRepository {
    void save(Message message);
    List<Message> findConversation(Long userId1, Long userId2);

}
