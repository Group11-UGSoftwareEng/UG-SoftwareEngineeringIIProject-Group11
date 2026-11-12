package ie.universityofgalway.group11.expensestracker.budgeting.application.category;

import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.EditCategoryDetailsCommand;
import ie.universityofgalway.group11.expensestracker.budgeting.application.common.UseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.Category;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryId;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryName;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryRepository;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryBudget;

/**
 * Changes a category name and saves the category.
 * Its ID, budget and linked expenses remain unchanged.
 */
public class EditCategoryDetailsUseCase
        implements UseCase<EditCategoryDetailsCommand, Category> {

    private final CategoryRepository categoryRepository;

    /**
     * Creates the category editing use case.
     *
     * @param categoryRepository repository used to find and save categories
     */
    public EditCategoryDetailsUseCase(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Finds the category, validates the new name and saves the change.
     *
     * @param input category ID and new name
     * @return the saved category
     * @throws IllegalArgumentException if the category is missing or the name is invalid
     */
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