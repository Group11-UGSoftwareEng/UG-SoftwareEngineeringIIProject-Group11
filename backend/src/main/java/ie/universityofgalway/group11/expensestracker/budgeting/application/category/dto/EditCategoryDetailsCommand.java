package ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto;

import java.util.UUID;

public record EditCategoryDetailsCommand(UUID categoryId, String newName) {

    public EditCategoryDetailsCommand {
        if (categoryId == null) {
            throw new IllegalArgumentException("Category ID is required.");
        }

        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("Category name is required.");
        }

        newName = newName.trim();
    }
}