package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.category;

import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.Category;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryId;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.CategoryRepository;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.AdapterHelper;
import ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance.expense.ExpenseEntity;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CategoryPersistanceAdapter implements CategoryRepository {

    private final CategoryJPARepository categoryJpaRepository;
    private final AdapterHelper helper;

    public CategoryPersistanceAdapter(CategoryJPARepository categoryJpaRepository) {
        this.categoryJpaRepository = categoryJpaRepository;
        this.helper = new AdapterHelper();

    }

    @Override
    public Optional<Category> findById(CategoryId categoryId) {
       return categoryJpaRepository.findById(categoryId.asUuid()).map(helper::toCategoryDomain);
    }
}

//
//@Repository
//public class StudentPersistenceAdapter implements StudentRepository {
//
//    private final StudentJPARepository jpaRepository;
//
//    public StudentPersistenceAdapter(StudentJPARepository jpaRepository) {
//        this.jpaRepository = jpaRepository;
//    }
//
//    @Override
//    public Optional<Student> findById(StudentId id) {
//        return jpaRepository.findById(id.asUuid())
//                .map(this::toDomain);
//    }
//
//    @Override
//    public List<Student> findAll() {
//        return jpaRepository.findAll().stream()
//                .map(this::toDomain)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public Optional<Student> findByEmail(EmailAddress email) {
//        return jpaRepository.findByEmail(email.value())
//                .map(this::toDomain);
//    }
//
//    @Override
//    public void save(Student student) {
//        jpaRepository.save(toEntity(student));
//    }
//
//    @Override
//    public void deleteById(StudentId id) {
//        jpaRepository.deleteById(id.asUuid());
//    }
//
