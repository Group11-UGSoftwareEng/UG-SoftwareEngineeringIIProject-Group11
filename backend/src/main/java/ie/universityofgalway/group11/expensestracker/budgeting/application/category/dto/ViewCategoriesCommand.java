package ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto;

import java.time.YearMonth;

public record ViewCategoriesCommand(YearMonth period) {
    public ViewCategoriesCommand() {
        this(null);
    }
}