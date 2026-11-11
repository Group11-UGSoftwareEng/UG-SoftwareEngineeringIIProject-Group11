import { useEffect, useState } from "react";

import ExpenseForm from "./ExpenseForm";
import Expense from "./Expense";
import CategoryNameEditor from "./CategoryNameEditor";
import CategoryDeleteButton from "./CategoryDeleteButton";
import BudgetLimits from "./BudgetLimits";
import { useExpenses } from "./contexts/ExpenseContext";

import {
  getCategories,
  getCategoryExpenses,
  saveCategory,
} from "../api/categoryApi";


/**
 * Loads saved categories and their expenses.
 * Displays category names, expense lists and expense forms.
 */
export default function MainPage({ onDataChanged }) {
  const {
    expenses,
    setCategoryExpenses,
    removeExpenseFromContext,
  } = useExpenses();

  const [categories, setCategories] = useState([]);
  const [openCategoryIds, setOpenCategoryIds] = useState([]);
  const [formCategoryIds, setFormCategoryIds] = useState([]);
  const [newCategoryName, setNewCategoryName] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    let cancelled = false;

    // Loads categories and their expenses, then updates the page if still mounted.
    async function loadData() {
      try {
        const savedCategories = await getCategories();

        const results = await Promise.all(
          savedCategories.map(async (category) => ({
            categoryId: category.id,
            expenses: await getCategoryExpenses(category.id),
          }))
        );

        if (cancelled) return;

        setCategories(savedCategories);
        results.forEach(({ categoryId, expenses: categoryExpenses }) => {
          setCategoryExpenses(categoryId, categoryExpenses);
        });
      } catch (loadError) {
        if (!cancelled) {
          console.error("Failed to load expenses:", loadError);
          setError("Could not load your expenses. Please try refreshing.");
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
    // Load once on mount; the context action may be recreated by its provider.
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);


  // Reload a category after an expense is edited.
  async function refreshCategoryExpenses(categoryId) {
    const latestExpenses = await getCategoryExpenses(
      categoryId
    );

    setCategoryExpenses(categoryId, latestExpenses);
  }

  // Shows or hides the selected category's expenses.
  function toggleCategory(categoryId) {
    setOpenCategoryIds((previous) =>
      previous.includes(categoryId)
        ? previous.filter((id) => id !== categoryId)
        : [...previous, categoryId]
    );
  }

  // Shows or hides the form for adding an expense.
  function toggleForm(categoryId) {
    setFormCategoryIds((previous) =>
      previous.includes(categoryId)
        ? previous.filter((id) => id !== categoryId)
        : [...previous, categoryId]
    );
  }

  async function refreshCategoryExpenses(categoryId) {
    const updatedExpenses = await getCategoryExpenses(categoryId);
    setCategoryExpenses(categoryId, updatedExpenses);
  }

  async function handleExpenseUpdated(categoryId) {
    try {
      await refreshCategoryExpenses(categoryId);
      onDataChanged?.();
    } catch (updateError) {
      console.error("Failed to refresh category expenses:", updateError);
      setError("Could not refresh your expenses. Please try again.");
    }
  }

  function handleExpenseDeleted(expenseId) {
    removeExpenseFromContext(expenseId);
    onDataChanged?.();
  }

  function handleCategoryDeleted(categoryId) {
    setCategories((previous) =>
      previous.filter((category) => category.id !== categoryId)
    );
    setOpenCategoryIds((previous) =>
      previous.filter((id) => id !== categoryId)
    );
    setFormCategoryIds((previous) =>
      previous.filter((id) => id !== categoryId)
    );
    onDataChanged?.();
  }

  async function handleAddCategory() {
    const trimmedName = newCategoryName.trim();
    if (!trimmedName) return;

    try {
      setError("");
      await saveCategory({ name: trimmedName });
      const updatedCategories = await getCategories();
      setCategories(updatedCategories);
      setNewCategoryName("");
      onDataChanged?.();
    } catch (addError) {
      console.error("Failed to add category:", addError);
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
          onChange={(event) => setNewCategoryName(event.target.value)}
          onKeyDown={(event) => {
            if (event.key === "Enter") handleAddCategory();
          }}
        />
        <button
          type="button"
          onClick={handleAddCategory}
          style={{ marginLeft: "10px" }}
        >
          Add Category
        </button>
      </div>

      {expenses.length === 0 && (
        <p>You have not added any expenses yet.</p>
      )}

      {categories.length === 0 && <p>No categories added yet.</p>}

      <div
        style={{
          display: "flex",
          flexWrap: "wrap",
          gap: "40px",
        }}
      >
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
              <CategoryNameEditor
                category={category}
                onRenamed={(categoryId, newName) => {
                  setCategories((previous) =>
                    previous.map((item) =>
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
                type="button"
                onClick={() => toggleCategory(category.id)}
                aria-expanded={isOpen}
              >
                {isOpen ? "Hide expenses" : "View expenses"}
              </button>

              <button type="button" onClick={() => toggleForm(category.id)}>
                {isFormOpen ? "Hide form" : "Add an expense"}
              </button>

              {isFormOpen && <ExpenseForm categoryId={category.id} />}

              {isOpen && (
                <div style={{ marginTop: "12px" }}>
                  {categoryExpenses.length === 0 ? (
                    <p>No expenses in this category yet.</p>
                  ) : (
                    categoryExpenses.map((expense) => (
                      <Expense
                        key={expense.id}
                        {...expense}
                        onUpdated={() => handleExpenseUpdated(category.id)}
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
