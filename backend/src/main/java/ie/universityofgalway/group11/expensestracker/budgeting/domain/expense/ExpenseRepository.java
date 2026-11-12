package ie.universityofgalway.group11.expensestracker.budgeting.domain.expense;

// import ie.universityofgalway.group11.expensestracker.useradmission.domain.user.UserId;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.Category;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryId;

/**
 * Repository contract for expense aggregates.
 * Implementations persist and retrieve expenses for one or more categories.
 */
@Repository
public interface ExpenseRepository {

    /**
     * Finds a stored expense by identifier.
     *
     * @param id unique expense identifier
     * @return matching expense if it exists
     */
    Optional<Expense> findById(ExpenseId id);

    /**
     * Persists a new expense and associates it with the provided category.
     *
     * @param expense the domain expense to save
     * @param toCategory destination category
     * @return the saved expense
     */
    Expense save(Expense expense, Category toCategory);

    /**
     * Removes the expense record from storage.
     *
     * @param id identifier of the expense to delete
     */
    void deleteById(ExpenseId id);

    /**
     * @return all expenses currently available in the repository
     */
    List<Expense> findAll();

    /**
     * Lists all expenses linked to a specific category.
     *
     * @param categoryId category to inspect
     * @return matching expenses
     */
    List<Expense> findByCategoryId(CategoryId categoryId);
}