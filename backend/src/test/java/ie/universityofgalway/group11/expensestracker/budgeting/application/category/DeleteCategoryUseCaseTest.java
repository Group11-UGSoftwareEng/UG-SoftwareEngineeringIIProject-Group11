
package ie.universityofgalway.group11.expensestracker.budgeting.application.category;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.DeleteCategoryCommand;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.DeleteCategoryResponse;
import ie.universityofgalway.group11.expensestracker.budgeting.application.common.exceptions.CategoryInUseException;
import ie.universityofgalway.group11.expensestracker.budgeting.application.common.exceptions.CategoryNotFoundException;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.Category;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryBudget;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryId;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryName;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryRepository;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Amount;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Currency;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Description;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.InMemoryBudgetingRepository;

class DeleteCategoryUseCaseTest {
    static class FakeCategoryRepository implements CategoryRepository {
        final Map<CategoryId, Category> store = new HashMap<>();
        void add(Category category) { 
            store.put(category.id(), category); 
        }

        @Override 
        public Optional<Category> findById(CategoryId id) { 
            return Optional.ofNullable(store.get(id)); 
        }
        @Override 
        public List<Category> findAllCategories() { 
            return new ArrayList<>(store.values()); 
        }
        @Override 
        public void deleteById(CategoryId id) { 
            store.remove(id); 
        }

        @Override
        public Category save(Category category) {
            store.put(category.id(), category);
            return category;
        }

        @Override
        public boolean existsByName(CategoryName name) {
            return store.values().stream().anyMatch(c ->
                    c.name().name().equalsIgnoreCase(name.name()));
        }

        @Override
        public List<Category> findByPeriod(YearMonth period) {
            return findAllCategories().stream()
                    .filter(c -> c.year() == period.getYear() && c.month() == period.getMonthValue())
                    .toList();
        }

        @Override
        public Optional<YearMonth> findLatestPeriod() {
            return findAllCategories().stream()
                    .map(c -> YearMonth.of(c.year(), c.month()))
                    .max(YearMonth::compareTo);
        }

        @Override
        public void saveAll(List<Category> categories) {
            categories.forEach(this::save);
        }

    }

    private FakeCategoryRepository categories;
    private InMemoryBudgetingRepository expenses;
    private DeleteCategoryUseCase useCase;
    private Category food;

    private Category newCategory(String name) {
        Category category = Category.create(CategoryName.of(name), YearMonth.of(2026, 11), CategoryBudget.of(100.0));
        categories.add(category);
        return category;
    }

    private void addExpenseTo(Category category) {
        expenses.save(Expense.create(
                category.id(),
                Amount.of(10.00),
                Description.of("Test expense"),
                Currency.of("EUR")), category);
    }

    @BeforeEach
    void setUp() {
        categories = new FakeCategoryRepository();
        expenses = new InMemoryBudgetingRepository();
        useCase = new DeleteCategoryUseCase(categories, expenses);
        food = newCategory("Food");
    }

    @Test
    void deletesACategoryWithNoExpenses() {
        DeleteCategoryResponse response = useCase.execute(new DeleteCategoryCommand(food.id().asUuid()));
        assertTrue(response.success());
        assertTrue(categories.findById(food.id()).isEmpty());
    }

    @Test
    void blocksDeletionWhenExpensesExistAndReportsTheCount() {
        addExpenseTo(food);
        addExpenseTo(food);
        CategoryInUseException e = null;
        try {
            useCase.execute(new DeleteCategoryCommand(food.id().asUuid()));
        } 
        catch (CategoryInUseException thrown) {
            e = thrown;
        }
        assertNotNull(e);
        assertEquals(2L, e.expenseCount());
        assertTrue(categories.findById(food.id()).isPresent());
        assertEquals(2, expenses.findAll().size());
    }

    @Test
    void expensesInOtherCategoriesDoNotBlockDeletion() {
        Category transport = newCategory("Transport");
        addExpenseTo(transport);
        DeleteCategoryResponse response = useCase.execute(new DeleteCategoryCommand(food.id().asUuid()));
        assertTrue(response.success());
        assertTrue(categories.findById(transport.id()).isPresent());
    }

    @Test
    void deletingAMissingCategoryIsReported() {
        assertThrows(CategoryNotFoundException.class, () -> useCase.execute(new DeleteCategoryCommand(CategoryId.random().asUuid())));
    }
}
