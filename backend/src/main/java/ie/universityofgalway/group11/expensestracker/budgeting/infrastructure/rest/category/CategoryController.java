package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.rest.category;

import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.ViewCategoryExpensesResponse;
import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.AddExpenseToCategoryUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.dto.AddExpenseToCategoryCommand;
import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.dto.AddExpenseToCategoryResponse;
import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.ViewCategoryExpensesUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.rest.category.dto.AddExpenseToCategoryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final AddExpenseToCategoryUseCase addExpenseToCategoryUseCase;
    private final ViewCategoryExpensesUseCase viewCategoryExpensesUseCase;

    // TODO wiring
    public CategoryController(AddExpenseToCategoryUseCase addExpenseToCategoryUseCase, ViewCategoryExpensesUseCase viewCategoryExpensesUseCase) {
        this.addExpenseToCategoryUseCase = addExpenseToCategoryUseCase;
        this.viewCategoryExpensesUseCase = viewCategoryExpensesUseCase;
    }

    // TODO (Query instead of null)
    @GetMapping("/{categoryId}")
    public ResponseEntity<ViewCategoryExpensesResponse> getCategoryExpenses() {
        return ResponseEntity.ok(viewCategoryExpensesUseCase.execute(null));
    }

    @PostMapping("/{categoryId}")
    public ResponseEntity<AddExpenseToCategoryResponse> addExpense(@PathVariable UUID categoryId,
                                                                   @RequestBody AddExpenseToCategoryRequest request) {
        AddExpenseToCategoryCommand command = new AddExpenseToCategoryCommand(categoryId, request.amount(), request.description(), request.currency());
        return ResponseEntity.ok(addExpenseToCategoryUseCase.execute(command));
    }


}
