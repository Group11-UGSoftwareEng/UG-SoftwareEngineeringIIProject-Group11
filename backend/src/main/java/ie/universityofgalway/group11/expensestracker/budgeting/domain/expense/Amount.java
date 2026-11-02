package ie.universityofgalway.group11.expensestracker.budgeting.domain.expense;

import ie.universityofgalway.group11.expensestracker.budgeting.application.common.exceptions.AmountException;

import java.math.BigDecimal;

public class Amount {

    private final BigDecimal value;

    private Amount(BigDecimal value) {
        if (value == null) {
            throw new AmountException("Amount is required");
        }
        if (value.signum() <= 0) {
            throw new AmountException("Amount must be greater than 0");
        }
        this.value = value;
    }

    public static Amount of(Double value) {
        if (value == null) {
            throw new AmountException("Amount is missing or entered incorrectly.");
        }
        return new Amount(BigDecimal.valueOf(value));
    }

    public BigDecimal value() {
        return value;
    }

    @Override
    public String toString() {
        return "" + value();
    }
}
