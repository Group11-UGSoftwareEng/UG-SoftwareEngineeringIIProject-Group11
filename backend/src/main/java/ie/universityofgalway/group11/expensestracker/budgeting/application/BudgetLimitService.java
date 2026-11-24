
package ie.universityofgalway.group11.expensestracker.budgeting.application;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.budget.BudgetExpenseQueryRepository;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.budget.BudgetLimitEntity;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.budget.BudgetLimitRepository;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.category.CategoryEntity;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.category.CategoryJPARepository;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.expense.ExpenseEntity;

@Service
public class BudgetLimitService {

    private final BudgetLimitRepository budgetRepository;
    private final BudgetExpenseQueryRepository expenseRepository;
    private final CategoryJPARepository categoryRepository;

    public BudgetLimitService(
            BudgetLimitRepository budgetRepository,
            BudgetExpenseQueryRepository expenseRepository,
            CategoryJPARepository categoryRepository) {
        this.budgetRepository = budgetRepository;
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
    }

    public record BudgetStatus(
            BigDecimal limit,
            BigDecimal spent,
            BigDecimal remaining,
            BigDecimal percentageUsed,
            String status
    ) {}

    public record CategoryBudgetStatus(
            UUID categoryId,
            String categoryName,
            BudgetStatus budget
    ) {}

    public record MonthlyBudgetResponse(
            String month,
            String currency,
            BudgetStatus overall,
            List<CategoryBudgetStatus> categories,
            BigDecimal baseOverallLimit,
            BigDecimal rolloverReceived,
            boolean rolloverEnabled
    ) {}

    public record SaveBudgetRequest(
            String currency,
            BigDecimal limit,
            Boolean rolloverEnabled
    ) {}

    private String validateCurrency(String value) {
        String currency = value == null
                ? "EUR"
                : value.trim().toUpperCase(Locale.ROOT);

        if (!Set.of("EUR", "USD", "GBP").contains(currency)) {
            throw new IllegalArgumentException(
                    "Currency must be EUR, USD or GBP"
            );
        }

        return currency;
    }

    private BigDecimal validateLimit(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException(
                    "Budget limit must be greater than zero"
            );
        }

        if (amount.scale() > 2) {
            throw new IllegalArgumentException(
                    "Budget limit cannot have more than 2 decimal places"
            );
        }

        if (amount.precision() - amount.scale() > 17) {
            throw new IllegalArgumentException(
                    "Budget limit is too large"
            );
        }

