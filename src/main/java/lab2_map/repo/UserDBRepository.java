package lab2_map.repo;

import lab2_map.connections.DatabaseConnection;
import lab2_map.domain.*;
import lab2_map.util.Page;
import lab2_map.util.Pageable;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserDBRepository {

    private final Connection conn;

    public UserDBRepository() {
        this.conn = DatabaseConnection.getConnection();
    }

    // build duck from result set
    private Duck buildDuck(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String username = rs.getString("username");
        String email = rs.getString("email");
        String password = rs.getString("password");

        UserType type = UserType.valueOf(rs.getString("duck_type"));
        double speed = rs.getDouble("speed");
        double endurance = rs.getDouble("endurance");

        return new Duck(id, username, email, password, type, speed, endurance);
    }

    // build person
    private Person buildPerson(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String username = rs.getString("username");
        String email = rs.getString("email");
        String password = rs.getString("password");

        String first = rs.getString("first_name");
        String last = rs.getString("last_name");

        Date d = rs.getDate("birth_date");
        LocalDate birth = (d == null ? null : d.toLocalDate());

        String occupation = rs.getString("occupation");
        double empathy = rs.getDouble("empathy_level");

        return new Person(id, username, email, password, first, last, birth, occupation, empathy);
    }




    public Long save(User u) {
        try {
            String sqlUser = "INSERT INTO users(username, email, password) VALUES(?, ?, ?) RETURNING id";

            PreparedStatement ps = conn.prepareStatement(sqlUser);
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPassword());

            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return null;

            Long id = rs.getLong(1);
            u.setId(id);

            if (u instanceof Person p) {
                String sql = "INSERT INTO persoane(user_id, first_name, last_name, birth_date, occupation, empathy_level) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement ps2 = conn.prepareStatement(sql);
                ps2.setLong(1, id);
                ps2.setString(2, p.getFirstName());
                ps2.setString(3, p.getLastName());

                if (p.getBirthDate() != null)
                    ps2.setDate(4, Date.valueOf(p.getBirthDate()));
                else ps2.setNull(4, Types.DATE);

                ps2.setString(5, p.getOccupation());
                ps2.setDouble(6, p.getEmpathyLevel());
                ps2.executeUpdate();
            }

            if (u instanceof Duck d) {
                String sql = "INSERT INTO ducks(user_id, duck_type, speed, endurance) VALUES (?, ?, ?, ?)";
                PreparedStatement ps2 = conn.prepareStatement(sql);
                ps2.setLong(1, id);
                ps2.setString(2, d.getType().name());
                ps2.setDouble(3, d.getSpeed());
                ps2.setDouble(4, d.getEndurance());
                ps2.executeUpdate();
            }

            return id;

        } catch (SQLException e) {
            System.out.println("Error at save user: " + e.getMessage());
            return null;
        }
    }




    public void delete(Long id) {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM users WHERE id = ?");
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error delete user: " + e.getMessage());
        }
    }


    //count ducks for pagination

    private int countDucks(UserType filter) {
        try {
            String sql = "SELECT COUNT(*) FROM ducks";

            if (filter != null) sql += " WHERE duck_type = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            if (filter != null)
                ps.setString(1, filter.name());

            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);

        } catch (SQLException e) {
            System.out.println("Error at countDucks: " + e.getMessage());
            return 0;
        }
    }
    //load ducks for a page
    private List<Duck> loadPageDucks(Pageable pageable, UserType filter) {
        List<Duck> list = new ArrayList<>();

        try {
            String sql =
                    "SELECT u.id, u.username, u.email, u.password, " +
                            "       d.duck_type, d.speed, d.endurance " +
                            "FROM ducks d JOIN users u ON d.user_id = u.id ";

            if (filter != null)
                sql += "WHERE d.duck_type = ? ";

            sql += "ORDER BY u.id LIMIT ? OFFSET ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            int idx = 1;

            if (filter != null)
                ps.setString(idx++, filter.name());

            ps.setInt(idx++, pageable.getPageSize());
            ps.setInt(idx, pageable.getPageSize() * pageable.getPageNumber());

            ResultSet rs = ps.executeQuery();

            while (rs.next())
                list.add(buildDuck(rs));

        } catch (SQLException e) {
            System.out.println("Error loadPageDucks: " + e.getMessage());
        }

        return list;
    }
    //find ducks on page with filter

    public Page<Duck> findDucksOnPage(int page, int size, UserType filter) {
        Pageable pageable = new Pageable(page, size);
        int total = countDucks(filter);
        List<Duck> content = loadPageDucks(pageable, filter);
        return new Page<>(content, total);
    }


    //find one user by id

    public User findOne(Long id) {
        try {
            // Try Person
            String sqlP =
                    "SELECT u.id, u.username, u.email, u.password, p.* " +
                            "FROM users u JOIN persoane p ON u.id = p.user_id WHERE u.id = ?";

            PreparedStatement ps = conn.prepareStatement(sqlP);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return buildPerson(rs);

            // Try Duck
            String sqlD =
                    "SELECT u.id, u.username, u.email, u.password, d.* " +
                            "FROM users u JOIN ducks d ON u.id = d.user_id WHERE u.id = ?";

            PreparedStatement ps2 = conn.prepareStatement(sqlD);
            ps2.setLong(1, id);
            ResultSet r2 = ps2.executeQuery();

            if (r2.next()) return buildDuck(r2);

        } catch (SQLException e) {
            System.out.println("Error findOne: " + e.getMessage());
        }

        return null;
    }
}
