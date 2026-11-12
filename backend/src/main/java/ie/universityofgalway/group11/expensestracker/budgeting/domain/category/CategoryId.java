package ie.universityofgalway.group11.expensestracker.budgeting.domain.category;

import java.util.Objects;
import java.util.UUID;

/**
 * Value object representing the unique identifier of a category.
 * This wrapper ensures that category references are strongly typed and consistent across the domain model.
 */
public class CategoryId {

    private final UUID value;

    private CategoryId(UUID value) {
        this.value = value;
    }

    /**
     * Creates a category identifier from an existing UUID.
     *
     * @param id the UUID to wrap
     * @return a new category identifier value object
     */
    public static CategoryId of(UUID id) {
        return new CategoryId(id);
    }

    /**
     * Generates a fresh random category identifier.
     *
     * @return a new identifier
     */
    public static CategoryId random() {
        return new CategoryId(UUID.randomUUID());
    }

    /**
     * @return the underlying UUID value
     */
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
