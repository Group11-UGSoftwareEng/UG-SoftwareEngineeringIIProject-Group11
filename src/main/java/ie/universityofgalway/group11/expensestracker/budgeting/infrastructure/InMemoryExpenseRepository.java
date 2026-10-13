package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure;

import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseRepository;

import java.util.ArrayList;
import java.util.List;

public class InMemoryExpenseRepository implements ExpenseRepository {

    private final List<Expense> expenses;

    public InMemoryExpenseRepository(List<Expense> expenses) {
        this.expenses = new ArrayList<>(expenses);
    }

    @Override
    public List<Expense> findByUserId(String userId) {
        List<Expense> userExpenses = new ArrayList<>();

        for (Expense expense : expenses) {
            if (expense.getUserId().equals(userId)) {
                userExpenses.add(expense);
            }
        }

        return userExpenses;
    }
}