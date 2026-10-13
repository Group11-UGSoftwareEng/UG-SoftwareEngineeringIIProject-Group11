package ie.universityofgalway.group11.expensestracker.budgeting.domain.expense;
import java.util.List;

public interface ExpenseRepository {

    List<Expense> findByUserId(String userId);

}
