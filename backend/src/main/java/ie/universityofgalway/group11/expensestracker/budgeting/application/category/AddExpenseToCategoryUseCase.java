package ie.universityofgalway.group11.expensestracker.budgeting.application.category;

import ie.universityofgalway.group11.expensestracker.budgeting.application.common.UseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.application.common.exceptions.CategoryException;
import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.dto.AddExpenseToCategoryCommand;
import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.dto.AddExpenseToCategoryResponse;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.Category;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryId;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryRepository;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.*;

public class AddExpenseToCategoryUseCase implements UseCase<AddExpenseToCategoryCommand, AddExpenseToCategoryResponse> {

    private final CategoryRepository categoryRepository;

    public AddExpenseToCategoryUseCase(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // TODO: add to an existing category: category = findById(id), category.addExpense(expense).
    @Override
    public AddExpenseToCategoryResponse execute(AddExpenseToCategoryCommand command) {
        Expense expense = Expense.create(CategoryId.of(command.categoryId()), Amount.of(command.amount()), Description.of(command.description()), Currency.of(command.currency()));
        Expense addedExpense = categoryRepository.add(expense);
        Category categoryWhereExpenseIsAdded = categoryRepository.findById(CategoryId.of(command.categoryId()));

        if (categoryWhereExpenseIsAdded == null) {
            throw new CategoryException("Category does not exist in database. It is not possible to add an expense to this UUID.");
        }

        categoryWhereExpenseIsAdded.addExpense(expense);

        return new AddExpenseToCategoryResponse(addedExpense.categoryId().asUuid(),
                                                addedExpense.id().asUuid(),
                                                addedExpense.amount().value(),
                                                addedExpense.date(),
                                                addedExpense.description().toString(),
                                                addedExpense.currency().code());
    }

}
