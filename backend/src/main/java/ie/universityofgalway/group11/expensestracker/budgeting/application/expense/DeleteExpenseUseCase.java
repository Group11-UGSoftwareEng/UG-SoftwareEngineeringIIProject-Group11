package ie.universityofgalway.group11.expensestracker.budgeting.application.expense;

import ie.universityofgalway.group11.expensestracker.budgeting.application.common.UseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.application.common.exceptions.ExpenseNotFoundException;
import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.dto.DeleteExpenseCommand;
import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.dto.DeleteExpenseResponse;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseId;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseRepository;

public class DeleteExpenseUseCase implements UseCase<DeleteExpenseCommand, DeleteExpenseResponse> {
    private final ExpenseRepository expenseRepository;
    public DeleteExpenseUseCase(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }
    @Override
    public DeleteExpenseResponse execute(DeleteExpenseCommand input) {
        ExpenseId id = ExpenseId.of(command.expenseId());
        Expense expense = expenseRepository.findById(input.ExpenseId()).orElseThrow(() -> new ExpenseNotFoundException(input.ExpenseId()));
        expenseRepository.deleteById(input.expenseId());
        return new DeleteExpenseResponse(true, "Expense Deleted.");
    }
}
