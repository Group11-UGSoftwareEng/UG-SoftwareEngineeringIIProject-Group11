
package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure;

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

    // =========================================================
    // 1. ORIGINAL EXPENSE ENDPOINT
    // GET /api/expenses
    //
    // Preserves the original plain-text response for Postman.
    // =========================================================

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

    // =========================================================
    // 2. JSON RESPONSE OBJECTS FOR REACT
    // =========================================================

    public record ExpenseDetails(
            UUID id,
            UUID categoryId,
            String categoryName,
            Double amount,
            String description,
            String currency,
            LocalDate date
    ) {}

    public record CategoryDetails(
            UUID id,
            String name
    ) {}

    // =========================================================
    // 3. CATEGORY LIST
    // GET /api/expenses/categories
    //
    // Used by the category dropdown in Expense.jsx.
    // =========================================================

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

    // =========================================================
    // 4. EXPENSE LIST AS JSON
    // GET /api/expenses/list
    //
    // Used by Expense.jsx to display saved records.
    // =========================================================

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

    // =========================================================
    // 5. DATABASE ENTITY TO JSON CONVERSION
    //
    // Gets descriptions, amounts, dates and categories
    // from the existing persisted expense records.
    // =========================================================

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
