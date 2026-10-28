package ie.universityofgalway.group11.expensestracker.budgeting.application.expense.dto;

import java.util.UUID;

public record DeleteExpenseCommand(
    UUID expenseId
) {}
