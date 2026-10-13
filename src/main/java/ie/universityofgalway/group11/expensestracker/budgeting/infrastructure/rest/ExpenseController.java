package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.rest;

import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.ViewMyExpensesUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ExpenseController {

    private final ViewMyExpensesUseCase viewMyExpensesUseCase;

    public ExpenseController(ViewMyExpensesUseCase viewMyExpensesUseCase) {
        this.viewMyExpensesUseCase = viewMyExpensesUseCase;
    }

    @GetMapping(value = "/demo/expenses/{userId}", produces = "text/plain")
    public String viewExpenses(@PathVariable("userId") String userId) {

        // Get the user's expenses
        List<Expense> expenses = viewMyExpensesUseCase.execute(userId);

        if (expenses.isEmpty()) {
            return "You have not added any expenses yet.";
        }

        String result = "My Expenses\n\n";

        // Add each expense to the result
        for (Expense expense : expenses) {
            result += "Amount: " + expense.getAmount() + "\n";
            result += "Date: " + expense.getDate() + "\n";
            result += "Category: " + expense.getCategory() + "\n\n";
        }

        return result;
    }
}