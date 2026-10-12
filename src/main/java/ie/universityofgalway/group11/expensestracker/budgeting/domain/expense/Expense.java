package ie.universityofgalway.group11.expensestracker.budgeting.domain.expense;

import java.time.LocalDateTime;

public class Expense {
    private final ExpenseId id;
    private LocalDateTime date;
    private Money money;
    private Description desc;

    private Expense(ExpenseId id, LocalDateTime date, Description desc, Money money) {
        this.id = id;
        this.date = date;
        this.desc = desc;
        this.money = money;
    }

    public static Expense create(Description desc, Money money) {
        // TODO enforce invariants here
        return new Expense(ExpenseId.random(), LocalDateTime.now(), desc, money);
    }

    public ExpenseId id() {
        return id;
    }

    public LocalDateTime date() {
        return date;
    }

    public Money money() {
        return money;
    }

    public Description description() {
        return desc;
    }
}