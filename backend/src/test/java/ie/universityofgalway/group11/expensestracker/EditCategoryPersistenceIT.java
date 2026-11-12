package ie.universityofgalway.group11.expensestracker;

import ie.universityofgalway.group11.expensestracker.budgeting.application.category.EditCategoryDetailsUseCase;
import ie.universityofgalway.group11.expensestracker.budgeting.application.category.dto.EditCategoryDetailsCommand;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryRepository;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.category.CategoryEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class EditCategoryPersistenceIT {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void savesNewNameAndKeepsCategoryDetails() {
        UUID categoryId = UUID.randomUUID();
        String suffix = categoryId.toString().substring(0, 8);

        CategoryEntity category = new CategoryEntity();
        category.setId(categoryId);
        category.setName("Original " + suffix);
        category.setBudget(200.0);
        category.setYear(2026);
        category.setMonth(11);

        entityManager.persist(category);
        entityManager.flush();
        entityManager.clear();

        EditCategoryDetailsUseCase useCase =
                new EditCategoryDetailsUseCase(categoryRepository);

        String newName = "Updated " + suffix;

        useCase.execute(
                new EditCategoryDetailsCommand(categoryId, newName));

        entityManager.flush();
        entityManager.clear();

        CategoryEntity saved =
                entityManager.find(CategoryEntity.class, categoryId);

        assertNotNull(saved);
        assertEquals(newName, saved.getName());
        assertEquals(categoryId, saved.getId());
        assertEquals(Double.valueOf(200.0), saved.getBudget());
        assertEquals(Integer.valueOf(2026), saved.getYear());
        assertEquals(Integer.valueOf(11), saved.getMonth());
    }
}