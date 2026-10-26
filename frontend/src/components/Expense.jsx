import React, { useState } from 'react';

const Expense = () => {

  const [expenses, setExpenses] = useState([]);

  const handleAddExpense = () => {

    const newExpense = {
      id: Date.now(),
      title: "New Expense",
      amount: 100,
      currency: "$"

    };

    setExpenses([...expenses, newExpense]);
  };

  return (
    <div>
      <button onClick={handleAddExpense}>Add Expense</button>


      <div>
        {expenses.map((expense) => (
          <div key={expense.id}>
            <form>
              <input
                type="text"
                value={expense.title}
                onChange={(e) => {
                  const updatedExpenses = expenses.map((e) =>
                    e.id === expense.id ? { ...e, title: e.target.value } : e
                  );
                  setExpenses(updatedExpenses);
                }}
              />
              <input
                type="number"
                value={expense.amount}
                onChange={(e) => {
                  const updatedExpenses = expenses.map((e) =>
                    e.id === expense.id ? { ...e, amount: parseFloat(e.target.value) || 0 } : e
                  );
                  setExpenses(updatedExpenses);
                }}
              />
              <input
                type="text"
                value={expense.description}
                onChange={(e) => {
                  const updatedExpenses = expenses.map((e) =>
                    e.id === expense.id ? { ...e, description: e.target.value } : e
                  );
                  setExpenses(updatedExpenses);
                }}
              />
            </form>
            <button onClick={() => setExpenses(expenses.filter((e) => e.id !== expense.id))}>
              Delete the expense
            </button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Expense;