package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
public interface CategoryJPARepository extends JpaRepository<CategoryEntity, UUID> {
    Optional<CategoryEntity> findById(UUID id);
    List<CategoryEntity> findByYearAndMonth(Integer year, Integer month);

    Optional<CategoryEntity> findFirstByOrderByYearDescMonthDesc();

    boolean existsByNameIgnoreCase(String name);

    boolean existsByName(String name);
}