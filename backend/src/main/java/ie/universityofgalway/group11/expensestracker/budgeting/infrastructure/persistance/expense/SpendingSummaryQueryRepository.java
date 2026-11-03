
package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.expense;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface SpendingSummaryQueryRepository
        extends Repository<ExpenseEntity, UUID> {

    @Query("""
        SELECT e
        FROM ExpenseEntity e
        JOIN FETCH e.category
        WHERE e.date >= :startDate
          AND e.date <= :endDate
          AND UPPER(e.currency) = :currency
        """)
    List<ExpenseEntity> findForSummary(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("currency") String currency
    );
}
