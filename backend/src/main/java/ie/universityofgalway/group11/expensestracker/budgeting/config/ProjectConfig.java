package ie.universityofgalway.group11.expensestracker.budgeting.config;

//import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.ViewAllExpensesUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.AddExpenseToCategoryUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.ViewCategoryExpensesUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.ViewAllExpensesUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryRepository;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseRepository;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.AdapterHelper;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.category.CategoryJPARepository;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.category.CategoryPersistanceAdapter;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.expense.ExpenseJPARepository;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.expense.ExpensePersistanceAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    public ViewCategoryExpensesUseCase viewCategoryExpensesUseCase(CategoryRepository categoryRepository) {
        return new ViewCategoryExpensesUseCase(categoryRepository);
    }

    @Bean
    public ViewAllExpensesUseCase viewMyExpensesUseCase(
            ExpenseRepository expenseRepository) {
        return new ViewAllExpensesUseCase(expenseRepository);
    }
}