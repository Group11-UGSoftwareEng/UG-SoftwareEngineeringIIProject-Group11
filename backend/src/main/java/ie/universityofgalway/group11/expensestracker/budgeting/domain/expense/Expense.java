package ie.universityofgalway.group11.expensestracker.budgeting.domain.expense;

import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.Category;

import java.time.LocalDate;
import java.util.Objects;

public class Expense {

    private final ExpenseId id;
    private Amount amount;
    private LocalDate date;
    private Description desc;
    private Currency currency;

    private Expense(ExpenseId id, Amount amount, LocalDate date, Description desc, Currency currency) {
        this.id = Objects.requireNonNull(id);
        this.amount = Objects.requireNonNull(amount);
        this.date = Objects.requireNonNull(date);
        this.desc = Objects.requireNonNull(desc);
        this.currency = Objects.requireNonNull(currency);
    }

    public static Expense create(Amount amount, Description desc, Currency currency) {
        return new Expense(ExpenseId.random(), amount, LocalDate.now(), desc, currency);
    }

    public void update(Amount amount, LocalDate date, Description desc) {
        this.amount = Objects.requireNonNull(amount);
        this.date = Objects.requireNonNull(date);
        this.desc = Objects.requireNonNull(desc);
    }

    // Domain Behaviour

    public void updateCurrency(Currency currency) {
        // TODO: actual currency conversion logic
        this.currency = Objects.requireNonNull(currency);
    }

    public void updateAmount(Amount amount) {
        this.amount = Objects.requireNonNull(amount);
    }

    public void updateDescription(Description desc) {
        this.desc = Objects.requireNonNull(desc);
    }

    public void updateDate(LocalDate date) {
        this.date = Objects.requireNonNull(date);
    }

    public ExpenseId id() { return id; }
    public Amount amount() { return amount; }
    public LocalDate date() { return date; }
    public Description description() { return desc; }
    public Currency currency() { return currency; }
}
