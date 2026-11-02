
package budgeting.application;

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
import java.util.UUID;

import org.springframework.stereotype.Service;

import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.category.CategoryJPARepository;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.expense.ExpenseEntity;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.expense.ExpenseJPARepository;

@Service
public class SpendingSummaryService {

    private final ExpenseJPARepository expenseRepository;
    private final CategoryJPARepository categoryRepository;

    public SpendingSummaryService(
            ExpenseJPARepository expenseRepository,
            CategoryJPARepository categoryRepository
    ) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
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

    private static class Totals {
        BigDecimal sum = BigDecimal.ZERO;
        int count;

        void add(BigDecimal amount) {
            sum = sum.add(amount);
            count++;
        }
    }

    public SpendingSummary getCurrentMonthSummary(String currency) {

        String code = currency == null
                ? "EUR"
                : currency.trim().toUpperCase(Locale.ROOT);

        if (!List.of("EUR", "USD", "GBP").contains(code)) {
            throw new IllegalArgumentException(
                    "Currency must be EUR, USD or GBP"
            );
        }

        // Automatically determine the current month.
        LocalDate today = LocalDate.now();
        YearMonth month = YearMonth.from(today);

        LocalDate from = month.atDay(1);
        LocalDate to = today;

        // Retrieve category names from existing database records.
        Map<UUID, String> names = new HashMap<>();

        categoryRepository.findAll().forEach(category ->
                names.put(
                        category.getId(),
                        category.getName()
                )
        );

        Map<UUID, Totals> grouped = new HashMap<>();

        BigDecimal total = BigDecimal.ZERO;
        int count = 0;

        // Read existing saved expenses directly.
        // No changes to Expense.java are required.
        for (ExpenseEntity expense : expenseRepository.findAll()) {

            if (
                    expense.getDate() == null ||
                    expense.getDate().isBefore(from) ||
                    expense.getDate().isAfter(to) ||
                    !code.equalsIgnoreCase(expense.getCurrency()) ||
                    expense.getCategory() == null ||
                    expense.getCategory().getId() == null ||
                    expense.getAmount() == null
            ) {
                continue;
            }

            UUID categoryId = expense.getCategory().getId();

            BigDecimal amount = BigDecimal.valueOf(
                    expense.getAmount()
            );

            grouped.computeIfAbsent(
                    categoryId,
                    id -> new Totals()
            ).add(amount);

            total = total.add(amount);
            count++;
        }

        List<CategorySummary> categories = new ArrayList<>();

        for (var entry : grouped.entrySet()) {

            Totals categoryTotals = entry.getValue();

            BigDecimal share = total.signum() == 0
                    ? money(BigDecimal.ZERO)
                    : categoryTotals.sum
                            .multiply(BigDecimal.valueOf(100))
                            .divide(
                                    total,
                                    2,
                                    RoundingMode.HALF_UP
                            );

            String categoryName = names.getOrDefault(
                    entry.getKey(),
                    "Category " + entry.getKey()
                            .toString()
                            .substring(0, 8)
            );

            BigDecimal averageTransaction = categoryTotals.sum
                    .divide(
                            BigDecimal.valueOf(categoryTotals.count),
                            2,
                            RoundingMode.HALF_UP
                    );

            categories.add(
                    new CategorySummary(
                            entry.getKey().toString(),
                            categoryName,
                            money(categoryTotals.sum),
                            share,
                            categoryTotals.count,
                            averageTransaction
                    )
            );
        }

        // Show highest-spending categories first.
        categories.sort(
                Comparator.comparing(
                        CategorySummary::totalSpent
                ).reversed()
        );

        return new SpendingSummary(
                from,
                to,
                code,
                money(total),
                count,
                List.copyOf(categories)
        );
    }

    private static BigDecimal money(BigDecimal amount) {
        return amount.setScale(
                2,
                RoundingMode.HALF_UP
        );
    }
}
