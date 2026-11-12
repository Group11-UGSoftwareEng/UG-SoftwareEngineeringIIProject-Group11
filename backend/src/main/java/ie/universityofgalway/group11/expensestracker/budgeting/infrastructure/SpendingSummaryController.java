
package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure;

import ie.universityofgalway.group11.expensestracker.budgeting.application.SpendingSummaryService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

/**
 * REST controller for expense summary queries across an optional date range.
 * This endpoint aggregates spending information into a single summary payload for the UI.
 */
@RestController
@RequestMapping("/api/expenses/summary")
public class SpendingSummaryController {

    private final SpendingSummaryService service;

    /**
     * Creates the controller with the summary service dependency.
     *
     * @param service service that calculates aggregated spending data
     */
    public SpendingSummaryController(
            SpendingSummaryService service) {
        this.service = service;
    }

    /**
     * Returns a summary of expenses for the requested date range and currency.
     *
     * @param from optional start date filter
     * @param to optional end date filter
     * @param currency currency used for aggregated display values
     * @return summary payload containing total spending and related values
     */
    @GetMapping
    public SpendingSummaryService.SpendingSummary getSummary(

            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate from,

            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate to,

            @RequestParam(defaultValue = "EUR")
            String currency
    ) {
        return service.summarize(from, to, currency);
    }

    /**
     * Converts invalid summary requests into a consistent bad-request response.
     *
     * @param exception input validation error
     * @return JSON map with an error message
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleBadRequest(
            IllegalArgumentException exception) {

        return Map.of(
                "error",
                exception.getMessage() == null
                        ? "Invalid summary request"
                        : exception.getMessage()
        );
    }
}
