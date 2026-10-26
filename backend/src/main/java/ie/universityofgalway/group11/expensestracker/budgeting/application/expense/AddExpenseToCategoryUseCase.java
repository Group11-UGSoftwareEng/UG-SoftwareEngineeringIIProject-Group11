package ie.universityofgalway.group11.expensestracker.budgeting.application.expense;

import ie.universityofgalway.group11.expensestracker.budgeting.application.common.UseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.dto.AddExpenseToCategoryCommand;
import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.dto.AddExpenseToCategoryResponse;
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
    public AddExpenseToCategoryResponse execute(AddExpenseToCategoryCommand input) {
        Expense expense = Expense.create(CategoryId.of(input.categoryId()), Amount.of(input.amount()), Description.of(input.description()), Currency.of(input.currency()));
        Expense addedExpense = categoryRepository.add(expense);
        System.out.println(addedExpense);
        return new AddExpenseToCategoryResponse(addedExpense.categoryId().asUuid(),
                                      addedExpense.id().asUuid(),
                                      addedExpense.amount().value(),
                                      addedExpense.date(),
                                      addedExpense.description().toString(),
                                      addedExpense.currency().code());
    }

}
