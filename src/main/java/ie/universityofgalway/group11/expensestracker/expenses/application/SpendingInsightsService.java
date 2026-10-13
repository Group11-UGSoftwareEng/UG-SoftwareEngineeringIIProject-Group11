
package ie.universityofgalway.group11.expensestracker.expenses.application;

import ie.universityofgalway.group11.expensestracker.expenses.domain.expense.Expense;
import ie.universityofgalway.group11.expensestracker.expenses.domain.expense.ExpenseRepository;
import ie.universityofgalway.group11.expensestracker.useradmission.domain.user.UserId;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

@Service
public class SpendingInsightsService {

    private final ExpenseRepository repository;

    public SpendingInsightsService(ExpenseRepository repository) {
        this.repository = repository;
    }

    private static void validatePeriod(LocalDate from, LocalDate to) {
        if (from == null || to == null || from.isAfter(to)) {
            throw new IllegalArgumentException(
                "Period must have valid from/to dates"
            );
        }
    }

    private Map<String, BigDecimal> totals(
            UserId owner, LocalDate from, LocalDate to) {

        validatePeriod(from, to);
        Map<String, BigDecimal> totals = new TreeMap<>();

        for (Expense expense : repository.findAllByOwnerId(owner)) {
            if (!expense.getDate().isBefore(from)
                    && !expense.getDate().isAfter(to)) {

                totals.merge(
                    expense.getCategory().name(),
                    expense.getAmount().value(),
                    BigDecimal::add
                );
            }
        }
        return totals;
    }

