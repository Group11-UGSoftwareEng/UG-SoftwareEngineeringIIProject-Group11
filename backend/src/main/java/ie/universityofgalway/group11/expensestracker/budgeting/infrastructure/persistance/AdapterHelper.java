package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance;

// Expense should actually not be an aggregate root itself,
// and therefore not have its own repository.
// It is an aggregate root for now.

import java.time.YearMonth;

import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.Category;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryBudget;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryId;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryName;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Amount;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Currency;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Description;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseId;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.category.CategoryEntity;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.expense.ExpenseEntity;

/**
 * Shared conversion utility between domain aggregates and JPA entities.
 * It centralises the translation logic used by both category and expense persistence adapters.
 */
public class AdapterHelper {

    /**
     * Converts a domain expense into an entity linked to the given category.
     *
     * @param expense domain expense to persist
     * @param category owning category entity
     * @return JPA expense entity
     */
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

    /**
     * Rebuilds a domain expense from a persisted expense entity.
     *
     * @param entity JPA expense entity
     * @return equivalent domain object
     */
    public Expense toExpenseDomain(ExpenseEntity entity) {
        return Expense.createWithId(
                ExpenseId.of(entity.getId()),
                toCategoryId(entity.getCategory()),
                Amount.of(entity.getAmount()),
                entity.getDate(),
                Description.of(entity.getDescription()),
                Currency.of(entity.getCurrency())
        );
    }

    /**
     * Converts a domain category to a JPA entity.
     *
     * @param category domain category
     * @return persisted category entity
     */
    public CategoryEntity toCategoryEntity(Category category) {
        CategoryEntity entity = new CategoryEntity();
        entity.setId(category.id().asUuid());
        entity.setName(category.name().name());
        entity.setBudget(category.budget() == null ? null : category.budget().value().doubleValue());
        entity.setYear(category.year());
        entity.setMonth(category.month());

        return entity;
    }

    /**
     * Converts a persisted category entity back into the domain model.
     *
     * @param entity JPA category entity
     * @return equivalent category aggregate
     */
    public Category toCategoryDomain(CategoryEntity entity) {
        Category category = Category.createWithId(
                CategoryId.of(entity.getId()),
                CategoryName.of(entity.getName()),
                YearMonth.of(entity.getYear(), entity.getMonth()),
                entity.getBudget() == null ? null : CategoryBudget.of(entity.getBudget())
                );
        return category;
    }

    /**
     * Extracts the category identifier from a category entity.
     *
     * @param entity category entity
     * @return category identifier value object
     */
    public CategoryId toCategoryId(CategoryEntity entity) {
        return CategoryId.of(entity.getId());
    }
}
