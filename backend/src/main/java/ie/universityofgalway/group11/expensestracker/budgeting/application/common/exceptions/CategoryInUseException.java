package ie.universityofgalway.group11.expensestracker.budgeting.application.common.exceptions;

public class CategoryInUseException extends RuntimeException {
    private final long expenseCount;
    public CategoryInUseException(long expenseCount) {
        super("This category has " + expenseCount + " expense(s). Move or delete them before deleting the category.");
        this.expenseCount = expenseCount;
    }
    public long expenseCount() {
        return expenseCount;
    }
}