package ie.universityofgalway.group11.expensestracker.useradmission.domain.user;

import java.util.HashSet;
import java.util.Set;

public class User {
    private final UserId id;
    private UserName name;
    private EmailAddress email;
    //private Description description;
    private final Set<Admission> admissions = new HashSet<>();

    // Private constructor, can not trust the caller always
    private User(UserId id, UserName name, EmailAddress email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public static User create(UserName name, EmailAddress email) {
        // TODO enforce invariants here
        return new User(UserId.random(), name, email);
    }

    public UserId id() {
        return id;
    }

    public UserName name() {
        return name;
    }

    public EmailAddress email() {
        return email;
    }




}
