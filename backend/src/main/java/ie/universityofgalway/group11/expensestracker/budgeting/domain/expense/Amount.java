package ie.universityofgalway.group11.expensestracker.budgeting.domain.expense;

import java.math.BigDecimal;

public class Amount {

    private final BigDecimal value;

    public Amount(BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("Amount is required");
        }
        if (value.signum() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        this.value = value;
    }
}
