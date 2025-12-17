package lab2_map.repo;

import lab2_map.connections.DatabaseConnection;
import lab2_map.domain.Event;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDBRepository {

    private final Connection conn;

    public EventDBRepository() {
        this.conn = DatabaseConnection.getConnection();
    }

    private Event buildEvent(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String name = rs.getString("name");
        return new Event(id, name);
    }

    public Long save(Event e, Long creatorId) {

        String sql = """
            INSERT INTO events(name, creator_id)
            VALUES (?, ?) RETURNING id
            """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, e.getName());

            if (creatorId != null) {
                ps.setLong(2, creatorId);
            } else {
                ps.setNull(2, Types.BIGINT);
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Long id = rs.getLong("id");
                e.setId(id);
                return id;
            }

        } catch (SQLException ex) {
            System.out.println("Error saving event: " + ex.getMessage());
        }

        return null;
    }

    public Event findOne(Long id) {

        String sql = "SELECT * FROM events WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return buildEvent(rs);

        } catch (SQLException e) {
            System.out.println("Error finding event: " + e.getMessage());
        }

        return null;
    }

    public List<Event> findAll() {

        List<Event> list = new ArrayList<>();
        String sql = "SELECT * FROM events";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(buildEvent(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error loading events: " + e.getMessage());
        }

        return list;
    }

    public void delete(Long id) {

        String sql = "DELETE FROM events WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error deleting event: " + e.getMessage());
        }
    }
}
