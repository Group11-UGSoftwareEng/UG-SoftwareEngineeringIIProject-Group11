package ie.universityofgalway.group11.expensestracker.useradmission.domain.user;

import java.util.Objects;
import java.util.regex.Pattern;

public final class EmailAddress {

    private static final Pattern EMAIL_RE = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private final String value;

    private EmailAddress(String value) {
        this.value = value;
    }

    public static EmailAddress of(String email) {
        if (email == null || !EMAIL_RE.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email: " + email);
        }
        return new EmailAddress(email.toLowerCase());
    }
    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null) return false;

        if (this == o) return true;
        if (!(o instanceof EmailAddress)) return false;
        EmailAddress other = (EmailAddress)o;
        return value.equals(other.value);
    }
    @Override public int hashCode() {
        return Objects.hash(value);
    }

    @Override public String toString() {
        return value;
    }
}
