package ie.universityofgalway.group11.expensestracker;

import ie.universityofgalway.group11.expensestracker.budgeting.application.category.EditCategoryDetailsUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.EditCategoryDetailsCommand;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.*;
import org.junit.jupiter.api.Test;

import java.time.YearMonth;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EditCategoryDetailsUseCaseTest {

    @Test
    void updatesNameAndBudgetAndSavesCategory() {
        CategoryRepository repository = mock(CategoryRepository.class);
        UUID categoryId = UUID.randomUUID();
        CategoryId id = CategoryId.of(categoryId);

        Category category = Category.createWithId(
                id,
                CategoryName.of("Food"),
                YearMonth.of(2026, 11),
                CategoryBudget.of(200.0));

        when(repository.findById(id)).thenReturn(Optional.of(category));
        when(repository.save(category)).thenReturn(category);

        EditCategoryDetailsUseCase useCase =
                new EditCategoryDetailsUseCase(repository);

        Category result = useCase.execute(
                new EditCategoryDetailsCommand(categoryId, "Groceries", 300.0));

        assertSame(category, result);
        assertEquals("Groceries", result.name().name());
	assertEquals(0, java.math.BigDecimal.valueOf(300.0)
        	.compareTo(result.budget().value()));
        assertEquals(id, result.id());
        assertEquals(2026, result.year());
        assertEquals(11, result.month());

        verify(repository).save(category);
    }

    @Test
    void rejectsMissingCategoryWithoutSaving() {
        CategoryRepository repository = mock(CategoryRepository.class);
        UUID categoryId = UUID.randomUUID();

        when(repository.findById(CategoryId.of(categoryId)))
                .thenReturn(Optional.empty());

        EditCategoryDetailsUseCase useCase =
                new EditCategoryDetailsUseCase(repository);

        assertThrows(IllegalArgumentException.class, () ->
                useCase.execute(new EditCategoryDetailsCommand(
                        categoryId, "Groceries", 300.0)));

        verify(repository, never()).save(any(Category.class));
    }
}