
package ie.universityofgalway.group11.expensestracker.budgeting.application;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseRepository;

@Service
public class BudgetCarryoverService {

    private final ExpenseRepository expenseRepository;

    // In-memory budget configurations.
    private final Map<BudgetKey, BudgetPlan> budgetPlans =
            new ConcurrentHashMap<>();

    public BudgetCarryoverService(
            ExpenseRepository expenseRepository
    ) {
        this.expenseRepository = expenseRepository;
    }

    private record BudgetKey(
            UUID categoryId,
            String currency
    ) {}

    private record BudgetPlan(
            YearMonth startMonth,
            BigDecimal monthlyBudget
    ) {}

    public record BudgetPeriod(
            YearMonth month,
            BigDecimal baseBudget,
            BigDecimal carriedOver,
            BigDecimal availableBudget,
            BigDecimal totalSpent,
            BigDecimal remainingBudget,
            BigDecimal carriedToNextMonth,
            boolean overBudget
    ) {}

    public record BudgetReport(
            UUID categoryId,
            String currency,
            YearMonth startMonth,
            YearMonth requestedMonth,
            BudgetPeriod period
    ) {}

    // Configure a recurring monthly category budget.
    public void configureBudget(
            UUID categoryId,
            String currency,
            YearMonth startMonth,
            BigDecimal monthlyBudget
    ) {
        if (categoryId == null || startMonth == null) {
            throw new IllegalArgumentException(
                    "Category ID and start month are required"
            );
        }

        if (monthlyBudget == null ||
                monthlyBudget.signum() < 0) {
            throw new IllegalArgumentException(
                    "Monthly budget must not be negative"
            );
        }

        String validCurrency = validateCurrency(currency);

        BudgetKey key = new BudgetKey(
                categoryId,
                validCurrency
        );

        budgetPlans.put(
                key,
                new BudgetPlan(
                        startMonth,
                        money(monthlyBudget)
                )
        );
    }

    // Calculate the spending for a specific category
    // during a specific month.
    private BigDecimal spendingForMonth(
            List<Expense> expenses,
            UUID categoryId,
            String currency,
            YearMonth month
    ) {
        BigDecimal total = BigDecimal.ZERO;

        for (Expense expense : expenses) {

            if (expense.date() == null) {
                continue;
            }

            if (!YearMonth.from(expense.date())
                    .equals(month)) {
                continue;
            }

            if (!expense.categoryId().asUuid()
                    .equals(categoryId)) {
                continue;
            }

            if (!expense.currency().code()
                    .equals(currency)) {
                continue;
            }

            total = total.add(
                    expense.amount().value()
            );
        }

        return money(total);
    }

    // Calculate carryover for consecutive months.
    public BudgetReport getBudgetReport(
            UUID categoryId,
            String currency,
            YearMonth requestedMonth
    ) {
        if (categoryId == null || requestedMonth == null) {
            throw new IllegalArgumentException(
                    "Category ID and month are required"
            );
        }

        String validCurrency = validateCurrency(currency);

        BudgetPlan plan = budgetPlans.get(
                new BudgetKey(categoryId, validCurrency)
        );

        if (plan == null) {
            throw new IllegalArgumentException(
                    "No budget configured for this category and currency"
            );
        }

        if (requestedMonth.isBefore(plan.startMonth())) {
            throw new IllegalArgumentException(
                    "Requested month is before the budget start month"
            );
        }

        long numberOfMonths =
                java.time.temporal.ChronoUnit.MONTHS.between(
                        plan.startMonth(),
                        requestedMonth
                ) + 1;

        if (numberOfMonths > 120) {
            throw new IllegalArgumentException(
                    "Maximum calculation range is 120 months"
            );
        }

        List<Expense> expenses = expenseRepository.findAll();

        BigDecimal carryover = BigDecimal.ZERO;
        BudgetPeriod result = null;

        YearMonth currentMonth = plan.startMonth();

        while (!currentMonth.isAfter(requestedMonth)) {

            BigDecimal baseBudget = plan.monthlyBudget();

            BigDecimal available = baseBudget.add(
                    carryover
            );

            BigDecimal spent = spendingForMonth(
                    expenses,
                    categoryId,
                    validCurrency,
                    currentMonth
            );

            BigDecimal remaining = available.subtract(
                    spent
            );

            BigDecimal nextCarryover = remaining.max(
                    BigDecimal.ZERO
            );

            result = new BudgetPeriod(
                    currentMonth,
                    money(baseBudget),
                    money(carryover),
                    money(available),
                    money(spent),
                    money(remaining),
                    money(nextCarryover),
                    remaining.signum() < 0
            );

            carryover = nextCarryover;
            currentMonth = currentMonth.plusMonths(1);
        }

        return new BudgetReport(
                categoryId,
                validCurrency,
                plan.startMonth(),
                requestedMonth,
                result
        );
    }

    private String validateCurrency(String currency) {
        if (currency == null) {
            throw new IllegalArgumentException(
                    "Currency is required"
            );
        }

        String code = currency.toUpperCase(Locale.ROOT);

        if (!List.of("EUR", "USD", "GBP").contains(code)) {
            throw new IllegalArgumentException(
                    "Currency must be EUR, USD or GBP"
            );
        }

        return code;
    }

    private static BigDecimal money(BigDecimal amount) {
        return amount.setScale(
                2,
                RoundingMode.HALF_UP
        );
    }
}
