package ie.universityofgalway.group11.expensestracker.budgeting.domain.category;

import java.util.Objects;
import java.util.UUID;

public class CategoryId {

    private final UUID value;

    private CategoryId(UUID value) {
        this.value = value;
    }

    public static CategoryId of(UUID id) {
        return new CategoryId(id);
    }

    public static CategoryId random() {
        return new CategoryId(UUID.randomUUID());
    }

    public UUID asUuid() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryId)) return false;
        CategoryId other = (CategoryId) o;

        return value.equals(other.value);
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
