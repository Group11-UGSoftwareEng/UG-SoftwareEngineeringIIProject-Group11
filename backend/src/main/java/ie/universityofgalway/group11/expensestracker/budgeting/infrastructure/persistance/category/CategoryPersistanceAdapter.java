package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.category;

import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.Category;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryId;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryRepository;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.AdapterHelper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryName;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JPA-backed implementation of the category repository contract.
 * It translates between the domain model and persisted category entities.
 */
@Repository
public class CategoryPersistanceAdapter implements CategoryRepository {

    private final CategoryJPARepository categoryJpaRepository;
    private final AdapterHelper helper;

    /**
     * Creates the adapter with the JPA repository dependency.
     *
     * @param categoryJpaRepository repository managing category entities
     */
    public CategoryPersistanceAdapter(CategoryJPARepository categoryJpaRepository) {
        this.categoryJpaRepository = categoryJpaRepository;
        this.helper = new AdapterHelper();
    }

    /**
     * Finds a category by identifier.
     *
     * @param categoryId category ID to resolve
     * @return matching category if present
     */
    @Override
    public Optional<Category> findById(CategoryId categoryId) {
        return categoryJpaRepository.findById(categoryId.asUuid())
                .map(helper::toCategoryDomain);
    }

    /**
     * Returns all persisted categories as domain objects.
     *
     * @return all known categories
     */
    @Override
    public List<Category> findAllCategories() {
        List<Category> categories = new ArrayList<>();

        for (CategoryEntity entity : categoryJpaRepository.findAll()) {
            categories.add(helper.toCategoryDomain(entity));
        }

        return categories;
    }

    /**
     * Deletes the category with the given identifier.
     *
     * @param id category identifier to delete
     */
    @Override
    @Transactional
    public void deleteById(CategoryId id) {
        categoryJpaRepository.deleteById(id.asUuid());
    }

    /**
     * Finds categories by the creation/period month.
     *
     * @param period year-month filter
     * @return matching categories
     */
    @Override
    public List<Category> findByPeriod(YearMonth period) {
        List<CategoryEntity> entities =
                categoryJpaRepository.findByYearAndMonth(
                        period.getYear(), period.getMonthValue());

        List<Category> categories = new ArrayList<>();

        for (CategoryEntity entity : entities) {
            categories.add(helper.toCategoryDomain(entity));
        }

        return categories;
    }

    /**
     * Finds the most recent stored period.
     *
     * @return latest year-month in the database
     */
    @Override
    public Optional<YearMonth> findLatestPeriod() {
        return categoryJpaRepository.findFirstByOrderByYearDescMonthDesc()
                .map(entity ->
                        YearMonth.of(entity.getYear(), entity.getMonth()));
    }

    /**
     * Saves a batch of categories.
     *
     * @param categories categories to persist
     */
    @Override
    @Transactional
    public void saveAll(List<Category> categories) {
        List<CategoryEntity> entities = new ArrayList<>();

        for (Category category : categories) {
            entities.add(helper.toCategoryEntity(category));
        }

        categoryJpaRepository.saveAll(entities);
    }

    /**
     * Checks whether a category name is already used.
     *
     * @param name category name to test
     * @return true if the name already exists
     */
    @Override
    public boolean existsByName(CategoryName name) {
        return false;
    }

    /**
     * Persists a single category.
     *
     * @param category category aggregate to save
     * @return saved category instance
     */
    @Override
    @Transactional
    public Category save(Category category) {
        CategoryEntity entity = categoryJpaRepository
                .findById(category.id().asUuid())
                .orElseGet(() -> {
                    CategoryEntity newEntity = new CategoryEntity();
                    newEntity.setId(category.id().asUuid());
                    newEntity.setYear(YearMonth.now().getYear());
                    newEntity.setMonth(YearMonth.now().getMonthValue());
                    return newEntity;
                });

        entity.setName(category.name().name());

        // Save the budget if one was provided
        if (category.budget() != null) {
            entity.setBudget(category.budget().value().doubleValue());
        } else {
            entity.setBudget(null);
        }

        categoryJpaRepository.save(entity);

        return category;
    }
}
