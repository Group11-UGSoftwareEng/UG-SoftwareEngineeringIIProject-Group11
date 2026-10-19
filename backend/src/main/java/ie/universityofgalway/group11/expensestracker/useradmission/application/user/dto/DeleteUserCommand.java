package ie.universityofgalway.group11.expensestracker.useradmission.application.user.dto;

public record DeleteUserCommand(String userId) {
    public String userId(){
            return userId;
    }
}
