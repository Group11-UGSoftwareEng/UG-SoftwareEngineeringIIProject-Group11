/**
package ie.universityofgalway.group11.expensestracker.budgeting.application;

import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryId;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Amount;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Currency;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Description;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.InMemoryBudgetingRepository;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BudgetCarryoverServiceTest {

    private final InMemoryBudgetingRepository repository =
            new InMemoryBudgetingRepository();

    private final BudgetCarryoverService service =
            new BudgetCarryoverService(repository);

    private final CategoryId food = CategoryId.random();
    private final CategoryId transport = CategoryId.random();

    private void addExpense(
            CategoryId category,
            double amount,
            String date,
            String currency
    ) {
        Expense expense = Expense.create(
                category,
                Amount.of(amount),
                Description.of("Test expense"),
                Currency.of(currency)
        );

        expense.updateDate(LocalDate.parse(date));
        repository.save(expense);
    }

    @Test
    void unusedBudgetCarriesToNextMonth() {

        service.configureBudget(
                food.asUuid(),
                "EUR",
                YearMonth.of(2026, 10),
                new BigDecimal("500.00")
        );

        addExpense(food, 350, "2026-10-10", "EUR");
        addExpense(food, 400, "2026-11-10", "EUR");

        var october = service.getBudgetReport(
                food.asUuid(),
                "EUR",
                YearMonth.of(2026, 10)
        ).period();

        assertEquals(
                new BigDecimal("150.00"),
                october.carriedToNextMonth()
        );

        var november = service.getBudgetReport(
                food.asUuid(),
                "EUR",
                YearMonth.of(2026, 11)
        ).period();

        assertEquals(
                new BigDecimal("150.00"),
                november.carriedOver()
        );

        assertEquals(
                new BigDecimal("650.00"),
                november.availableBudget()
        );

        assertEquals(
                new BigDecimal("250.00"),
                november.carriedToNextMonth()
        );
    }

    @Test
    void overspendingDoesNotCreateNegativeCarryover() {

        service.configureBudget(
                food.asUuid(),
                "EUR",
                YearMonth.of(2026, 10),
                new BigDecimal("500")
        );

        addExpense(food, 600, "2026-10-10", "EUR");

        var october = service.getBudgetReport(
                food.asUuid(),
                "EUR",
                YearMonth.of(2026, 10)
        ).period();

        assertTrue(october.overBudget());

        assertEquals(
                new BigDecimal("-100.00"),
                october.remainingBudget()
        );

        assertEquals(
                new BigDecimal("0.00"),
                october.carriedToNextMonth()
        );

        var november = service.getBudgetReport(
                food.asUuid(),
                "EUR",
                YearMonth.of(2026, 11)
        ).period();

        assertEquals(
                new BigDecimal("500.00"),
                november.availableBudget()
        );
    }

    @Test
    void ignoresUnrelatedCategoriesAndCurrencies() {

        service.configureBudget(
                food.asUuid(),
                "EUR",
                YearMonth.of(2026, 10),
                new BigDecimal("500")
        );

        addExpense(food, 100, "2026-10-10", "EUR");
        addExpense(food, 300, "2026-10-10", "USD");
        addExpense(transport, 250, "2026-10-10", "EUR");

        var report = service.getBudgetReport(
                food.asUuid(),
                "EUR",
                YearMonth.of(2026, 10)
        ).period();

        assertEquals(
                new BigDecimal("100.00"),
                report.totalSpent()
        );

        assertEquals(
                new BigDecimal("400.00"),
                report.carriedToNextMonth()
        );
    }

    @Test
    void recalculatesWhenPreviousExpensesChange() {

        service.configureBudget(
                food.asUuid(),
                "EUR",
                YearMonth.of(2026, 10),
                new BigDecimal("500")
        );

        addExpense(food, 100, "2026-10-10", "EUR");

        var original = service.getBudgetReport(
                food.asUuid(),
                "EUR",
                YearMonth.of(2026, 11)
        ).period();

        assertEquals(
                new BigDecimal("400.00"),
                original.carriedOver()
        );

        // New expense added to October.
        addExpense(food, 150, "2026-10-15", "EUR");

        var updated = service.getBudgetReport(
                food.asUuid(),
                "EUR",
                YearMonth.of(2026, 11)
        ).period();

        assertEquals(
                new BigDecimal("250.00"),
                updated.carriedOver()
        );
    }

    @Test
    void rejectsInvalidBudgetConfiguration() {

        assertThrows(
                IllegalArgumentException.class,
                () -> service.configureBudget(
                        food.asUuid(),
                        "EUR",
                        YearMonth.of(2026, 10),
                        new BigDecimal("-100")
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.getBudgetReport(
                        UUID.randomUUID(),
                        "EUR",
                        YearMonth.of(2026, 10)
                )
        );
    }
}
*/