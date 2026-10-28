package ie.universityofgalway.group11.expensestracker.budgeting.domain.expense;

// import ie.universityofgalway.group11.expensestracker.useradmission.domain.user.UserId;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository {

    Optional<Expense> findById(ExpenseId id);

    Expense save(Expense expense);

    void deleteById(ExpenseId id);

    // Get all expenses for this user
    //List<Expense> findByOwnerId(UserId ownerId);
    List<Expense> findAll();
}