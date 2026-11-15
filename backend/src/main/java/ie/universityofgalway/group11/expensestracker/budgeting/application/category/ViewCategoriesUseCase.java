package ie.universityofgalway.group11.expensestracker.budgeting.application.category;

import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.ViewCategoriesCommand;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.ViewCategoriesResponse;
import ie.universityofgalway.group11.expensestracker.budgeting.application.common.UseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.Category;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryRepository;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class ViewCategoriesUseCase
        implements UseCase<ViewCategoriesCommand, ViewCategoriesResponse> {

    private final ResetBudgetingUseCase resetBudgetingUseCase;
    private final CategoryRepository categoryRepository;
    private final YearMonth budgetingPeriod;

    public ViewCategoriesUseCase(
            ResetBudgetingUseCase resetBudgetingUseCase,
            CategoryRepository categoryRepository,
            YearMonth budgetingPeriod) {
        this.resetBudgetingUseCase = resetBudgetingUseCase;
        this.categoryRepository = categoryRepository;
        this.budgetingPeriod = budgetingPeriod;
    }

    @Override
    public ViewCategoriesResponse execute(ViewCategoriesCommand command) {
        List<ViewCategoriesResponse.CategoryDetails> categories =
                new ArrayList<>();

        // If a specific past period is requested and it's not the current active period,
        // fetch directly from the repository without auto-copying categories.
        List<Category> domainCategories;
        if (command.period() != null && !command.period().equals(budgetingPeriod)) {
            domainCategories = categoryRepository.findByPeriod(command.period());
        } else {
            domainCategories = resetBudgetingUseCase.execute(budgetingPeriod);
        }

        for (Category category : domainCategories) {
            categories.add(new ViewCategoriesResponse.CategoryDetails(
                    category.id().asUuid(),
                    category.name().name(),
                    category.budget().value(),
                    category.year(),
                    category.month()
            ));
        }

        return new ViewCategoriesResponse(categories);
    }
}