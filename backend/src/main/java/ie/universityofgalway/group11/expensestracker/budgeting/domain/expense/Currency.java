package ie.universityofgalway.group11.expensestracker.budgeting.domain.expense;

import java.util.List;

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

    public static Currency of(String code) {
        return new Currency(code);
    }

    public String code() {
        return code;
    }

    @Override
    public String toString() {
        return code();
    }

}
