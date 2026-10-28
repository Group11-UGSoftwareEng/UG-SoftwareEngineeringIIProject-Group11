package ie.universityofgalway.group11.expensestracker.budgeting.application.category;

import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.DeleteCategoryCommand;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.DeleteCategoryResponse;
import ie.universityofgalway.group11.expensestracker.budgeting.application.common.exceptions.CategoryInUseException;
import ie.universityofgalway.group11.expensestracker.budgeting.application.common.exceptions.CategoryNotFoundException;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.Category;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryId;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryName;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Amount;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Currency;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Description;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.InMemoryBudgetingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeleteCategoryUseCaseTest {
    private InMemoryBudgetingRepository repository;
    private DeleteCategoryUseCase useCase;
    private Category food;

    @BeforeEach
    void setUp() {
        repository = new InMemoryBudgetingRepository();
        useCase = new DeleteCategoryUseCase(repository, repository);
        food = repository.save(Category.create(CategoryName.of("Food")));
    }
    private void addExpenseTo(Category category) {
        repository.save(Expense.create(
                category.id(),
                Amount.of(10.00),
                Description.of("Test expense"),
                Currency.of("EUR")));
    }

    @Test
    void deletesACategoryWithNoExpenses() {
        DeleteCategoryResponse response = useCase.execute(new DeleteCategoryCommand(food.id().asUuid()));
        assertTrue(response.success());
        assertTrue(repository.findById(food.id()).isEmpty());
    }

    @Test
    void blocksDeletionWhenExpensesExistAndReportsTheCount() {
        addExpenseTo(food);
        addExpenseTo(food);
        CategoryInUseException e = assertThrowsInUse(food);
        assertEquals(2L, e.expenseCount());
        assertTrue(repository.findById(food.id()).isPresent());
        assertEquals(2, repository.findAll().size());
    }

    @Test
    void expensesInOtherCategoriesDoNotBlockDeletion() {
        Category transport = repository.save(Category.create(CategoryName.of("Transport")));
        addExpenseTo(transport);
        DeleteCategoryResponse response = useCase.execute(new DeleteCategoryCommand(food.id().asUuid()));
        assertTrue(response.success());
        assertTrue(repository.findById(transport.id()).isPresent());
    }

    @Test
    void deletingAMissingCategoryIsReported() {
        assertThrows(CategoryNotFoundException.class, () -> useCase.execute(new DeleteCategoryCommand(CategoryId.random().asUuid())));
    }

    private CategoryInUseException assertThrowsInUse(Category category) {
        try {
            useCase.execute(new DeleteCategoryCommand(category.id().asUuid()));
        } catch (CategoryInUseException e) {
            return e;
        }
        fail("Expected CategoryInUseException");
        return null;
    }
}