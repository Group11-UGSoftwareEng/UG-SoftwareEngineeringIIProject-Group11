package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.rest.category;

import ie.universityofgalway.group11.expensestracker.budgeting.application.category.AddExpenseToCategoryUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.dto.AddExpenseToCategoryCommand;
import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.dto.AddExpenseToCategoryResponse;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.ViewCategoryExpensesUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.rest.category.dto.AddExpenseToCategoryRequest;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.EditCategoryDetailsUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.EditCategoryDetailsCommand;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.rest.category.dto.EditCategoryDetailsRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.ViewCategoryExpensesCommand;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.ViewCategoryExpensesResponse;

import java.util.Map;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.ViewCategoriesUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.ViewCategoriesCommand;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.ViewCategoriesResponse;
import java.util.UUID;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.AddCategoryUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.AddCategoryCommand;
import ie.universityofgalway.group11.expensestracker.budgeting.application.common.exceptions.CategoryException;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.rest.category.dto.AddCategoryRequest;

@CrossOrigin
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final AddExpenseToCategoryUseCase addExpenseToCategoryUseCase;
    private final ViewCategoryExpensesUseCase viewCategoryExpensesUseCase;
    private final AddCategoryUseCase addCategoryUseCase;
    private final EditCategoryDetailsUseCase editCategoryDetailsUseCase;
    private final ViewCategoriesUseCase viewCategoriesUseCase;

    public CategoryController(
            AddExpenseToCategoryUseCase addExpenseToCategoryUseCase,
            ViewCategoryExpensesUseCase viewCategoryExpensesUseCase,
            EditCategoryDetailsUseCase editCategoryDetailsUseCase,
            ViewCategoriesUseCase viewCategoriesUseCase,
            AddCategoryUseCase addCategoryUseCase) {

        this.addExpenseToCategoryUseCase = addExpenseToCategoryUseCase;
        this.viewCategoryExpensesUseCase = viewCategoryExpensesUseCase;
        this.editCategoryDetailsUseCase = editCategoryDetailsUseCase;
        this.viewCategoriesUseCase = viewCategoriesUseCase;
        this.addCategoryUseCase = addCategoryUseCase;
    }
  
  
    @GetMapping
    public ResponseEntity<ViewCategoriesResponse> getCategories() {
        ViewCategoriesCommand command = new ViewCategoriesCommand();

        return ResponseEntity.ok(viewCategoriesUseCase.execute(command));
    }

    @GetMapping("/{categoryId}/expenses")
    public ResponseEntity<?> getCategoryExpenses(
            @PathVariable("categoryId") UUID categoryId) {

        ViewCategoryExpensesCommand command =
                new ViewCategoryExpensesCommand(categoryId);

        try {
            return ResponseEntity.ok(
                    viewCategoryExpensesUseCase.execute(command));
        } catch (IllegalArgumentException error) {
            return ResponseEntity.status(404).body(error.getMessage());
        }
    }
    @PostMapping("/{categoryId}")
    public ResponseEntity<AddExpenseToCategoryResponse> addExpense(@PathVariable UUID categoryId,
                                                                   @RequestBody AddExpenseToCategoryRequest request) {
        AddExpenseToCategoryCommand command = new AddExpenseToCategoryCommand(categoryId, request.amount(), request.description(), request.currency());
        return ResponseEntity.ok(addExpenseToCategoryUseCase.execute(command));
    }
    @PutMapping("/{categoryId}")
    public ResponseEntity<String> editCategory(
            @PathVariable("categoryId") UUID categoryId,
            @RequestBody EditCategoryDetailsRequest request) {

        EditCategoryDetailsCommand command =
                new EditCategoryDetailsCommand(categoryId, request.newName());

        editCategoryDetailsUseCase.execute(command);

        return ResponseEntity.ok("Category name updated.");
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleInvalidInput(IllegalArgumentException error) {
        return ResponseEntity.badRequest().body(Map.of("message", error.getMessage()));
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
