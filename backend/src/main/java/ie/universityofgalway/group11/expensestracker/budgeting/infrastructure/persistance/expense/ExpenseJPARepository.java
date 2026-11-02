package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.expense;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExpenseJPARepository
        extends JpaRepository<ExpenseEntity, UUID> {

    List<ExpenseEntity> findByCategory_Id(UUID categoryId);
}