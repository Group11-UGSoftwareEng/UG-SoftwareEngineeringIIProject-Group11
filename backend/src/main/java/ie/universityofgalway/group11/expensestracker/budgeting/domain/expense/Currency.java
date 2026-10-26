package ie.universityofgalway.group11.expensestracker.budgeting.domain.expense;

public class Currency {
    private String code;


    private Currency(String code) {
        this.code = code;
    }

    public static Currency of(String code) {
        return new Currency(code);
    }

    public String code() {
        return code;
    }

}
