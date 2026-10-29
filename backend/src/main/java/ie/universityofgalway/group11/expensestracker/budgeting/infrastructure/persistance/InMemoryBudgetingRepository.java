package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance;

import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.*;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseId;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseRepository;
// import ie.universityofgalway.group11.expensestracker.useradmission.domain.user.UserId;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/** Stub repository for the prototype; replace with a database-backed one later. */
public class InMemoryBudgetingRepository implements ExpenseRepository, CategoryRepository {

    private final Map<ExpenseId, Expense> expenseStore = new ConcurrentHashMap<>();

    private final Map<CategoryId, Category> categoryStore = new ConcurrentHashMap<>();


    //Expense Repository

    @Override
    public Optional<Expense> findById(ExpenseId id) {
        return Optional.ofNullable(expenseStore.get(id));
    }

    @Override
    public Expense save(Expense expense, Category toCategory) {
        return expenseStore.put(expense.id(), expense);
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
    public Optional<Category> findById(CategoryId categoryId) {
        // TODO: remove placeholders when possible to add categories. They match the frontend examples (UUIDs must match). they do not match the database samples.
        CategoryId id1 = CategoryId.of(UUID.fromString("7f3a9c21-8d64-4e2b-a5f0-1c9e6b72d843"));
        CategoryId id2 =  CategoryId.of(UUID.fromString("3f6a1b2c-9d84-4e71-a6c3-5b8f02d9471a"));
        categoryStore.put(id1,
                        Category.createWithId(id1, CategoryName.of("Example Category 1"), YearMonth.of(2026, 10), CategoryBudget.of(10.0)));
        categoryStore.put(id2,
                Category.createWithId(id2, CategoryName.of("Example Category 2"), YearMonth.of(2026, 10), CategoryBudget.of(1.0)));

        return Optional.ofNullable(categoryStore.get(categoryId));
    }

}