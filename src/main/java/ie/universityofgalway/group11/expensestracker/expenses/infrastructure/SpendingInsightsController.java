
package ie.universityofgalway.group11.expensestracker.expenses.infrastructure;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ie.universityofgalway.group11.expensestracker.expenses.application.SpendingInsightsService;
import ie.universityofgalway.group11.expensestracker.useradmission.domain.user.UserId;

@RestController
@RequestMapping("/api/expense-management/users/{userId}/insights")
public class SpendingInsightsController {

    private final SpendingInsightsService service;

    public SpendingInsightsController(SpendingInsightsService service) {
        this.service = service;
    }

    // US1: Compare spending across two periods

    @GetMapping("/compare")
    public SpendingInsightsService.SpendingComparison compare(
            @PathVariable UUID userId,
            @RequestParam LocalDate previousFrom,
            @RequestParam LocalDate previousTo,
            @RequestParam LocalDate currentFrom,
            @RequestParam LocalDate currentTo) {

        return service.compare(
            UserId.of(userId),
            previousFrom,
            previousTo,
            currentFrom,
            currentTo
        );
    }

    // US2: Detect unusual spending patterns

    @GetMapping("/anomalies")
    public SpendingInsightsService.AnomalyReport anomalies(
            @PathVariable UUID userId,
            @RequestParam LocalDate historyFrom,
            @RequestParam LocalDate historyTo,
            @RequestParam LocalDate currentFrom,
            @RequestParam LocalDate currentTo,
            @RequestParam(defaultValue = "150")
                BigDecimal thresholdPercent) {

        return service.anomalies(
            UserId.of(userId),
            historyFrom,
            historyTo,
            currentFrom,
            currentTo,
            thresholdPercent
        );
    }

    // US3: Forecast end-of-period expenditure

    @GetMapping("/forecast")
    public SpendingInsightsService.Forecast forecast(
            @PathVariable UUID userId,
            @RequestParam LocalDate from,
            @RequestParam LocalDate to,
            @RequestParam LocalDate asOf,
            @RequestParam(required = false)
                BigDecimal budget) {

        return service.forecast(
            UserId.of(userId),
            from,
            to,
            asOf,
            budget
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(
        org.springframework.http.HttpStatus.BAD_REQUEST
    )
    public Map<String, String> invalid(
            IllegalArgumentException error) {

        return Map.of("error", error.getMessage());
    }
}
