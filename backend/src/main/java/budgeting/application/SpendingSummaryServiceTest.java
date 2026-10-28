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

import static org.junit.jupiter.api.Assertions.*;

class SpendingSummaryServiceTest {

    private final InMemoryBudgetingRepository repository =
            new InMemoryBudgetingRepository();

    private final SpendingSummaryService service =
            new SpendingSummaryService(repository);

    private final CategoryId food = CategoryId.random();
    private final CategoryId transport = CategoryId.random();
    private final CategoryId shopping = CategoryId.random();

    private void add(
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
    void calculatesCategoryTotalsAndPercentages() {

        add(food, 100, "2026-10-01", "EUR");
        add(food, 50, "2026-10-02", "EUR");
        add(food, 75, "2026-10-03", "EUR");

        add(transport, 40, "2026-10-04", "EUR");
        add(transport, 60, "2026-10-05", "EUR");

        add(shopping, 125, "2026-10-06", "EUR");
        add(shopping, 50, "2026-10-07", "EUR");

        var result = service.getSummary(
                LocalDate.of(2026, 10, 1),
                LocalDate.of(2026, 10, 31),
                "EUR"
        );

        assertEquals(
                new BigDecimal("500.00"),
                result.totalSpent()
        );

        assertEquals(7, result.transactionCount());
        assertEquals(3, result.categories().size());

        var foodResult = result.categories().stream()
                .filter(c -> c.categoryId()
                        .equals(food.toString()))
                .findFirst()
                .orElseThrow();

        assertEquals(
                new BigDecimal("225.00"),
                foodResult.totalSpent()
        );

        assertEquals(
                new BigDecimal("45.00"),
                foodResult.percentageOfTotal()
        );

        assertEquals(3, foodResult.transactionCount());

        assertEquals(
                new BigDecimal("75.00"),
                foodResult.averageTransaction()
        );
    }

    @Test
    void excludesOtherCurrenciesAndDates() {

        add(food, 100, "2026-10-10", "EUR");
        add(food, 200, "2026-10-10", "USD");
        add(food, 300, "2026-09-10", "EUR");

        var result = service.getSummary(
                LocalDate.of(2026, 10, 1),
                LocalDate.of(2026, 10, 31),
                "EUR"
        );

        assertEquals(
                new BigDecimal("100.00"),
                result.totalSpent()
        );

        assertEquals(1, result.transactionCount());
    }

    @Test
    void handlesNoExpenses() {

        var result = service.getSummary(
                LocalDate.of(2026, 10, 1),
                LocalDate.of(2026, 10, 31),
                "EUR"
        );

        assertEquals(
                new BigDecimal("0.00"),
                result.totalSpent()
        );

        assertEquals(0, result.transactionCount());
        assertTrue(result.categories().isEmpty());
    }

    @Test
    void rejectsInvalidDatesAndCurrency() {

        assertThrows(
                IllegalArgumentException.class,
                () -> service.getSummary(
                        LocalDate.of(2026, 10, 31),
                        LocalDate.of(2026, 10, 1),
                        "EUR"
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.getSummary(
                        LocalDate.of(2026, 10, 1),
                        LocalDate.of(2026, 10, 31),
                        "AUD"
                )
        );
    }
}
*/