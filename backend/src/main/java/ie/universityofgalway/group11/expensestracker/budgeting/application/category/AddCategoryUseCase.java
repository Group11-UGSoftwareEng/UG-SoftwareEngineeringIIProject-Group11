package ie.universityofgalway.group11.expensestracker.budgeting.application.category;

import java.time.YearMonth;

import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryBudget;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.AddCategoryCommand;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.AddCategoryResponse;
import ie.universityofgalway.group11.expensestracker.budgeting.application.common.UseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.application.common.exceptions.CategoryException;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.Category;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryName;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryRepository;

public class AddCategoryUseCase implements UseCase<AddCategoryCommand, AddCategoryResponse> {

    private final CategoryRepository categoryRepository;

    public AddCategoryUseCase(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public AddCategoryResponse execute(AddCategoryCommand input) {
        CategoryName name = CategoryName.of(input.categoryName()); // throws if blank

        if (categoryRepository.existsByName(name)) {
            throw new CategoryException(
                    "A category with the name '" + input.categoryName() + "' already exists.");
        }

        CategoryBudget budget = (input.budget() != null)
                ? CategoryBudget.of(input.budget())
                : null;

        Category category = Category.create(name, YearMonth.now(), budget);
        categoryRepository.save(category);

        return new AddCategoryResponse(true,
                "Category '" + input.categoryName() + "' created successfully.",
                input.categoryName());
    }
}