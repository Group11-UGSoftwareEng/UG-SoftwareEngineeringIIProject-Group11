package ie.universityofgalway.group11.expensestracker.budgeting.domain.category;

import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;

import java.util.HashSet;
import java.util.Set;
import java.time.YearMonth;

public class Category {

    private final CategoryId id;
    private CategoryName name;
    private CategoryBudget budget;
    private YearMonth dateAdded;
    private final Set<Expense> expenses = new HashSet<>();

    private Category(CategoryId id, CategoryName name, YearMonth yearAndMonthWhenCreated) {
        this.id = id;
        this.name = name;
        this.dateAdded = yearAndMonthWhenCreated;
    }

    public static Category create(CategoryName name, YearMonth yearAndMonthWhenCreated) {
        return new Category(CategoryId.random(), name, yearAndMonthWhenCreated);
    }

    public CategoryId id() {
        return id;
    }

    public CategoryName name() {
        return name;
    }

    public int year() {
        return dateAdded.getYear();
    }

    public int month() {
        return dateAdded.getMonthValue();
    }

    // Domain Behaviour

    public void changeName(CategoryName newName) {
        this.name = newName;
    }

    public void addExpense(Expense expense) {
        this.expenses.add(expense);
    }

}
