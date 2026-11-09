package ie.universityofgalway.group11.expensestracker.budgeting.application.expense;

import ie.universityofgalway.group11.expensestracker.budgeting.application.common.UseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.dto.ViewAllExpensesResponse;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseRepository;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.rest.expense.dto.ViewAllExpensesQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Loads all expenses and sorts them by date, newest first.
 */
public class ViewAllExpensesUseCase implements UseCase<ViewAllExpensesQuery, ViewAllExpensesResponse> {

    private final ExpenseRepository expenseRepository;

    /**
     * Creates the expense viewing use case.
     *
     * @param expenseRepository repository used to load expenses
     */
    public ViewAllExpensesUseCase(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    /**
     * Returns expenses ordered from newest to oldest.
     *
     * @param query request to view all expenses
     * @return response containing the sorted expenses, or an empty list
     */
    @Override
    public ViewAllExpensesResponse execute(ViewAllExpensesQuery query) {
        List<Expense> expenses =
                new ArrayList<>(expenseRepository.findAll());

        expenses.sort((first, second) ->
                second.date().compareTo(first.date()));

        return new ViewAllExpensesResponse(expenses);
    }
}