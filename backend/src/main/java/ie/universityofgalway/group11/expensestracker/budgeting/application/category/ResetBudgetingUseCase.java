package ie.universityofgalway.group11.expensestracker.budgeting.application.category;

import ie.universityofgalway.group11.expensestracker.budgeting.application.common.UseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.Category;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryRepository;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Loads categories for a budgeting month.
 * If none exist, copies names and budgets from the latest saved month.
 * Existing categories and expenses remain unchanged.
 */
public class ResetBudgetingUseCase
        implements UseCase<YearMonth, List<Category>> {

    private final CategoryRepository categoryRepository;

    /**
     * Creates the monthly budgeting use case.
     *
     * @param categoryRepository repository used to load and save categories
     */
    public ResetBudgetingUseCase(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Returns existing categories or creates categories for the requested month.
     * New categories have new IDs and no expenses.
     *
     * @param currentPeriod month to load
     * @return categories for the month, or an empty list if no saved period exists
     */
    @Override
    public List<Category> execute(YearMonth currentPeriod) {
        List<Category> currentCategories =
                categoryRepository.findByPeriod(currentPeriod);

        if (!currentCategories.isEmpty()) {
            return currentCategories;
        }

        Optional<YearMonth> latestPeriod =
                categoryRepository.findLatestPeriod();

        if (latestPeriod.isEmpty()) {
            return new ArrayList<>();
        }

        List<Category> previousCategories =
                categoryRepository.findByPeriod(latestPeriod.get());

        List<Category> newCategories = new ArrayList<>();

        for (Category previous : previousCategories) {
            Category category = Category.create(
                    previous.name(),
                    currentPeriod,
                    previous.budget());

            newCategories.add(category);
        }

        categoryRepository.saveAll(newCategories);

        return newCategories;
    }
}