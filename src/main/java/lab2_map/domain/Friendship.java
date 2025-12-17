package lab2_map.domain;

/**
 * Represents a friendship relationship between two users.
 */
public class Friendship extends Entity<Long> {
    private User user1;
    private User user2;

    public Friendship(Long id, User user1, User user2) {
        super.setId(id);
        this.user1 = user1;
        this.user2 = user2;
    }

    public User getUser1() { return user1; }
    public User getUser2() { return user2; }

    @Override
    public String toString() {
        return user1.getUsername() + " ↔ " + user2.getUsername();
    }
}
