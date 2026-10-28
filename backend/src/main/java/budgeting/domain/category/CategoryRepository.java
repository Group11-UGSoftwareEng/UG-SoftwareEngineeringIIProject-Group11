package ie.universityofgalway.group11.expensestracker.budgeting.domain.category;

import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;

public interface CategoryRepository {

    Expense add(Expense expense);
}
