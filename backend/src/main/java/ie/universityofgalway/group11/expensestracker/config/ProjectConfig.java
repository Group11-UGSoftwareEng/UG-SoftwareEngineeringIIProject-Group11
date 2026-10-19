package ie.universityofgalway.group11.expensestracker.config;

import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.ViewMyExpensesUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseRepository;
import ie.universityofgalway.group11.expensestracker.useradmission.application.user.AddUserUseCase;
import ie.universityofgalway.group11.expensestracker.useradmission.application.user.DeleteUserUseCase;
import ie.universityofgalway.group11.expensestracker.useradmission.domain.user.UserRepository;
import ie.universityofgalway.group11.expensestracker.useradmission.infrastructure.InMemoryUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {

    // USER ADMISSION CONTEXT
    @Bean
    public UserRepository userRepository() {
        return new InMemoryUserRepository();
    }

    @Bean
    public AddUserUseCase addUserUseCase(UserRepository userRepository) {
        return new AddUserUseCase(userRepository);
    }

    @Bean
    public DeleteUserUseCase deleteUserUseCase(UserRepository userRepository) {
        return new DeleteUserUseCase(userRepository);
    }

//    @Bean
//    public UpdateUserDetailsUseCase updateUserDetailsUseCase(UserRepository userRepository) {
//        return new UpdateUserDetailsUseCase(userRepository);
//    }

//    @Bean
//    public ViewAllUsersUseCase viewAllUsersUseCase(UserRepository userRepository) {
//        return new ViewAllUsersUseCase(userRepository);
//    }


    // BUDGETING CONTEXT
    @Bean
    public ViewMyExpensesUseCase viewMyExpensesUseCase(
            ExpenseRepository expenseRepository) {

        return new ViewMyExpensesUseCase(expenseRepository);
    }

}
