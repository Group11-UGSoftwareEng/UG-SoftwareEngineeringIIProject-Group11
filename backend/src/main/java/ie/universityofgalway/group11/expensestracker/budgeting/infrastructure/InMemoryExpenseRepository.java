package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure;

import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseId;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseRepository;
// import ie.universityofgalway.group11.expensestracker.useradmission.domain.user.UserId;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
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

//    @Override
//    public List<Expense> findByOwnerId(UserId ownerId) {
//        List<Expense> userExpenses = new ArrayList<>();
//
//        // Find expenses belonging to this user
//        for (Expense expense : store.values()) {
//            if (expense.isOwnedBy(ownerId)) {
//                userExpenses.add(expense);
//            }
//        }
//
//        return userExpenses;
//    }
}