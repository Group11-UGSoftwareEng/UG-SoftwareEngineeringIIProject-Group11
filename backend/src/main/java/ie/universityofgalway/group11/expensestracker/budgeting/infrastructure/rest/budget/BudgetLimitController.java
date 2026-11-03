
package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.rest.budget;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ie.universityofgalway.group11.expensestracker.budgeting.application.BudgetLimitService;

@RestController
@RequestMapping("/api/budgets/monthly")
public class BudgetLimitController {

    private final BudgetLimitService service;

    public BudgetLimitController(
            BudgetLimitService service) {
        this.service = service;
    }

    @GetMapping
    public BudgetLimitService.MonthlyBudgetResponse getBudgets(
            @RequestParam(defaultValue = "EUR")
            String currency) {

        return service.getMonthlyBudgets(currency);
    }

    @PutMapping("/overall")
    public BudgetLimitService.MonthlyBudgetResponse saveOverall(
            @RequestBody BudgetLimitService.SaveBudgetRequest request) {

        return service.saveOverall(request);
    }

    @PutMapping("/categories/{categoryId}")
    public BudgetLimitService.MonthlyBudgetResponse saveCategory(
            @PathVariable UUID categoryId,
            @RequestBody BudgetLimitService.SaveBudgetRequest request) {

        return service.saveCategory(categoryId, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> invalid(
            IllegalArgumentException exception) {

        return Map.of("error", exception.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> notFound(
            NoSuchElementException exception) {

        return Map.of("error", exception.getMessage());
    }
}
