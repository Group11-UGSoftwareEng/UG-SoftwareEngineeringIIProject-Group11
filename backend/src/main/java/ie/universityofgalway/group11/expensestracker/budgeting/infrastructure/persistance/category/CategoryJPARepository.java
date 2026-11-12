package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

/**
 * Spring Data repository for category entities.
 * It exposes the persistence queries needed to read and filter categories by identifier and period.
 */
public interface CategoryJPARepository extends JpaRepository<CategoryEntity, UUID> {
    /**
     * Finds a category entity by its primary key.
     *
     * @param id category UUID
     * @return matching entity if present
     */
    Optional<CategoryEntity> findById(UUID id);

    /**
     * Finds all categories within a specific year and month.
     *
     * @param year year to match
     * @param month month to match
     * @return categories for that period
     */
    List<CategoryEntity> findByYearAndMonth(Integer year, Integer month);

    /**
     * Finds the most recent category period in storage.
     *
     * @return latest category entity in descending year/month order
     */
    Optional<CategoryEntity> findFirstByOrderByYearDescMonthDesc();

    /**
     * Checks whether a category already exists with the same name regardless of case.
     *
     * @param name category name
     * @return true if a match is found
     */
    boolean existsByNameIgnoreCase(String name);

    /**
     * Checks whether a category with the given exact name exists.
     *
     * @param name category name
     * @return true if the name is already present
     */
    boolean existsByName(String name);
}