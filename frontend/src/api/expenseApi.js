
import axios from "axios";

const BASE_URL = "/api/expenses";

/**
 * Edit an existing expense.
 *
 * The Spring Boot API supports editing:
 * - amount
 * - date
 * - description
 *
 * Currency and category remain unchanged.
 */
export const editExpense = async (
  expenseId,
  updatedExpense
) => {
  const response = await axios.put(
    `${BASE_URL}/${encodeURIComponent(expenseId)}`,
    {
      amount: updatedExpense.amount,
      date: updatedExpense.date,
      description: updatedExpense.description
    }
  );

  return response.data;
};

/**
 * Delete an existing expense.
 *
 * Included as an API helper for the existing
 * backend DELETE endpoint. This does not
 * automatically add a Delete button.
 */
export const deleteExpense = async (expenseId) => {
  const response = await axios.delete(
    `${BASE_URL}/${encodeURIComponent(expenseId)}`
  );

  return response.data;
};
