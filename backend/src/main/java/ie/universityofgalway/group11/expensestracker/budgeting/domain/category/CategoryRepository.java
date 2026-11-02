package ie.universityofgalway.group11.expensestracker.budgeting.domain.category;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository {
    Optional<Category> findById(CategoryId categoryId);
    List<Category> findAllCategories();
    void deleteById(CategoryId id);
}
