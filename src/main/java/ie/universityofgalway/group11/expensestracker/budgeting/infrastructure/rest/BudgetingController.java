package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.rest;

import ie.universityofgalway.group11.expensestracker.useradmission.application.user.AddUserUseCase;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/budgeting")
public class BudgetingController {

    public BudgetingController(AddUserUseCase addUserUseCase) {

    }

}
