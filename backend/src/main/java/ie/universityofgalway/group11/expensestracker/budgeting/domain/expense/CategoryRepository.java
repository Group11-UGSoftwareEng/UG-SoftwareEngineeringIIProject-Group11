package ie.universityofgalway.group11.expensestracker.budgeting.domain.expense;

public interface CategoryRepository {
    boolean existsByNameIgnoreCaseAndUserId(String name, String userId);
    Category save(Category category, String userId);
}