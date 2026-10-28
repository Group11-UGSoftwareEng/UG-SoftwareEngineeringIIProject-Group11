package ie.universityofgalway.group11.expensestracker.budgeting.domain.category;

import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;

import java.util.HashSet;
import java.util.Set;

public class Category {

    private final CategoryId id;
    private CategoryName name;
    private CategoryBudget budget;
    private final Set<Expense> expenses = new HashSet<>();


    private Category(CategoryId id, CategoryName name) {
        this.id = id;
        this.name = name;
    }

    public static Category create(CategoryName name) {
        return new Category(CategoryId.random(), name);
    }

    public CategoryId id() {
        return id;
    }

    public CategoryName name() {
        return name;
    }

    // Domain Behaviour

    public void changeName(CategoryName newName) {
        this.name = newName;
    }

    public void addExpense(Expense expense) {
        this.expenses.add(expense);
    }


    



}
