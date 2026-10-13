package ie.universityofgalway.group11.expensestracker.budgeting.application.expense;

import ie.universityofgalway.group11.expensestracker.budgeting.application.common.UseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseRepository;

import java.util.ArrayList;
import java.util.List;

public class ViewMyExpensesUseCase implements UseCase<String, List<Expense>> {

    private final ExpenseRepository expenseRepository;

    public ViewMyExpensesUseCase(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public List<Expense> execute(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User ID is required.");
        }

        // Get this user's expenses
        List<Expense> expenses =
                new ArrayList<>(expenseRepository.findByUserId(userId));

        // Show the newest expenses first
        expenses.sort((first, second) ->
                second.getDate().compareTo(first.getDate()));

        return expenses;
    }
}