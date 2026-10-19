package ie.universityofgalway.group11.expensestracker.useradmission.application.user;

import ie.universityofgalway.group11.expensestracker.config.UseCase;
import ie.universityofgalway.group11.expensestracker.useradmission.application.user.dto.DeleteUserCommand;
import ie.universityofgalway.group11.expensestracker.useradmission.application.user.dto.DeleteUserResponse;
import ie.universityofgalway.group11.expensestracker.useradmission.domain.user.UserId;
import ie.universityofgalway.group11.expensestracker.useradmission.domain.user.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

public class DeleteUserUseCase implements UseCase<DeleteUserCommand, DeleteUserResponse> {
    private final UserRepository userRepository;

    public DeleteUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public DeleteUserResponse execute(DeleteUserCommand command) {
        userRepository.deleteById(UserId.of(UUID.fromString(command.userId())));
        return new DeleteUserResponse(command.userId());
    }
}


