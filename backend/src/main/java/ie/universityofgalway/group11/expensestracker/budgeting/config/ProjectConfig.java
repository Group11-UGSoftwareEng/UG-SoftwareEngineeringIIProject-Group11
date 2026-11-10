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
@Configuration
public class ProjectConfig {

    // Repository
//    @Bean
//    InMemoryBudgetingRepository inMemoryBudgetingRepository() {
//        return new InMemoryBudgetingRepository();
//    }

    public CategoryPersistanceAdapter categoryRepository(CategoryJPARepository categoryJPARepository) {
        return new CategoryPersistanceAdapter(categoryJPARepository);
    }

    public ExpensePersistanceAdapter expensePersistanceAdapter(ExpenseJPARepository expenseJPARepository,
                                                               CategoryJPARepository categoryJPARepository) {
        return new ExpensePersistanceAdapter(expenseJPARepository, categoryJPARepository);
    }

    // Use Cases

    @Bean
    public AddExpenseToCategoryUseCase addExpenseUseCase(ExpenseRepository expenseRepository, CategoryRepository categoryRepository) {
        return new AddExpenseToCategoryUseCase(expenseRepository, categoryRepository);
    }

    @Bean
    public ViewCategoryExpensesUseCase viewCategoryExpensesUseCase(
            CategoryRepository categoryRepository,
            ExpenseRepository expenseRepository) {

        return new ViewCategoryExpensesUseCase(
                categoryRepository, expenseRepository);
    }
    @Bean
    public ResetBudgetingUseCase resetBudgetingUseCase(
            CategoryRepository categoryRepository) {
        return new ResetBudgetingUseCase(categoryRepository);
    }

    @Bean
    public ViewAllExpensesUseCase viewMyExpensesUseCase(
            ExpenseRepository expenseRepository) {
        return new ViewAllExpensesUseCase(expenseRepository);
    }
    @Bean
    public AddCategoryUseCase addCategoryUseCase(CategoryRepository categoryRepository) {
        return new AddCategoryUseCase(categoryRepository);
    }
      
    @Bean
    public EditCategoryDetailsUseCase editCategoryDetailsUseCase(
            CategoryRepository categoryRepository) {

        return new EditCategoryDetailsUseCase(categoryRepository);
    }

    @Bean
    public ViewCategoriesUseCase viewCategoriesUseCase(
            ResetBudgetingUseCase resetBudgetingUseCase,
            YearMonth budgetingPeriod) {
        return new ViewCategoriesUseCase(
                resetBudgetingUseCase, budgetingPeriod);
    }

    @Bean
    public YearMonth budgetingPeriod() {
        return YearMonth.now();
    }

    @Bean
    public DeleteCategoryUseCase deleteCategoryUseCase(CategoryRepository categoryRepository, ExpenseRepository expenseRepository) {
        return new DeleteCategoryUseCase(categoryRepository, expenseRepository);
    }
    @Bean
    public DeleteExpenseUseCase deleteExpenseUseCase(ExpenseRepository expenseRepository) {
        return new DeleteExpenseUseCase(expenseRepository);
    }

    @Bean
    public EditExpenseUseCase editExpenseUseCase(ExpenseRepository expenseRepository, CategoryRepository categoryRepository) {
        return new EditExpenseUseCase(expenseRepository, categoryRepository);
    }
}