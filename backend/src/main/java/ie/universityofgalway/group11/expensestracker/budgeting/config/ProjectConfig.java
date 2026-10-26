package ie.universityofgalway.group11.expensestracker.budgeting.config;

//import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.ViewMyExpensesUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.AddExpenseToCategoryUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.ViewCategoryExpensesUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryRepository;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.InMemoryBudgetingRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {

    // Repositories

    @Bean
    public CategoryRepository categoryRepository() {
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

}
