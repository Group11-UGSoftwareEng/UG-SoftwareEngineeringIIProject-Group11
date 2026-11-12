package ie.universityofgalway.group11.expensestracker.budgeting.domain.category;

import java.math.BigDecimal;

/**
 * Value object representing the spending limit for a category.
 * The value must always be positive so that a category cannot have an invalid or zero budget.
 */
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

    /**
     * Creates a category budget from a nullable numeric value.
     *
     * @param value budget amount in the chosen currency
     * @return a validated category budget
     */
    public static CategoryBudget of(Double value) {
        if (value == null) {
            throw new IllegalArgumentException("Budget for the category is missing."); //TODO: put into common
        }
        return new CategoryBudget(BigDecimal.valueOf(value));
    }

    /**
     * @return the precise numeric value of the budget
     */
    public BigDecimal value() {
        return value;
    }


}
