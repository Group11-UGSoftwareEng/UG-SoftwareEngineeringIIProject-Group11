
package budgeting.infrastructure.rest.expense;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/expenses/summary")
public class SpendingSummaryController {

    private final Object service;

    public SpendingSummaryController(
            ApplicationContext applicationContext
    ) {
        this.service = applicationContext.getBean("spendingSummaryService");
    }
    
    // GET /api/expenses/summary
    // GET /api/expenses/summary?currency=EUR

    @GetMapping
    public Object getSummary(
            @RequestParam(defaultValue = "EUR") String currency
    ) {
        try {
            return service.getClass()
                    .getMethod("getCurrentMonthSummary", String.class)
                    .invoke(service, currency);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException(
                    "Unable to retrieve the spending summary", exception
            );
        }
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
