package lab2_map.domain;

import java.util.Objects;

public abstract class Entity<ID> {

    protected ID id;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    /**
     * Two entities are equal if and only if:
     * - They are of the same class
     * - AND both IDs are non-null AND equal
     *
     * This prevents BFS infinite loops and ensures that
     * database-loaded objects referring to the same row
     * are treated as exactly the same node.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entity<?> entity = (Entity<?>) o;

        // entities without ID are never equal
        if (id == null || entity.id == null)
            return false;

        return Objects.equals(id, entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
