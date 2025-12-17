package lab2_map.repo;

import lab2_map.connections.DatabaseConnection;
import lab2_map.domain.Card;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CardDBRepository {

    private final Connection conn;

    public CardDBRepository() {
        this.conn = DatabaseConnection.getConnection();
    }

    public Long save(Card<?> card) {

        String sql = "INSERT INTO cards(name) VALUES (?) RETURNING id";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, card.getName());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Long id = rs.getLong(1);
                card.setId(id);
                return id;
            }

        } catch (SQLException e) {
            System.out.println("Error saving card: " + e.getMessage());
        }

        return null;
    }

    public void delete(Long id) {

        String sql = "DELETE FROM cards WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting card: " + e.getMessage());
        }
    }

    public List<Card<?>> findAll() {

        List<Card<?>> list = new ArrayList<>();

        String sql = "SELECT * FROM cards";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Long id = rs.getLong("id");
                String name = rs.getString("name");

                Card<?> card = new Card<>(id, name);
                list.add(card);
            }

        } catch (SQLException e) {
            System.out.println("Error loading cards: " + e.getMessage());
        }

        return list;
    }
}
