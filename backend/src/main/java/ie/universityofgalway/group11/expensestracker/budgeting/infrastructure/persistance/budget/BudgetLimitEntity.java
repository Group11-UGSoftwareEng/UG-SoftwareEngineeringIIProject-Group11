
package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.budget;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

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

    @Column(name = "limit_amount", nullable = false,
            precision = 19, scale = 2)
    private BigDecimal limitAmount;

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
