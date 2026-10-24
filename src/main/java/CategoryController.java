package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.rest.category;

import ie.universityofgalway.group11.expensestracker.budgeting.application.category.AddCategoryUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.AddCategoryCommand;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.AddCategoryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    private final AddCategoryUseCase addCategoryUseCase;

    public CategoryController(AddCategoryUseCase addCategoryUseCase) {
        this.addCategoryUseCase = addCategoryUseCase;
    }

    @PostMapping
    public ResponseEntity<AddCategoryResponse> addCategory(@RequestBody AddCategoryCommand command) {
        AddCategoryResponse response = addCategoryUseCase.execute(command);
        return ResponseEntity.ok(response);
    }
}