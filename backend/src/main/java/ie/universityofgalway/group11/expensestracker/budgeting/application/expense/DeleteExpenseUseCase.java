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
        ExpenseId id = ExpenseId.of(input.expenseId());

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(ExpenseNotFoundException::new);

        expenseRepository.deleteById(expense.id());

        return new DeleteExpenseResponse(true, "Expense Deleted.");
    }
}
