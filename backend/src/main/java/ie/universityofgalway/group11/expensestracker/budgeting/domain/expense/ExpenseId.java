package ie.universityofgalway.group11.expensestracker.budgeting.domain.expense;

import java.util.Objects;
import java.util.UUID;

public record ExpenseId(UUID value) {
    public ExpenseId {
        Objects.requireNonNull(value, "Expense id must not be null");
    }
    public static ExpenseId newId() {
        return new ExpenseId(UUID.randomUUID());
    }
}
