package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.category;


import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.expense.ExpenseEntity;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "category")
public class CategoryEntity {

    @Id
    private UUID id;

    private Double budget;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Integer month;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ExpenseEntity> expenses = new HashSet<>();

    public UUID getId() {
        return id;
    }

    public Double getBudget() {
        return budget;
    }

    public String getName() {
        return name;
    }

    public Integer getYear() {
        return year;
    }

    public Integer getMonth() {
        return month;
    }

    public Set<ExpenseEntity> getExpenses() {
        return expenses;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }
}
