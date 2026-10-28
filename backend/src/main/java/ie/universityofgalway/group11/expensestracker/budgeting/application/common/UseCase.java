package ie.universityofgalway.group11.expensestracker.budgeting.application.common;

import ie.universityofgalway.group11.expensestracker.budgeting.application.common.exceptions.CategoryException;

public interface UseCase<I, O> {
    O execute(I input) throws CategoryException;
}
