package ie.universityofgalway.group11.expensestracker.useradmission.infrastructure.rest.user;

import ie.universityofgalway.group11.expensestracker.useradmission.application.user.AddUserUseCase;
import ie.universityofgalway.group11.expensestracker.useradmission.application.user.DeleteUserUseCase;
import ie.universityofgalway.group11.expensestracker.useradmission.application.user.UpdateUserDetailsUseCase;
import ie.universityofgalway.group11.expensestracker.useradmission.application.user.ViewAllUsersUseCase;
import ie.universityofgalway.group11.expensestracker.useradmission.application.user.dto.AddUserCommand;
import ie.universityofgalway.group11.expensestracker.useradmission.application.user.dto.AddUserResponse;
import ie.universityofgalway.group11.expensestracker.useradmission.infrastructure.rest.user.dto.AddUserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    // Use Cases
    private final AddUserUseCase addUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    //private final UpdateUserDetailsUseCase updateUserDetailsUseCase;
    //private final ViewAllUsersUseCase viewAllUsersUseCase;

    // Constructor
    public UserController(AddUserUseCase addUserUseCase,
                          DeleteUserUseCase deleteUserUseCase
                          //UpdateUserDetailsUseCase updateUserDetailsUseCase,
                          //ViewAllUsersUseCase viewAllUsersUseCase
                          ) {
        this.addUserUseCase = addUserUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
        //this.updateUserDetailsUseCase = updateUserDetailsUseCase;
        //this.viewAllUsersUseCase = viewAllUsersUseCase;
    }


    //REST commands (ResponseEntity for status codes)
    @PostMapping
    public ResponseEntity<AddUserResponse> addUser(@RequestBody AddUserRequest request) {
        AddUserCommand command = new AddUserCommand(request.firstName(), request.lastName(), request.email());
        return ResponseEntity.ok(addUserUseCase.execute(command)); // Response Entity
    }

}
