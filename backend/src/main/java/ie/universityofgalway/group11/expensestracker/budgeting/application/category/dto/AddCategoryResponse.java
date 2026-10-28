package ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto;

public record AddCategoryResponse(boolean success, String message, String categoryName) {
}