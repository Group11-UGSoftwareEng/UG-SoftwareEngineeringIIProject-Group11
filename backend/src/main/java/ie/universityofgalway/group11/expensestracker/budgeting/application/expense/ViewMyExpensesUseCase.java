package ie.universityofgalway.group11.expensestracker.budgeting.application.expense;

import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseRepository;
import ie.universityofgalway.group11.expensestracker.config.UseCase;
import ie.universityofgalway.group11.expensestracker.useradmission.domain.user.UserId;

import java.util.ArrayList;
import java.util.List;

public class ViewMyExpensesUseCase implements UseCase<UserId, List<Expense>> {

    private final ExpenseRepository expenseRepository;

    public ViewMyExpensesUseCase(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public List<Expense> execute(UserId userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID is required.");
        }

        // Get this user's expenses
        List<Expense> expenses =
                new ArrayList<>(expenseRepository.findByOwnerId(userId));

        // Show the newest expenses first
        expenses.sort((first, second) ->
                second.getDate().compareTo(first.getDate()));

        return expenses;
    }
}