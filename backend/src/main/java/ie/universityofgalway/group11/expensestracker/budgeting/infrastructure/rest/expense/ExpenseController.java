package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.rest.expense;

import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.ViewAllExpensesResponse;
import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.ViewAllExpensesUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.rest.expense.dto.ViewAllExpensesQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//import ie.universityofgalway.group11.expensestracker.useradmission.domain.user.UserId;

import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseRepository;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    //TODO unused...
    private ExpenseRepository expenseRepository;


    private final ViewAllExpensesUseCase viewAllExpensesUseCase;

    public ExpenseController(ViewAllExpensesUseCase viewAllExpensesUseCase) {
        this.viewAllExpensesUseCase = viewAllExpensesUseCase;
    }

    @GetMapping(produces = "text/plain")
    public String viewExpenses() {
        ViewAllExpensesResponse response = viewAllExpensesUseCase.execute(new ViewAllExpensesQuery());
        List<Expense> expenses = response.expenses();

        if (expenses.isEmpty()) {
            return "No expenses have been added yet.";
        }
        String result = "Expenses\n\n";
        int count = 1;
        for (Expense expense : expenses) {
            result += count++ + ". " + expense.toString() + "\n";
        }
        return result;
    }
}