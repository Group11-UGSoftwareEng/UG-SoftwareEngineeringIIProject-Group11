
package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.expense;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface MonthlyProjectionQueryRepository
        extends Repository<ExpenseEntity, UUID> {

    @Query("""
        SELECT e
        FROM ExpenseEntity e
        JOIN FETCH e.category
        WHERE e.date >= :start
          AND e.date <= :end
          AND UPPER(e.currency) = :currency
        """)
    List<ExpenseEntity> findMonthToDate(
            @Param("start") LocalDate start,
            @Param("end") LocalDate end,
            @Param("currency") String currency
    );
}
