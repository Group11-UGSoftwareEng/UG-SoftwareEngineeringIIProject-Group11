package ie.universityofgalway.group11.expensestracker.budgeting.domain.category;

import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;

import java.util.UUID;

public interface CategoryRepository {

    Expense add(Expense expense);
    Category findById(CategoryId categoryId);
}
