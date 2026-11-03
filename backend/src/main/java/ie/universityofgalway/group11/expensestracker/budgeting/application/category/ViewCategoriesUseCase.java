package ie.universityofgalway.group11.expensestracker.budgeting.application.category;

import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.ViewCategoriesCommand;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.ViewCategoriesResponse;
import ie.universityofgalway.group11.expensestracker.budgeting.application.common.UseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.Category;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryRepository;

import java.util.ArrayList;
import java.util.List;

public class ViewCategoriesUseCase
        implements UseCase<ViewCategoriesCommand, ViewCategoriesResponse> {

    private final CategoryRepository categoryRepository;

    public ViewCategoriesUseCase(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ViewCategoriesResponse execute(ViewCategoriesCommand command) {
        List<ViewCategoriesResponse.CategoryDetails> categories =
                new ArrayList<>();

        for (Category category : categoryRepository.findAllCategories()) {
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