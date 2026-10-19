package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.rest.category;

import ie.universityofgalway.group11.expensestracker.useradmission.application.user.AddUserUseCase;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    public CategoryController(AddUserUseCase addUserUseCase) {

    }

}
