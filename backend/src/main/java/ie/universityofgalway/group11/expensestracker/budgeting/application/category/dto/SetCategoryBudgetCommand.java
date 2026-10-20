package ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto;

import java.math.BigDecimal;

public record SetCategoryBudgetCommand(String userId, String categoryName, BigDecimal limit) {
    public SetCategoryBudgetCommand {
        if (limit == null || limit.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Budget limit must be greater than zero.");
        }
        if (categoryName == null || categoryName.isBlank()) {
            throw new IllegalArgumentException("Category name is required.");
        }
    }
}