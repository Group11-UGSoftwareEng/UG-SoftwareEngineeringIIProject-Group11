package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.expense;

import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.Category;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseId;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseRepository;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.AdapterHelper;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.category.CategoryEntity;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.category.CategoryJPARepository;

import org.springframework.stereotype.Repository;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryId;

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
            CategoryJPARepository categoryJpaRepository
    ) {
        this.expenseJpaRepository = expenseJpaRepository;
        this.categoryJpaRepository = categoryJpaRepository;
        this.helper = new AdapterHelper();
    }

    @Override
    public Optional<Expense> findById(ExpenseId id) {
        return expenseJpaRepository.findById(id.asUuid())
                .map(helper::toExpenseDomain);
    }

    @Override
    public Expense save(Expense expense, Category toCategory) {

        CategoryEntity existingCategory = categoryJpaRepository
                .findById(expense.categoryId().asUuid())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Category not found in database"
                        )
                );

        ExpenseEntity expenseEntity =
                helper.toExpenseEntity(expense, existingCategory);

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
        expenseJpaRepository.deleteById(id.asUuid());
    }
    @Override
    public List<Expense> findByCategoryId(CategoryId categoryId) {
        return expenseJpaRepository
                .findByCategory_Id(categoryId.asUuid())
                .stream()
                .map(helper::toExpenseDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Expense> findAll() {
        return expenseJpaRepository.findAll()
                .stream()
                .map(helper::toExpenseDomain)
                .collect(Collectors.toList());
    }
}
