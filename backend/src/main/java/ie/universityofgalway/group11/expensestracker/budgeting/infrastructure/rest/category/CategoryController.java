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
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.ViewCategoriesUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.ViewCategoriesCommand;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.ViewCategoriesResponse;
import java.util.UUID;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.AddCategoryUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.AddCategoryCommand;
import ie.universityofgalway.group11.expensestracker.budgeting.application.common.exceptions.CategoryException;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.rest.category.dto.AddCategoryRequest;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.DeleteCategoryUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.DeleteCategoryCommand;
import ie.universityofgalway.group11.expensestracker.budgeting.application.common.exceptions.CategoryInUseException;
import ie.universityofgalway.group11.expensestracker.budgeting.application.common.exceptions.CategoryNotFoundException;
import java.util.Map;

/**
 * REST controller for category management endpoints.
 * It exposes operations to create categories, update them, delete them, and inspect their expenses.
 */
@CrossOrigin
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final AddExpenseToCategoryUseCase addExpenseToCategoryUseCase;
    private final ViewCategoryExpensesUseCase viewCategoryExpensesUseCase;
    private final AddCategoryUseCase addCategoryUseCase;
    private final EditCategoryDetailsUseCase editCategoryDetailsUseCase;
    private final ViewCategoriesUseCase viewCategoriesUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;

    /**
     * Creates the controller and wires in the required use cases.
     *
     * @param addExpenseToCategoryUseCase service for adding expenses to categories
     * @param viewCategoryExpensesUseCase service for listing category expenses
     * @param deleteCategoryUseCase service for removing categories
     * @param editCategoryDetailsUseCase service for renaming categories
     * @param viewCategoriesUseCase service for listing categories
     * @param addCategoryUseCase service for creating categories
     */
    public CategoryController(
            AddExpenseToCategoryUseCase addExpenseToCategoryUseCase,
            ViewCategoryExpensesUseCase viewCategoryExpensesUseCase,
            DeleteCategoryUseCase deleteCategoryUseCase,
            EditCategoryDetailsUseCase editCategoryDetailsUseCase,
            ViewCategoriesUseCase viewCategoriesUseCase,
            AddCategoryUseCase addCategoryUseCase) {

        this.addExpenseToCategoryUseCase = addExpenseToCategoryUseCase;
        this.viewCategoryExpensesUseCase = viewCategoryExpensesUseCase;
        this.editCategoryDetailsUseCase = editCategoryDetailsUseCase;
        this.viewCategoriesUseCase = viewCategoriesUseCase;
        this.deleteCategoryUseCase = deleteCategoryUseCase;
        this.addCategoryUseCase = addCategoryUseCase;
    }

    /**
     * Lists all categories currently tracked by the application.
     *
     * @return category list response
     */
    @GetMapping
    public ResponseEntity<ViewCategoriesResponse> getCategories() {
        ViewCategoriesCommand command = new ViewCategoriesCommand();

        return ResponseEntity.ok(viewCategoriesUseCase.execute(command));
    }

    /**
     * Fetches the expenses of a specific category.
     *
     * @param categoryId identifier of the category
     * @return category expense payload or a 404 error
     */
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

    /**
     * Adds a new expense to the selected category.
     *
     * @param categoryId target category identifier
     * @param request incoming request payload with amount, description, and currency
     * @return created expense response
     */
    @PostMapping("/{categoryId}")
    public ResponseEntity<AddExpenseToCategoryResponse> addExpense(@PathVariable UUID categoryId,
                                                                   @RequestBody AddExpenseToCategoryRequest request) {
        AddExpenseToCategoryCommand command = new AddExpenseToCategoryCommand(categoryId, request.amount(), request.description(), request.currency());
        return ResponseEntity.ok(addExpenseToCategoryUseCase.execute(command));
    }

    /**
     * Deletes a category and returns a domain-specific response if the category is still in use.
     *
     * @param categoryId identifier of the category to remove
     * @return success response or structured error payload
     */
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable("categoryId") UUID categoryId) {
        try {
            return ResponseEntity.ok(deleteCategoryUseCase.execute(new DeleteCategoryCommand(categoryId)));
        }
        catch (CategoryNotFoundException error) {
            return ResponseEntity.status(404).body(error.getMessage());
        }
        catch (CategoryInUseException error) {
            return ResponseEntity.status(409).body(Map.of(
                    "message", error.getMessage(),
                    "expenseCount", error.expenseCount()));
        }
    }

    /**
     * Updates a category name.
     *
     * @param categoryId category to rename
     * @param request incoming payload with the new category name
     * @return confirmation message
     */
    @PutMapping("/{categoryId}")
    public ResponseEntity<String> editCategory(
            @PathVariable("categoryId") UUID categoryId,
            @RequestBody EditCategoryDetailsRequest request) {

        EditCategoryDetailsCommand command =
                new EditCategoryDetailsCommand(categoryId, request.newName());

        editCategoryDetailsUseCase.execute(command);

        return ResponseEntity.ok("Category name updated.");
    }

    /**
     * Converts validation errors into HTTP 400 responses.
     *
     * @param error validation exception
     * @return error payload
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleInvalidInput(IllegalArgumentException error) {
        return ResponseEntity.badRequest().body(Map.of("message", error.getMessage()));
    }

    /**
     * Creates a new category.
     *
     * @param request category creation payload
     * @return created category or an error payload
     */
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
