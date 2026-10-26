package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure;

import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.Category;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryId;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryRepository;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseId;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseRepository;
// import ie.universityofgalway.group11.expensestracker.useradmission.domain.user.UserId;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/** Stub repository for the prototype; replace with a database-backed one later. */
@Repository
public class InMemoryBudgetingRepository implements ExpenseRepository, CategoryRepository {

    private final Map<CategoryId, Expense> categoryExpensesStore = new ConcurrentHashMap<>();
    private final Map<ExpenseId, Expense> expenseStore = new ConcurrentHashMap<>();
    private final Map<CategoryId, Category> categoryStore = new ConcurrentHashMap<>();

    // Expense Store
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


    // Category Expenses Store

    @Override
    public Expense add(Expense expense) {
        categoryExpensesStore.put(expense.categoryId(), expense);
        return expense;
    }

    // Category Store
    // TODO missing at the moment

}