    private static BigDecimal sum(Map<String, BigDecimal> totals) {
        return totals.values().stream()
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // UC01: SPENDING TRENDS

    public record CategoryComparison(
        String category,
        BigDecimal previous,
        BigDecimal current,
        BigDecimal difference,
        BigDecimal percentageChange,
        String trend
    ) {}

    public record SpendingComparison(
        LocalDate previousFrom,
        LocalDate previousTo,
        LocalDate currentFrom,
        LocalDate currentTo,
        BigDecimal previousTotal,
        BigDecimal currentTotal,
        BigDecimal difference,
        BigDecimal percentageChange,
        String trend,
        List<CategoryComparison> categories
    ) {}

    private static BigDecimal percentage(
            BigDecimal current, BigDecimal previous) {

        if (previous.signum() == 0) return null;

        return current.subtract(previous)
            .multiply(BigDecimal.valueOf(100))
            .divide(previous, 2, RoundingMode.HALF_UP);
    }

    private static String trend(BigDecimal difference) {
        int value = difference.signum();

        return value > 0 ? "INCREASED"
            : value < 0 ? "DECREASED"
            : "UNCHANGED";
    }

    public SpendingComparison compare(
            UserId owner,
            LocalDate previousFrom, LocalDate previousTo,
            LocalDate currentFrom, LocalDate currentTo) {

        Map<String, BigDecimal> previous =
            totals(owner, previousFrom, previousTo);

        Map<String, BigDecimal> current =
            totals(owner, currentFrom, currentTo);

        TreeSet<String> categories =
            new TreeSet<>(previous.keySet());

        categories.addAll(current.keySet());

        List<CategoryComparison> rows = new ArrayList<>();

        for (String category : categories) {
            BigDecimal before = previous.getOrDefault(
                category, BigDecimal.ZERO
            );

            BigDecimal after = current.getOrDefault(
                category, BigDecimal.ZERO
            );

            BigDecimal diff = after.subtract(before);

            rows.add(new CategoryComparison(
                category,
                before,
                after,
                diff,
                percentage(after, before),
                trend(diff)
            ));
        }

        BigDecimal before = sum(previous);
        BigDecimal after = sum(current);
        BigDecimal diff = after.subtract(before);

        return new SpendingComparison(
            previousFrom,
            previousTo,
            currentFrom,
            currentTo,
            before,
            after,
            diff,
            percentage(after, before),
            trend(diff),
            rows
        );
    }

    // UC02: UNUSUAL SPENDING DETECTION

    public record Anomaly(
        String category,
        BigDecimal historicalAverage,
        BigDecimal currentSpending,
        BigDecimal percentageOfAverage
    ) {}

    public record AnomalyReport(
        LocalDate historyFrom,
        LocalDate historyTo,
        LocalDate currentFrom,
        LocalDate currentTo,
        BigDecimal thresholdPercent,
        int historicalPeriods,
        List<Anomaly> warnings,
        List<String> insufficientDataCategories
    ) {}

    public AnomalyReport anomalies(
            UserId owner,
            LocalDate historyFrom, LocalDate historyTo,
            LocalDate currentFrom, LocalDate currentTo,
            BigDecimal thresholdPercent) {

        validatePeriod(historyFrom, historyTo);
        validatePeriod(currentFrom, currentTo);

        if (!historyTo.isBefore(currentFrom)) {
            throw new IllegalArgumentException(
                "History must finish before current period"
            );
        }

        if (thresholdPercent == null ||
            thresholdPercent.compareTo(
                BigDecimal.valueOf(100)) <= 0) {

            throw new IllegalArgumentException(
                "thresholdPercent must be greater than 100"
            );
        }

        long days = ChronoUnit.DAYS.between(
            currentFrom, currentTo
        ) + 1;

        long historyDays = ChronoUnit.DAYS.between(
            historyFrom, historyTo
        ) + 1;

        if (historyDays % days != 0 ||
            historyDays / days < 2) {

            throw new IllegalArgumentException(
                "History must cover at least two complete current-length periods"
            );
        }

        int periods = Math.toIntExact(historyDays / days);

        Map<String, BigDecimal> historic =
            totals(owner, historyFrom, historyTo);

        Map<String, BigDecimal> now =
            totals(owner, currentFrom, currentTo);

        TreeSet<String> categories =
            new TreeSet<>(historic.keySet());

        categories.addAll(now.keySet());

        List<Anomaly> warnings = new ArrayList<>();
        List<String> insufficient = new ArrayList<>();

        for (String category : categories) {
            BigDecimal past = historic.getOrDefault(
                category, BigDecimal.ZERO
            );

            BigDecimal recent = now.getOrDefault(
                category, BigDecimal.ZERO
            );

            if (past.signum() == 0) {
                insufficient.add(category);
                continue;
            }

            BigDecimal average = past.divide(
                BigDecimal.valueOf(periods),
                4,
                RoundingMode.HALF_UP
            );

            BigDecimal ratio = recent
                .multiply(BigDecimal.valueOf(100))
                .divide(
                    average,
                    2,
                    RoundingMode.HALF_UP
                );

            if (ratio.compareTo(thresholdPercent) > 0) {
                warnings.add(new Anomaly(
                    category,
                    average,
                    recent,
                    ratio
                ));
            }
        }

        return new AnomalyReport(
            historyFrom,
            historyTo,
            currentFrom,
            currentTo,
            thresholdPercent,
            periods,
            warnings,
            insufficient
        );
    }

    // UC03: PROJECTED SPENDING

    public record Forecast(
        LocalDate from,
        LocalDate to,
        LocalDate asOf,
        BigDecimal spentToDate,
        long elapsedDays,
        long totalDays,
        BigDecimal averageDailySpending,
        BigDecimal projectedTotal,
        BigDecimal budget,
        BigDecimal projectedRemaining,
        boolean projectedOverBudget,
        String status
    ) {}

    public Forecast forecast(
            UserId owner,
            LocalDate from,
            LocalDate to,
            LocalDate asOf,
            BigDecimal budget) {

        validatePeriod(from, to);

        if (asOf == null) {
            throw new IllegalArgumentException(
                "asOf is required"
            );
        }

        if (budget != null && budget.signum() < 0) {
            throw new IllegalArgumentException(
                "Budget cannot be negative"
            );
        }

        long totalDays = ChronoUnit.DAYS.between(
            from, to
        ) + 1;

        if (asOf.isBefore(from)) {
            return new Forecast(
                from, to, asOf,
                BigDecimal.ZERO,
                0,
                totalDays,
                null,
                null,
                budget,
                null,
                false,
                "NOT_STARTED"
            );
        }

        LocalDate effectiveEnd =
            asOf.isAfter(to) ? to : asOf;

        long elapsed = ChronoUnit.DAYS.between(
            from, effectiveEnd
        ) + 1;

        BigDecimal spent = sum(
            totals(owner, from, effectiveEnd)
        );

        BigDecimal daily = spent.divide(
            BigDecimal.valueOf(elapsed),
            2,
            RoundingMode.HALF_UP
        );

        boolean complete = !asOf.isBefore(to);

        BigDecimal projection = complete
            ? spent
            : spent.multiply(BigDecimal.valueOf(totalDays))
                .divide(
                    BigDecimal.valueOf(elapsed),
                    2,
                    RoundingMode.HALF_UP
                );

        BigDecimal remaining = budget == null
            ? null
            : budget.subtract(projection);

        return new Forecast(
            from,
            to,
            asOf,
            spent,
            elapsed,
            totalDays,
            daily,
            projection,
            budget,
            remaining,
            budget != null &&
                projection.compareTo(budget) > 0,
            complete ? "COMPLETE" : "ESTIMATE"
        );
    }
}
