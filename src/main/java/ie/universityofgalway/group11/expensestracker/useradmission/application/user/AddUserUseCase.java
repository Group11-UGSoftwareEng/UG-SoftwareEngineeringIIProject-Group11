package ie.universityofgalway.group11.expensestracker.useradmission.application.user;

import ie.universityofgalway.group11.expensestracker.useradmission.application.common.UseCase;
import ie.universityofgalway.group11.expensestracker.useradmission.application.user.dto.AddUserCommand;
import ie.universityofgalway.group11.expensestracker.useradmission.application.user.dto.AddUserResponse;
import ie.universityofgalway.group11.expensestracker.useradmission.domain.user.EmailAddress;
import ie.universityofgalway.group11.expensestracker.useradmission.domain.user.User;
import ie.universityofgalway.group11.expensestracker.useradmission.domain.user.UserName;
import ie.universityofgalway.group11.expensestracker.useradmission.domain.user.UserRepository;

public class AddUserUseCase implements UseCase<AddUserCommand, AddUserResponse> {

    private final UserRepository userRepository;

    public AddUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public AddUserResponse execute(AddUserCommand request) {
        User user = User.create(UserName.of(request.firstName(), request.lastName()), EmailAddress.of(request.email()));
        userRepository.save(user);
        return new AddUserResponse(user.id().toString(), user.name().fullName(), user.email().toString());
    }
}
