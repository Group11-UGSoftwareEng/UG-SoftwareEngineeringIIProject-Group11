package ie.universityofgalway.group11.expensestracker.budgeting.application.category;

import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.ViewCategoriesCommand;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.*;
import org.junit.jupiter.api.Test;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;

class ViewCategoriesUseCaseTest {

    @Test
    void returnsSavedCategoryDetails() {
        Category category = Category.create(
                CategoryName.of("Food"),
                YearMonth.of(2026, 11),
                CategoryBudget.of(200.0));

        ViewCategoriesUseCase useCase =
                new ViewCategoriesUseCase(
                        new ResetBudgetingUseCase(repositoryWith(List.of(category))),
                        YearMonth.of(2026, 11)
                );

        var response = useCase.execute(new ViewCategoriesCommand());

        assertEquals(1, response.categories().size());

        var result = response.categories().get(0);

        assertEquals(category.id().asUuid(), result.id());
        assertEquals("Food", result.name());
        assertEquals(category.budget().value(), result.budget());
        assertEquals(2026, result.year());
        assertEquals(11, result.month());
    }

    @Test
    void returnsEmptyListWhenNoCategoriesExist() {
        ViewCategoriesUseCase useCase =
                new ViewCategoriesUseCase(
                        new ResetBudgetingUseCase(repositoryWith(List.of())),
                        YearMonth.of(2026, 11)
                );

        var response = useCase.execute(new ViewCategoriesCommand());

        assertTrue(response.categories().isEmpty());
    }

    private CategoryRepository repositoryWith(List<Category> categories) {
        return new CategoryRepository() {
            @Override
            public void deleteById(CategoryId id) {
                throw new UnsupportedOperationException(
                        "Deleting is not needed in these view tests.");
            }
            @Override
            public Optional<Category> findById(CategoryId categoryId) {
                return categories.stream()
                        .filter(category -> category.id().equals(categoryId))
                        .findFirst();
            }

            @Override
            public Category save(Category category) {
                return null;
            }

            @Override
            public List<Category> findAllCategories() {
                return categories;
            }

            @Override
            public List<Category> findByPeriod(YearMonth period) {
                return categories.stream()
                        .filter(category ->
                                category.year() == period.getYear()
                                        && category.month() == period.getMonthValue())
                        .toList();
            }

            @Override
            public Optional<YearMonth> findLatestPeriod() {
                return categories.stream()
                        .map(category ->
                                YearMonth.of(category.year(), category.month()))
                        .max(YearMonth::compareTo);
            }

            @Override
            public void saveAll(List<Category> categories) {
                throw new UnsupportedOperationException(
                        "Saving is not needed in these view tests.");
            }

            @Override
            public boolean existsByName(CategoryName name) {
                return false;
            }
        };
    }
}