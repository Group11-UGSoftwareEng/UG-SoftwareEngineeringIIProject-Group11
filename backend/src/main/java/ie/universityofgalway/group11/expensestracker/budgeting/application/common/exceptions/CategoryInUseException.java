package ie.universityofgalway.group11.expensestracker.budgeting.application.common.exceptions;

public class CategoryInUseException extends RuntimeException {

    private final long expenseCount;

    public CategoryInUseException(long expenseCount) {
        super("Category cannot be deleted because it contains "
                + expenseCount + " expenses.");
        this.expenseCount = expenseCount;
    }

    public long expenseCount() {
        return expenseCount;
    }
}