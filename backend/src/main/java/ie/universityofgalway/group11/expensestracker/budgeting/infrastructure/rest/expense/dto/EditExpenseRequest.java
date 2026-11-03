package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.rest.expense.dto;

import java.time.LocalDate;

public record EditExpenseRequest(Double amount, LocalDate date, String description) {
}