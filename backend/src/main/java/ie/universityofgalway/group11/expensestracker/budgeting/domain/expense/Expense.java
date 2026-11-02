package ie.universityofgalway.group11.expensestracker.budgeting.domain.expense;

import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.Category;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryId;

import java.time.LocalDate;
import java.util.Objects;

public class Expense {

    private final CategoryId categoryId;
    private final ExpenseId id;
    private Amount amount;
    private LocalDate date;
    private Description desc;
    private Currency currency;

    private Expense(ExpenseId id, CategoryId categoryId, Amount amount, LocalDate date, Description desc, Currency currency) {
        this.categoryId = Objects.requireNonNull(categoryId);
        this.id = Objects.requireNonNull(id);
        this.amount = Objects.requireNonNull(amount);
        this.date = Objects.requireNonNull(date);
        this.desc = Objects.requireNonNull(desc);
        this.currency = Objects.requireNonNull(currency);
    }

    public static Expense create(CategoryId categoryId, Amount amount, Description desc, Currency currency) {
        return new Expense(ExpenseId.random(), categoryId, amount, LocalDate.now(), desc, currency);
    }

    public static Expense createWithId(ExpenseId id, CategoryId categoryId, Amount amount, Description desc, Currency currency) {

        return new Expense(id, categoryId, amount, LocalDate.now(), desc, currency);
    }

    // Domain Behaviour

    public void update(Amount amount, Description desc, Currency currency) {
        this.amount = Objects.requireNonNull(amount);
        this.desc = Objects.requireNonNull(desc);
        this.currency = Objects.requireNonNull(currency);
    }

    public void updateCurrency(Currency currency) {
        // TODO: actual currency conversion logic
        Amount newAmount = amount();

        updateAmount(newAmount);
        this.currency = Objects.requireNonNull(currency);
    }

    public void updateAmount(Amount amount) {
        this.amount = Objects.requireNonNull(amount);
    }

    public void updateDescription(Description desc) {
        this.desc = Objects.requireNonNull(desc);
    }

    public CategoryId categoryId() { return categoryId; }
    public ExpenseId id() { return id; }
    public Amount amount() { return amount; }
    public LocalDate date() { return date; }
    public Description description() { return desc; }
    public Currency currency() { return currency; }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", categoryId=" + categoryId +
                ", amount=" + amount +
                ", date=" + date +
                ", description=" + desc +
                ", currency=" + currency +
                '}';
    }

    // Domain behaviour

}
