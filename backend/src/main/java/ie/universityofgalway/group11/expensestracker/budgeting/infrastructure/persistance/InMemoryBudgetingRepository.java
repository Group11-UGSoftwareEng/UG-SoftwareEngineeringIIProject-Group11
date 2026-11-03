
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
 * In-memory repository used for testing and prototyping.
 *
 * Each new repository starts empty.
 * Categories and expenses must be added explicitly.
 *
 * This does not affect PostgreSQL persistence.
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

    @Override
    public Optional<Expense> findById(ExpenseId id) {
        return Optional.ofNullable(expenseStore.get(id));
    }

    @Override
    public Expense save(Expense expense, Category toCategory) {
        expenseStore.put(expense.id(), expense);
        return expense;
    }

    @Override
    public void deleteById(ExpenseId id) {
        expenseStore.remove(id);
    }

    @Override
    public List<Expense> findAll() {
        return new ArrayList<>(expenseStore.values());
    }

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

    @Override
    public Optional<Category> findById(CategoryId categoryId) {
        return Optional.ofNullable(
                categoryStore.get(categoryId)
        );
    }

    @Override
    public Category save(Category category) {
        categoryStore.put(category.id(), category);
        return category;
    }

    @Override
    public void deleteById(CategoryId id) {
        categoryStore.remove(id);
    }

    @Override
    public List<Category> findAllCategories() {
        return new ArrayList<>(categoryStore.values());
    }

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

    @Override
    public void saveAll(List<Category> categories) {

        for (Category category : categories) {
            categoryStore.put(
                    category.id(),
                    category
            );
        }
    }

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
