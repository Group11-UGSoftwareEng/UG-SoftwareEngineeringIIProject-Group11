package ie.universityofgalway.group11.expensestracker.budgeting.infrastructure;

import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Amount;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Category;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.Expense;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseId;
import ie.universityofgalway.group11.expensestracker.budgeting.domain.expense.ExpenseRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PostgresExpenseRepository implements ExpenseRepository {

    private final JdbcTemplate jdbcTemplate;

    public PostgresExpenseRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Expense save(Expense expense) {
        String sql = """
                INSERT INTO expenses (id, amount, expense_date, category)
                VALUES (?, ?, ?, ?)
                ON CONFLICT (id) DO UPDATE SET
                    amount = EXCLUDED.amount,
                    expense_date = EXCLUDED.expense_date,
                    category = EXCLUDED.category
                """;

        jdbcTemplate.update(sql,
                expense.getId().value(),
                expense.getAmount().value(),
                java.sql.Date.valueOf(expense.getDate()),
                expense.getCategory().name());

        return expense;
    }

    @Override
    public Optional<Expense> findById(ExpenseId id) {
        String sql = "SELECT * FROM expenses WHERE id = ?";

        List<Expense> expenses =
                jdbcTemplate.query(sql, this::readExpense, id.value());

        if (expenses.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(expenses.get(0));
    }

    @Override
    public List<Expense> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM expenses", this::readExpense);
    }

    @Override
    public void deleteById(ExpenseId id) {
        jdbcTemplate.update(
                "DELETE FROM expenses WHERE id = ?", id.value());
    }

    private Expense readExpense(ResultSet row, int rowNumber)
            throws SQLException {

        ExpenseId id = new ExpenseId(row.getObject("id", UUID.class));
        Amount amount = new Amount(row.getBigDecimal("amount"));
        Category category = new Category(row.getString("category"));

        return new Expense(
                id,
                amount,
                row.getDate("expense_date").toLocalDate(),
                category);
    }
}