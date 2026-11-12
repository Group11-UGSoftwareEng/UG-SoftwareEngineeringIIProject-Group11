package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.category;


import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.expense.ExpenseEntity;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * JPA entity representing a budgeting category and its associated expenses.
 * The unique constraint ensures that each category name appears only once per month and year.
 */
@Entity
@Table(
        name = "category",
        uniqueConstraints = @UniqueConstraint(
                name = "uc_category_name_period",
                columnNames = {"name", "year", "month"}
        )
)
public class CategoryEntity {

    @Id
    private UUID id;

    private Double budget;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Integer month;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ExpenseEntity> expenses = new HashSet<>();

    /**
     * @return category identifier
     */
    public UUID getId() {
        return id;
    }

    /**
     * @return monthly budget value for the category
     */
    public Double getBudget() {
        return budget;
    }

    /**
     * @return category display name
     */
    public String getName() {
        return name;
    }

    /**
     * @return tracking year for the category
     */
    public Integer getYear() {
        return year;
    }

    /**
     * @return tracking month for the category
     */
    public Integer getMonth() {
        return month;
    }

    /**
     * @return stored expenses linked to this category
     */
    public Set<ExpenseEntity> getExpenses() {
        return expenses;
    }

    /**
     * Sets the category identity.
     *
     * @param id the new identifier
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Sets the category budget.
     *
     * @param budget new budget value
     */
    public void setBudget(Double budget) {
        this.budget = budget;
    }

    /**
     * Sets the category name.
     *
     * @param name new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the category year.
     *
     * @param year year of creation or tracking period
     */
    public void setYear(Integer year) {
        this.year = year;
    }

    /**
     * Sets the category month.
     *
     * @param month month of creation or tracking period
     */
    public void setMonth(Integer month) {
        this.month = month;
    }
}
