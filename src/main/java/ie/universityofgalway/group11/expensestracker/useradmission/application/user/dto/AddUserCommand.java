package ie.universityofgalway.group11.expensestracker.useradmission.application.user.dto;

public record AddUserCommand(String firstName, String lastName, String email) {
    public String firstName() {
        return firstName;
    }
    public String lastName() {
        return lastName;
    }
    public String email() {
        return email;
    }
}
