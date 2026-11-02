import { useState } from "react";
import ExpenseForm from "./ExpenseForm";
import Expense from "./Expense";
import { useExpenses } from "./contexts/ExpenseContext";

// Currently just an example for demonstration purposes.


export default function MainPage() {

    
    const { expenses } = useExpenses();

    //TODO Sithum: 
        // Replace the placeholder 
        // with actual corresponding 
        // categoryId  
    const [openCategoryIds, setOpenCategoryIds] = useState([]);

    const toggleCategory = (categoryId) => {
        setOpenCategoryIds((currentIds) =>
        currentIds.includes(categoryId)
            ? currentIds.filter((id) => id !== categoryId)
            : [...currentIds, categoryId]
        );
    };

    const categories = [
        { id: "7f3a9c21-8d64-4e2b-a5f0-1c9e6b72d843", name: "Example Category 1" },
        { id: "3f6a1b2c-9d84-4e71-a6c3-5b8f02d9471a", name: "Example Category 2" }
    ];
 
    return (
    <div>
      <h1>Expenses tracker</h1>

      <div style={{ display: "flex", gap: "250px" }}>
        {categories.map((category) => (
          <div key={category.id}>
            <div style={{ display: "flex", gap: "30px" }}>
              <h2>{category.name}</h2>

              <button onClick={() => toggleCategory(category.id)}>
                {openCategoryIds.includes(category.id)
                  ? "Hide"
                  : "Add an expense"}
              </button>
            </div>

            {openCategoryIds.includes(category.id) && (
              <ExpenseForm categoryId={category.id} />
            )}

            {expenses.filter((e) => e.categoryId === category.id)
              .map((expense) => (
                <Expense
                key={expense.id}
                {...expense}
                />)
            )}
          </div>
        ))}
      </div>
    </div>
  );
}