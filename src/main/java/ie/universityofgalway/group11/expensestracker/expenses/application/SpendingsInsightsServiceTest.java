
package ie.universityofgalway.group11.expensestracker.expenses.application;

import ie.universityofgalway.group11.expensestracker.expenses.domain.expense.Amount;
import ie.universityofgalway.group11.expensestracker.expenses.domain.expense.Category;
import ie.universityofgalway.group11.expensestracker.expenses.infrastructure.InMemoryExpenseRepository;
import ie.universityofgalway.group11.expensestracker.useradmission.domain.user.UserId;

import java.math.BigDecimal;
import java.time.LocalDate;

class SpendingInsightsServiceTest {

    private static void assertEquals(int expected, int actual) {
        if (expected != actual) {
            throw new AssertionError("Expected " + expected + " but was " + actual);
        }
    }

    private static void assertEquals(Object expected, Object actual) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError("Expected " + expected + " but was " + actual);
        }
    }

    private static void assertNull(Object value) {
        if (value != null) {
            throw new AssertionError("Expected null but was " + value);
        }
    }

    private static void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("Expected condition to be true");
        }
    }

    private final InMemoryExpenseRepository repository =
        new InMemoryExpenseRepository();

    private final ExpenseService expenses =
        new ExpenseService(repository);

    private final SpendingInsightsService insights =
        new SpendingInsightsService(repository);

    private final UserId owner = UserId.random();

    private void add(
            String amount,
            String date,
            String category) {

        expenses.addExpense(
            owner,
            new Amount(new BigDecimal(amount)),
            LocalDate.parse(date),
            new Category(category)
        );
    }

    void comparisonIncludesCategoryTotalsAndUndefinedPercent() {
        add("20", "2026-09-03", "Food");
        add("30", "2026-10-03", "Food");
        add("10", "2026-10-04", "Transport");

        var result = insights.compare(
            owner,
            LocalDate.parse("2026-09-01"),
            LocalDate.parse("2026-09-30"),
            LocalDate.parse("2026-10-01"),
            LocalDate.parse("2026-10-31")
        );

        assertEquals(
            0,
            result.previousTotal()
                .compareTo(new BigDecimal("20"))
        );

        assertEquals(
            0,
            result.currentTotal()
                .compareTo(new BigDecimal("40"))
        );

        assertEquals(
            0,
            result.percentageChange()
                .compareTo(new BigDecimal("100"))
        );

        assertEquals(2, result.categories().size());

        assertNull(
            result.categories()
                .stream()
                .filter(c ->
                    c.category().equals("Transport"))
                .findFirst()
                .orElseThrow()
                .percentageChange()
        );
    }

    void anomalousCategoryDetectedWithoutZeroDivision() {
        add("10", "2026-09-02", "Food");
        add("10", "2026-09-09", "Food");
        add("25", "2026-09-17", "Food");
        add("4", "2026-09-17", "Transport");

        var result = insights.anomalies(
            owner,
            LocalDate.parse("2026-09-01"),
            LocalDate.parse("2026-09-14"),
            LocalDate.parse("2026-09-15"),
            LocalDate.parse("2026-09-21"),
            new BigDecimal("150")
        );

        assertEquals(1, result.warnings().size());

        assertEquals(
            "Food",
            result.warnings().getFirst().category()
        );

        assertEquals(
            1,
            result.insufficientDataCategories().size()
        );
    }

    void forecastUsesElapsedDaysAndFlagsBudget() {
        add("50", "2026-10-01", "Food");
        add("50", "2026-10-05", "Food");

        // Must not influence the earlier forecast.
        add("999", "2026-10-27", "Food");

        var result = insights.forecast(
            owner,
            LocalDate.parse("2026-10-01"),
            LocalDate.parse("2026-10-10"),
            LocalDate.parse("2026-10-05"),
            new BigDecimal("150")
        );

        assertEquals(
            0,
            result.projectedTotal()
                .compareTo(new BigDecimal("200"))
        );

        assertTrue(result.projectedOverBudget());
        assertEquals("ESTIMATE", result.status());
    }

    void emptyAndNotStartedPeriodsAreHandled() {
        var result = insights.forecast(
            owner,
            LocalDate.parse("2026-10-01"),
            LocalDate.parse("2026-10-31"),
            LocalDate.parse("2026-09-30"),
            null
        );

        assertTrue("NOT_STARTED".equals(result.status()));
        assertNull(result.projectedTotal());

        var empty = insights.compare(
            owner,
            LocalDate.parse("2026-08-01"),
            LocalDate.parse("2026-08-31"),
            LocalDate.parse("2026-09-01"),
            LocalDate.parse("2026-09-30")
        );

        assertNull(empty.percentageChange());
    }
}
