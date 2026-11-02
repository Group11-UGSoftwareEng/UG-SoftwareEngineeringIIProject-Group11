package ie.universityofgalway.group11.expensestracker.budgeting.application.expense;

import ie.universityofgalway.group11.expensestracker.budgeting.application.common.exceptions.ExpenseNotFoundException;
import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.dto.DeleteExpenseCommand;
import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.dto.DeleteExpenseResponse;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryId;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Amount;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Currency;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Description;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseId;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.InMemoryBudgetingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeleteExpenseUseCaseTest {
    private InMemoryBudgetingRepository repository;
    private DeleteExpenseUseCase useCase;
    private Expense expense;

    @BeforeEach
    void setUp() {
        repository = new InMemoryBudgetingRepository();
        useCase = new DeleteExpenseUseCase(repository);
        expense = repository.save(Expense.create(
                CategoryId.random(),
                Amount.of(10.00),
                Description.of("Lunch"),
                Currency.of("EUR")));
    }

    @Test
    void deletesAnExistingExpense() {
        DeleteExpenseResponse response = useCase.execute(new DeleteExpenseCommand(expense.id().asUuid()));
        assertTrue(response.success());
        assertTrue(repository.findById(expense.id()).isEmpty());
    }

    @Test
    void deletingAMissingExpenseIsReported() {
        assertThrows(ExpenseNotFoundException.class, () -> useCase.execute(new DeleteExpenseCommand(ExpenseId.random().asUuid())));
    }

    @Test
    void onlyTheChosenExpenseIsDeleted() {
        Expense other = repository.save(Expense.create(
                CategoryId.random(),
                Amount.of(4.00),
                Description.of("Bus"),
                Currency.of("EUR")));

        useCase.execute(new DeleteExpenseCommand(expense.id().asUuid()));
        assertTrue(repository.findById(other.id()).isPresent());
    }
}