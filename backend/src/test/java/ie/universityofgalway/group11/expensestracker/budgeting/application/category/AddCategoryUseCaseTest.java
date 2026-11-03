package ie.universityofgalway.group11.expensestracker.budgeting.application.category;

import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.AddCategoryCommand;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.AddCategoryResponse;
import ie.universityofgalway.group11.expensestracker.budgeting.application.common.exceptions.CategoryException;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.Category;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryId;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryName;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AddCategoryUseCaseTest {

    // Fake database: just a list in memory
    static class FakeCategoryRepository implements CategoryRepository {
        final List<Category> saved = new ArrayList<>();

        @Override
        public Optional<Category> findById(CategoryId id) {
            return saved.stream().filter(c -> c.id().equals(id)).findFirst();
        }

        @Override
        public boolean existsByName(CategoryName name) {
            return saved.stream().anyMatch(c ->
                    c.name().name().equalsIgnoreCase(name.name()));
        }

        @Override
        public void save(Category category) {
            saved.add(category);
        }
    }

    FakeCategoryRepository repo;
    AddCategoryUseCase useCase;

    @BeforeEach
    void setUp() {
        repo = new FakeCategoryRepository();
        useCase = new AddCategoryUseCase(repo);
    }

    @Test
    void addsNewCategory() {
        AddCategoryResponse response = useCase.execute(new AddCategoryCommand("Food"));

        assertTrue(response.success());
        assertEquals(1, repo.saved.size());
    }

    @Test
    void rejectsDuplicateTitleIgnoringCase() {
        useCase.execute(new AddCategoryCommand("Food"));

        assertThrows(CategoryException.class,
                () -> useCase.execute(new AddCategoryCommand("food")));
    }

    @Test
    void rejectsBlankName() {
        assertThrows(IllegalArgumentException.class,
                () -> useCase.execute(new AddCategoryCommand("  ")));
    }
}