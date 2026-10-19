package ie.universityofgalway.group11.expensestracker.budgeting.domain.expense;

import ie.universityofgalway.group11.expensestracker.useradmission.domain.user.UserId;

import java.time.LocalDate;
import java.util.Objects;

public class Expense {

    private final ExpenseId id;
    private final UserId ownerId;
    private Amount amount;
    private LocalDate date;
    private Category category;

    public Expense(ExpenseId id, UserId ownerId, Amount amount, LocalDate date, Category category) {
        this.id = Objects.requireNonNull(id);
        this.ownerId = Objects.requireNonNull(ownerId);
        this.amount = Objects.requireNonNull(amount);
        this.date = Objects.requireNonNull(date);
        this.category = Objects.requireNonNull(category);
    }

    public boolean isOwnedBy(UserId userId) {
        return ownerId.equals(userId);
    }

    public void update(Amount amount, LocalDate date, Category category) {
        this.amount = Objects.requireNonNull(amount);
        this.date = Objects.requireNonNull(date);
        this.category = Objects.requireNonNull(category);
    }

    public ExpenseId getId() { return id; }
    public UserId getOwnerId() { return ownerId; }
    public Amount getAmount() { return amount; }
    public LocalDate getDate() { return date; }
    public Category getCategory() { return category; }
}
