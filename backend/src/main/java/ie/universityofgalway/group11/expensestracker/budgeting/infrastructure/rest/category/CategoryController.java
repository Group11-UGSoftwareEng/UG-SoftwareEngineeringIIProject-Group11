package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.rest.category;

import ie.universityofgalway.group11.expensestracker.budgeting.application.category.AddExpenseToCategoryUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.dto.AddExpenseToCategoryCommand;
import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.dto.AddExpenseToCategoryResponse;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.ViewCategoryExpensesUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.rest.category.dto.AddExpenseToCategoryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.AddCategoryUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.AddCategoryCommand;
import ie.universityofgalway.group11.expensestracker.budgeting.application.common.exceptions.CategoryException;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.rest.category.dto.AddCategoryRequest;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final AddExpenseToCategoryUseCase addExpenseToCategoryUseCase;
    private final ViewCategoryExpensesUseCase viewCategoryExpensesUseCase;
    private final AddCategoryUseCase addCategoryUseCase;

    public CategoryController(AddExpenseToCategoryUseCase addExpenseToCategoryUseCase,
                              ViewCategoryExpensesUseCase viewCategoryExpensesUseCase,
                              AddCategoryUseCase addCategoryUseCase) {
        this.addExpenseToCategoryUseCase = addExpenseToCategoryUseCase;
        this.viewCategoryExpensesUseCase = viewCategoryExpensesUseCase;
        this.addCategoryUseCase = addCategoryUseCase;
    }

    // TODO implement (Query instead of null)
//    @GetMapping("/{categoryId}")
//    public ResponseEntity<ViewCategoryExpensesResponse> getCategoryExpenses() {
//        return ResponseEntity.ok(viewCategoryExpensesUseCase.execute(null));
//    }

    @PostMapping("/{categoryId}")
    public ResponseEntity<AddExpenseToCategoryResponse> addExpense(@PathVariable UUID categoryId,
                                                                   @RequestBody AddExpenseToCategoryRequest request) {
        AddExpenseToCategoryCommand command = new AddExpenseToCategoryCommand(categoryId, request.amount(), request.description(), request.currency());
        return ResponseEntity.ok(addExpenseToCategoryUseCase.execute(command));
    }

//    @PostMapping
//    public ResponseEntity<AddCategoryResponse> addCategory(@RequestBody AddCategoryCommand command) {
//        AddCategoryResponse response = addCategoryUseCase.execute(command);
//        return ResponseEntity.ok(response);
//    }
@PostMapping
public ResponseEntity<?> addCategory(@RequestBody AddCategoryRequest request) {
    try {
        return ResponseEntity.ok(addCategoryUseCase.execute(new AddCategoryCommand(request.name())));
    } catch (CategoryException e) {
        return ResponseEntity.status(409).body(Map.of("message", e.getMessage()));
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }
}

}
