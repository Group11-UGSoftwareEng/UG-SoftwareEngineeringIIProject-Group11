
package ie.universityofgalway.group11.expensestracker.budgeting.application;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Service;

import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseRepository;

@Service
public class SpendingSummaryService {

    private final ExpenseRepository expenseRepository;

    public SpendingSummaryService(
            ExpenseRepository expenseRepository
    ) {
        this.expenseRepository = expenseRepository;
    }

    // Result returned for each expense category.
    public record CategorySummary(
            String categoryId,
            BigDecimal totalSpent,
            BigDecimal percentageOfTotal,
            int transactionCount,
            BigDecimal averageTransaction
    ) {}

    // Complete spending summary response.
    public record SpendingSummary(
            LocalDate from,
            LocalDate to,
            String currency,
            BigDecimal totalSpent,
            int transactionCount,
            List<CategorySummary> categories
    ) {}

    // Internal accumulator for each category.
    private static class CategoryTotals {
        BigDecimal total = BigDecimal.ZERO;
        int count = 0;

        void add(BigDecimal amount) {
            total = total.add(amount);
            count++;
        }
    }

    public SpendingSummary getSummary(
            LocalDate from,
            LocalDate to,
            String currency
    ) {

        // Validate the selected date range.
        if (from == null || to == null || from.isAfter(to)) {
            throw new IllegalArgumentException(
                    "Please provide a valid date range"
            );
        }

        // Keep different currencies separate.
        if (currency == null ||
                !List.of("EUR", "USD", "GBP").contains(currency)) {
            throw new IllegalArgumentException(
                    "Currency must be EUR, USD or GBP"
            );
        }

        // Retrieve the recorded expenses.
        List<Expense> expenses = expenseRepository.findAll();

        Map<String, CategoryTotals> grouped =
                new TreeMap<>();

        BigDecimal totalSpent = BigDecimal.ZERO;
        int transactionCount = 0;

        // Filter and group the expenses.
        for (Expense expense : expenses) {

            LocalDate date = expense.date();

            if (date == null ||
                    date.isBefore(from) ||
                    date.isAfter(to)) {
                continue;
            }

            if (!currency.equals(expense.currency().code())) {
                continue;
            }

            BigDecimal amount = expense.amount().value();
            String categoryId = expense.categoryId().toString();

            CategoryTotals totals = grouped.computeIfAbsent(
                    categoryId,
                    key -> new CategoryTotals()
            );

            totals.add(amount);

            totalSpent = totalSpent.add(amount);
            transactionCount++;
        }

        // Calculate category-specific statistics.
        List<CategorySummary> categorySummaries =
                new ArrayList<>();

        for (Map.Entry<String, CategoryTotals> entry
                : grouped.entrySet()) {

            CategoryTotals totals = entry.getValue();

            BigDecimal percentage =
                    totalSpent.signum() == 0
                            ? BigDecimal.ZERO.setScale(2)
                            : totals.total
                                .multiply(BigDecimal.valueOf(100))
                                .divide(
                                    totalSpent,
                                    2,
                                    RoundingMode.HALF_UP
                                );

            BigDecimal average = totals.total.divide(
                    BigDecimal.valueOf(totals.count),
                    2,
                    RoundingMode.HALF_UP
            );

            categorySummaries.add(
                    new CategorySummary(
                            entry.getKey(),
                            money(totals.total),
                            percentage,
                            totals.count,
                            average
                    )
            );
        }

        // Largest spending categories appear first.
        categorySummaries.sort(
                (a, b) -> b.totalSpent()
                        .compareTo(a.totalSpent())
        );

        return new SpendingSummary(
                from,
                to,
                currency,
                money(totalSpent),
                transactionCount,
                List.copyOf(categorySummaries)
        );
    }

    private static BigDecimal money(BigDecimal amount) {
        return amount.setScale(
                2,
                RoundingMode.HALF_UP
        );
    }
}
