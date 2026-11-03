package ie.universityofgalway.group11.expensestracker.budgeting.domain.category;

import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository {


    Optional<Category> findById(CategoryId categoryId);

    Category save(Category category);
    List<Category> findAllCategories();

    void deleteById(CategoryId id);

    List<Category> findByPeriod(YearMonth period);

    Optional<YearMonth> findLatestPeriod();

    void saveAll(List<Category> categories);

    boolean existsByName(CategoryName name);
}
