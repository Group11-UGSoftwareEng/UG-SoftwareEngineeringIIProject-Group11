package ie.universityofgalway.group11.expensestracker.budgeting.application.category;

import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.ViewCategoriesCommand;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.ViewCategoriesResponse;
import ie.universityofgalway.group11.expensestracker.budgeting.application.common.UseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.Category;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryRepository;

import java.math.BigDecimal;
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
        YearMonth requestedPeriod = (command != null && command.period() != null)
                ? command.period()
                : budgetingPeriod;

        List<Category> categoryList = requestedPeriod.equals(budgetingPeriod)
                ? resetBudgetingUseCase.execute(budgetingPeriod)
                : categoryRepository.findByPeriod(requestedPeriod);

        List<ViewCategoriesResponse.CategoryDetails> categories = new ArrayList<>();

        for (Category category : categoryList) {
            BigDecimal budgetValue = category.budget() != null
                    ? category.budget().value()
                    : null;

            categories.add(new ViewCategoriesResponse.CategoryDetails(
                    category.id().asUuid(),
                    category.name().name(),
                    budgetValue,
                    category.year(),
                    category.month()
            ));
        }

        return new ViewCategoriesResponse(categories);
    }
}