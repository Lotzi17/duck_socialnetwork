package lab2_map.repo;

import lab2_map.connections.DatabaseConnection;
import lab2_map.domain.Friendship;
import lab2_map.domain.User;

import java.sql.*;
import java.util.*;

public class FriendshipDBRepository {

    private final Connection conn;

    public FriendshipDBRepository() {
        this.conn = DatabaseConnection.getConnection();
    }

    public List<Friendship> findAll(UserDBRepository userRepo) {

        List<Friendship> list = new ArrayList<>();
        String sql = "SELECT * FROM friendships";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Long id = rs.getLong("id");
                Long u1 = rs.getLong("user1");
                Long u2 = rs.getLong("user2");

                User user1 = userRepo.findOne(u1);
                User user2 = userRepo.findOne(u2);

                if (user1 != null && user2 != null) {
                    list.add(new Friendship(id, user1, user2));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error at findAll friendships: " + e.getMessage());
        }

        return list;
    }

    public Long save(Long u1, Long u2) {

        if (u1.equals(u2)) {
            System.out.println("Error: users cannot friend themselves");
            return null;
        }

        long low = Math.min(u1, u2);
        long high = Math.max(u1, u2);

        String sql = "INSERT INTO friendships(user1, user2) VALUES (?, ?) RETURNING id";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, low);
            ps.setLong(2, high);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getLong("id");

        } catch (SQLException e) {
            System.out.println("Error at save friendship: " + e.getMessage());
        }

        return null;
    }

    public void delete(Long id) {

        String sql = "DELETE FROM friendships WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error at delete friendship: " + e.getMessage());
        }
    }


    public List<User> getFriends(Long userId, UserDBRepository userRepo) {

        List<User> friends = new ArrayList<>();

        String sql = """
                SELECT user1, user2
                FROM friendships
                WHERE user1 = ? OR user2 = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.setLong(2, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Long u1 = rs.getLong("user1");
                Long u2 = rs.getLong("user2");

                Long friendId = (u1.equals(userId)) ? u2 : u1;

                User friend = userRepo.findOne(friendId);
                if (friend != null) friends.add(friend);
            }

        } catch (SQLException e) {
            System.out.println("Error at getFriends: " + e.getMessage());
        }

        return friends;
    }


    public List<User> getAllUsersWithFriends(UserDBRepository userRepo) {

        List<User> users = new ArrayList<>();

        String sql = """
                SELECT DISTINCT user1 AS uid FROM friendships
                UNION
                SELECT DISTINCT user2 AS uid FROM friendships
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Long id = rs.getLong("uid");
                User u = userRepo.findOne(id);
                if (u != null) users.add(u);
            }

        } catch (SQLException e) {
            System.out.println("Error at getAllUsersWithFriends: " + e.getMessage());
        }

        return users;
    }


    public List<Set<User>> computeCommunities(List<User> allUsers, UserDBRepository userRepo) {

        List<Set<User>> result = new ArrayList<>();
        Set<Long> visited = new HashSet<>();

        for (User u : allUsers) {
            if (u == null) continue;
            if (!visited.contains(u.getId())) {

                Set<User> comp = bfsComponent(u, userRepo);

                for (User x : comp) {
                    visited.add(x.getId());
                }

                result.add(comp);
            }
        }
        return result;
    }

    private Set<User> bfsComponent(User start, UserDBRepository userRepo) {

        Set<User> comp = new HashSet<>();
        Queue<User> queue = new LinkedList<>();

        if (start == null) return comp;

        comp.add(start);
        queue.add(start);

        while (!queue.isEmpty()) {

            User u = queue.poll();
            if (u == null) continue;

            for (User f : getFriends(u.getId(), userRepo)) {
                if (f == null) continue;
                if (!comp.contains(f)) {
                    comp.add(f);
                    queue.add(f);
                }
            }
        }

        return comp;
    }


    public Set<User> biggestDiameterCommunity(UserDBRepository userRepo) {

        List<User> allUsers = getAllUsersWithFriends(userRepo);
        List<Set<User>> communities = computeCommunities(allUsers, userRepo);

        if (communities.isEmpty()) {
            // nu exista comunitati => intoarcem multime goala
            return new HashSet<>();
        }

        Set<User> best = null;
        int maxDiameter = -1;

        for (Set<User> c : communities) {
            int d = computeDiameter(c, userRepo);
            if (d > maxDiameter) {
                maxDiameter = d;
                best = c;
            }
        }

        return best == null ? new HashSet<>() : best;
    }

    private int computeDiameter(Set<User> community, UserDBRepository userRepo) {
        int diameter = 0;
        for (User u : community) {
            if (u == null) continue;
            diameter = Math.max(diameter, bfsMaxDistance(u, community, userRepo));
        }
        return diameter;
    }

    private int bfsMaxDistance(User start, Set<User> community, UserDBRepository userRepo) {

        Queue<User> queue = new LinkedList<>();
        Map<Long, Integer> dist = new HashMap<>();

        queue.add(start);
        dist.put(start.getId(), 0);

        int maxDist = 0;

        while (!queue.isEmpty()) {

            User u = queue.poll();
            if (u == null) continue;

            int d = dist.get(u.getId());

            for (User f : getFriends(u.getId(), userRepo)) {
                if (f == null) continue;
                if (community.contains(f) && !dist.containsKey(f.getId())) {
                    dist.put(f.getId(), d + 1);
                    maxDist = Math.max(maxDist, d + 1);
                    queue.add(f);
                }
            }
        }

        return maxDist;
    }
}
