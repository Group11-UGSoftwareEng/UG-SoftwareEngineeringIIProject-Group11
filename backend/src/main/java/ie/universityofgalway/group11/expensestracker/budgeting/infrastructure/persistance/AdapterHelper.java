package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance;

// Expense should actually not be an aggregate root itself,
// and therefore not have its own repository.
// It is an aggregate root for now.

import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.Category;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryBudget;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryId;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryName;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.*;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.category.CategoryEntity;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.expense.ExpenseEntity;

import java.time.YearMonth;

public class AdapterHelper {

    public ExpenseEntity toExpenseEntity(Expense expense, CategoryEntity category) {
        ExpenseEntity entity = new ExpenseEntity();
        entity.setId(expense.id().asUuid());
        entity.setAmount(expense.amount().value().doubleValue());
        entity.setCategory(category);
        entity.setCurrency(expense.currency().code());
        entity.setDate(expense.date());
        entity.setDescription(expense.description().toString());
        return entity;
    }

    public Expense toExpenseDomain(ExpenseEntity entity) {
        Expense expense = Expense.createWithId(
                ExpenseId.of(entity.getId()),
                toCategoryId(entity.getCategory()),
                Amount.of(entity.getAmount()),
                Description.of(entity.getDescription()),
                Currency.of(entity.getCurrency())
                );
        return expense;
    }

    public CategoryEntity toCategoryEntity(Category category) {
        CategoryEntity entity = new CategoryEntity();
        entity.setId(category.id().asUuid());
        entity.setName(category.name().name());
        entity.setBudget(category.budget().value().doubleValue());

        return entity;
    }

    public Category toCategoryDomain(CategoryEntity entity) {
        Category category = Category.createWithId(
                CategoryId.of(entity.getId()),
                CategoryName.of(entity.getName()),
                YearMonth.of(entity.getYear(), entity.getMonth()),
                CategoryBudget.of(entity.getBudget())
                );
        return category;
    }

    public CategoryId toCategoryId(CategoryEntity entity) {
        return CategoryId.of(entity.getId());
    }
}
