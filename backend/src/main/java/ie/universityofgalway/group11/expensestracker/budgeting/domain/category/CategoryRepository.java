package ie.universityofgalway.group11.expensestracker.budgeting.domain.category;

import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

/**
 * Persistence contract for category aggregates.
 * Implementations are responsible for storing and retrieving category state across time periods.
 */
public interface CategoryRepository {

    /**
     * Finds a single category by its unique identifier.
     *
     * @param categoryId identifier of the category
     * @return the category if present
     */
    Optional<Category> findById(CategoryId categoryId);

    /**
     * Persists or updates a category.
     *
     * @param category category to store
     * @return the stored category instance
     */
    Category save(Category category);

    /**
     * @return all categories currently known to the repository
     */
    List<Category> findAllCategories();

    /**
     * Deletes a category by identifier.
     *
     * @param id category to remove
     */
    void deleteById(CategoryId id);

    /**
     * Lists categories belonging to a specific month.
     *
     * @param period target period
     * @return matching categories
     */
    List<Category> findByPeriod(YearMonth period);

    /**
     * Finds the latest budgeting period available in storage.
     *
     * @return most recent year/month if any category exists
     */
    Optional<YearMonth> findLatestPeriod();

    /**
     * Persists multiple categories in one operation.
     *
     * @param categories categories to save
     */
    void saveAll(List<Category> categories);

    /**
     * Checks whether a category name already exists in the repository.
     *
     * @param name candidate category name
     * @return true if the name is already taken
     */
    boolean existsByName(CategoryName name);
}
