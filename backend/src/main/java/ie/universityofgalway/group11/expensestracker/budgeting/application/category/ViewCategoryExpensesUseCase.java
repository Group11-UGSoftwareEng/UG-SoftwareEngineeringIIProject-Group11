package ie.universityofgalway.group11.expensestracker.budgeting.application.category;

import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.ViewCategoryExpensesCommand;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.ViewCategoryExpensesResponse;
import ie.universityofgalway.group11.expensestracker.budgeting.application.common.UseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryRepository;

public class ViewCategoryExpensesUseCase implements UseCase<ViewCategoryExpensesCommand, ViewCategoryExpensesResponse> {

    private final CategoryRepository categoryRepository;

    public ViewCategoryExpensesUseCase(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ViewCategoryExpensesResponse execute(ViewCategoryExpensesCommand command) {
        // TODO Implementation for viewing category expenses
        return null;
    }
}
