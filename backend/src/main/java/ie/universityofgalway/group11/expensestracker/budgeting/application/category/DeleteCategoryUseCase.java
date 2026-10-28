package ie.universityofgalway.group11.expensestracker.budgeting.application.category;

import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.DeleteCategoryCommand;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.DeleteCategoryResponse;
import ie.universityofgalway.group11.expensestracker.budgeting.application.common.UseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.application.common.exceptions.CategoryInUseException;
import ie.universityofgalway.group11.expensestracker.budgeting.application.common.exceptions.CategoryNotFoundException;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.Category;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryId;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryRepository;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseRepository;

public class DeleteCategoryUseCase implements UseCase<DeleteCategoryCommand, DeleteCategoryResponse> {
    private final CategoryRepository categoryRepository;
    private final ExpenseRepository expenseRepository;
    public DeleteCategoryUseCase(CategoryRepository categoryRepository, ExpenseRepository expenseRepository) {
        this.categoryRepository = categoryRepository;
        this.expenseRepository = expenseRepository;
    }

    @Override
    public DeleteCategoryResponse execute(DeleteCategoryCommand command) {
        CategoryId id = CategoryId.of(command.categoryId());
        Category category = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
        long expenseCount = expenseRepository.findAll().stream().filter(expense -> expense.categoryId().equals(id)).count();
        if (expenseCount > 0) {
            throw new CategoryInUseException(expenseCount);
        }
        categoryRepository.deleteById(category.id());
        return new DeleteCategoryResponse(true, "Category deleted.");
    }
}
