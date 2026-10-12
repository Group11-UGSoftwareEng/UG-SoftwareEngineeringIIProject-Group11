package ie.universityofgalway.group11.expensestracker.useradmission.domain.user;

import java.util.Objects;

public class UserName {

    private final String firstName;
    private final String lastName;

    private UserName(String firstName, String lastName) {
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
    }

    public static UserName of(String firstName, String lastName) {
        if (firstName == null || firstName.isBlank())
            throw new IllegalArgumentException("First name required");
        if (lastName == null || lastName.isBlank())
            throw new IllegalArgumentException("Last name required");

        return new UserName(firstName, lastName);
    }

    public String fullName() {
        return firstName + " " + lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserName)) return false;
        UserName other = (UserName) o;

        return firstName.equals(other.firstName) && lastName.equals(other.lastName);
    }
    @Override public int hashCode() {
        return Objects.hash(firstName, lastName);
    }

    @Override public String toString() {
        return fullName();
    }

}
