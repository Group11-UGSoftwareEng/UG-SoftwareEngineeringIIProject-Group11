package ie.universityofgalway.group11.expensestracker.budgeting.application.expense.dto;

public record DeleteExpenseResponse(
    boolean success,
    String message
) {}
