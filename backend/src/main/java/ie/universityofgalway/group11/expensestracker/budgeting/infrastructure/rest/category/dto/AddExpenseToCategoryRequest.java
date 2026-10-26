package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.rest.category.dto;

import java.util.UUID;

public record AddExpenseToCategoryRequest(UUID categoryId, Double amount, String description, String currency) { }
