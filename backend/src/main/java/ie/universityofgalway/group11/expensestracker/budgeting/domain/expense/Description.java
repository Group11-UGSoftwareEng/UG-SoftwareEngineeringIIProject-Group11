package ie.universityofgalway.group11.expensestracker.budgeting.domain.expense;

import java.util.Objects;

public class Description {

    private final String text;

    private Description(String text) {
        if (text == null || text.trim().isEmpty()) {
            text = "No description provided";
        }
        this.text = text;
    }

    public static Description of(String text) {
        return new Description(text);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Description)) return false;
        Description other = (Description)o;

        return text.equals(other.text);
    }
    @Override public int hashCode() {
        return Objects.hash(text);
    }

    @Override public String toString() {
        return text;
    }

}