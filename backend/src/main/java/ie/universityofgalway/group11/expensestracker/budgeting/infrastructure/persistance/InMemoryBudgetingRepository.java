
package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.Category;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryId;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryName;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryRepository;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseId;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseRepository;

/**
 * Simple in-memory repository used for testing and prototyping.
 * It mirrors the domain repository contract without depending on database infrastructure.
 */
public class InMemoryBudgetingRepository
        implements ExpenseRepository, CategoryRepository {

    private final Map<ExpenseId, Expense> expenseStore =
            new ConcurrentHashMap<>();

    private final Map<CategoryId, Category> categoryStore =
            new ConcurrentHashMap<>();

    public InMemoryBudgetingRepository() {
        // Start with an empty repository.
    }

    // ========================================
    // EXPENSE REPOSITORY
    // ========================================

    /**
     * Finds an expense by its unique identifier.
     *
     * @param id expense identifier
     * @return matching expense if present
     */
    @Override
    public Optional<Expense> findById(ExpenseId id) {
        return Optional.ofNullable(expenseStore.get(id));
    }

    /**
     * Stores an expense in memory.
     *
     * @param expense expense to persist
     * @param toCategory owning category
     * @return stored expense
     */
    @Override
    public Expense save(Expense expense, Category toCategory) {
        expenseStore.put(expense.id(), expense);
        return expense;
    }

    /**
     * Removes an expense from the in-memory store.
     *
     * @param id expense identifier to remove
     */
    @Override
    public void deleteById(ExpenseId id) {
        expenseStore.remove(id);
    }

    /**
     * Returns every expense currently stored in memory.
     *
     * @return list of all expenses
     */
    @Override
    public List<Expense> findAll() {
        return new ArrayList<>(expenseStore.values());
    }

    /**
     * Finds all expenses for a particular category.
     *
     * @param categoryId target category
     * @return matching expense records
     */
    @Override
    public List<Expense> findByCategoryId(
            CategoryId categoryId) {

        List<Expense> expenses = new ArrayList<>();

        for (Expense expense : expenseStore.values()) {
            if (expense.categoryId().equals(categoryId)) {
                expenses.add(expense);
            }
        }

        return expenses;
    }

    // ========================================
    // CATEGORY REPOSITORY
    // ========================================

    /**
     * Finds a category by identifier.
     *
     * @param categoryId target identifier
     * @return category if it exists
     */
    @Override
    public Optional<Category> findById(CategoryId categoryId) {
        return Optional.ofNullable(
                categoryStore.get(categoryId)
        );
    }

    /**
     * Stores a category in memory.
     *
     * @param category category to persist
     * @return stored category
     */
    @Override
    public Category save(Category category) {
        categoryStore.put(category.id(), category);
        return category;
    }

    /**
     * Removes a category by identifier.
     *
     * @param id category to delete
     */
    @Override
    public void deleteById(CategoryId id) {
        categoryStore.remove(id);
    }

    /**
     * Lists all categories in memory.
     *
     * @return all categories
     */
    @Override
    public List<Category> findAllCategories() {
        return new ArrayList<>(categoryStore.values());
    }

    /**
     * Retrieves categories for a specific monthly period.
     *
     * @param period target year-month
     * @return categories in that period
     */
    @Override
    public List<Category> findByPeriod(YearMonth period) {

        List<Category> categories = new ArrayList<>();

        for (Category category : categoryStore.values()) {
            if (category.year() == period.getYear()
                    && category.month() == period.getMonthValue()) {

                categories.add(category);
            }
        }

        return categories;
    }

    /**
     * Determines the latest available category period.
     *
     * @return most recent year-month, if any categories exist
     */
    @Override
    public Optional<YearMonth> findLatestPeriod() {

        YearMonth latest = null;

        for (Category category : categoryStore.values()) {

            YearMonth period = YearMonth.of(
                    category.year(),
                    category.month()
            );

            if (latest == null || period.isAfter(latest)) {
                latest = period;
            }
        }

        return Optional.ofNullable(latest);
    }

    /**
     * Saves a batch of categories.
     *
     * @param categories categories to insert or update
     */
    @Override
    public void saveAll(List<Category> categories) {

        for (Category category : categories) {
            categoryStore.put(
                    category.id(),
                    category
            );
        }
    }

    /**
     * Checks whether a category name already exists in the in-memory store.
     *
     * @param name candidate category name
     * @return true if the name is already present
     */
    @Override
    public boolean existsByName(CategoryName name) {

        return categoryStore.values()
                .stream()
                .anyMatch(category ->
                        category.name()
                                .name()
                                .equalsIgnoreCase(name.name())
                );
    }
}
