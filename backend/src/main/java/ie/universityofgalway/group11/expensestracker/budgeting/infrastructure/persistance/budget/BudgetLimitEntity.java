
package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.budget;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "monthly_budget_limit")
public class BudgetLimitEntity {

    @Id
    @Column(name = "scope_key", length = 100)
    private String scopeKey;

    @Column(name = "budget_month", nullable = false, length = 7)
    private String month;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(name = "category_id")
    private UUID categoryId;

    @Column(
            name = "limit_amount",
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal limitAmount;

    @Column(name = "rollover_enabled", nullable = false)
    private boolean rolloverEnabled = false;

    protected BudgetLimitEntity() {
    }

    public BudgetLimitEntity(
            String scopeKey,
            String month,
            String currency,
            UUID categoryId,
            BigDecimal limitAmount) {
        this.scopeKey = scopeKey;
        this.month = month;
        this.currency = currency;
        this.categoryId = categoryId;
        this.limitAmount = limitAmount;
    }

    public boolean isRolloverEnabled() {
        return rolloverEnabled;
    }

    public void setRolloverEnabled(boolean enabled) {
        this.rolloverEnabled = enabled;
    }

    public String getScopeKey() {
        return scopeKey;
    }

    public String getMonth() {
        return month;
    }

    public String getCurrency() {
        return currency;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public BigDecimal getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(BigDecimal amount) {
        this.limitAmount = amount;
    }
}
