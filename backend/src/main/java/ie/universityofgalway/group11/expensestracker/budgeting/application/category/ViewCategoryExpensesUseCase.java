package ie.universityofgalway.group11.expensestracker.budgeting.application.category;

import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.ViewCategoryExpensesCommand;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.ViewCategoryExpensesResponse;
import ie.universityofgalway.group11.expensestracker.budgeting.application.common.UseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryId;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryRepository;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseRepository;

import java.util.ArrayList;
import java.util.List;

public class ViewCategoryExpensesUseCase
        implements UseCase<ViewCategoryExpensesCommand, ViewCategoryExpensesResponse> {

    private final CategoryRepository categoryRepository;
    private final ExpenseRepository expenseRepository;

    public ViewCategoryExpensesUseCase(
            CategoryRepository categoryRepository,
            ExpenseRepository expenseRepository) {
        this.categoryRepository = categoryRepository;
        this.expenseRepository = expenseRepository;
    }

    @Override
    public ViewCategoryExpensesResponse execute(
            ViewCategoryExpensesCommand command) {

        CategoryId categoryId = CategoryId.of(command.categoryId());

        categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Category not found."));

        List<Expense> expenses = new ArrayList<>(
                expenseRepository.findByCategoryId(categoryId));

        expenses.sort((first, second) ->
                second.date().compareTo(first.date()));

        List<ViewCategoryExpensesResponse.ExpenseDetails> details =
                new ArrayList<>();

        for (Expense expense : expenses) {
            details.add(new ViewCategoryExpensesResponse.ExpenseDetails(
                    expense.id().asUuid(),
                    expense.categoryId().asUuid(),
                    expense.amount().value(),
                    expense.date(),
                    expense.description().toString(),
                    expense.currency().code()));
        }

        return new ViewCategoryExpensesResponse(details);
    }
}