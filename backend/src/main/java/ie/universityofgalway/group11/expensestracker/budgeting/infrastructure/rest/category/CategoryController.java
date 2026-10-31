package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.rest.category;

import ie.universityofgalway.group11.expensestracker.budgeting.application.category.AddExpenseToCategoryUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.dto.AddExpenseToCategoryCommand;
import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.dto.AddExpenseToCategoryResponse;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.ViewCategoryExpensesUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.rest.category.dto.AddExpenseToCategoryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.ViewCategoryExpensesCommand;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.ViewCategoryExpensesResponse;

import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final AddExpenseToCategoryUseCase addExpenseToCategoryUseCase;
    private final ViewCategoryExpensesUseCase viewCategoryExpensesUseCase;

    public CategoryController(AddExpenseToCategoryUseCase addExpenseToCategoryUseCase, ViewCategoryExpensesUseCase viewCategoryExpensesUseCase) {
        this.addExpenseToCategoryUseCase = addExpenseToCategoryUseCase;
        this.viewCategoryExpensesUseCase = viewCategoryExpensesUseCase;
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

//    @PostMapping
//    public ResponseEntity<AddCategoryResponse> addCategory(@RequestBody AddCategoryCommand command) {
//        AddCategoryResponse response = addCategoryUseCase.execute(command);
//        return ResponseEntity.ok(response);
//    }


}
