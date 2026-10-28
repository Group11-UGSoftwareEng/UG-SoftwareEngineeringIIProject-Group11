package ie.universityofgalway.group11.expensestracker.budgeting.application.expense.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record AddExpenseToCategoryResponse(UUID categoryId,
                                           UUID expenseId,
                                           BigDecimal amount,
                                           LocalDate dateAdded,
                                           String description,
                                           String currency) {}