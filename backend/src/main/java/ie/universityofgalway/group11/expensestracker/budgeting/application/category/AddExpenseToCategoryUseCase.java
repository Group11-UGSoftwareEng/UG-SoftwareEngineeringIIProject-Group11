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

import java.util.Optional;

public class AddExpenseToCategoryUseCase implements UseCase<AddExpenseToCategoryCommand, AddExpenseToCategoryResponse> {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;

    public AddExpenseToCategoryUseCase(ExpenseRepository expenseRepository, CategoryRepository categoryRepository) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
    }

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
