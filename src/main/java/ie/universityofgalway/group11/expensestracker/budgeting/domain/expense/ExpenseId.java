package ie.universityofgalway.group11.expensestracker.budgeting.domain.expense;

import java.util.Objects;
import java.util.UUID;

public class ExpenseId {
    private final UUID value;

    private ExpenseId(UUID value) {
        this.value = value;
    }

    public static ExpenseId of(UUID id) {
        return new ExpenseId(id);
    }

    public static ExpenseId random() {
        return new ExpenseId(UUID.randomUUID());
    }

    public UUID asUuid() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ExpenseId)) return false;
        ExpenseId other = (ExpenseId) obj;

        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
