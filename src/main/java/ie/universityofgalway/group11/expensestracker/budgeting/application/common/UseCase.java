package ie.universityofgalway.group11.expensestracker.budgeting.application.common;

public interface UseCase<I, O> {
    O execute(I input);
}
