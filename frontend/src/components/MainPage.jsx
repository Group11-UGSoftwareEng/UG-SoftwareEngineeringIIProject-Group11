import { useEffect, useState } from "react";
import ExpenseForm from "./ExpenseForm";
import Expense from "./Expense";
import { useExpenses } from "./contexts/ExpenseContext";
import CategoryNameEditor from "./CategoryNameEditor";
import CategoryDeleteButton from "./CategoryDeleteButton";

import {
    getCategories,
    getCategoryExpenses,
    saveCategory
} from "../api/categoryApi";


export default function MainPage({onDataChanged}) {
  const { expenses, setCategoryExpenses, removeExpenseFromContext} = useExpenses();

  const [categories, setCategories] = useState([]);
  const [openCategoryIds, setOpenCategoryIds] = useState([]);
  const [formCategoryIds, setFormCategoryIds] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    let cancelled = false;

    async function loadData() {
      try {
        const savedCategories = await getCategories();

        const results = await Promise.all(
          savedCategories.map(async (category) => ({
            categoryId: category.id,
            expenses: await getCategoryExpenses(category.id)
          }))
        );

    const [categories, setCategories] = useState([]);
    const [openCategoryIds, setOpenCategoryIds] = useState([]);
    const [formCategoryIds, setFormCategoryIds] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const [newCategoryName, setNewCategoryName] = useState("");

    useEffect(() => {
        let cancelled = false;

        async function loadData() {
            try {
                const savedCategories = await getCategories();

                const results = await Promise.all(
                    savedCategories.map(async (category) => ({
                        categoryId: category.id,
                        expenses: await getCategoryExpenses(category.id)
                    }))
                );

                if (cancelled) return;

                setCategories(savedCategories);

                results.forEach((result) => {
                    setCategoryExpenses(result.categoryId, result.expenses);
                });
            } catch (loadError) {
                if (!cancelled) {
                    console.error(loadError);
                    setError("Could not load your expenses. Please try refreshing.");
  function toggleForm(categoryId) {
    setFormCategoryIds(previous =>
      previous.includes(categoryId)
        ? previous.filter(id => id !== categoryId)
        : [...previous, categoryId]
    );
  }

  function handleExpenseDeleted(expenseId) {
    removeExpenseFromContext(expenseId);

    if (onDataChanged) {
      onDataChanged();
    }
  }

  function handleCategoryDeleted(categoryId) {
    setCategories(previous =>
      previous.filter(item => item.id !== categoryId)
    );
    setOpenCategoryIds(previous =>
      previous.filter(id => id !== categoryId)
    );
    setFormCategoryIds(previous =>
      previous.filter(id => id !== categoryId)
    );

    if (onDataChanged) {
      onDataChanged();
    }
  }

  if (loading) {
    return <p>Loading expenses...</p>;
  }

  if (error) {
    return <p role="alert">{error}</p>;
  }

  return (
    <div>
      <h1>Expenses tracker</h1>

      {expenses.length === 0 && (
        <p>You have not added any expenses yet.</p>
      )}

      {categories.length === 0 && (
        <p>No categories added yet.</p>
      )}

      <div
        style={{
          display: "flex",
          flexWrap: "wrap",
          gap: "40px"
        }}
      >
        {categories.map(category => {
          const categoryExpenses = expenses
            .filter(
              expense =>
                expense.categoryId === category.id
            )
            .sort((a, b) =>
              (b.date ?? b.dateAdded ?? "")
                .localeCompare(
                  a.date ?? a.dateAdded ?? ""
                )
            );

          const isOpen = openCategoryIds.includes(
            category.id
          );

          const isFormOpen = formCategoryIds.includes(
            category.id
          );

          return (
            <section
              key={category.id}
              style={{ minWidth: "280px" }}
            >
              <CategoryNameEditor
                category={category}
                onRenamed={(categoryId, newName) => {
                  setCategories(previous =>
                    previous.map(item =>
                      item.id === categoryId
                        ? { ...item, name: newName }
                        : item
                    )
                  );
                }}
              />

              <CategoryDeleteButton
                category={category}
                onDeleted={handleCategoryDeleted}
              />

              <button
                onClick={() =>
                  toggleCategory(category.id)
                }
            } finally {
                if (!cancelled) {
                    setLoading(false);
                }
            }
        }

        loadData();

        return () => {
            cancelled = true;
        };
    }, []);

    function toggleCategory(categoryId) {
        setOpenCategoryIds((previous) =>
            previous.includes(categoryId)
                ? previous.filter((id) => id !== categoryId)
                : [...previous, categoryId]
        );
    }

    function toggleForm(categoryId) {
        setFormCategoryIds((previous) =>
            previous.includes(categoryId)
                ? previous.filter((id) => id !== categoryId)
                : [...previous, categoryId]
        );
    }

    async function handleAddCategory() {
        if (!newCategoryName.trim()) return;

        try {
            await saveCategory({ name: newCategoryName });
            const updatedCategories = await getCategories();
            setCategories(updatedCategories);

            setNewCategoryName("");
            setError("");
        } catch (err) {
            console.error("Failed to add category:", err);
            setError("Failed to add new category. Please try again.");
        }
    }

    if (loading) {
        return <p>Loading expenses...</p>;
    }

    if (error) {
        return <p role="alert">{error}</p>;
    }

    return (
        <div>
            <h1>Expenses tracker</h1>

            <div style={{ marginBottom: "20px" }}>
                <input
                    type="text"
                    placeholder="New category name"
                    value={newCategoryName}
                    onChange={(e) => setNewCategoryName(e.target.value)}
                />
                <button onClick={handleAddCategory} style={{ marginLeft: "10px" }}>
                    Add Category
                </button>
            </div>

            {expenses.length === 0 && (
                <p>You have not added any expenses yet.</p>
            )}

            {categories.length === 0 && <p>No categories added yet.</p>}

            <div style={{ display: "flex", flexWrap: "wrap", gap: "40px" }}>
                {categories.map((category) => {
                    const categoryExpenses = expenses
                        .filter((expense) => expense.categoryId === category.id)
                        .sort((a, b) =>
                            (b.date ?? b.dateAdded ?? "").localeCompare(
                                a.date ?? a.dateAdded ?? ""
                            )
                        );

                    const isOpen = openCategoryIds.includes(category.id);
                    const isFormOpen = formCategoryIds.includes(category.id);

                    return (
                        <section key={category.id} style={{ minWidth: "280px" }}>
                            <h2>{category.name}</h2>

                            <button
                                onClick={() => toggleCategory(category.id)}
                                aria-expanded={isOpen}
                            >
                                {isOpen ? "Hide expenses" : "View expenses"}
                            </button>

                            <button onClick={() => toggleForm(category.id)}>
                                {isFormOpen ? "Hide form" : "Add an expense"}
                            </button>

                            {isFormOpen && (
                                <ExpenseForm categoryId={category.id} />
                            )}

                            {isOpen && (
                                <div style={{ marginTop: "12px" }}>
                                    {categoryExpenses.length === 0 ? (
                                        <p>No expenses in this category yet.</p>
                                    ) : (
                                        categoryExpenses.map((expense) => (
                                            <Expense key={expense.id} {...expense} />
                                        ))
                                    )}
                                </div>
                            )}
                        </section>
                    );
                })}
            </div>
        </div>
    );
              )}

              {isOpen && (
                <div style={{ marginTop: "12px" }}>
                  {categoryExpenses.length === 0 ? (
                    <p>
                      No expenses in this category yet.
                    </p>
                  ) : (
                    categoryExpenses.map(expense => (
                      <Expense
                        key={expense.id}
                        {...expense}
                        onUpdated={async() => {
                          await refreshCategoryExpenses(
                            category.id
                          );

                          if (onDataChanged) {
                            onDataChanged();
                          }
                        }}
                        onDeleted={handleExpenseDeleted}
                      />
                    ))
                  )}
                </div>
              )}
            </section>
          );
        })}
      </div>

      <BudgetLimits />
    </div>
  );
}