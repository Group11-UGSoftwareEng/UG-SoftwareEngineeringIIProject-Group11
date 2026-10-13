package ie.universityofgalway.group11.expensestracker.budgeting.application.expense;

import ie.universityofgalway.group11.expensestracker.expenses.domain.expense.Amount;
import ie.universityofgalway.group11.expensestracker.expenses.domain.expense.Category;
import ie.universityofgalway.group11.expensestracker.expenses.domain.expense.Expense;
import ie.universityofgalway.group11.expensestracker.expenses.domain.expense.ExpenseId;
import ie.universityofgalway.group11.expensestracker.expenses.infrastructure.InMemoryExpenseRepository;
import ie.universityofgalway.group11.expensestracker.useradmission.domain.user.UserId;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ViewMyExpensesUseCaseTest {

    @Test
    void showsOnlyMyExpensesWithNewestFirst() {
        InMemoryExpenseRepository repository =
                new InMemoryExpenseRepository();

        UserId user1 = UserId.random();
        UserId user2 = UserId.random();

        Expense older = new Expense(
                ExpenseId.newId(), user1,
                new Amount(new BigDecimal("12.50")),
                LocalDate.of(2026, 10, 10),
                new Category("Food"));

        Expense newer = new Expense(
                ExpenseId.newId(), user1,
                new Amount(new BigDecimal("5.00")),
                LocalDate.of(2026, 10, 12),
                new Category("Transport"));

        Expense otherUserExpense = new Expense(
                ExpenseId.newId(), user2,
                new Amount(new BigDecimal("20.00")),
                LocalDate.of(2026, 10, 11),
                new Category("Shopping"));

        repository.save(older);
        repository.save(newer);
        repository.save(otherUserExpense);

        ViewMyExpensesUseCase useCase =
                new ViewMyExpensesUseCase(repository);

        List<Expense> result = useCase.execute(user1);

        // Only user1's expenses, newest first
        assertEquals(2, result.size());
        assertEquals(newer.getId(), result.get(0).getId());
        assertEquals(older.getId(), result.get(1).getId());
    }

    @Test
    void returnsEmptyListWhenUserHasNoExpenses() {
        InMemoryExpenseRepository repository =
                new InMemoryExpenseRepository();

        ViewMyExpensesUseCase useCase =
                new ViewMyExpensesUseCase(repository);

        assertTrue(useCase.execute(UserId.random()).isEmpty());
    }
}