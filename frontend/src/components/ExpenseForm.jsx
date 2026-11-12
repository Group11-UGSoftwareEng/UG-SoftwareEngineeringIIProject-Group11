import React, { useState } from 'react';
import { saveExpense } from "../api/categoryApi";
import { useExpenses } from "./contexts/ExpenseContext";

/**
 * Form used to create a new expense for the selected category.
 * It collects the amount, description, and currency, then sends the data to the backend.
 *
 * @param {Object} props
 * @param {string} props.categoryId identifier of the category the expense belongs to
 */
export default function ExpenseForm({ categoryId }) {

  // Local form state for the user-entered values.
  const [amount, setAmount] = useState("");
  const [description, setDescription] = useState("");
  const [currency, setCurrency] = useState("EUR");
  const { addExpenseToContext } = useExpenses();

  /**
   * Validates the input values, saves the new expense via the API, and updates the local expense list.
   */
  async function handleAddExpense() {

    // Build the payload expected by the backend and ensure blank descriptions are normalized.
    const newExpense = {
      categoryId: categoryId,
      date: new Date().toLocaleDateString("en-CA"),
      amount: parseFloat(amount),
      description: description.trim() || "No description provided", // "" is treated as false
      currency: currency
    };

    try {
      const savedExpense = await saveExpense(categoryId, newExpense);

      addExpenseToContext({
        ...(savedExpense ?? {})
        //TODO remove this and make savedExpense return correct one and use this instead: ...(savedExpense ?? {}),
      });

      setAmount("");
      setDescription("");
    } catch (error) {
      console.error("Error adding expense:", error);
      alert("There are issues with the entered data Or backend is not working properly. Please check and try again.");
    }
  }

  return (
    <div>
      <form onSubmit={(e) => {
        e.preventDefault();
        handleAddExpense();
      }}>
        <input
          type="text"
          placeholder="Description"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
        />
        <input
          type="Number"
          placeholder="0.00"
          min="0.01"
          step="0.01"
          value={amount}
          onChange={(e) => setAmount(e.target.value)}
          required
        />
        <select
          value={currency}
          onChange={(e) => setCurrency(e.target.value)}
          required
        >
          <option value="EUR">EUR (€)</option>
          <option value="USD">USD ($)</option>
          <option value="GBP">GBP (£)</option>
        </select>

        <button type="submit">Add</button>
      </form>
    </div>
  );
};