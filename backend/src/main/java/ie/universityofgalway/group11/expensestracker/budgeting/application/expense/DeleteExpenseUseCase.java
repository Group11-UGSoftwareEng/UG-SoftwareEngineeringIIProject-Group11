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
    public DeleteExpenseResponse execute(DeleteExpenseCommand command) {
        ExpenseId id = ExpenseId.of(command.expenseId());
        Expense expense = expenseRepository.findById(id).orElseThrow(() -> new ExpenseNotFoundException(id));
        expenseRepository.deleteById(expense.id());
        return new DeleteExpenseResponse(true, "Expense Deleted.");
    }
}
