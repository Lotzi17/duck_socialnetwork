package lab2_map.repo;

import lab2_map.domain.Message;
import lab2_map.connections.DatabaseConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageDBRepository implements MessageRepository {

    @Override
    public void save(Message message) {
        String sql = """
            INSERT INTO messages(sender_id, receiver_id, content, sent_at)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, message.getFromUserId());
            ps.setLong(2, message.getToUserId());
            ps.setString(3, message.getContent());
            ps.setTimestamp(4, Timestamp.valueOf(message.getDate()));

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Message> findConversation(Long user1, Long user2) {
        String sql = """
            SELECT * FROM messages
            WHERE (sender_id = ? AND receiver_id = ?)
               OR (sender_id = ? AND receiver_id = ?)
            ORDER BY sent_at
        """;

        List<Message> result = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, user1);
            ps.setLong(2, user2);
            ps.setLong(3, user2);
            ps.setLong(4, user1);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                result.add(new Message(
                        rs.getLong("id"),
                        rs.getLong("sender_id"),
                        rs.getLong("receiver_id"),
                        rs.getString("content"),
                        rs.getTimestamp("sent_at").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
