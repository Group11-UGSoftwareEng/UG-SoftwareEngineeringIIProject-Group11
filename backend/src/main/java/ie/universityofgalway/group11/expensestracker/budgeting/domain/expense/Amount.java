package ie.universityofgalway.group11.expensestracker.budgeting.domain.expense;

import ie.universityofgalway.group11.expensestracker.budgeting.application.common.exceptions.AmountException;

import java.math.BigDecimal;

/**
 * Value object representing a positive monetary amount.
 * This keeps all expense amounts validated before they can be stored or compared.
 */
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

    /**
     * Creates a validated amount from a numeric value.
     *
     * @param value monetary amount as a decimal number
     * @return a positive domain amount
     */
    public static Amount of(Double value) {
        if (value == null) {
            throw new AmountException("Amount is missing or entered incorrectly.");
        }
        return new Amount(BigDecimal.valueOf(value));
    }

    /**
     * @return the exact monetary value as a BigDecimal
     */
    public BigDecimal value() {
        return value;
    }

    @Override
    public String toString() {
        return "" + value();
    }
}
