package ie.universityofgalway.group11.expensestracker.budgeting.application.expense.dto;

import java.util.UUID;

public record AddExpenseToCategoryCommand(
        UUID categoryId,
        Double amount,
        String description,
        String currency
) {}
