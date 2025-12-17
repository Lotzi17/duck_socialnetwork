package lab2_map.repo;

import lab2_map.connections.DatabaseConnection;
import lab2_map.domain.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventSubscriptionDBRepository {

    private final Connection conn;

    public EventSubscriptionDBRepository() {
        this.conn = DatabaseConnection.getConnection();
    }

    public void subscribe(Long eventId, Long userId) {

        String sql = """
            INSERT INTO event_subscriptions(event_id, user_id)
            VALUES (?, ?)
            """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, eventId);
            ps.setLong(2, userId);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Subscription error: " + e.getMessage());
        }
    }

    public List<User> findUsersForEvent(Long eventId) {

        List<User> list = new ArrayList<>();

        String sql = """
            SELECT u.* FROM users u
            INNER JOIN event_subscriptions es
            ON u.id = es.user_id
            WHERE es.event_id = ?
            """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, eventId);
            ResultSet rs = ps.executeQuery();

            UserDBRepository userRepo = new UserDBRepository();

            while (rs.next()) {
                Long id = rs.getLong("id");
                User u = userRepo.findOne(id);
                list.add(u);
            }

        } catch (SQLException e) {
            System.out.println("Error loading event subscribers: " + e.getMessage());
        }

        return list;
    }

    public void deleteSubscriptionsForEvent(Long eventId) {

        String sql = "DELETE FROM event_subscriptions WHERE event_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, eventId);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error deleting event subscriptions: " + e.getMessage());
        }
    }
}
