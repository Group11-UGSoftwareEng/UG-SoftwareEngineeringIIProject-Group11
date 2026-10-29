package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.expense;

import ie.universityofgalway.group11.expensestracker.budgeting.application.category.AddExpenseToCategoryUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.Category;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseId;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseRepository;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.AdapterHelper;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.category.CategoryEntity;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.category.CategoryJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ExpensePersistanceAdapter implements ExpenseRepository {

    private final ExpenseJPARepository expenseJpaRepository;
    private final CategoryJPARepository categoryJpaRepository;
    private final AdapterHelper helper;

    public ExpensePersistanceAdapter(
            ExpenseJPARepository expenseJpaRepository,
            CategoryJPARepository categoryJPARepository) {

        this.expenseJpaRepository = expenseJpaRepository;
        this.categoryJpaRepository = categoryJPARepository;
        this.helper = new AdapterHelper();
    }
    @Override
    public Optional<Expense> findById(ExpenseId id) {
        return Optional.empty();
    }

    @Override
    public Expense save(Expense expense, Category toCategory) {
        CategoryEntity categoryEntity = helper.toCategoryEntity(toCategory);
        ExpenseEntity expenseEntity = helper.toExpenseEntity(expense, categoryEntity);
        expenseJpaRepository.save(expenseEntity);
        return expense;
    }

    public CategoryJPARepository getCategoryJpaRepository() {
        return categoryJpaRepository;
    }

    public ExpenseJPARepository getExpenseJpaRepository() {
        return expenseJpaRepository;
    }

    @Override
    public void deleteById(ExpenseId id) {

    }

    @Override
    public List<Expense> findAll() {
        return expenseJpaRepository.findAll().stream()
                .map(helper::toExpenseDomain)
                .collect(Collectors.toList());
    }
}
