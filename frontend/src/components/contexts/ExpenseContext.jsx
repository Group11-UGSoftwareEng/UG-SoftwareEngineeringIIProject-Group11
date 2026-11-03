import { createContext, useContext, useState } from "react";

const ExpensesContext = createContext();

export function ExpensesProvider({ children }) {
  const [expenses, setExpenses] = useState([]);

  const addExpenseToContext = (expense) => {
    setExpenses((previous) => [
      ...previous.filter((item) => item.id !== expense.id),
      expense
    ]);
  };

  const setCategoryExpenses = (categoryId, categoryExpenses) => {
    setExpenses((previous) => [
      ...previous.filter((item) => item.categoryId !== categoryId),
      ...categoryExpenses
    ]);
  };

  return (
      <ExpensesContext.Provider
          value={{
            expenses,
            addExpenseToContext,
            setCategoryExpenses
          }}
      >
        {children}
      </ExpensesContext.Provider>
  );
}

export function useExpenses() {
  return useContext(ExpensesContext);
}