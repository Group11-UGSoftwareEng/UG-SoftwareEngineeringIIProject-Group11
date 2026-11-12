package ie.universityofgalway.group11.expensestracker.budgeting.domain.category;

import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;

import java.util.HashSet;
import java.util.Set;
import java.time.YearMonth;

/**
 * Represents a budgeting category that groups related expenses and stores a monthly budget.
 * Each category has its own identity, display name, creation period, and collection of expenses.
 */
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

    /**
     * Creates a new category with a generated identifier.
     *
     * @param name category title
     * @param yearAndMonthWhenCreated month in which the category was created
     * @param budget maximum budget assigned to the category
     * @return a newly created category aggregate
     */
    public static Category create(CategoryName name, YearMonth yearAndMonthWhenCreated, CategoryBudget budget) {
        return new Category(CategoryId.random(), name, yearAndMonthWhenCreated, budget);
    }

    /**
     * Creates a category using an explicitly supplied identifier, mainly for persistence and tests.
     *
     * @param id the category identifier
     * @param name the category name
     * @param yearAndMonthWhenCreated category creation month
     * @param budget category budget
     * @return a category instance linked to the provided identifier
     */
    public static Category createWithId(CategoryId id, CategoryName name, YearMonth yearAndMonthWhenCreated, CategoryBudget budget) {
        return new Category(id, name, yearAndMonthWhenCreated, budget);
    }

    /**
     * @return the unique category identifier
     */
    public CategoryId id() {
        return id;
    }

    /**
     * @return the category display name
     */
    public CategoryName name() {
        return name;
    }

    /**
     * @return the year of the category's creation period
     */
    public int year() {
        return dateAdded.getYear();
    }

    /**
     * @return the month number of the category's creation period
     */
    public int month() {
        return dateAdded.getMonthValue();
    }

    /**
     * @return the budget currently assigned to this category
     */
    public CategoryBudget budget() {return budget; }

    /**
     * @return an immutable snapshot of the expenses belonging to this category
     */
    public Set<Expense> expenses() {return Set.copyOf(expenses); }

    /**
     * Updates the human-readable category name.
     *
     * @param newName replacement category name
     */
    public void changeName(CategoryName newName) {
        this.name = newName;
    }

    /**
     * Adds a single expense to the category's in-memory collection.
     *
     * @param expense the expense to associate with this category
     */
    public void addExpenseToTheCategory(Expense expense) {
        this.expenses.add(expense);
    }

    /**
     * Replaces the category budget.
     *
     * @param budget the new budget for the category
     */
    public void updateBudget(CategoryBudget budget) {
        this.budget = budget;
    }

}
