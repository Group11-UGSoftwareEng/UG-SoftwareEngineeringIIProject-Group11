package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.rest.expense;

import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.ViewMyExpensesUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//import ie.universityofgalway.group11.expensestracker.useradmission.domain.user.UserId;

import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    //TODO unused...
    private ExpenseRepository expenseRepository;


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
            result += "Amount: " + expense.amount().value() + "\n";
            result += "Date: " + expense.date() + "\n";
            //result += "Category: " + expense.getCategory().name() + "\n\n";
        }

        return result;
    }
}