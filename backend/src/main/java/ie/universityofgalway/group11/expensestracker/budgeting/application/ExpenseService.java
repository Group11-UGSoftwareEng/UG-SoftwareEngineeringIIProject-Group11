package ie.universityofgalway.group11.expensestracker.budgeting.application;

import ie.universityofgalway.group11.expensestracker.budgeting.application.common.exceptions.ExpenseNotFoundException;
import ie.universityofgalway.group11.expensestracker.budgeting.application.common.exceptions.NotExpenseOwnerException;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Amount;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Category;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseId;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

//@Service
//public class ExpenseService {
//
//    private final ExpenseRepository expenseRepository;
//
//    public ExpenseService(ExpenseRepository expenseRepository) {
//        this.expenseRepository = expenseRepository;
//    }
//
//    /** US4 AC1: edit one of my own expenses and save the changes. */
//    public Expense editExpense(ExpenseId id, UserId requester,
//                               Amount amount, LocalDate date, Category category) {
//        Expense expense = findOwnedExpense(id, requester);
//        expense.update(amount, date, category);
//        return expenseRepository.save(expense);
//    }
//
//    /** US4 AC2: delete one of my own expenses. */
//    public void deleteExpense(ExpenseId id, UserId requester) {
//        findOwnedExpense(id, requester);
//        expenseRepository.deleteById(id);
//    }
//
//    private Expense findOwnedExpense(ExpenseId id, UserId requester) {
//        Expense expense = expenseRepository.findById(id)
//                .orElseThrow(() -> new ExpenseNotFoundException(id));
//        if (!expense.isOwnedBy(requester)) {
//            throw new NotExpenseOwnerException();
//        }
//        return expense;
//    }
//}
