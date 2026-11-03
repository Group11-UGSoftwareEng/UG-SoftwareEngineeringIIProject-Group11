
package ie.universityofgalway.group11.expensestracker.budgeting.application;

import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.expense.ExpenseEntity;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.expense.SpendingSummaryQueryRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

@Service
public class SpendingSummaryService {

    private final SpendingSummaryQueryRepository repository;

    public SpendingSummaryService(
            SpendingSummaryQueryRepository repository) {
        this.repository = repository;
    }

    public record CategorySummary(
            String categoryId,
            String categoryName,
            BigDecimal totalSpent,
            BigDecimal percentageOfTotal,
            int transactionCount,
            BigDecimal averageTransaction
    ) {}

    public record SpendingSummary(
            LocalDate from,
            LocalDate to,
            String currency,
            BigDecimal totalSpent,
            int transactionCount,
            List<CategorySummary> categories
    ) {}

    private static class CategoryTotals {
        String name;
        BigDecimal total = BigDecimal.ZERO;
        int count = 0;

        CategoryTotals(String name) {
            this.name = name;
        }

        void add(BigDecimal amount) {
            total = total.add(amount);
            count++;
        }
    }

    @Transactional(readOnly = true)
    public SpendingSummary summarize(
            LocalDate from,
            LocalDate to,
            String currency) {

        LocalDate today = LocalDate.now();

        if (from == null) {
            from = YearMonth.from(today).atDay(1);
        }

        if (to == null) {
            to = today;
        }

        if (from.isAfter(to)) {
            throw new IllegalArgumentException(
                    "Start date cannot be after end date"
            );
        }

        String selectedCurrency = currency == null
                ? "EUR"
                : currency.trim().toUpperCase(Locale.ROOT);

        if (!Set.of("EUR", "USD", "GBP")
                .contains(selectedCurrency)) {
            throw new IllegalArgumentException(
                    "Currency must be EUR, USD or GBP"
            );
        }

        List<ExpenseEntity> expenses =
                repository.findForSummary(
                        from, to, selectedCurrency
                );

        Map<UUID, CategoryTotals> grouped =
                new HashMap<>();

        BigDecimal overallTotal = BigDecimal.ZERO;
        int overallCount = 0;

        for (ExpenseEntity expense : expenses) {

            if (expense.getAmount() == null
                    || expense.getCategory() == null
                    || expense.getCategory().getId() == null) {
                continue;
            }

            UUID categoryId =
                    expense.getCategory().getId();

            String categoryName =
                    expense.getCategory().getName();

            if (categoryName == null
                    || categoryName.isBlank()) {
                categoryName = "Unlabelled category";
            }

            BigDecimal amount =
                    BigDecimal.valueOf(expense.getAmount());

            String finalName = categoryName;

            CategoryTotals totals =
                    grouped.computeIfAbsent(
                            categoryId,
                            id -> new CategoryTotals(finalName)
                    );

            totals.add(amount);

            overallTotal = overallTotal.add(amount);
            overallCount++;
        }

        List<CategorySummary> categorySummaries =
                new ArrayList<>();

        for (Map.Entry<UUID, CategoryTotals> entry :
                grouped.entrySet()) {

            CategoryTotals totals = entry.getValue();

            BigDecimal percentage =
                    overallTotal.signum() == 0
                            ? BigDecimal.ZERO
                            : totals.total
                                .multiply(BigDecimal.valueOf(100))
                                .divide(
                                        overallTotal,
                                        2,
                                        RoundingMode.HALF_UP
                                );

            BigDecimal average =
                    totals.total.divide(
                            BigDecimal.valueOf(totals.count),
                            2,
                            RoundingMode.HALF_UP
                    );

            categorySummaries.add(
                    new CategorySummary(
                            entry.getKey().toString(),
                            totals.name,
                            money(totals.total),
                            percentage,
                            totals.count,
                            average
                    )
            );
        }

        categorySummaries.sort(
                Comparator.comparing(
                        CategorySummary::totalSpent
                ).reversed().thenComparing(
                        CategorySummary::categoryName
                )
        );

        return new SpendingSummary(
                from,
                to,
                selectedCurrency,
                money(overallTotal),
                overallCount,
                List.copyOf(categorySummaries)
        );
    }

    private static BigDecimal money(BigDecimal amount) {
        return amount.setScale(
                2, RoundingMode.HALF_UP
        );
    }
}
