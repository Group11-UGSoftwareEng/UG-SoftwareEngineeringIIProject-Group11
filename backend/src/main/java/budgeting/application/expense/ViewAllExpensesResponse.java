package ie.universityofgalway.group11.expensestracker.budgeting.application.expense;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;
import java.util.List;

public record ViewAllExpensesResponse(List<Expense> expenses) {
    //TODO: public record ExpenseSummary(String id, String amount, String description, LocalDate....) {}
}
