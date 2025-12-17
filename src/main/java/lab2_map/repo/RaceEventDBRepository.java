package lab2_map.repo;

import lab2_map.connections.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RaceEventDBRepository {

    private final Connection conn;

    public RaceEventDBRepository() {
        this.conn = DatabaseConnection.getConnection();
    }

    public void saveRaceConfig(Long eventId, int participantsCount) {

        String sql = """
            INSERT INTO race_events(event_id, participants_count)
            VALUES (?, ?)
            """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, eventId);
            ps.setInt(2, participantsCount);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error saving race event: " + e.getMessage());
        }
    }

    public void saveBuoy(Long eventId, double distance) {

        String sql = """
            INSERT INTO race_buoys(event_id, distance)
            VALUES (?, ?)
            """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, eventId);
            ps.setDouble(2, distance);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error saving buoy: " + e.getMessage());
        }
    }

    public Integer findParticipantsCount(Long eventId) {

        String sql = "SELECT participants_count FROM race_events WHERE event_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, eventId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Error loading race config: " + e.getMessage());
        }

        return null;
    }

    public List<Double> findBuoys(Long eventId) {

        List<Double> distances = new ArrayList<>();

        String sql = "SELECT distance FROM race_buoys WHERE event_id = ? ORDER BY id";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, eventId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                distances.add(rs.getDouble("distance"));
            }

        } catch (SQLException e) {
            System.out.println("Error loading race buoys: " + e.getMessage());
        }

        return distances;
    }

    public void deleteRaceEvent(Long eventId) {

        try (PreparedStatement ps1 = conn.prepareStatement("DELETE FROM race_buoys WHERE event_id = ?");
             PreparedStatement ps2 = conn.prepareStatement("DELETE FROM race_events WHERE event_id = ?")) {

            ps1.setLong(1, eventId);
            ps1.executeUpdate();

            ps2.setLong(1, eventId);
            ps2.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error deleting race event: " + e.getMessage());
        }
    }
}
