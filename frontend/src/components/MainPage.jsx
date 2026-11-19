import { useEffect, useState } from "react";
import ExpenseForm from "./ExpenseForm";
import Expense from "./Expense";
import CategoryDeleteButton from "./CategoryDeleteButton";
import { useExpenses } from "./contexts/ExpenseContext";
import {
    getCategories,
    getCategoryExpenses,
    saveCategory,
    updateCategory
} from "../api/categoryApi";

const MONTHS = [
    { value: 1, label: "January" },
    { value: 2, label: "February" },
    { value: 3, label: "March" },
    { value: 4, label: "April" },
    { value: 5, label: "May" },
    { value: 6, label: "June" },
    { value: 7, label: "July" },
    { value: 8, label: "August" },
    { value: 9, label: "September" },
    { value: 10, label: "October" },
    { value: 11, label: "November" },
    { value: 12, label: "December" }
];

export default function MainPage({onDataChanged}) {
    const { expenses, setCategoryExpenses, removeExpenseFromContext } = useExpenses();

    const now = new Date();
    const currentYear = now.getFullYear();
    const currentMonth = now.getMonth() + 1;

    const [selectedYear, setSelectedYear] = useState(currentYear);
    const [selectedMonth, setSelectedMonth] = useState(currentMonth);

    const isCurrentPeriod =
        selectedYear === currentYear && selectedMonth === currentMonth;

    const yearOptions = Array.from({ length: 5 }, (_, i) => currentYear - i);

    const [categories, setCategories] = useState([]);
    const [openCategoryIds, setOpenCategoryIds] = useState([]);
    const [formCategoryIds, setFormCategoryIds] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const [newCategoryName, setNewCategoryName] = useState("");
    const [newCategoryBudget, setNewCategoryBudget] = useState("");

    const [editingCategoryId, setEditingCategoryId] = useState(null);
    const [editCategoryName, setEditCategoryName] = useState("");
    const [editCategoryBudget, setEditCategoryBudget] = useState("");

    useEffect(() => {
        let cancelled = false;

        async function loadData() {
            setLoading(true);
            setError("");
            setOpenCategoryIds([]);
            setFormCategoryIds([]);
            setEditingCategoryId(null);

            try {
                const savedCategories = await getCategories(selectedYear, selectedMonth);

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
    }, [selectedYear, selectedMonth]);

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

    function notifyDataChanged() {
        if (onDataChanged) onDataChanged();
    }

    function handleExpenseDeleted(expenseId) {
        removeExpenseFromContext(expenseId);
        notifyDataChanged();
    }
    
    async function handleExpenseUpdated(categoryId) {
        const updatedExpenses = await getCategoryExpenses(categoryId);
        setCategoryExpenses(categoryId, updatedExpenses);
        notifyDataChanged();
    }
    
    function handleCategoryDeleted(categoryId) {
        setCategories((previous) => previous.filter((item) => item.id !== categoryId));
        setOpenCategoryIds((previous) => previous.filter((id) => id !== categoryId));
        setFormCategoryIds((previous) => previous.filter((id) => id !== categoryId));
        notifyDataChanged();
    }

    async function handleAddCategory() {
        if (!newCategoryName.trim() || !isCurrentPeriod) return;

        try {
            const budgetValue = newCategoryBudget ? parseFloat(newCategoryBudget) : null;

            await saveCategory({ name: newCategoryName, budget: budgetValue });
            const updatedCategories = await getCategories(selectedYear, selectedMonth);
            setCategories(updatedCategories);

            setNewCategoryName("");
            setNewCategoryBudget("");
            setError("");
        } catch (err) {
            console.error("Failed to add category:", err);
            setError("Failed to add new category. Please try again.");
        }
    }
    
    // Opens the editor with the category's current name and budget.
    function startEditingCategory(category) {
        setEditingCategoryId(category.id);
        setEditCategoryName(category.name);
        setEditCategoryBudget(category.budget != null ? category.budget : "");
    }
    
    // Closes the editor and clears any unsaved changes
    function cancelEditingCategory() {
        setEditingCategoryId(null);
        setEditCategoryName("");
        setEditCategoryBudget("");
    }
    
    // Saves the changes and reloads the categories for the selected month
    async function handleUpdateCategory(categoryId) {
        if (!editCategoryName.trim() || !isCurrentPeriod) return;

        try {
            const budgetValue = editCategoryBudget ? parseFloat(editCategoryBudget) : null;

            await updateCategory(categoryId, {
                newName: editCategoryName,
                newBudget: budgetValue
            });

            const updatedCategories = await getCategories(selectedYear, selectedMonth);
            setCategories(updatedCategories);

            cancelEditingCategory();
            setError("");
        } catch (err) {
            console.error("Failed to update category:", err);
            setError("Failed to update category. Please try again.");
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

            <div style={{ marginBottom: "20px", display: "flex", gap: "10px", alignItems: "center", flexWrap: "wrap" }}>
                <label htmlFor="period-month"><strong>Viewing Period:</strong></label>
                <select
                    id="period-month"
                    value={selectedMonth}
                    onChange={(e) => setSelectedMonth(Number(e.target.value))}
                >
                    {MONTHS.map((m) => (
                        <option key={m.value} value={m.value}>
                            {m.label}
                        </option>
                    ))}
                </select>

                <select
                    id="period-year"
                    aria-label="Year"
                    value={selectedYear}
                    onChange={(e) => setSelectedYear(Number(e.target.value))}
                >
                    {yearOptions.map((y) => (
                        <option key={y} value={y}>
                            {y}
                        </option>
                    ))}
                </select>

                {!isCurrentPeriod && (
                    <>
                        <button
                            onClick={() => {
                                setSelectedYear(currentYear);
                                setSelectedMonth(currentMonth);
                            }}
                        >
                            Back to Current Month
                        </button>
                        <span style={{ color: "#555", fontStyle: "italic" }}>
                            (Viewing historical records — read-only)
                        </span>
                    </>
                )}
            </div>

            {isCurrentPeriod && (
                <div style={{ marginBottom: "20px" }}>
                    <input
                        type="text"
                        placeholder="New category name"
                        value={newCategoryName}
                        onChange={(e) => setNewCategoryName(e.target.value)}
                    />
                    <input
                        type="number"
                        min="0.01"
                        step="0.01"
                        placeholder="Budget Limit (optional)"
                        value={newCategoryBudget}
                        onChange={(e) => setNewCategoryBudget(e.target.value)}
                        style={{ marginLeft: "10px", width: "160px" }}
                    />
                    <button onClick={handleAddCategory} style={{ marginLeft: "10px" }}>
                        Add Category
                    </button>
                </div>
            )}

            {categories.length === 0 ? (
                <p>No categories or expenses recorded for {MONTHS.find((m) => m.value === selectedMonth)?.label} {selectedYear}.</p>
            ) : (
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
                        const totalSpent = categoryExpenses.reduce((sum, exp) => sum + (exp.amount || 0), 0);
                        const hasBudget = category.budget != null;
                        const isOverBudget = hasBudget && totalSpent >= category.budget;
                        const isEditing = editingCategoryId === category.id;

                        return (
                            <section
                                key={category.id}
                                style={{
                                    minWidth: "280px",
                                    padding: "16px",
                                    border: isOverBudget && !isEditing ? "2px solid red" : "1px solid #ccc",
                                    backgroundColor: isOverBudget && !isEditing ? "#ffeeee" : "transparent",
                                    borderRadius: "8px"
                                }}
                            >
                                {isEditing && isCurrentPeriod ? (
                                    <div style={{ marginBottom: "10px" }}>
                                        <input
                                            type="text"
                                            value={editCategoryName}
                                            onChange={(e) => setEditCategoryName(e.target.value)}
                                            style={{ display: "block", marginBottom: "8px", width: "100%", boxSizing: "border-box" }}
                                        />
                                        <input
                                            type="number"
                                            min="0.01"
                                            step="0.01"
                                            placeholder="Budget Limit (optional)"
                                            value={editCategoryBudget}
                                            onChange={(e) => setEditCategoryBudget(e.target.value)}
                                            style={{ display: "block", marginBottom: "8px", width: "100%", boxSizing: "border-box" }}
                                        />
                                        <button onClick={() => handleUpdateCategory(category.id)}>Save</button>
                                        <button onClick={cancelEditingCategory} style={{ marginLeft: "8px" }}>Cancel</button>
                                    </div>
                                ) : (
                                    <>
                                        <h2 style={{ color: isOverBudget ? "red" : "inherit", marginTop: "0" }}>
                                            {category.name}
                                        </h2>

                                        {hasBudget ? (
                                            <p style={{ fontWeight: "bold", color: isOverBudget ? "red" : "inherit" }}>
                                                Spent: €{totalSpent.toFixed(2)} / €{category.budget.toFixed(2)}
                                                {isOverBudget && " ⚠️ Over Budget!"}
                                            </p>
                                        ) : (
                                            <p>Spent: €{totalSpent.toFixed(2)} (No Limit)</p>
                                        )}

                                        <div style={{ display: "flex", gap: "5px", flexWrap: "wrap" }}>
                                            {isCurrentPeriod && (
                                                <button onClick={() => startEditingCategory(category)}>
                                                    Edit Category
                                                </button>
                                            )}
                                            <button
                                                onClick={() => toggleCategory(category.id)}
                                                aria-expanded={isOpen}
                                            >
                                                {isOpen ? "Hide expenses" : "View expenses"}
                                            </button>
                                            {isCurrentPeriod && (
                                                <button onClick={() => toggleForm(category.id)}>
                                                    {isFormOpen ? "Hide form" : "Add an expense"}
                                                </button>
                                            )}
                                            {isCurrentPeriod && (
                                                <CategoryDeleteButton
                                                    category={category}
                                                    onDeleted={handleCategoryDeleted}
                                                />
                                            )}
                                        </div>
                                    </>
                                )}

                                {isFormOpen && !isEditing && isCurrentPeriod && (
                                    <div style={{ marginTop: "12px" }}>
                                        <ExpenseForm categoryId={category.id} />
                                    </div>
                                )}

                                {isOpen && !isEditing && (
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
            )}
        </div>
    );
}
