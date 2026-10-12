package ie.universityofgalway.group11.expensestracker.useradmission.infrastructure;

import ie.universityofgalway.group11.expensestracker.useradmission.domain.user.User;
import ie.universityofgalway.group11.expensestracker.useradmission.domain.user.UserRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository implements UserRepository {

    private final Map<UUID, User> store = new ConcurrentHashMap<>();

    @Override
    public void save(User User) {
        store.put(User.id().asUuid(), User);
    }
    
}
