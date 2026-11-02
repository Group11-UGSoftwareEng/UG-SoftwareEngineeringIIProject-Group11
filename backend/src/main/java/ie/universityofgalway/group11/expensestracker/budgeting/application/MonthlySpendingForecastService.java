
package ie.universityofgalway.group11.expensestracker.budgeting.application;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.category.CategoryJPARepository;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.expense.ExpenseEntity;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.expense.ExpenseJPARepository;

@Service
public class MonthlySpendingForecastService {

    private final ExpenseJPARepository expenses;
    private final CategoryJPARepository categories;

    public MonthlySpendingForecastService(
            ExpenseJPARepository expenses,
            CategoryJPARepository categories
    ) {
        this.expenses = expenses;
        this.categories = categories;
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

    public ForecastResult forecast(LocalDate asOf, String currency) {
        if (asOf == null) {
            throw new IllegalArgumentException("Date is required");
        }

        String code = currency == null
                ? "EUR"
                : currency.trim().toUpperCase(Locale.ROOT);

        if (!List.of("EUR", "USD", "GBP").contains(code)) {
            throw new IllegalArgumentException(
                    "Currency must be EUR, USD or GBP"
            );
        }

        YearMonth currentMonth = YearMonth.from(asOf);
        int elapsedDays = asOf.getDayOfMonth();
        int totalDays = currentMonth.lengthOfMonth();

        Map<UUID, String> categoryNames = categories.findAll()
                .stream()
                .collect(Collectors.toMap(
                        category -> category.getId(),
                        category -> category.getName(),
                        (first, second) -> first
                ));

        // Use the original database expense date directly.
        // Expense.java and AdapterHelper.java are unchanged.
        Map<UUID, List<ExpenseEntity>> grouped = expenses.findAll()
                .stream()
                .filter(expense ->
                        expense.getDate() != null
                        && !expense.getDate().isAfter(asOf)
                )
                .filter(expense ->
                        YearMonth.from(expense.getDate())
                                .equals(currentMonth)
                )
                .filter(expense ->
                        code.equalsIgnoreCase(expense.getCurrency())
                )
                .filter(expense ->
                        expense.getCategory() != null
                        && expense.getCategory().getId() != null
                )
                .collect(Collectors.groupingBy(
                        expense -> expense.getCategory().getId()
                ));

        List<CategoryProjection> rows = new ArrayList<>();
        BigDecimal spentTotal = BigDecimal.ZERO;
        BigDecimal projectedTotal = BigDecimal.ZERO;
        int transactionCount = 0;

        for (var entry : grouped.entrySet()) {
            BigDecimal spent = entry.getValue()
                    .stream()
                    .map(expense ->
                            BigDecimal.valueOf(expense.getAmount())
                    )
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal projected = spent
                    .multiply(BigDecimal.valueOf(totalDays))
                    .divide(
                            BigDecimal.valueOf(elapsedDays),
                            2,
                            RoundingMode.HALF_UP
                    );

            UUID categoryId = entry.getKey();

            String categoryName = categoryNames.getOrDefault(
                    categoryId,
                    "Category " + categoryId.toString().substring(0, 8)
            );

            rows.add(new CategoryProjection(
                    categoryId,
                    categoryName,
                    money(spent),
                    money(projected),
                    entry.getValue().size()
            ));

            spentTotal = spentTotal.add(spent);
            projectedTotal = projectedTotal.add(projected);
            transactionCount += entry.getValue().size();
        }

        rows.sort(
                Comparator.comparing(
                        CategoryProjection::projectedMonthEnd
                ).reversed()
        );

        return new ForecastResult(
                currentMonth.toString(),
                asOf,
                code,
                elapsedDays,
                totalDays,
                money(spentTotal),
                money(projectedTotal),
                money(projectedTotal.subtract(spentTotal)),
                transactionCount,
                List.copyOf(rows),
                "Recorded current-month spending / days elapsed x days in month",
                transactionCount == 0
                        ? "No expenses have been recorded this month."
                        : "Projection based only on expenses recorded this month. Estimates can fluctuate early in the month."
        );
    }

    private static BigDecimal money(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }
}
