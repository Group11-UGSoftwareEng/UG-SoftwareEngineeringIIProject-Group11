package ie.universityofgalway.group11.expensestracker.budgeting.application.category;

import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.InMemoryBudgetingRepository;
import org.junit.jupiter.api.Test;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.Category;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryName;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryBudget;
import java.util.List;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Amount;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Currency;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Description;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ResetBudgetingUseCaseTest {
    @Test
    void copiesCategoryToNewMonthAndKeepsBudget() {
        InMemoryBudgetingRepository repository =
                new InMemoryBudgetingRepository();

        Category previous = Category.create(
                CategoryName.of("Food"),
                YearMonth.of(2026, 10),
                CategoryBudget.of(200.0));

        repository.saveAll(List.of(previous));

        ResetBudgetingUseCase useCase =
                new ResetBudgetingUseCase(repository);

        List<Category> result =
                useCase.execute(YearMonth.of(2026, 11));

        assertEquals(1, result.size());

        Category current = result.get(0);

        assertEquals("Food", current.name().name());
        assertEquals(previous.budget().value(), current.budget().value());
        assertNotEquals(previous.id(), current.id());
        assertEquals(2026, current.year());
        assertEquals(11, current.month());

        assertEquals(1,
                repository.findByPeriod(YearMonth.of(2026, 11)).size());
        assertEquals(previous.id(),
                repository.findByPeriod(YearMonth.of(2026, 10)).get(0).id());
    }
    @Test
    void keepsCurrentCategoriesWithoutCreatingDuplicates() {
        InMemoryBudgetingRepository repository =
                new InMemoryBudgetingRepository();

        Category previous = Category.create(
                CategoryName.of("Transport"),
                YearMonth.of(2026, 10),
                CategoryBudget.of(100.0));

        Category current = Category.create(
                CategoryName.of("Food"),
                YearMonth.of(2026, 11),
                CategoryBudget.of(200.0));

        repository.saveAll(List.of(previous, current));

        ResetBudgetingUseCase useCase =
                new ResetBudgetingUseCase(repository);

        useCase.execute(YearMonth.of(2026, 11));
        List<Category> result =
                useCase.execute(YearMonth.of(2026, 11));

        assertEquals(1, result.size());
        assertEquals(current.id(), result.get(0).id());
        assertEquals("Food", result.get(0).name().name());
        assertEquals(1,
                repository.findByPeriod(YearMonth.of(2026, 11)).size());
    }
    @Test
    void returnsEmptyListWhenNoCategoriesExist() {
        InMemoryBudgetingRepository repository =
                new InMemoryBudgetingRepository();

        ResetBudgetingUseCase useCase =
                new ResetBudgetingUseCase(repository);

        assertTrue(
                useCase.execute(YearMonth.of(2026, 11)).isEmpty());
    }
    @Test
    void keepsOldExpensesOutOfNewMonth() {
        InMemoryBudgetingRepository repository =
                new InMemoryBudgetingRepository();

        Category previous = Category.create(
                CategoryName.of("Food"),
                YearMonth.of(2026, 10),
                CategoryBudget.of(200.0));

        Expense expense = Expense.create(
                previous.id(),
                Amount.of(25.0),
                Description.of("Groceries"),
                Currency.of("EUR"));

        previous.addExpenseToTheCategory(expense);
        repository.saveAll(List.of(previous));

        ResetBudgetingUseCase useCase =
                new ResetBudgetingUseCase(repository);

        Category current =
                useCase.execute(YearMonth.of(2026, 11)).get(0);

        assertTrue(current.expenses().isEmpty());

        Category savedPrevious = repository
                .findByPeriod(YearMonth.of(2026, 10)).get(0);

        assertEquals(1, savedPrevious.expenses().size());
        assertTrue(savedPrevious.expenses().contains(expense));
        assertEquals(previous.id(), expense.categoryId());
    }
    @Test
    void copiesOnlyLatestMonthAcrossYears() {
        InMemoryBudgetingRepository repository =
                new InMemoryBudgetingRepository();

        Category older = Category.create(
                CategoryName.of("Old category"),
                YearMonth.of(2026, 11),
                CategoryBudget.of(50.0));

        Category latest = Category.create(
                CategoryName.of("Food"),
                YearMonth.of(2026, 12),
                CategoryBudget.of(200.0));

        repository.saveAll(List.of(older, latest));

        ResetBudgetingUseCase useCase =
                new ResetBudgetingUseCase(repository);

        List<Category> result =
                useCase.execute(YearMonth.of(2027, 1));

        assertEquals(1, result.size());

        Category current = result.get(0);

        assertEquals("Food", current.name().name());
        assertEquals(latest.budget().value(), current.budget().value());
        assertNotEquals(latest.id(), current.id());
        assertEquals(2027, current.year());
        assertEquals(1, current.month());
    }
}