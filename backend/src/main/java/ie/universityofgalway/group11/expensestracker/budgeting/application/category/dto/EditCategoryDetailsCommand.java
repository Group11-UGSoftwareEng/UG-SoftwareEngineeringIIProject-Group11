package ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto;

public record EditCategoryDetailsCommand(String newName) {

    public EditCategoryDetailsCommand {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("Category name is required.");
        }

        newName = newName.trim();
    }
}