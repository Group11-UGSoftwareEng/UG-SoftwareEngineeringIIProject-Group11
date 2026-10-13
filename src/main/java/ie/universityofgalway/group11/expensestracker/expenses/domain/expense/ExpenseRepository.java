package ie.universityofgalway.group11.expensestracker.expenses.domain.expense;

import java.util.Optional;

public interface ExpenseRepository {

    Optional<Expense> findById(ExpenseId id);

    Expense save(Expense expense);

    void deleteById(ExpenseId id);
}
