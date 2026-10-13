package ie.universityofgalway.group11.expensestracker.expenses.domain.expense;

public record Category(String name) {

    public Category {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Category is required");
        }
        name = name.trim();
    }
}
