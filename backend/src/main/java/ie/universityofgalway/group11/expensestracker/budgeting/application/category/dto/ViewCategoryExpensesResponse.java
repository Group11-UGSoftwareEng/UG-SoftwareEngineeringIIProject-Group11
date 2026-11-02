package ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ViewCategoryExpensesResponse(List<ExpenseDetails> expenses) {

    public record ExpenseDetails(
            UUID id,
            UUID categoryId,
            BigDecimal amount,
            LocalDate date,
            String description,
            String currency) {
    }
}