package ie.universityofgalway.group11.expensestracker.useradmission.domain.user;

import java.util.UUID;

public interface UserRepository {
    void save(User user);
    void deleteById(UserId userId);
}
