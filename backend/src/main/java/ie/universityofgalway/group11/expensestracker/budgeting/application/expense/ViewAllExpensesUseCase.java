package ie.universityofgalway.group11.expensestracker.budgeting.application.expense;

import ie.universityofgalway.group11.expensestracker.budgeting.application.common.UseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.dto.ViewAllExpensesResponse;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseRepository;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.rest.expense.dto.ViewAllExpensesQuery;

import java.util.ArrayList;
import java.util.List;

public class ViewAllExpensesUseCase implements UseCase<ViewAllExpensesQuery, ViewAllExpensesResponse> {

    private final ExpenseRepository expenseRepository;

    public ViewAllExpensesUseCase(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public ViewAllExpensesResponse execute(ViewAllExpensesQuery query) {
        List<Expense> expenses =
                new ArrayList<>(expenseRepository.findAll());

        expenses.sort((first, second) ->
                second.date().compareTo(first.date()));

        return new ViewAllExpensesResponse(expenses);
    }
}