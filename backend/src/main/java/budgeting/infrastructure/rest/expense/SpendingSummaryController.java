
package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.rest.expense;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
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

    /**
     * Example:
     * GET /api/expenses/summary
     *     ?from=2026-10-01
     *     &to=2026-10-31
     *     &currency=EUR
     */
    @GetMapping
    public SpendingSummaryService.SpendingSummary getSummary(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate from,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate to,

            @RequestParam(defaultValue = "EUR")
            String currency
    ) {
        return service.getSummary(
                from,
                to,
                currency
        );
    }

    // Return HTTP 400 for invalid service arguments.
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleInvalidRequest(
            IllegalArgumentException exception
    ) {
        return Map.of(
                "error",
                exception.getMessage() == null
                        ? "Invalid summary request"
                        : exception.getMessage()
        );
    }
}
