package ie.universityofgalway.group11.expensestracker.budgeting.config;

import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.ViewMyExpensesUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {

    @Bean
    public ViewMyExpensesUseCase viewMyExpensesUseCase(
            ExpenseRepository expenseRepository) {

        return new ViewMyExpensesUseCase(expenseRepository);
    }
}