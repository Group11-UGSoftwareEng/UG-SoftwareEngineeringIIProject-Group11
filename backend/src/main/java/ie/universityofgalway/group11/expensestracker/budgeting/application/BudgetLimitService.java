
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
            List<CategoryBudgetStatus> categories
    ) {}

    public record SaveBudgetRequest(
            String currency,
            BigDecimal limit
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
        BigDecimal limit = validateLimit(request.limit());
        String month = YearMonth.now().toString();

        String id = key(month, currency, null);

        BudgetLimitEntity entity = budgetRepository
                .findById(id)
                .orElse(new BudgetLimitEntity(
                        id, month, currency, null, limit
                ));

        entity.setLimitAmount(limit);
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

        for (BudgetLimitEntity budget :
                budgetRepository.findByMonthAndCurrency(
                        month.toString(), currency)) {

            if (budget.getCategoryId() == null) {
                overallLimit = budget.getLimitAmount();
            } else {
                limits.put(
                        budget.getCategoryId(),
                        budget.getLimitAmount()
                );
            }
        }

        List<CategoryBudgetStatus> categories =
                new ArrayList<>();

        for (CategoryEntity category :
                categoryRepository.findAll()) {

            UUID categoryId = category.getId();

            categories.add(new CategoryBudgetStatus(
                    categoryId,
                    category.getName(),
                    calculateStatus(
                            limits.get(categoryId),
                            categorySpending.getOrDefault(
                                    categoryId, BigDecimal.ZERO
                            )
                    )
            ));
        }

        categories.sort(
                Comparator.comparing(
                        CategoryBudgetStatus::categoryName,
                        String.CASE_INSENSITIVE_ORDER
                )
        );

        return new MonthlyBudgetResponse(
                month.toString(),
                currency,
                calculateStatus(overallLimit, totalSpent),
                List.copyOf(categories)
        );
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
                .divide(limit, 2, RoundingMode.HALF_UP);

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
        return amount.setScale(2, RoundingMode.HALF_UP);
    }
}
