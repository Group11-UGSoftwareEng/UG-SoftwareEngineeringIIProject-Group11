package ie.universityofgalway.group11.expensestracker.budgeting.application.common.exceptions;

public class NotExpenseOwnerException extends RuntimeException {

    public NotExpenseOwnerException() {
        super("You can only edit or delete your own expenses");
    }
}
