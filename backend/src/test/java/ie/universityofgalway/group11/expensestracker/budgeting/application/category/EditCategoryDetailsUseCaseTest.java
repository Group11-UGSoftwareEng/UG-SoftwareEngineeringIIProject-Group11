package ie.universityofgalway.group11.expensestracker.budgeting.application.category;

import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.EditCategoryDetailsCommand;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.Category;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryName;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.InMemoryBudgetingRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryBudget;
import java.time.YearMonth;

class EditCategoryDetailsUseCaseTest {

    @Test
    void changesNameAndKeepsSameId() {
        InMemoryBudgetingRepository repository =
                new InMemoryBudgetingRepository();

        Category category = Category.create(
                CategoryName.of("Food"),
                YearMonth.of(2026, 10),
                CategoryBudget.of(200.0));
        repository.save(category);

        EditCategoryDetailsUseCase useCase =
                new EditCategoryDetailsUseCase(repository);

        EditCategoryDetailsCommand command =
                new EditCategoryDetailsCommand(
                        category.id().asUuid(), "Groceries");

        useCase.execute(command);

        Category saved = repository.findById(category.id()).orElseThrow();

        assertEquals("Groceries", saved.name().name());
        assertEquals(category.id(), saved.id());
    }
    @Test
    void rejectsBlankNameAndKeepsOldName() {
        InMemoryBudgetingRepository repository =
                new InMemoryBudgetingRepository();

        Category category = Category.create(
                CategoryName.of("Food"),
                YearMonth.of(2026, 10),
                CategoryBudget.of(200.0));
        repository.save(category);

        EditCategoryDetailsUseCase useCase =
                new EditCategoryDetailsUseCase(repository);

        assertThrows(IllegalArgumentException.class, () ->
                useCase.execute(new EditCategoryDetailsCommand(
                        category.id().asUuid(), "   ")));

        Category saved = repository.findById(category.id()).orElseThrow();
        assertEquals("Food", saved.name().name());
    }
    @Test
    void rejectsCategoryThatDoesNotExist() {
        InMemoryBudgetingRepository repository =
                new InMemoryBudgetingRepository();

        EditCategoryDetailsUseCase useCase =
                new EditCategoryDetailsUseCase(repository);

        EditCategoryDetailsCommand command =
                new EditCategoryDetailsCommand(
                        java.util.UUID.randomUUID(), "Transport");

        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(command));

        assertEquals("Category not found.", error.getMessage());
    }
}