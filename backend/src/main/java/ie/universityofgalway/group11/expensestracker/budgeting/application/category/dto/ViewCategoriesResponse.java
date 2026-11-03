package ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ViewCategoriesResponse(List<CategoryDetails> categories) {

    public record CategoryDetails(
            UUID id,
            String name,
            BigDecimal budget,
            int year,
            int month) {
    }
}