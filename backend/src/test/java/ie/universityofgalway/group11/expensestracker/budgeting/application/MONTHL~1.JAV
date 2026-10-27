
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

    public MonthlySpendingForecastController(MonthlySpendingForecastService service) {
        this.service = service;
    }

    @GetMapping
    public MonthlySpendingForecastService.ForecastResult forecast(
            @RequestParam(required = false) LocalDate asOf,
            @RequestParam(defaultValue = "3") int historyMonths,
            @RequestParam(defaultValue = "EUR") String currency) {
        return service.forecast(asOf == null ? LocalDate.now() : asOf,
                historyMonths, currency);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> invalid(IllegalArgumentException e) {
        return Map.of("error", e.getMessage() == null ? "Invalid forecast request" : e.getMessage());
    }
}
