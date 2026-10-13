package ie.universityofgalway.group11.expensestracker.budgeting.application;

import ie.universityofgalway.group11.expensestracker.budgeting.application.common.UseCase;

public class SetCategoryBudgetUseCase implements UseCase<SetCategoryBudgetCommand, SetCategoryBudgetResponse> {

    // I will inject your BudgetRepository here later once the database is set up
    public SetCategoryBudgetUseCase() {
    }

    @Override
    public SetCategoryBudgetResponse execute(SetCategoryBudgetCommand input) {
        
        // TODO: Call the repository to save the budget threshold to the database
        // budgetRepository.save(input.userId(), input.categoryName(), input.limit());

        return new SetCategoryBudgetResponse(
            true,
            "Budget limit of " + input.limit() + " successfully set for category: " + input.categoryName()
        );
    }
}