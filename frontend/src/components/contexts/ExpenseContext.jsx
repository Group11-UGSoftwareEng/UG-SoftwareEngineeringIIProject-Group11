import { createContext, useContext, useState } from "react";

const ExpensesContext = createContext();

export function ExpensesProvider({ children }) {
  const [expenses, setExpenses] = useState([]);

  const addExpenseToContext = (expense) => {
    setExpenses((previous) => [...previous, expense]);
  };

  return (
    <ExpensesContext.Provider value={{ expenses, addExpenseToContext }}>
      {children}
    </ExpensesContext.Provider>
  );
}

export function useExpenses() {
  return useContext(ExpensesContext);
}