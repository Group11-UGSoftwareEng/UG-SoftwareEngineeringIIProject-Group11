package ie.universityofgalway.group11.expensestracker.expenses.domain.expense;

import java.math.BigDecimal;

public record Amount(BigDecimal value) {

    public Amount {
        if (value == null) {
            throw new IllegalArgumentException("Amount is required");
        }
        if (value.signum() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
    }
}
