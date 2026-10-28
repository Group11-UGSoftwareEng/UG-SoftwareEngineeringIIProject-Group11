package ie.universityofgalway.group11.expensestracker.budgeting.domain.expense;

import java.math.BigDecimal;

public class Amount {

    private final BigDecimal value;

    private Amount(BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("Amount is required");
        }
        if (value.signum() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        this.value = value;
    }

    public static Amount of(Double value) {
        if (value == null) {
            throw new IllegalArgumentException("Amount is missing.");
        }
        return new Amount(BigDecimal.valueOf(value));
    }

    public BigDecimal value() {
        return value;
    }
}
