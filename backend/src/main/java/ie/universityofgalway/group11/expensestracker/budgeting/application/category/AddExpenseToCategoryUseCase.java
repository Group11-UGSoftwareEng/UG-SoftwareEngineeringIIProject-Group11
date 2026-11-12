package ie.universityofgalway.group11.expensestracker.budgeting.application.category;

import ie.universityofgalway.group11.expensestracker.budgeting.application.common.UseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.application.common.exceptions.CategoryException;
import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.dto.AddExpenseToCategoryCommand;
import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.dto.AddExpenseToCategoryResponse;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.Category;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryId;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryRepository;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.*;
import jakarta.transaction.Transactional;

/**
 * Application service responsible for creating a new expense inside an existing category.
 * This use case validates the target category, constructs the domain object, persists it,
 * and returns the resulting expense data to the caller.
 */
public class AddExpenseToCategoryUseCase implements UseCase<AddExpenseToCategoryCommand, AddExpenseToCategoryResponse> {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;

    /**
     * Creates a new use case instance with the required repositories.
     *
     * @param expenseRepository repository used to persist the expense record
     * @param categoryRepository repository used to resolve the destination category
     */
    public AddExpenseToCategoryUseCase(ExpenseRepository expenseRepository, CategoryRepository categoryRepository) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Adds a new expense to the specified category and returns a response model.
     *
     * @param command request data containing the target category and expense details
     * @return the newly created expense snapshot
     * @throws CategoryException when the target category cannot be found
     */
    @Transactional
    @Override
    public AddExpenseToCategoryResponse execute(AddExpenseToCategoryCommand command) {
        Category toCategory = categoryRepository
                .findById(CategoryId.of(command.categoryId()))
                .orElseThrow(() ->
                        new CategoryException(
                                "Category does not exist. Cannot add expense."
                        ));

        Expense addedExpense = Expense.create(
                CategoryId.of(command.categoryId()),
                Amount.of(command.amount()),
                Description.of(command.description()),
                Currency.of(command.currency()));


        expenseRepository.save(addedExpense, toCategory); // saves to database
        toCategory.addExpenseToTheCategory(addedExpense); // saves to the category list


        return new AddExpenseToCategoryResponse(addedExpense.categoryId().asUuid(),
                                                addedExpense.id().asUuid(),
                                                addedExpense.amount().value(),
                                                addedExpense.date(),
                                                addedExpense.description().toString(),
                                                addedExpense.currency().code());
    }

}
