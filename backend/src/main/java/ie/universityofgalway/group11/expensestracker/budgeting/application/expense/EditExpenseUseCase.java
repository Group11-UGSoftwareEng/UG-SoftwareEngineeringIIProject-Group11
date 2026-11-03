package ie.universityofgalway.group11.expensestracker.budgeting.application.expense;

import ie.universityofgalway.group11.expensestracker.budgeting.application.common.UseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.application.common.exceptions.CategoryNotFoundException;
import ie.universityofgalway.group11.expensestracker.budgeting.application.common.exceptions.ExpenseNotFoundException;
import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.dto.EditExpenseCommand;
import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.dto.EditExpenseResponse;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.Category;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryRepository;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Amount;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Description;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseId;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseRepository;

public class EditExpenseUseCase implements UseCase<EditExpenseCommand, EditExpenseResponse> {
    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    public EditExpenseUseCase(ExpenseRepository expenseRepository, CategoryRepository categoryRepository) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public EditExpenseResponse execute(EditExpenseCommand command) {
        ExpenseId id = ExpenseId.of(command.expenseId());
        Expense expense = expenseRepository.findById(id).orElseThrow(() -> new ExpenseNotFoundException(id));
        Amount amount = Amount.of(command.amount());
        Description description = Description.of(command.description());
        if (command.date() == null) {
            throw new IllegalArgumentException("Date is required");
        }
        Category category = categoryRepository.findById(expense.categoryId()).orElseThrow(() -> new CategoryNotFoundException(expense.categoryId()));
        expense.updateAmount(amount);
        expense.updateDate(command.date());
        expense.updateDescription(description);
        Expense saved = expenseRepository.save(expense, category);
        return new EditExpenseResponse(
                saved.id().asUuid(),
                saved.amount().value(),
                saved.date(),
                saved.description().toString());
    }
}