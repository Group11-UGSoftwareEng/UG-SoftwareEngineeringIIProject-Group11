package ie.universityofgalway.group11.expensestracker.useradmission.application.user.dto;

public record DeleteUserResponse(String userId) {
    public String response() {
        return userId;
    }
}

