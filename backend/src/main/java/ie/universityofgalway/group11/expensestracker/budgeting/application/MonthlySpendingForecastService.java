
package ie.universityofgalway.group11.expensestracker.budgeting.application;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseRepository;

@Service
public class MonthlySpendingForecastService {

    private static final int SCALE = 2;
    private final ExpenseRepository repository;

    public MonthlySpendingForecastService(ExpenseRepository repository) {
        this.repository = repository;
    }

    public record MonthlyTotal(
            YearMonth month,
            BigDecimal total
    ) {}

    public record ForecastResult(
            String currency,
            LocalDate asOf,
            int requestedHistoryMonths,
            List<MonthlyTotal> historicalMonths,
            BigDecimal historicalMonthlyAverage,
            BigDecimal currentMonthSpent,
            BigDecimal currentMonthRunRate,
            BigDecimal projectedCurrentMonth,
            BigDecimal projectedNextMonth,
            String method,
            String note
    ) {}

    public ForecastResult forecast(
            LocalDate asOf,
            int historyMonths,
            String currency
    ) {
        Objects.requireNonNull(asOf, "asOf is required");

        if (historyMonths < 1 || historyMonths > 24) {
            throw new IllegalArgumentException(
                    "historyMonths must be between 1 and 24"
            );
        }

        if (currency == null ||
                !List.of("EUR", "USD", "GBP").contains(currency)) {
            throw new IllegalArgumentException(
                    "currency must be EUR, USD or GBP"
            );
        }

        YearMonth currentMonth = YearMonth.from(asOf);

        List<Expense> expenses = repository.findAll().stream()
                .filter(e -> e.date() != null)
                .filter(e -> !e.date().isAfter(asOf))
                .filter(e -> currency.equals(e.currency().code()))
                .toList();

        List<MonthlyTotal> previous = new ArrayList<>();
        BigDecimal historyTotal = BigDecimal.ZERO;

        for (int i = historyMonths; i >= 1; i--) {
            YearMonth month = currentMonth.minusMonths(i);

            BigDecimal monthly = totalForMonth(expenses, month);

            previous.add(new MonthlyTotal(
                    month,
                    money(monthly)
            ));

            historyTotal = historyTotal.add(monthly);
        }

        BigDecimal historicalAverage = historyTotal.divide(
                BigDecimal.valueOf(historyMonths),
                SCALE,
                RoundingMode.HALF_UP
        );

        BigDecimal spent = totalForMonth(expenses, currentMonth);

        int elapsed = asOf.getDayOfMonth();
        int days = currentMonth.lengthOfMonth();
        int remaining = days - elapsed;

        BigDecimal runRate = spent
                .multiply(BigDecimal.valueOf(days))
                .divide(
                        BigDecimal.valueOf(elapsed),
                        SCALE,
                        RoundingMode.HALF_UP
                );

        BigDecimal projectedCurrent = spent.add(
                historicalAverage
                        .multiply(BigDecimal.valueOf(remaining))
                        .divide(
                                BigDecimal.valueOf(days),
                                SCALE,
                                RoundingMode.HALF_UP
                        )
        );

        BigDecimal projectedNext = historyTotal
                .add(projectedCurrent)
                .divide(
                        BigDecimal.valueOf(historyMonths + 1L),
                        SCALE,
                        RoundingMode.HALF_UP
                );

        String note = historyTotal.signum() == 0
                ? "No spending recorded in the historical months; estimates have limited evidence."
                : "Forecast based on recorded spending; one-off purchases and seasonality may change actual totals.";

        return new ForecastResult(
                currency,
                asOf,
                historyMonths,
                List.copyOf(previous),
                money(historicalAverage),
                money(spent),
                money(runRate),
                money(projectedCurrent),
                money(projectedNext),
                "Current month = spent to date + historical monthly average times remaining fraction of month; "
                        + "next month = average of historical monthly totals and projected current month.",
                note
        );
    }

    private static BigDecimal totalForMonth(
            List<Expense> expenses,
            YearMonth month
    ) {
        return expenses.stream()
                .filter(e -> YearMonth.from(e.date()).equals(month))
                .map(e -> e.amount().value())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static BigDecimal money(BigDecimal value) {
        return value.setScale(SCALE, RoundingMode.HALF_UP);
    }
}
