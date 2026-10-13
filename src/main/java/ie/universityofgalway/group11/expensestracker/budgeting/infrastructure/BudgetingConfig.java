package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure;

import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.ViewMyExpensesUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

@Configuration
public class BudgetingConfig {

    @Bean
    public ExpenseRepository expenseRepository() {

        ArrayList<Expense> expenses = new ArrayList<>();

        // Create some expenses to test the program
        Expense food = new Expense("user1", new BigDecimal("12.50"),
                LocalDate.of(2026, 10, 10), "Food");

        Expense transport = new Expense("user1", new BigDecimal("5.00"),
                LocalDate.of(2026, 10, 12), "Transport");

        Expense shopping = new Expense("user2", new BigDecimal("20.00"),
                LocalDate.of(2026, 10, 11), "Shopping");

        // Add the expenses to the list
        expenses.add(food);
        expenses.add(transport);
        expenses.add(shopping);

        return new InMemoryExpenseRepository(expenses);
    }

    @Bean
    public ViewMyExpensesUseCase viewMyExpensesUseCase(
            ExpenseRepository expenseRepository) {

        return new ViewMyExpensesUseCase(expenseRepository);
    }
}