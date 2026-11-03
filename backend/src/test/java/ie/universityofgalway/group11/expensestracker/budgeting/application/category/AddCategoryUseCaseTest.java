
package ie.universityofgalway.group11.expensestracker.budgeting.application.category;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.AddCategoryCommand;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.AddCategoryResponse;
import ie.universityofgalway.group11.expensestracker.budgeting.application.common.exceptions.CategoryException;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.Category;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryId;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryName;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryRepository;

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
        public Category save(Category category) {
            saved.removeIf(c -> c.id().equals(category.id()));
            saved.add(category);
            return category;
        }

        @Override
        public List<Category> findAllCategories() {
            return new ArrayList<>(saved);
        }

        @Override
        public void deleteById(CategoryId id) {
            saved.removeIf(c -> c.id().equals(id));
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
