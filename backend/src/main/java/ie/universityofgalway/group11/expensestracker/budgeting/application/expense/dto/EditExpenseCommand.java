package ie.universityofgalway.group11.expensestracker.budgeting.application.expense.dto;

import java.time.LocalDate;
import java.util.UUID;

public record EditExpenseCommand(
        UUID expenseId,
        Double amount,
        LocalDate date,
        String description
) 
{}
