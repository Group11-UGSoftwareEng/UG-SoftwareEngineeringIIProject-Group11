package ie.universityofgalway.group11.expensestracker.useradmission.infrastructure.rest;

import ie.universityofgalway.group11.expensestracker.useradmission.application.user.AddUserUseCase;
import ie.universityofgalway.group11.expensestracker.useradmission.application.user.dto.AddUserCommand;
import ie.universityofgalway.group11.expensestracker.useradmission.application.user.dto.AddUserResponse;
import ie.universityofgalway.group11.expensestracker.useradmission.infrastructure.rest.dto.AddUserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final AddUserUseCase addUserUseCase;

    public UserController(AddUserUseCase addUserUseCase) {
        this.addUserUseCase = addUserUseCase;
    }

    @PostMapping
    public ResponseEntity<AddUserResponse> addUser(@RequestBody AddUserRequest request) {
        AddUserCommand command = new AddUserCommand(request.firstName(), request.lastName(), request.email());
        return ResponseEntity.ok(addUserUseCase.execute(command));
    }

}
