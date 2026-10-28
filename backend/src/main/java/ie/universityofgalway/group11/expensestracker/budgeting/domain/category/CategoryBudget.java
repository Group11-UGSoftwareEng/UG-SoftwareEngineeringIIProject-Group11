package ie.universityofgalway.group11.expensestracker.budgeting.domain.category;

import java.math.BigDecimal;

public class CategoryBudget {

    private final BigDecimal value;

    private CategoryBudget(BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("Budget for the category is required");
        }
        if (value.signum() <= 0) {
            throw new IllegalArgumentException("Budget for the category must be greater than 0");
        }
        this.value = value;
    }

    public static CategoryBudget of(Double value) {
        if (value == null) {
            throw new IllegalArgumentException("Budget for the category is missing."); //TODO: put into common
        }
        return new CategoryBudget(BigDecimal.valueOf(value));
    }

    public BigDecimal value() {
        return value;
    }


}
