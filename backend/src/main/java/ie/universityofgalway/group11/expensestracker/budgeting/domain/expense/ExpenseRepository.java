package ie.universityofgalway.group11.expensestracker.budgeting.domain.expense;

// import ie.universityofgalway.group11.expensestracker.useradmission.domain.user.UserId;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.Category;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryId;

@Repository
public interface ExpenseRepository {

    Optional<Expense> findById(ExpenseId id);

    Expense save(Expense expense, Category toCategory);

    void deleteById(ExpenseId id);

    // Get all expenses for this user
    //List<Expense> findByOwnerId(UserId ownerId);
    List<Expense> findAll();
    List<Expense> findByCategoryId(CategoryId categoryId);
}