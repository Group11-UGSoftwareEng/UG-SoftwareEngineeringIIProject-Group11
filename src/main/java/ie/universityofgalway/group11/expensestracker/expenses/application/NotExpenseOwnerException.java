package ie.universityofgalway.group11.expensestracker.expenses.application;

public class NotExpenseOwnerException extends RuntimeException {

    public NotExpenseOwnerException() {
        super("You can only edit or delete your own expenses");
    }
}
