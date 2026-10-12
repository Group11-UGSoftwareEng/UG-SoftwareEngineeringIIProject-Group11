package ie.universityofgalway.group11.expensestracker.useradmission.domain.user;

import java.util.Objects;
import java.util.UUID;

public class UserId {
    private final UUID value;

    private UserId(UUID value) {
        this.value = value;
    }

    public static UserId of(UUID id) {
        return new UserId(id);
    }

    public static UserId random() {
        return new UserId(UUID.randomUUID());
    }

    public UUID asUuid() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof UserId)) return false;
        UserId other = (UserId) obj;

        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
