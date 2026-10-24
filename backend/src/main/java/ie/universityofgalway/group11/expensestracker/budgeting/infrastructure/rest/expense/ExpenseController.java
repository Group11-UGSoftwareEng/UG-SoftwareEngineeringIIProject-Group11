package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.rest.expense;

import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.ViewMyExpensesUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    private final ViewMyExpensesUseCase viewMyExpensesUseCase;

    public ExpenseController(ViewMyExpensesUseCase viewMyExpensesUseCase) {
        this.viewMyExpensesUseCase = viewMyExpensesUseCase;
    }

    @GetMapping(produces = "text/plain")
    public String viewExpenses() {
        List<Expense> expenses = viewMyExpensesUseCase.execute(null);

        if (expenses.isEmpty()) {
            return "No expenses have been added yet.";
        }

        String result = "Expenses\n\n";

        for (Expense expense : expenses) {
            result += "Amount: " + expense.getAmount().value() + "\n";
            result += "Date: " + expense.getDate() + "\n";
            result += "Category: " + expense.getCategory().name() + "\n\n";
        }

        return result;
    }
}