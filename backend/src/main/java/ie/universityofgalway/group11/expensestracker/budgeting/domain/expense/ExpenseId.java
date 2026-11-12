package ie.universityofgalway.group11.expensestracker.budgeting.domain.expense;

import java.util.Objects;
import java.util.UUID;

/**
 * Value object used to uniquely identify an expense within the domain model.
 */
public class ExpenseId {
    private final UUID value;

    private ExpenseId(UUID value) {
        this.value = Objects.requireNonNull(value, "Expense id must not be null");
    }

    /**
     * Creates an expense identifier from an existing UUID.
     *
     * @param value source UUID
     * @return typed expense identifier
     */
    public static ExpenseId of(UUID value) {
        return new ExpenseId(value);
    }

    /**
     * Generates a new unique identifier for a newly created expense.
     *
     * @return a random expense identifier
     */
    public static ExpenseId random() {
        return new ExpenseId(UUID.randomUUID());
    }

    /**
     * @return underlying UUID representation
     */
    public UUID asUuid() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExpenseId)) return false;
        ExpenseId other = (ExpenseId) o;

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
