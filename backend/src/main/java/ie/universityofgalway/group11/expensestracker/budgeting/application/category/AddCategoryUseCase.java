package ie.universityofgalway.group11.expensestracker.budgeting.application.category;

import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.AddCategoryCommand;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.AddCategoryResponse;
import ie.universityofgalway.group11.expensestracker.budgeting.application.common.UseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Category;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.CategoryRepository;

public class AddCategoryUseCase implements UseCase<AddCategoryCommand, AddCategoryResponse> {

    private final CategoryRepository categoryRepository;

    public AddCategoryUseCase(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public AddCategoryResponse execute(AddCategoryCommand input) {
        Category category = new Category(input.categoryName());

        // Rule check: titles/names cannot be duplicate (case-insensitive)
        if (categoryRepository != null && categoryRepository.existsByNameIgnoreCaseAndUserId(category.name(), input.userId())) {
            throw new IllegalArgumentException("A category with the name '" + category.name() + "' already exists.");
        }

        if (categoryRepository != null) {
            categoryRepository.save(category, input.userId());
        }

        return new AddCategoryResponse(
                true,
                "Category '" + category.name() + "' created successfully.",
                category.name()
        );
    }
}