        return amount.setScale(2, RoundingMode.UNNECESSARY);
    }

    private String key(
            String month,
            String currency,
            UUID categoryId) {

        return month + ":" + currency + ":" +
                (categoryId == null
                        ? "OVERALL"
                        : "CATEGORY:" + categoryId);
    }

    @Transactional
    public MonthlyBudgetResponse saveOverall(
            SaveBudgetRequest request) {

        String currency = validateCurrency(request.currency());
        String month = YearMonth.now().toString();
        String id = key(month, currency, null);

        BudgetLimitEntity entity = budgetRepository
                .findById(id)
                .orElse(null);

        // Changing only the checkbox does not require a new limit.
        BigDecimal limit = request.limit() == null
                ? (entity == null ? null : entity.getLimitAmount())
                : validateLimit(request.limit());

        if (limit == null) {
            throw new IllegalArgumentException(
                    "Set an overall budget amount first"
            );
        }

        if (entity == null) {
            entity = new BudgetLimitEntity(
                    id, month, currency, null, limit
            );
        }

        entity.setLimitAmount(limit);

        if (request.rolloverEnabled() != null) {
            entity.setRolloverEnabled(
                    request.rolloverEnabled()
            );
        }

        budgetRepository.save(entity);
        return getMonthlyBudgets(currency);
    }

    @Transactional
    public MonthlyBudgetResponse saveCategory(
            UUID categoryId,
            SaveBudgetRequest request) {

        if (!categoryRepository.existsById(categoryId)) {
            throw new NoSuchElementException(
                    "Category not found"
            );
        }

        String currency = validateCurrency(request.currency());
        BigDecimal limit = validateLimit(request.limit());
        String month = YearMonth.now().toString();

        String id = key(month, currency, categoryId);

        BudgetLimitEntity entity = budgetRepository
                .findById(id)
                .orElse(new BudgetLimitEntity(
                        id, month, currency, categoryId, limit
                ));

        entity.setLimitAmount(limit);
        budgetRepository.save(entity);

        return getMonthlyBudgets(currency);
    }

    @Transactional(readOnly = true)
    public MonthlyBudgetResponse getMonthlyBudgets(
            String currencyInput) {

        String currency = validateCurrency(currencyInput);
        YearMonth month = YearMonth.now();

        Map<UUID, BigDecimal> categorySpending =
                new HashMap<>();

        BigDecimal totalSpent = BigDecimal.ZERO;

        List<ExpenseEntity> expenses =
                expenseRepository.findMonthlyExpenses(
                        month.atDay(1),
                        LocalDate.now(),
                        currency
                );

        for (ExpenseEntity expense : expenses) {
            if (expense.getAmount() == null) {
                continue;
            }

            BigDecimal amount =
                    BigDecimal.valueOf(expense.getAmount());

            totalSpent = totalSpent.add(amount);

            if (expense.getCategory() != null) {
                UUID categoryId =
                        expense.getCategory().getId();

                if (categoryId != null) {
                    categorySpending.merge(
                            categoryId,
                            amount,
                            BigDecimal::add
                    );
                }
            }
        }

        Map<UUID, BigDecimal> limits = new HashMap<>();
        BigDecimal overallLimit = null;
        boolean rolloverEnabled = false;

        for (BudgetLimitEntity budget :
                budgetRepository.findByMonthAndCurrency(
                        month.toString(), currency)) {

            if (budget.getCategoryId() == null) {
                overallLimit = budget.getLimitAmount();
                rolloverEnabled = budget.isRolloverEnabled();
            } else {
                limits.put(
                        budget.getCategoryId(),
                        budget.getLimitAmount()
                );
            }
        }

        // Keep the existing duplicate-category display fix.
        Map<String, CategoryBudgetStatus> uniqueCategories =
                new LinkedHashMap<>();

        for (CategoryEntity category :
                categoryRepository.findByYearAndMonth(
                        month.getYear(),
                        month.getMonthValue())) {

            UUID categoryId = category.getId();

            String name = category.getName() == null ||
                    category.getName().isBlank()
                    ? "Unlabelled category"
                    : category.getName().trim();

            String nameKey =
                    name.toLowerCase(Locale.ROOT);

            BigDecimal spent =
                    categorySpending.getOrDefault(
                            categoryId,
                            BigDecimal.ZERO
                    );

            BigDecimal limit = limits.get(categoryId);

            CategoryBudgetStatus previous =
                    uniqueCategories.get(nameKey);

            if (previous == null) {
                uniqueCategories.put(
                        nameKey,
                        new CategoryBudgetStatus(
                                categoryId,
                                name,
                                calculateStatus(limit, spent)
                        )
                );
            } else {
                BudgetStatus prior = previous.budget();

                BigDecimal combinedLimit = prior.limit();

                if (limit != null) {
                    combinedLimit = combinedLimit == null
                            ? limit
                            : combinedLimit.add(limit);
                }

                uniqueCategories.put(
                        nameKey,
                        new CategoryBudgetStatus(
                                previous.categoryId(),
                                previous.categoryName(),
                                calculateStatus(
                                        combinedLimit,
                                        prior.spent().add(spent)
                                )
                        )
                );
            }
        }

        List<CategoryBudgetStatus> categories =
                new ArrayList<>(uniqueCategories.values());

        categories.sort(
                Comparator.comparing(
                        CategoryBudgetStatus::categoryName,
                        String.CASE_INSENSITIVE_ORDER
                )
        );

        BigDecimal rolloverReceived =
                calculateIncomingRollover(month, currency, 120);

        BigDecimal effectiveLimit = overallLimit == null
                ? null
                : overallLimit.add(rolloverReceived);

        return new MonthlyBudgetResponse(
                month.toString(),
                currency,
                calculateStatus(effectiveLimit, totalSpent),
                List.copyOf(categories),
                overallLimit,
                money(rolloverReceived),
                rolloverEnabled
        );
    }

    // A month carries its unused money forward only if
    // rollover was enabled for that source month.
    // Overspending does not become a negative rollover.
    private BigDecimal calculateIncomingRollover(
            YearMonth targetMonth,
            String currency,
            int monthsRemaining) {

        if (monthsRemaining <= 0) {
            return BigDecimal.ZERO;
        }

        YearMonth previous = targetMonth.minusMonths(1);

        BudgetLimitEntity previousBudget = budgetRepository
                .findById(
                        key(previous.toString(), currency, null)
                )
                .orElse(null);

        if (previousBudget == null ||
                !previousBudget.isRolloverEnabled()) {
            return BigDecimal.ZERO;
        }

        BigDecimal earlierCarry =
                calculateIncomingRollover(
                        previous,
                        currency,
                        monthsRemaining - 1
                );

        BigDecimal previousAvailable =
                previousBudget.getLimitAmount()
                        .add(earlierCarry);

        BigDecimal previousSpent = BigDecimal.ZERO;

        for (ExpenseEntity expense :
                expenseRepository.findMonthlyExpenses(
                        previous.atDay(1),
                        previous.atEndOfMonth(),
                        currency)) {

            if (expense.getAmount() != null) {
                previousSpent = previousSpent.add(
                        BigDecimal.valueOf(
                                expense.getAmount()
                        )
                );
            }
        }

        return previousAvailable
                .subtract(previousSpent)
                .max(BigDecimal.ZERO)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BudgetStatus calculateStatus(
            BigDecimal limit,
            BigDecimal spent) {

        BigDecimal roundedSpent = money(spent);

        if (limit == null) {
            return new BudgetStatus(
                    null,
                    roundedSpent,
                    null,
                    null,
                    "NOT_SET"
            );
        }

        BigDecimal remaining = money(
                limit.subtract(roundedSpent)
        );

        BigDecimal percentage = roundedSpent
                .multiply(BigDecimal.valueOf(100))
                .divide(
                        limit,
                        2,
                        RoundingMode.HALF_UP
                );

        String status;

        if (roundedSpent.compareTo(limit) >= 0) {
            status = "EXCEEDED";
        } else if (percentage.compareTo(
                BigDecimal.valueOf(80)) >= 0) {
            status = "WARNING";
        } else {
            status = "OK";
        }

        return new BudgetStatus(
                limit,
                roundedSpent,
                remaining,
                percentage,
                status
        );
    }

    private BigDecimal money(BigDecimal amount) {
        return amount.setScale(
                2,
                RoundingMode.HALF_UP
        );
    }
}
