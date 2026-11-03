
package ie.universityofgalway.group11.expensestracker.budgeting.application;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.expense.ExpenseEntity;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.expense.MonthlyProjectionQueryRepository;

@Service
public class MonthlySpendingProjectionService {

    private final MonthlyProjectionQueryRepository repository;

    public MonthlySpendingProjectionService(
            MonthlyProjectionQueryRepository repository) {
        this.repository = repository;
    }

    public record CategoryProjection(
            UUID categoryId,
            String categoryName,
            BigDecimal spent,
            BigDecimal projectedMonthEnd,
            int transactionCount
    ) {}

    public record ForecastResult(
            String month,
            LocalDate asOf,
            String currency,
            int elapsedDays,
            int totalDays,
            BigDecimal currentMonthSpent,
            BigDecimal projectedCurrentMonth,
            BigDecimal estimatedRemaining,
            int transactionCount,
            List<CategoryProjection> categories,
            String method,
            String note
    ) {}

    private static class Totals {
        final String name;
        BigDecimal spent = BigDecimal.ZERO;
        int count;

        Totals(String name) {
            this.name = name;
        }
    }

    @Transactional(readOnly = true)
    public ForecastResult forecast(
            LocalDate asOf,
            String currency) {

        LocalDate today = LocalDate.now();

        if (asOf == null) {
            asOf = today;
        }

        if (!asOf.equals(today)) {
            throw new IllegalArgumentException(
                    "Forecast is only available for the current date"
            );
        }

        String code = currency == null
                ? "EUR"
                : currency.trim().toUpperCase(Locale.ROOT);

        if (!Set.of("EUR", "USD", "GBP").contains(code)) {
            throw new IllegalArgumentException(
                    "Currency must be EUR, USD or GBP"
            );
        }

        YearMonth month = YearMonth.from(asOf);

        int elapsedDays = asOf.getDayOfMonth();
        int totalDays = month.lengthOfMonth();

        Map<UUID, Totals> totalsByCategory = new HashMap<>();

        List<ExpenseEntity> expenses =
                repository.findMonthToDate(
                        month.atDay(1),
                        asOf,
                        code
                );

        for (ExpenseEntity expense : expenses) {

            if (expense.getCategory() == null
                    || expense.getCategory().getId() == null
                    || expense.getAmount() == null) {
                continue;
            }

            UUID categoryId = expense.getCategory().getId();

            String categoryName =
                    expense.getCategory().getName();

            if (categoryName == null || categoryName.isBlank()) {
                categoryName = "Unlabelled category";
            }

            String finalName = categoryName;

            Totals totals = totalsByCategory.computeIfAbsent(
                    categoryId,
                    ignored -> new Totals(finalName)
            );

            totals.spent = totals.spent.add(
                    BigDecimal.valueOf(expense.getAmount())
            );

            totals.count++;
        }

        List<CategoryProjection> rows = new ArrayList<>();

        BigDecimal totalSpent = BigDecimal.ZERO;
        int transactionCount = 0;

        for (Map.Entry<UUID, Totals> entry :
                totalsByCategory.entrySet()) {

            Totals totals = entry.getValue();

            BigDecimal projected = project(
                    totals.spent,
                    elapsedDays,
                    totalDays
            );

            rows.add(
                    new CategoryProjection(
                            entry.getKey(),
                            totals.name,
                            money(totals.spent),
                            projected,
                            totals.count
                    )
            );

            totalSpent = totalSpent.add(totals.spent);
            transactionCount += totals.count;
        }

        rows.sort(
                Comparator.comparing(
                        CategoryProjection::projectedMonthEnd
                ).reversed().thenComparing(
                        CategoryProjection::categoryName
                )
        );

        BigDecimal projectedTotal = project(
                totalSpent,
                elapsedDays,
                totalDays
        );

        BigDecimal estimatedRemaining =
                projectedTotal.subtract(totalSpent);

        String note = transactionCount == 0
                ? "No expenses recorded in the current month for this currency."
                : "Linear daily-average estimate, not a guarantee. "
                    + "Forecasts can fluctuate early in the month.";

        return new ForecastResult(
                month.toString(),
                asOf,
                code,
                elapsedDays,
                totalDays,
                money(totalSpent),
                projectedTotal,
                money(estimatedRemaining),
                transactionCount,
                List.copyOf(rows),
                "Month-to-date spending / elapsed calendar days "
                    + "x total calendar days",
                note
        );
    }

    private static BigDecimal project(
            BigDecimal spent,
            int elapsed,
            int total) {

        return spent.multiply(BigDecimal.valueOf(total))
                .divide(
                        BigDecimal.valueOf(elapsed),
                        2,
                        RoundingMode.HALF_UP
                );
    }

    private static BigDecimal money(BigDecimal value) {
        return value.setScale(
                2,
                RoundingMode.HALF_UP
        );
    }
}
