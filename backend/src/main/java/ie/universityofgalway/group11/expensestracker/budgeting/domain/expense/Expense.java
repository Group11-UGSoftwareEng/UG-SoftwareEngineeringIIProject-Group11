package ie.universityofgalway.group11.expensestracker.budgeting.domain.expense;

import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.Category;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryId;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Aggregate representing a single expense entry within a category.
 * Each expense has a unique identifier, monetary details, date, description, and currency.
 */
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

    /**
     * Creates a new expense for the current date using a generated identifier.
     *
     * @param categoryId the owning category
     * @param amount monetary amount
     * @param desc expense description
     * @param currency expense currency
     * @return a new expense instance
     */
    public static Expense create(CategoryId categoryId, Amount amount, Description desc, Currency currency) {
        return new Expense(ExpenseId.random(), categoryId, amount, LocalDate.now(), desc, currency);
    }

    /**
     * Creates an expense with a supplied identifier for persistence or restoration.
     *
     * @param id persisted expense identifier
     * @param categoryId owning category
     * @param amount value to record
     * @param desc text description
     * @param currency currency code
     * @return restored or reconstructed expense
     */
    public static Expense createWithId(ExpenseId id, CategoryId categoryId, Amount amount, Description desc, Currency currency) {
        return new Expense(id, categoryId, amount, LocalDate.now(), desc, currency);
    }

    /**
     * Creates an expense with an explicitly provided date.
     *
     * @param id persisted expense identifier
     * @param categoryId category containing the expense
     * @param amount amount value
     * @param date date when the expense occurred
     * @param desc description text
     * @param currency currency code
     * @return an expense instance with the given date
     */
    public static Expense createWithId(
            ExpenseId id, CategoryId categoryId, Amount amount,
            LocalDate date, Description desc, Currency currency) {

        return new Expense(id, categoryId, amount, date, desc, currency);
    }

    /**
     * Updates the main expense details in one operation.
     *
     * @param amount new amount
     * @param desc new description
     * @param currency new currency
     */
    public void update(Amount amount, Description desc, Currency currency) {
        this.amount = Objects.requireNonNull(amount);
        this.desc = Objects.requireNonNull(desc);
        this.currency = Objects.requireNonNull(currency);
    }

    /**
     * Updates the currency while keeping the same monetary value.
     *
     * @param currency new currency code
     */
    public void updateCurrency(Currency currency) {
        // TODO: actual currency conversion logic
        Amount newAmount = amount();

        updateAmount(newAmount);
        this.currency = Objects.requireNonNull(currency);
    }

    /**
     * Updates the stored amount.
     *
     * @param amount replacement amount
     */
    public void updateAmount(Amount amount) {
        this.amount = Objects.requireNonNull(amount);
    }

    /**
     * Updates the description.
     *
     * @param desc replacement description
     */
    public void updateDescription(Description desc) {
        this.desc = Objects.requireNonNull(desc);
    }

    /**
     * Updates the date associated with the expense.
     *
     * @param date new transaction date
     */
    public void updateDate(LocalDate date) {
        this.date = Objects.requireNonNull(date);
    }

    /**
     * @return owning category identifier
     */
    public CategoryId categoryId() { return categoryId; }

    /**
     * @return this expense's unique identifier
     */
    public ExpenseId id() { return id; }

    /**
     * @return current monetary amount
     */
    public Amount amount() { return amount; }

    /**
     * @return the date the expense was recorded or occurred
     */
    public LocalDate date() { return date; }

    /**
     * @return the expense description
     */
    public Description description() { return desc; }

    /**
     * @return the currency used for this expense
     */
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

}
