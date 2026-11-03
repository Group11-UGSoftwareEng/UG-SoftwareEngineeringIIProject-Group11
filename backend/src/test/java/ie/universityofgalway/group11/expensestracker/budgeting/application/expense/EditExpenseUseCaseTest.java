package ie.universityofgalway.group11.expensestracker.budgeting.application.expense;

import ie.universityofgalway.group11.expensestracker.budgeting.application.common.exceptions.ExpenseNotFoundException;
import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.dto.EditExpenseCommand;
import ie.universityofgalway.group11.expensestracker.budgeting.application.expense.dto.EditExpenseResponse;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.Category;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryBudget;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryId;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryName;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryRepository;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Amount;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Currency;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Description;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseId;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.InMemoryBudgetingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class EditExpenseUseCaseTest {
    static class SingleCategoryRepository implements CategoryRepository {
        final Category category;
        SingleCategoryRepository(Category category) { 
            this.category = category; 
        }
        @Override 
        public Optional<Category> findById(CategoryId id) {
            return category.id().equals(id) ? Optional.of(category) : Optional.empty();
        }
        @Override 
        public List<Category> findAllCategories() { 
            return List.of(category); 
        }
        @Override 
        public void deleteById(CategoryId id) 
        { }
    }
    private InMemoryBudgetingRepository expenses;
    private EditExpenseUseCase useCase;
    private Expense expense;
    
    @BeforeEach
    void setUp() {
        Category food = Category.create(CategoryName.of("Food"), YearMonth.of(2026, 11), CategoryBudget.of(100.0));
        expenses = new InMemoryBudgetingRepository();
        useCase = new EditExpenseUseCase(expenses, new SingleCategoryRepository(food));
        expense = expenses.save(Expense.create(food.id(), Amount.of(10.00), Description.of("Lunch"), Currency.of("EUR")), food);
    }
    @Test
    void editsAndSavesTheExpense() {
        EditExpenseResponse response = useCase.execute(new EditExpenseCommand(expense.id().asUuid(), 12.50, LocalDate.of(2026, 10, 2), "Lunch with friends"));
        assertEquals(new BigDecimal("12.5"), response.amount());
        Expense saved = expenses.findById(expense.id()).orElseThrow();
        assertEquals(new BigDecimal("12.5"), saved.amount().value());
        assertEquals(LocalDate.of(2026, 10, 2), saved.date());
        assertEquals("Lunch with friends", saved.description().toString());
    }
    @Test
    void rejectsZeroOrNegativeAmountAndKeepsOldValues() {
        assertThrows(RuntimeException.class, () -> useCase.execute(new EditExpenseCommand(expense.id().asUuid(), 0.0, LocalDate.of(2026, 10, 2), "x")));
        assertThrows(RuntimeException.class, () -> useCase.execute(new EditExpenseCommand(expense.id().asUuid(), -5.0, LocalDate.of(2026, 10, 2), "x")));
        Expense unchanged = expenses.findById(expense.id()).orElseThrow();
        assertEquals(new BigDecimal("10.0"), unchanged.amount().value());
        assertEquals("Lunch", unchanged.description().toString());
    }
    @Test
    void rejectsMissingAmountOrDate() {
        assertThrows(RuntimeException.class, () -> useCase.execute(new EditExpenseCommand(expense.id().asUuid(), null, LocalDate.of(2026, 10, 2), "x")));
        assertThrows(RuntimeException.class, () -> useCase.execute(new EditExpenseCommand(expense.id().asUuid(), 5.0, null, "x")));
    }
    @Test
    void editingAMissingExpenseIsReported() {
        assertThrows(ExpenseNotFoundException.class, () -> useCase.execute(new EditExpenseCommand(ExpenseId.random().asUuid(), 5.0, LocalDate.of(2026, 10, 2), "x")));
    }
}