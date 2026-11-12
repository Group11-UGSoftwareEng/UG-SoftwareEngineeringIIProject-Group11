package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.rest.expense;

import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.dto.ViewAllExpensesResponse;
import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.ViewAllExpensesUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.rest.expense.dto.ViewAllExpensesQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ie.universityofgalway.group11.expensestracker.budgeting.application.common.exceptions.AmountException;
import ie.universityofgalway.group11.expensestracker.budgeting.application.common.exceptions.CategoryNotFoundException;
import ie.universityofgalway.group11.expensestracker.budgeting.application.common.exceptions.ExpenseNotFoundException;
import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.DeleteExpenseUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.EditExpenseUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.dto.DeleteExpenseCommand;
import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.dto.EditExpenseCommand;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.rest.expense.dto.EditExpenseRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.UUID;
//import ie.universityofgalway.group11.expensestracker.useradmission.domain.user.UserId;

import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseRepository;

import java.util.List;

/**
 * REST controller for expense-level operations such as viewing, editing, and deleting expenses.
 */
@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    //TODO unused...
    private ExpenseRepository expenseRepository;

    private final ViewAllExpensesUseCase viewAllExpensesUseCase;
    private final EditExpenseUseCase editExpenseUseCase;
    private final DeleteExpenseUseCase deleteExpenseUseCase;

    /**
     * Creates the controller with the supporting use cases.
     *
     * @param viewAllExpensesUseCase service for listing expenses
     * @param editExpenseUseCase service for modifying an expense
     * @param deleteExpenseUseCase service for removing an expense
     */
    public ExpenseController(ViewAllExpensesUseCase viewAllExpensesUseCase, EditExpenseUseCase editExpenseUseCase, DeleteExpenseUseCase deleteExpenseUseCase) {
        this.viewAllExpensesUseCase = viewAllExpensesUseCase;
        this.editExpenseUseCase = editExpenseUseCase;
        this.deleteExpenseUseCase = deleteExpenseUseCase;
    }

    /**
     * Lists all stored expenses as simple text output.
     *
     * @return human-readable expense summary
     */
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

    /**
     * Updates an existing expense's details.
     *
     * @param expenseId identifier of the target expense
     * @param request new expense data
     * @return updated expense or a structured error response
     */
    @PutMapping("/{expenseId}")
    public ResponseEntity<?> editExpense(@PathVariable("expenseId") UUID expenseId, @RequestBody EditExpenseRequest request) {
        try {
            return ResponseEntity.ok(editExpenseUseCase.execute(new EditExpenseCommand(expenseId, request.amount(), request.date(), request.description())));
        }
        catch (ExpenseNotFoundException | CategoryNotFoundException error) {
            return ResponseEntity.status(404).body(error.getMessage());
        }
        catch (AmountException | IllegalArgumentException error) {
            return ResponseEntity.status(400).body(error.getMessage());
        }
    }

    /**
     * Deletes an expense by its identifier.
     *
     * @param expenseId expense to remove
     * @return success response or 404 when not found
     */
    @DeleteMapping("/{expenseId}")
    public ResponseEntity<?> deleteExpense(@PathVariable("expenseId") UUID expenseId) {
        try {
            return ResponseEntity.ok(deleteExpenseUseCase.execute(new DeleteExpenseCommand(expenseId)));
        }
        catch (ExpenseNotFoundException error) {
            return ResponseEntity.status(404).body(error.getMessage());
        }
    }
}