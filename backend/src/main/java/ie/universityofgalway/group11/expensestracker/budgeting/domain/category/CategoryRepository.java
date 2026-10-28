package ie.universityofgalway.group11.expensestracker.budgeting.domain.category;

import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;

import java.util.Optional;

public interface CategoryRepository {

    Expense add(Expense expense);

    Optional<Category> findById(CategoryId id);

    Category save(Category category);
}