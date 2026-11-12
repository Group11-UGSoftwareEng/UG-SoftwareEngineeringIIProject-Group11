package ie.universityofgalway.group11.expensestracker.budgeting.domain.expense;

import java.util.List;

/**
 * Value object representing the currency code used for an expense.
 * Only a small set of explicitly supported codes is accepted by the domain model.
 */
public class Currency {
    private final String code;


    private Currency(String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Currency code is required");
        }
        List<String> acceptedCurrencies = List.of("USD", "EUR", "GBP");
        if (!acceptedCurrencies.contains(code)) {
            throw new IllegalArgumentException("Unsupported currency code");
        }

        this.code = code;
    }

    /**
     * Creates a validated currency code.
     *
     * @param code ISO-style currency code
     * @return a supported domain currency
     */
    public static Currency of(String code) {
        return new Currency(code);
    }

    /**
     * @return the uppercase currency code
     */
    public String code() {
        return code;
    }

    @Override
    public String toString() {
        return code();
    }

}
