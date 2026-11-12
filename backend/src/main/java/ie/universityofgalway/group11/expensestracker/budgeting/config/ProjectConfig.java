package ie.universityofgalway.group11.expensestracker.budgeting.config;

//import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.ViewAllExpensesUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.AddExpenseToCategoryUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.ViewCategoryExpensesUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.ViewAllExpensesUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryRepository;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseRepository;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.EditCategoryDetailsUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.category.CategoryJPARepository;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.category.CategoryPersistanceAdapter;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.expense.ExpenseJPARepository;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.expense.ExpensePersistanceAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.AddCategoryUseCase;

import ie.universityofgalway.group11.expensestracker.budgeting.application.category.ResetBudgetingUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.ViewCategoriesUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.DeleteCategoryUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.DeleteExpenseUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.EditExpenseUseCase;
import java.time.YearMonth;

/**
 * Spring configuration for the budgeting module.
 * It wires repository adapters and use-case services into the application context.
 */
@Configuration
public class ProjectConfig {

    /**
     * Creates a category persistence adapter using the JPA repository.
     *
     * @param categoryJPARepository Spring Data repository for category entities
     * @return category repository adapter
     */
    public CategoryPersistanceAdapter categoryRepository(CategoryJPARepository categoryJPARepository) {
        return new CategoryPersistanceAdapter(categoryJPARepository);
    }

    /**
     * Creates the expense persistence adapter backed by the JPA repositories.
     *
     * @param expenseJPARepository Spring Data repository for expense entities
     * @param categoryJPARepository repository used to resolve category relationships
     * @return expense repository adapter
     */
    public ExpensePersistanceAdapter expensePersistanceAdapter(ExpenseJPARepository expenseJPARepository,
                                                               CategoryJPARepository categoryJPARepository) {
        return new ExpensePersistanceAdapter(expenseJPARepository, categoryJPARepository);
    }

    /**
     * Registers the use case for adding an expense to a category.
     *
     * @param expenseRepository expense persistence contract
     * @param categoryRepository category persistence contract
     * @return configured add-expense use case
     */
    @Bean
    public AddExpenseToCategoryUseCase addExpenseUseCase(ExpenseRepository expenseRepository, CategoryRepository categoryRepository) {
        return new AddExpenseToCategoryUseCase(expenseRepository, categoryRepository);
    }

    /**
     * Registers the category expense-summary use case.
     *
     * @param categoryRepository repository for category lookups
     * @param expenseRepository repository for expense lookups
     * @return configured category expense view service
     */
    @Bean
    public ViewCategoryExpensesUseCase viewCategoryExpensesUseCase(
            CategoryRepository categoryRepository,
            ExpenseRepository expenseRepository) {

        return new ViewCategoryExpensesUseCase(
                categoryRepository, expenseRepository);
    }

    /**
     * Registers the budgeting reset service.
     *
     * @param categoryRepository repository used to reset category budgets
     * @return configured reset use case
     */
    @Bean
    public ResetBudgetingUseCase resetBudgetingUseCase(
            CategoryRepository categoryRepository) {
        return new ResetBudgetingUseCase(categoryRepository);
    }

    /**
     * Registers the all-expenses view use case.
     *
     * @param expenseRepository repository for expense retrieval
     * @return configured expenses listing use case
     */
    @Bean
    public ViewAllExpensesUseCase viewMyExpensesUseCase(
            ExpenseRepository expenseRepository) {
        return new ViewAllExpensesUseCase(expenseRepository);
    }

    /**
     * Registers the category creation use case.
     *
     * @param categoryRepository repository for storing categories
     * @return configured category-creation service
     */
    @Bean
    public AddCategoryUseCase addCategoryUseCase(CategoryRepository categoryRepository) {
        return new AddCategoryUseCase(categoryRepository);
    }

    /**
     * Registers the category update use case.
     *
     * @param categoryRepository repository for category updates
     * @return configured category details edit service
     */
    @Bean
    public EditCategoryDetailsUseCase editCategoryDetailsUseCase(
            CategoryRepository categoryRepository) {

        return new EditCategoryDetailsUseCase(categoryRepository);
    }

    /**
     * Registers the category listing use case.
     *
     * @param resetBudgetingUseCase used to reset or initialise budgets
     * @param budgetingPeriod current budgeting period
     * @return configured category listing service
     */
    @Bean
    public ViewCategoriesUseCase viewCategoriesUseCase(
            ResetBudgetingUseCase resetBudgetingUseCase,
            YearMonth budgetingPeriod) {
        return new ViewCategoriesUseCase(
                resetBudgetingUseCase, budgetingPeriod);
    }

    /**
     * Exposes the current month as the active budgeting period.
     *
     * @return current year-month
     */
    @Bean
    public YearMonth budgetingPeriod() {
        return YearMonth.now();
    }

    /**
     * Registers the category deletion use case.
     *
     * @param categoryRepository repository for category removal
     * @param expenseRepository repository used to inspect remaining expenses
     * @return configured deletion service
     */
    @Bean
    public DeleteCategoryUseCase deleteCategoryUseCase(CategoryRepository categoryRepository, ExpenseRepository expenseRepository) {
        return new DeleteCategoryUseCase(categoryRepository, expenseRepository);
    }

    /**
     * Registers the expense deletion use case.
     *
     * @param expenseRepository repository for expense removal
     * @return configured expense deletion service
     */
    @Bean
    public DeleteExpenseUseCase deleteExpenseUseCase(ExpenseRepository expenseRepository) {
        return new DeleteExpenseUseCase(expenseRepository);
    }

    /**
     * Registers the expense editing use case.
     *
     * @param expenseRepository repository to update expense data
     * @param categoryRepository repository used when an expense's category relationship is involved
     * @return configured expense edit service
     */
    @Bean
    public EditExpenseUseCase editExpenseUseCase(ExpenseRepository expenseRepository, CategoryRepository categoryRepository) {
        return new EditExpenseUseCase(expenseRepository, categoryRepository);
    }
}