package ie.universityofgalway.group11.expensestracker.expenses.application;

import ie.universityofgalway.group11.expensestracker.expenses.domain.expense.ExpenseId;

public class ExpenseNotFoundException extends RuntimeException {

    public ExpenseNotFoundException(ExpenseId id) {
        super("Expense not found: " + id.value());
    }
}
