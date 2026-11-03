
package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure;

import ie.universityofgalway.group11.expensestracker.budgeting.application.SpendingSummaryService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses/summary")
public class SpendingSummaryController {

    private final SpendingSummaryService service;

    public SpendingSummaryController(
            SpendingSummaryService service) {
        this.service = service;
    }

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
