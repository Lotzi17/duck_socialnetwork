package lab2_map.repo;

import lab2_map.connections.DatabaseConnection;
import lab2_map.domain.Duck;
import lab2_map.domain.User;
import lab2_map.repo.UserDBRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CardMembersDBRepository {

    private final Connection conn;
    private final UserDBRepository userRepo;

    public CardMembersDBRepository(UserDBRepository userRepo) {
        this.conn = DatabaseConnection.getConnection();
        this.userRepo = userRepo;
    }

    public void addDuckToCard(Long cardId, Long duckId) {

        String sql = "INSERT INTO card_members(card_id, duck_id) VALUES (?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, cardId);
            ps.setLong(2, duckId);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error adding duck to card: " + e.getMessage());
        }
    }

    public List<Duck> findDucksForCard(Long cardId) {

        List<Duck> ducks = new ArrayList<>();

        String sql = "SELECT duck_id FROM card_members WHERE card_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, cardId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Long did = rs.getLong("duck_id");

                User u = userRepo.findOne(did);
                if (u instanceof Duck d) {
                    ducks.add(d);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error loading card members: " + e.getMessage());
        }

        return ducks;
    }

    public void removeAllForCard(Long cardId) {

        String sql = "DELETE FROM card_members WHERE card_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, cardId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error removing members from card: " + e.getMessage());
        }
    }
}
