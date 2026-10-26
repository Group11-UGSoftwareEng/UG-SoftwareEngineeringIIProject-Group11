package ie.universityofgalway.group11.expensestracker.budgeting.application.expense;

import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryId;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Amount;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseId;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.InMemoryExpenseRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ViewMyExpensesUseCaseTest {

    @Test
    void showsExpensesWithNewestFirst() {
        InMemoryExpenseRepository repository =
                new InMemoryExpenseRepository();

        // Expense older = new Expense(
        //        ExpenseId.random(),
        //       Amount.of(12.50),
        //        LocalDate.of(2026, 10, 10),
        //       CategoryId.random()
        //        );

        //  Expense newer = new Expense(
                //         ExpenseId.newId(),
                //         new Amount(new BigDecimal("5.00")),
                //       LocalDate.of(2026, 10, 12),
                //       new Category("Transport"));

        //repository.save(older);
        //repository.save(newer);

        //ViewMyExpensesUseCase useCase =
        //        new ViewMyExpensesUseCase(repository);

        //List<Expense> result = useCase.execute(null);

        //assertEquals(2, result.size());
        //assertEquals(newer.getId(), result.get(0).getId());
        //  assertEquals(older.getId(), result.get(1).getId());
    //}

        // @Test
        // void returnsEmptyListWhenNoExpensesExist() {
        //    InMemoryExpenseRepository repository =
        //       new InMemoryExpenseRepository();

        // ViewMyExpensesUseCase useCase =
        //        new ViewMyExpensesUseCase(repository);
//
        // assertTrue(useCase.execute(null).isEmpty());
    }//
}