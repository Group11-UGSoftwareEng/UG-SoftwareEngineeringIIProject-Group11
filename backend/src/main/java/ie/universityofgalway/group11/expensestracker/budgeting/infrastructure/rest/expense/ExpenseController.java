package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.rest.expense;

//import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.ViewMyExpensesUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;
//import ie.universityofgalway.group11.expensestracker.useradmission.domain.user.UserId;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

//    private final ViewMyExpensesUseCase viewMyExpensesUseCase;
//
//    public ExpenseController(ViewMyExpensesUseCase viewMyExpensesUseCase) {
//        this.viewMyExpensesUseCase = viewMyExpensesUseCase;
//    }
//
//    @GetMapping(value = "/demo/expenses/{userId}", produces = "text/plain")
//    public String viewExpenses(@PathVariable("userId") UUID userId) {
//
//        // Convert the ID to the team's UserId object
//        UserId ownerId = UserId.of(userId);
//        List<Expense> expenses = viewMyExpensesUseCase.execute(ownerId);
//
//        if (expenses.isEmpty()) {
//            return "You have not added any expenses yet.";
//        }
//
//        String result = "My Expenses\n\n";
//
//        for (Expense expense : expenses) {
//            result += "Amount: " + expense.getAmount().value() + "\n";
//            result += "Date: " + expense.getDate() + "\n";
//            result += "Category: " + expense.getCategory().name() + "\n\n";
//        }
//
//        return result;
//    }
}