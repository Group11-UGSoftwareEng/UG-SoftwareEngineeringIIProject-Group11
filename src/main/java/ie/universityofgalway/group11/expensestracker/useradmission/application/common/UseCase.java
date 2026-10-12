package ie.universityofgalway.group11.expensestracker.useradmission.application.common;

public interface UseCase<I, O> {
    O execute(I input);
}
