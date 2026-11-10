
package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.budget;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BudgetLimitRepository
        extends JpaRepository<BudgetLimitEntity, String> {

    List<BudgetLimitEntity> findByMonthAndCurrency(
            String month,
            String currency
    );
}
