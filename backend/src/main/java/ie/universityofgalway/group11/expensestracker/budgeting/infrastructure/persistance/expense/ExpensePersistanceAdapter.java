
package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.expense;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.Category;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryId;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseId;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseRepository;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.AdapterHelper;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.category.CategoryEntity;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.category.CategoryJPARepository;

/**
 * JPA-backed implementation of the expense repository contract.
 * It persists expense entities and converts them back into domain objects for application services.
 */
@Repository
public class ExpensePersistanceAdapter implements ExpenseRepository {

    private final ExpenseJPARepository expenseJpaRepository;
    private final CategoryJPARepository categoryJpaRepository;
    private final AdapterHelper helper;

    /**
     * Creates the adapter with the underlying JPA repositories.
     *
     * @param expenseJpaRepository repository for expense rows
     * @param categoryJpaRepository repository for resolving category ownership
     */
    public ExpensePersistanceAdapter(
            ExpenseJPARepository expenseJpaRepository,
            CategoryJPARepository categoryJPARepository) {

        this.expenseJpaRepository = expenseJpaRepository;
        this.categoryJpaRepository = categoryJPARepository;
        this.helper = new AdapterHelper();
    }

    /**
     * Finds an expense by identifier.
     *
     * @param id expense identifier
     * @return matching expense if present
     */
    @Override
    public Optional<Expense> findById(ExpenseId id) {
        return expenseJpaRepository.findById(id.asUuid()).map(helper::toExpenseDomain);
    }

    /**
     * Saves an expense while ensuring the referenced category exists in the database.
     *
     * @param expense domain expense to save
     * @param toCategory owning category
     * @return saved expense
     */
    @Override
    public Expense save(Expense expense, Category toCategory) {
        // Use the persisted category; a freshly converted category entity
        // would be transient and could break the foreign-key relationship.
        CategoryEntity categoryEntity = categoryJpaRepository
                .findById(expense.categoryId().asUuid())
                .orElseThrow(() -> new IllegalArgumentException("Category not found."));
        ExpenseEntity expenseEntity = helper.toExpenseEntity(expense, categoryEntity);
        expenseJpaRepository.save(expenseEntity);
        return expense;
    }

    /**
     * @return the category repository used by this adapter
     */
    public CategoryJPARepository getCategoryJpaRepository() {
        return categoryJpaRepository;
    }

    /**
     * @return the expense repository used by this adapter
     */
    public ExpenseJPARepository getExpenseJpaRepository() {
        return expenseJpaRepository;
    }

    /**
     * Deletes an expense by identifier.
     *
     * @param id expense identifier to remove
     */
    @Override
    public void deleteById(ExpenseId id) {
        expenseJpaRepository.deleteById(id.asUuid());
    }

    /**
     * Finds all expenses belonging to a category.
     *
     * @param categoryId category whose expenses should be returned
     * @return list of domain expenses for that category
     */
    @Override
    public List<Expense> findByCategoryId(CategoryId categoryId) {
        return expenseJpaRepository
                .findByCategory_Id(categoryId.asUuid())
                .stream()
                .map(helper::toExpenseDomain)
                .collect(Collectors.toList());
    }

    /**
     * Returns every expense currently stored.
     *
     * @return all expenses in the repository
     */
    @Override
    public List<Expense> findAll() {
        return expenseJpaRepository.findAll().stream()
                .map(helper::toExpenseDomain)
                .collect(Collectors.toList());
    }
}