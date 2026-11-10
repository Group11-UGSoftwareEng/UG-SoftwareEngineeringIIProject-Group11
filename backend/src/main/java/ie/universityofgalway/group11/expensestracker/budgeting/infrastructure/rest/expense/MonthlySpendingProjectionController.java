
package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.rest.expense;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ie.universityofgalway.group11.expensestracker.budgeting.application.MonthlySpendingProjectionService;

@RestController
@RequestMapping("/api/expenses/forecast")
public class MonthlySpendingProjectionController {

    private final MonthlySpendingProjectionService service;

    public MonthlySpendingProjectionController(
            MonthlySpendingProjectionService service) {
        this.service = service;
    }

    @GetMapping
    public MonthlySpendingProjectionService.ForecastResult forecast(
            @RequestParam(defaultValue = "EUR")
            String currency) {

        return service.forecast(
                LocalDate.now(),
                currency
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> invalid(
            IllegalArgumentException exception) {

        return Map.of(
                "error",
                exception.getMessage() == null
                        ? "Invalid request"
                        : exception.getMessage()
        );
    }
}
