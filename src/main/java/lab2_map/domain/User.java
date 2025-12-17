package lab2_map.domain;

import java.util.Objects;

/**
 * Base abstract class for all users (Person, Duck).
 */
public abstract class User extends Entity<Long> {

    protected String username;
    protected String email;
    protected String password;

    public User(Long id, String username, String email, String password) {
        super.setId(id);
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }

    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }

    /**
     * VERY IMPORTANT:
     * Users are considered equal if they have the same ID.
     * This fixes BFS loops, friendship traversal, etc.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;

        // if ID is null, consider not equal
        if (this.id == null || user.id == null)
            return false;

        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Useful for debugging, logs, lists, etc.
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() +
                "{id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
