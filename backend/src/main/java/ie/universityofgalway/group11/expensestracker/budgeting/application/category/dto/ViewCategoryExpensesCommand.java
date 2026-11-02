package ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto;

import java.util.UUID;

public record ViewCategoryExpensesCommand(UUID categoryId) {

    public ViewCategoryExpensesCommand {
        if (categoryId == null) {
            throw new IllegalArgumentException("Category ID is required.");
        }
    }
}