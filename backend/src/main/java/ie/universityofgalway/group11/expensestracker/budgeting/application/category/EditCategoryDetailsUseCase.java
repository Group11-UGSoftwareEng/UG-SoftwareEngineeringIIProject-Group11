package ie.universityofgalway.group11.expensestracker.budgeting.application.category;

import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.EditCategoryDetailsCommand;
import ie.universityofgalway.group11.expensestracker.budgeting.application.common.UseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.Category;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryId;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryName;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryRepository;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryBudget;

public class EditCategoryDetailsUseCase
        implements UseCase<EditCategoryDetailsCommand, Category> {

    private final CategoryRepository categoryRepository;

    public EditCategoryDetailsUseCase(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category execute(EditCategoryDetailsCommand input) {
        CategoryId id = CategoryId.of(input.categoryId());

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Category not found."));

        category.changeName(CategoryName.of(input.newName()));

        CategoryBudget updatedBudget = (input.newBudget() != null)
                ? CategoryBudget.of(input.newBudget())
                : null;
        category.updateBudget(updatedBudget);

        return categoryRepository.save(category);
    }
}