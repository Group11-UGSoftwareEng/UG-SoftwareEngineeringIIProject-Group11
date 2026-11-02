package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CategoryJPARepository extends JpaRepository<CategoryEntity, UUID> {
    Optional<CategoryEntity> findById(UUID id);



}