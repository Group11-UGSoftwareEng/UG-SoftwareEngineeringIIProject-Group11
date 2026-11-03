package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure.persistance;

import ie.universityofgalway.group11.expensestracker.budgeting.domain.category.*;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Amount;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Currency;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Description;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseId;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseRepository;

import java.time.YearMonth;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/** Stub repository for the prototype; replace with a database-backed one later. */
public class InMemoryBudgetingRepository implements ExpenseRepository, CategoryRepository {

    private final Map<ExpenseId, Expense> expenseStore = new ConcurrentHashMap<>();
    private final Map<CategoryId, Category> categoryStore = new ConcurrentHashMap<>();

    public InMemoryBudgetingRepository() {
        CategoryId id1 = CategoryId.of(UUID.fromString(
                "7f3a9c21-8d64-4e2b-a5f0-1c9e6b72d843"));
        CategoryId id2 = CategoryId.of(UUID.fromString(
                "3f6a1b2c-9d84-4e71-a6c3-5b8f02d9471a"));

        Category category1 = Category.createWithId(
                id1, CategoryName.of("Example Category 1"),
                YearMonth.of(2026, 10), CategoryBudget.of(10.0));

        Category category2 = Category.createWithId(
                id2, CategoryName.of("Example Category 2"),
                YearMonth.of(2026, 10), CategoryBudget.of(1.0));

        categoryStore.put(id1, category1);
        categoryStore.put(id2, category2);

        Expense expense1 = Expense.create(
                id1, Amount.of(2.50),
                Description.of("Coffee"), Currency.of("EUR"));

        Expense expense2 = Expense.create(
                id1, Amount.of(4.00),
                Description.of("Lunch"), Currency.of("EUR"));

        Expense expense3 = Expense.create(
                id2, Amount.of(0.50),
                Description.of("Sample purchase"), Currency.of("EUR"));

        category1.addExpenseToTheCategory(expense1);
        category1.addExpenseToTheCategory(expense2);
        category2.addExpenseToTheCategory(expense3);

        expenseStore.put(expense1.id(), expense1);
        expenseStore.put(expense2.id(), expense2);
        expenseStore.put(expense3.id(), expense3);
    }

    // Expense Repository

    @Override
    public Optional<Expense> findById(ExpenseId id) {
        return Optional.ofNullable(expenseStore.get(id));
    }

    @Override
    public Expense save(Expense expense, Category toCategory) {
        return expenseStore.put(expense.id(), expense);
    }

    @Override
    public void deleteById(ExpenseId id) {
        expenseStore.remove(id);
    }

    @Override
    public List<Expense> findAll() {
        return new ArrayList<>(expenseStore.values());
    }

    @Override
    public List<Expense> findByCategoryId(CategoryId categoryId) {
        List<Expense> expenses = new ArrayList<>();

        for (Expense expense : expenseStore.values()) {
            if (expense.categoryId().equals(categoryId)) {
                expenses.add(expense);
            }
        }

        return expenses;
    }
    // Category Repository

    @Override
    public Optional<Category> findById(CategoryId categoryId) {
        // TODO: remove placeholders when possible to add categories. They match the frontend examples (UUIDs must match). they do not match the database samples.
        CategoryId id1 = CategoryId.of(UUID.fromString("7f3a9c21-8d64-4e2b-a5f0-1c9e6b72d843"));
        CategoryId id2 =  CategoryId.of(UUID.fromString("3f6a1b2c-9d84-4e71-a6c3-5b8f02d9471a"));
        categoryStore.putIfAbsent(id1,
                        Category.createWithId(id1, CategoryName.of("Example Category 1"), YearMonth.of(2026, 10), CategoryBudget.of(10.0)));
        categoryStore.putIfAbsent(id2,
                Category.createWithId(id2, CategoryName.of("Example Category 2"), YearMonth.of(2026, 10), CategoryBudget.of(1.0)));

        return Optional.ofNullable(categoryStore.get(categoryId));
    }

    @Override
    public boolean existsByName(CategoryName name) {
        return categoryStore.values().stream()
                .anyMatch(c -> c.name().name().equalsIgnoreCase(name.name()));
    }

    @Override
    public List<Category> findAllCategories() {
        return new ArrayList<>(categoryStore.values());
    }


    @Override
    public void deleteById(CategoryId id) {
        categoryStore.remove(id);
    }

    @Override
    public Category save(Category category) {
        categoryStore.put(category.id(), category);
        return category;
    }

    @Override
    public List<Category> findByPeriod(YearMonth period) {
        List<Category> categories = new ArrayList<>();

        for (Category category : categoryStore.values()) {
            if (category.year() == period.getYear()
                    && category.month() == period.getMonthValue()) {
                categories.add(category);
            }
        }

        return categories;
    }

    @Override
    public Optional<YearMonth> findLatestPeriod() {
        YearMonth latest = null;

        for (Category category : categoryStore.values()) {
            YearMonth period =
                    YearMonth.of(category.year(), category.month());

            if (latest == null || period.isAfter(latest)) {
                latest = period;
            }
        }

        return Optional.ofNullable(latest);
    }

    @Override
    public void saveAll(List<Category> categories) {
        for (Category category : categories) {
            categoryStore.put(category.id(), category);
        }
    }
}