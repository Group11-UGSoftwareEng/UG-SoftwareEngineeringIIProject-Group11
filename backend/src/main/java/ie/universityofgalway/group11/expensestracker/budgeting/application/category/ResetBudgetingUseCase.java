package ie.universityofgalway.group11.expensestracker.budgeting.application.category;

import ie.universityofgalway.group11.expensestracker.budgeting.application.common.UseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.Category;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryRepository;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ResetBudgetingUseCase
        implements UseCase<YearMonth, List<Category>> {

    private final CategoryRepository categoryRepository;

    public ResetBudgetingUseCase(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

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