package ie.universityofgalway.group11.expensestracker.budgeting.config;

//import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.ViewAllExpensesUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.AddExpenseToCategoryUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.ViewCategoryExpensesUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.ViewAllExpensesUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryRepository;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseRepository;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.InMemoryBudgetingRepository;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.EditCategoryDetailsUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {

    // Repository

    @Bean
    InMemoryBudgetingRepository inMemoryBudgetingRepository() {
        return new InMemoryBudgetingRepository();
    }

    // Use Cases

    @Bean
    public AddExpenseToCategoryUseCase addExpenseUseCase(CategoryRepository categoryRepository) {
        return new AddExpenseToCategoryUseCase(categoryRepository);
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
    @Bean
    public EditCategoryDetailsUseCase editCategoryDetailsUseCase(
            CategoryRepository categoryRepository) {

        return new EditCategoryDetailsUseCase(categoryRepository);
    }
}