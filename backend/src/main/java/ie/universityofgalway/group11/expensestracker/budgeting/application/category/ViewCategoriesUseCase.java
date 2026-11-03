package ie.universityofgalway.group11.expensestracker.budgeting.application.category;

import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.ViewCategoriesCommand;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.ViewCategoriesResponse;
import ie.universityofgalway.group11.expensestracker.budgeting.application.common.UseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.Category;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class ViewCategoriesUseCase
        implements UseCase<ViewCategoriesCommand, ViewCategoriesResponse> {

    private final ResetBudgetingUseCase resetBudgetingUseCase;
    private final YearMonth budgetingPeriod;

    public ViewCategoriesUseCase(
            ResetBudgetingUseCase resetBudgetingUseCase,
            YearMonth budgetingPeriod) {
        this.resetBudgetingUseCase = resetBudgetingUseCase;
        this.budgetingPeriod = budgetingPeriod;
    }

    @Override
    public ViewCategoriesResponse execute(ViewCategoriesCommand command) {
        List<ViewCategoriesResponse.CategoryDetails> categories =
                new ArrayList<>();

        for (Category category :
                resetBudgetingUseCase.execute(budgetingPeriod)) {
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