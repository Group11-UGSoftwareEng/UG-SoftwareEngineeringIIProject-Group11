package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.expense;

import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.category.CategoryEntity;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

/**
 * JPA entity representing a single expense record linked to a category.
 */
@Entity
@Table(name = "expense")
public class ExpenseEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @Column(nullable = false)
    private Double amount;

    private String description;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private LocalDate date;

    /**
     * @return expense date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * @return currency code for this expense
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * @return expense description text
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return numeric expense amount
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * @return owning category entity
     */
    public CategoryEntity getCategory() {
        return category;
    }

    /**
     * @return expense identifier
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the unique expense identifier.
     *
     * @param id expense UUID
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Sets the owning category.
     *
     * @param category linked category entity
     */
    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    /**
     * Sets the amount value.
     *
     * @param amount numeric expense amount
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * Sets the description text.
     *
     * @param description expense description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the currency code.
     *
     * @param currency ISO currency code
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * Sets the expense date.
     *
     * @param date date of the expense
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }
}
