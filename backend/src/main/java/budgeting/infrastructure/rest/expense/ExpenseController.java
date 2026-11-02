
package budgeting.infrastructure.rest.expense;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.ViewAllExpensesUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.dto.ViewAllExpensesResponse;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.category.CategoryEntity;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.category.CategoryJPARepository;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.expense.ExpenseEntity;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.expense.ExpenseJPARepository;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.rest.expense.dto.ViewAllExpensesQuery;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ViewAllExpensesUseCase viewAllExpensesUseCase;
    private final ExpenseJPARepository expenseRepository;
    private final CategoryJPARepository categoryRepository;

    public ExpenseController(
            ViewAllExpensesUseCase viewAllExpensesUseCase,
            ExpenseJPARepository expenseRepository,
            CategoryJPARepository categoryRepository
    ) {
        this.viewAllExpensesUseCase = viewAllExpensesUseCase;
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
    }

    // Existing endpoint preserved.
    // GET /api/expenses
    @GetMapping(produces = "text/plain")
    public String viewExpenses() {

        ViewAllExpensesResponse response =
                viewAllExpensesUseCase.execute(
                        new ViewAllExpensesQuery()
                );

        List<Expense> expenses = response.expenses();

        if (expenses.isEmpty()) {
            return "No expenses have been added yet.";
        }

        StringBuilder result = new StringBuilder("Expenses\n\n");

        int count = 1;

        for (Expense expense : expenses) {
            result.append(count++)
                    .append(". ")
                    .append(expense)
                    .append("\n");
        }

        return result.toString();
    }

    // Response format for the React expense table.
    public record ExpenseDetails(
            UUID id,
            UUID categoryId,
            String categoryName,
            Double amount,
            String description,
            String currency,
            LocalDate date
    ) {}

    // Response format for the category dropdown.
    public record CategoryDetails(
            UUID id,
            String name
    ) {}

    // GET /api/expenses/categories
    @GetMapping(
            value = "/categories",
            produces = "application/json"
    )
    public List<CategoryDetails> listCategories() {

        return categoryRepository.findAll()
                .stream()
                .map(category -> new CategoryDetails(
                        category.getId(),
                        category.getName()
                ))
                .sorted(
                        Comparator.comparing(
                                CategoryDetails::name,
                                String.CASE_INSENSITIVE_ORDER
                        )
                )
                .toList();
    }

    // GET /api/expenses/list
    @GetMapping(
            value = "/list",
            produces = "application/json"
    )
    public List<ExpenseDetails> listExpenses() {

        Map<UUID, CategoryEntity> categories =
                categoryRepository.findAll()
                        .stream()
                        .collect(Collectors.toMap(
                                CategoryEntity::getId,
                                Function.identity(),
                                (first, second) -> first
                        ));

        return expenseRepository.findAll()
                .stream()
                .map(expense ->
                        toExpenseDetails(expense, categories)
                )
                .sorted(
                        Comparator.comparing(
                                ExpenseDetails::date,
                                Comparator.nullsLast(
                                        Comparator.reverseOrder()
                                )
                        )
                )
                .toList();
    }

    private ExpenseDetails toExpenseDetails(
            ExpenseEntity expense,
            Map<UUID, CategoryEntity> categories
    ) {

        UUID categoryId =
                expense.getCategory() == null
                        ? null
                        : expense.getCategory().getId();

        CategoryEntity category =
                categoryId == null
                        ? null
                        : categories.get(categoryId);

        String categoryName =
                category == null
                        ? "Uncategorized"
                        : category.getName();

        return new ExpenseDetails(
                expense.getId(),
                categoryId,
                categoryName,
                expense.getAmount(),
                expense.getDescription(),
                expense.getCurrency(),
                expense.getDate()
        );
    }
}
