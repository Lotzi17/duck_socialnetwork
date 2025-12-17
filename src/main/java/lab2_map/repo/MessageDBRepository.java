package lab2_map.repo;

import lab2_map.connections.DatabaseConnection;
import lab2_map.domain.Message;
import lab2_map.domain.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageDBRepository {

    private final Connection conn;

    public MessageDBRepository() {
        this.conn = DatabaseConnection.getConnection();
    }

    private Message buildMessage(ResultSet rs, UserDBRepository userRepo) throws SQLException {

        Long id = rs.getLong("id");
        Long senderId = rs.getLong("sender_id");
        Long receiverId = rs.getLong("receiver_id");
        String content = rs.getString("content");
        LocalDateTime timestamp = rs.getTimestamp("sent_at").toLocalDateTime();

        User sender = userRepo.findOne(senderId);
        User receiver = userRepo.findOne(receiverId);

        Message msg = new Message(sender, receiver, content);
        msg.setId(id);
        msg.setTimestamp(timestamp);

        return msg;
    }

    public Long save(Message message) {

        String sql = """
            INSERT INTO messages(sender_id, receiver_id, content, sent_at)
            VALUES (?, ?, ?, ?) RETURNING id
            """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, message.getSender().getId());
            ps.setLong(2, message.getReceiver().getId());
            ps.setString(3, message.getContent());
            ps.setTimestamp(4, Timestamp.valueOf(message.getTimestamp()));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getLong("id");

        } catch (SQLException e) {
            System.out.println("Error saving message: " + e.getMessage());
        }

        return null;
    }

    public List<Message> findMessagesForUser(Long userId, UserDBRepository userRepo) {

        List<Message> list = new ArrayList<>();

        String sql = """
            SELECT * FROM messages 
            WHERE receiver_id = ? 
            ORDER BY sent_at DESC
            """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(buildMessage(rs, userRepo));
            }

        } catch (SQLException e) {
            System.out.println("Error at findMessagesForUser: " + e.getMessage());
        }

        return list;
    }

    public List<Message> findAll(UserDBRepository userRepo) {

        List<Message> list = new ArrayList<>();

        String sql = "SELECT * FROM messages ORDER BY sent_at DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(buildMessage(rs, userRepo));
            }

        } catch (SQLException e) {
            System.out.println("Error at findAll messages: " + e.getMessage());
        }

        return list;
    }

    public void delete(Long id) {

        String sql = "DELETE FROM messages WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error deleting message: " + e.getMessage());
        }
    }
}
