package ie.universityofgalway.group11.expensestracker.config;

public interface UseCase<I, O> {
    O execute(I input);
}
