package ie.universityofgalway.group11.expensestracker.budgeting.domain.category;

public class CategoryName {

    private final String name;

    private CategoryName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name is required");
        }

        this.name = name;
    }

    public static CategoryName of(String name) {
        return new CategoryName(name);
    }

    public String name() {
        return name;
    }
}
