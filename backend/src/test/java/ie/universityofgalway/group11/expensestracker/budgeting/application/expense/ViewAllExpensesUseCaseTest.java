package ie.universityofgalway.group11.expensestracker.budgeting.application.expense;

import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.InMemoryBudgetingRepository;
import org.junit.jupiter.api.Test;

class ViewAllExpensesUseCaseTest {

    @Test
    void showsExpensesWithNewestFirst() {
        InMemoryBudgetingRepository repository =
                new InMemoryBudgetingRepository();

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

        //ViewAllExpensesUseCase useCase =
        //        new ViewAllExpensesUseCase(repository);

        //List<Expense> result = useCase.execute(null);

        //assertEquals(2, result.size());
        //assertEquals(newer.getId(), result.get(0).getId());
        //  assertEquals(older.getId(), result.get(1).getId());
    //}

        // @Test
        // void returnsEmptyListWhenNoExpensesExist() {
        //    InMemoryExpenseRepository repository =
        //       new InMemoryExpenseRepository();

        // ViewAllExpensesUseCase useCase =
        //        new ViewAllExpensesUseCase(repository);
//
        // assertTrue(useCase.execute(null).isEmpty());
    }//
}