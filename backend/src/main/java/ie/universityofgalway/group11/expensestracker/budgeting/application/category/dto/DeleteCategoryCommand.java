package ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto;

import java.util.UUID;

public record DeleteCategoryCommand(
    UUID categoryId
) {}
