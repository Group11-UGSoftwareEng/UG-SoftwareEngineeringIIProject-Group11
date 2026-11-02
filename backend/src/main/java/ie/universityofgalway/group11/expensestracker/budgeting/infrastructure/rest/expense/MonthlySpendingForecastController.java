
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

import ie.universityofgalway.group11.expensestracker.budgeting.application.MonthlySpendingForecastService;

@RestController
@RequestMapping("/api/expenses/forecast")
public class MonthlySpendingForecastController {

    private final MonthlySpendingForecastService service;

    public MonthlySpendingForecastController(
            MonthlySpendingForecastService service
    ) {
        this.service = service;
    }

    @GetMapping
    public MonthlySpendingForecastService.ForecastResult forecast(
            @RequestParam(defaultValue = "EUR") String currency,
            @RequestParam(required = false) LocalDate asOf
    ) {
        return service.forecast(
                asOf == null ? LocalDate.now() : asOf,
                currency
        );
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
