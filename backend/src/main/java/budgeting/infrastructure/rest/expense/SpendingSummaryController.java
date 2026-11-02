
package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.rest.expense;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ie.universityofgalway.group11.expensestracker.budgeting.application.SpendingSummaryService;

@RestController
@RequestMapping("/api/expenses/summary")
public class SpendingSummaryController {

    private final SpendingSummaryService service;

    public SpendingSummaryController(
            SpendingSummaryService service
    ) {
        this.service = service;
    }
    
    // GET /api/expenses/summary
    // GET /api/expenses/summary?currency=EUR

    @GetMapping
    public SpendingSummaryService.SpendingSummary getSummary(
            @RequestParam(defaultValue = "EUR") String currency
    ) {
        return service.getCurrentMonthSummary(currency);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> invalid(
            IllegalArgumentException exception
    ) {
        return Map.of(
                "error",
                exception.getMessage() == null
                        ? "Invalid request"
                        : exception.getMessage()
        );
    }
}
