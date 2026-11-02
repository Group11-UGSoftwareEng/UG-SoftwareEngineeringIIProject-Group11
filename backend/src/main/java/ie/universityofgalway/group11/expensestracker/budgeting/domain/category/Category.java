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

    private Category(CategoryId id, CategoryName name, YearMonth yearAndMonthWhenCreated, CategoryBudget budget) {
        this.id = id;
        this.name = name;
        this.dateAdded = yearAndMonthWhenCreated;
        this.budget = budget;
    }

    public static Category create(CategoryName name, YearMonth yearAndMonthWhenCreated, CategoryBudget budget) {
        return new Category(CategoryId.random(), name, yearAndMonthWhenCreated, budget);
    }

    public static Category createWithId(CategoryId id, CategoryName name, YearMonth yearAndMonthWhenCreated, CategoryBudget budget) {
        return new Category(id, name, yearAndMonthWhenCreated, budget);
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

    public CategoryBudget budget() {
        return budget;
    }

    // Domain Behaviour

    public void changeName(CategoryName newName) {
        this.name = newName;
    }

    public void addExpenseToTheCategory(Expense expense) {
        this.expenses.add(expense);
    }

    public void updateBudget(CategoryBudget budget) {
        this.budget = budget;
    }

}
