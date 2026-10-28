package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure;

import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.Category;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryId;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryName;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryRepository;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseId;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseRepository;
// import ie.universityofgalway.group11.expensestracker.useradmission.domain.user.UserId;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/** Stub repository for the prototype; replace with a database-backed one later. */
@Repository
public class InMemoryBudgetingRepository implements ExpenseRepository, CategoryRepository {

    private final Map<ExpenseId, Expense> expenseStore = new ConcurrentHashMap<>();

    private final Map<CategoryId, Category> categoryStore = new ConcurrentHashMap<>();


    //Expense Repository

    @Override
    public Optional<Expense> findById(ExpenseId id) {
        return Optional.ofNullable(expenseStore.get(id));
    }

    @Override
    public Expense save(Expense expense) {
        expenseStore.put(expense.id(), expense);
        return expense;
    }

    @Override
    public void deleteById(ExpenseId id) {
        expenseStore.remove(id);
    }

    @Override
    public List<Expense> findAll() {
        return new ArrayList<>(expenseStore.values());
    }


    // Category Repository

    @Override
    public Expense add(Expense expense) {
        expenseStore.put(expense.id(), expense);
        for (ExpenseId key : expenseStore.keySet()) {
            Expense value = expenseStore.get(key);
            System.out.println("Expense Added to category with UUID: " + key);
            System.out.println("Added Expense: " + value);
        }
        return expense;
    }

    @Override
    public Category findById(CategoryId categoryId) {
        // TODO: remove placeholders when possible to add categories. They match the frontend examples (UUIDs must match)
        categoryStore.put(CategoryId.of(UUID.fromString("7f3a9c21-8d64-4e2b-a5f0-1c9e6b72d843")),
                        Category.create(CategoryName.of("Example Category 1"), YearMonth.of(2026, 10)));
        categoryStore.put(CategoryId.of(UUID.fromString("7f3a9c21-8d64-4e2b-a5f0-1c9e6b72d843")),
                Category.create(CategoryName.of("Example Category 1"), YearMonth.of(2026, 10)));

        return categoryStore.get(categoryId);

    }

}