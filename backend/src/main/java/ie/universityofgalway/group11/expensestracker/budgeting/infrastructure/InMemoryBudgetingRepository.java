package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure;

import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryId;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryRepository;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseId;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseRepository;
// import ie.universityofgalway.group11.expensestracker.useradmission.domain.user.UserId;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/** Stub repository for the prototype; replace with a database-backed one later. */
@Repository
public class InMemoryBudgetingRepository implements ExpenseRepository, CategoryRepository {

    private final Map<ExpenseId, Expense> store = new ConcurrentHashMap<>();

    //Expense Repository

    @Override
    public Optional<Expense> findById(ExpenseId id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Expense save(Expense expense) {
        store.put(expense.id(), expense);
        return expense;
    }

    @Override
    public void deleteById(ExpenseId id) {
        store.remove(id);
    }

    @Override
    public List<Expense> findAll() {
        return new ArrayList<>(store.values());
    }


    // Category Repository

    @Override
    public Expense add(Expense expense) {
        store.put(expense.id(), expense);
        for (ExpenseId key : store.keySet()) {
            Expense value = store.get(key);
            System.out.println(key);
            System.out.println(value);
        }
        return expense;
    }

}