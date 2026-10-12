package ie.universityofgalway.group11.expensestracker.useradmission.infrastructure;

import ie.universityofgalway.group11.expensestracker.useradmission.application.user.AddUserUseCase;
import ie.universityofgalway.group11.expensestracker.useradmission.domain.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {

    @Bean
    public UserRepository userRepository() {
        return new InMemoryUserRepository();
    }

    @Bean
    public AddUserUseCase addUserUseCase(UserRepository userRepository) {
        return new AddUserUseCase(userRepository);
    }
}
