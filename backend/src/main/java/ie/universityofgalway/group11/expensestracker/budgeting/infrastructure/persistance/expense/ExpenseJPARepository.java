package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.expense;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data repository for expense records.
 * It provides a lookup by category to support category-level summaries and edits.
 */
public interface ExpenseJPARepository
        extends JpaRepository<ExpenseEntity, UUID> {

    /**
     * Finds all expenses linked to the given category ID.
     *
     * @param categoryId owning category UUID
     * @return expenses in that category
     */
    List<ExpenseEntity> findByCategory_Id(UUID categoryId);
}