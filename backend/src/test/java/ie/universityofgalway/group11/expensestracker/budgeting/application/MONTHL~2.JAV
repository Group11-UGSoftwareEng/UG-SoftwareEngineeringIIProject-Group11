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

class MonthlySpendingForecastServiceTest {
    private final InMemoryBudgetingRepository repo = new InMemoryBudgetingRepository();
    private final MonthlySpendingForecastService service = new MonthlySpendingForecastService(repo);

    private void add(String amount, String date, String currency) {
        Expense expense = Expense.create(CategoryId.random(), Amount.of(Double.parseDouble(amount)),
                Description.of("Test expense"), Currency.of(currency));
        expense.updateDate(LocalDate.parse(date));
        repo.save(expense);
    }

    @Test
    void estimatesCurrentAndNextMonthFromHistory() {
        add("100", "2026-07-15", "EUR");
        add("200", "2026-08-15", "EUR");
        add("300", "2026-09-15", "EUR");
        add("90", "2026-10-10", "EUR");
        add("900", "2026-10-28", "EUR"); // not yet recorded as of Oct 15
        add("10000", "2026-09-15", "USD"); // other currency is excluded

        var result = service.forecast(LocalDate.of(2026,10,15), 3, "EUR");
        assertEquals(new BigDecimal("200.00"), result.historicalMonthlyAverage());
        assertEquals(new BigDecimal("90.00"), result.currentMonthSpent());
        assertEquals(new BigDecimal("193.23"), result.projectedCurrentMonth());
        assertEquals(new BigDecimal("198.31"), result.projectedNextMonth());
        assertEquals(3, result.historicalMonths().size());
    }

    @Test
    void rejectsInvalidInputs() {
        assertThrows(IllegalArgumentException.class, () -> service.forecast(LocalDate.now(), 0, "EUR"));
        assertThrows(IllegalArgumentException.class, () -> service.forecast(LocalDate.now(), 3, "AUD"));
    }

    @Test
    void emptyHistoryDoesNotCrash() {
        var result = service.forecast(LocalDate.of(2026,10,15), 3, "EUR");
        assertEquals(new BigDecimal("0.00"), result.projectedNextMonth());
    }
}
*/
