package ie.universityofgalway.group11.expensestracker.budgeting.application.expense;

import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryId;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Amount;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Currency;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Description;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.InMemoryBudgetingRepository;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.rest.expense.dto.ViewAllExpensesQuery;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ViewAllExpensesUseCaseTest {

    @Test
    void showsExpensesWithNewestFirst() {
        InMemoryBudgetingRepository repository =
                new InMemoryBudgetingRepository();

        CategoryId categoryId = CategoryId.random();

        Expense older = Expense.create(
                categoryId,
                Amount.of(12.50),
                Description.of("Older expense"),
                Currency.of("EUR")
        );

        older.update(
                Amount.of(12.50),
                Description.of("Older expense"),
                Currency.of("EUR")
        );

        Expense newer = Expense.create(
                categoryId,
                Amount.of(5.00),
                Description.of("Newer expense"),
                Currency.of("EUR")
        );

        newer.update(
                Amount.of(5.00),
                Description.of("Newer expense"),
                Currency.of("EUR")
        );

        repository.add(older);
        repository.add(newer);

        ViewAllExpensesUseCase useCase =
                new ViewAllExpensesUseCase(repository);

        ViewAllExpensesResponse response =
                useCase.execute(new ViewAllExpensesQuery());

        List<Expense> result = response.expenses();

        assertEquals(2, result.size());
        assertEquals(newer.id(), result.get(0).id());
        assertEquals(older.id(), result.get(1).id());
    }

    @Test
    void returnsEmptyListWhenNoExpensesExist() {
        InMemoryBudgetingRepository repository =
                new InMemoryBudgetingRepository();

        ViewAllExpensesUseCase useCase =
                new ViewAllExpensesUseCase(repository);

        assertTrue(
                useCase.execute(new ViewAllExpensesQuery())
                        .expenses()
                        .isEmpty()
        );
    }
}