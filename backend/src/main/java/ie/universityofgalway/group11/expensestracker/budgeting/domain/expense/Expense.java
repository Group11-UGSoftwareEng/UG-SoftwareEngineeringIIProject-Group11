package ie.universityofgalway.group11.expensestracker.budgeting.domain.expense;

import java.time.LocalDate;
import java.util.Objects;

public class Expense {

    private final ExpenseId id;
    private Amount amount;
    private LocalDate date;
    private Category category;

    public Expense(ExpenseId id, Amount amount,
                   LocalDate date, Category category) {
        this.id = Objects.requireNonNull(id);
        this.amount = Objects.requireNonNull(amount);
        this.date = Objects.requireNonNull(date);
        this.category = Objects.requireNonNull(category);
    }

    public void update(Amount amount, LocalDate date, Category category) {
        this.amount = Objects.requireNonNull(amount);
        this.date = Objects.requireNonNull(date);
        this.category = Objects.requireNonNull(category);
    }

    public ExpenseId getId() { return id; }
    public Amount getAmount() { return amount; }
    public LocalDate getDate() { return date; }
    public Category getCategory() { return category; }
}