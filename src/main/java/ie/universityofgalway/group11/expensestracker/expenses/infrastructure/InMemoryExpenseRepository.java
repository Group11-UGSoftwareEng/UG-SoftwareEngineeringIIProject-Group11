package ie.universityofgalway.group11.expensestracker.expenses.infrastructure;

import ie.universityofgalway.group11.expensestracker.expenses.domain.expense.Expense;
import ie.universityofgalway.group11.expensestracker.expenses.domain.expense.ExpenseId;
import ie.universityofgalway.group11.expensestracker.expenses.domain.expense.ExpenseRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/** Stub repository for the prototype; replace with a database-backed one later. */
@Repository
public class InMemoryExpenseRepository implements ExpenseRepository {

    private final Map<ExpenseId, Expense> store = new ConcurrentHashMap<>();

    @Override
    public Optional<Expense> findById(ExpenseId id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Expense save(Expense expense) {
        store.put(expense.getId(), expense);
        return expense;
    }

    @Override
    public void deleteById(ExpenseId id) {
        store.remove(id);
    }
}