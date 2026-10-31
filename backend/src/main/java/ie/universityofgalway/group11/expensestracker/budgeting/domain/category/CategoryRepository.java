package ie.universityofgalway.group11.expensestracker.budgeting.domain.category;

import java.util.Optional;

public interface CategoryRepository {

    Optional<Category> findById(CategoryId categoryId);

    Category save(Category category);
}