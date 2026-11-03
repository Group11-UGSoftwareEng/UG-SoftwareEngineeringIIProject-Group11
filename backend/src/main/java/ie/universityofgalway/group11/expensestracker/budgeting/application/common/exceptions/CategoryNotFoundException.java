package ie.universityofgalway.group11.expensestracker.budgeting.application.common.exceptions;

public class CategoryNotFoundException extends RuntimeException {
   public CategoryNotFoundException(CategoryId id) {
        super("Category not found: " + id);
    }
}
