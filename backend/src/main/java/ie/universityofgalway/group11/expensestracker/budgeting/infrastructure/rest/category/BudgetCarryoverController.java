
package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.rest.category;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ie.universityofgalway.group11.expensestracker.budgeting.application.BudgetCarryoverService;

@RestController
@RequestMapping("/api/budgets/carryover")
public class BudgetCarryoverController {

    private final BudgetCarryoverService service;

    public BudgetCarryoverController(
            BudgetCarryoverService service
    ) {
        this.service = service;
    }

    public record ConfigureBudgetRequest(
            UUID categoryId,
            String currency,
            String startMonth,
            BigDecimal monthlyBudget
    ) {}

    // Configure monthly budget.
    //
    // POST /api/budgets/carryover

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> configure(
            @RequestBody ConfigureBudgetRequest request
    ) {
        if (request == null) {
            throw new IllegalArgumentException(
                    "Budget request is required"
            );
        }

        service.configureBudget(
                request.categoryId(),
                request.currency(),
                YearMonth.parse(request.startMonth()),
                request.monthlyBudget()
        );

        return Map.of(
                "message",
                "Monthly budget configured successfully"
        );
    }

    // View monthly budget and carryover.
    //
    // GET /api/budgets/carryover/{categoryId}
    // ?month=2026-11&currency=EUR

    @GetMapping("/{categoryId}")
    public BudgetCarryoverService.BudgetReport getReport(
            @PathVariable UUID categoryId,
            @RequestParam String month,
            @RequestParam(defaultValue = "EUR")
            String currency
    ) {
        return service.getBudgetReport(
                categoryId,
                currency,
                YearMonth.parse(month)
        );
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            java.time.format.DateTimeParseException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> invalidRequest(Exception e) {
        return Map.of(
                "error",
                e.getMessage() == null
                        ? "Invalid carryover request"
                        : e.getMessage()
        );
    }
}
