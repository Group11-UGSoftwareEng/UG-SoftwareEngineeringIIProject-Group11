package ie.universityofgalway.group11.expensestracker.budgeting.domain.category;

/**
 * Value object for a category title.
 * It prevents blank or null names from entering the domain model.
 */
public class CategoryName {

    private final String name;

    private CategoryName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name is required");
        }

        this.name = name;
    }

    /**
     * Creates a validated category name.
     *
     * @param name raw name text
     * @return a valid domain value object
     */
    public static CategoryName of(String name) {
        return new CategoryName(name);
    }

    /**
     * @return the underlying category name text
     */
    public String name() {
        return name;
    }
}
