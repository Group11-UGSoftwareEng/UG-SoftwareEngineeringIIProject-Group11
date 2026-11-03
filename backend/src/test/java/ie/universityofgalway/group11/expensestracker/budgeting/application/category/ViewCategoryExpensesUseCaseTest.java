package ie.universityofgalway.group11.expensestracker.budgeting.application.category;

import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.ViewCategoryExpensesCommand;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.ViewCategoryExpensesResponse;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.Category;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryId;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Amount;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Currency;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Description;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseId;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.InMemoryBudgetingRepository;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ViewCategoryExpensesUseCaseTest {
    @Test
    void returnsEmptyListWhenCategoryHasNoExpenses() {
        InMemoryBudgetingRepository repository =
                new InMemoryBudgetingRepository();

        CategoryId categoryId = CategoryId.of(UUID.fromString(
                "7f3a9c21-8d64-4e2b-a5f0-1c9e6b72d843"));

        ViewCategoryExpensesUseCase useCase =
                new ViewCategoryExpensesUseCase(repository, repository);

        ViewCategoryExpensesResponse response = useCase.execute(
                new ViewCategoryExpensesCommand(categoryId.asUuid()));

        assertTrue(response.expenses().isEmpty());
    }
    @Test
    void showsOnlyRequestedCategoryWithNewestFirst() {
        InMemoryBudgetingRepository repository =
                new InMemoryBudgetingRepository();

        CategoryId categoryId = CategoryId.of(UUID.fromString(
                "7f3a9c21-8d64-4e2b-a5f0-1c9e6b72d843"));

        CategoryId otherCategoryId = CategoryId.of(UUID.fromString(
                "3f6a1b2c-9d84-4e71-a6c3-5b8f02d9471a"));

        Category category = repository.findById(categoryId).orElseThrow();
        Category otherCategory =
                repository.findById(otherCategoryId).orElseThrow();

        Expense older = Expense.createWithId(
                ExpenseId.random(), categoryId, Amount.of(12.50),
                LocalDate.of(2026, 10, 10),
                Description.of("Groceries"), Currency.of("EUR"));

        Expense newer = Expense.createWithId(
                ExpenseId.random(), categoryId, Amount.of(5.00),
                LocalDate.of(2026, 10, 12),
                Description.of("Lunch"), Currency.of("EUR"));

        Expense other = Expense.createWithId(
                ExpenseId.random(), otherCategoryId, Amount.of(20.00),
                LocalDate.of(2026, 10, 13),
                Description.of("Bus ticket"), Currency.of("EUR"));

        repository.save(older, category);
        repository.save(newer, category);
        repository.save(other, otherCategory);

        ViewCategoryExpensesUseCase useCase =
                new ViewCategoryExpensesUseCase(repository, repository);

        ViewCategoryExpensesResponse response = useCase.execute(
                new ViewCategoryExpensesCommand(categoryId.asUuid()));

        assertEquals(2, response.expenses().size());
        assertEquals(newer.id().asUuid(), response.expenses().get(0).id());
        assertEquals(older.id().asUuid(), response.expenses().get(1).id());
        assertEquals(categoryId.asUuid(),
                response.expenses().get(0).categoryId());
        assertEquals(newer.date(), response.expenses().get(0).date());
        assertEquals(newer.amount().value(),
                response.expenses().get(0).amount());
        assertEquals("Lunch", response.expenses().get(0).description());
        assertEquals("EUR", response.expenses().get(0).currency());
    }
    @Test
    void rejectsCategoryThatDoesNotExist() {
        InMemoryBudgetingRepository repository =
                new InMemoryBudgetingRepository();

        ViewCategoryExpensesUseCase useCase =
                new ViewCategoryExpensesUseCase(repository, repository);

        ViewCategoryExpensesCommand command =
                new ViewCategoryExpensesCommand(
                        UUID.fromString(
                                "00000000-0000-0000-0000-000000000000"));

        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(command));

        assertEquals("Category not found.", error.getMessage());
    }
}