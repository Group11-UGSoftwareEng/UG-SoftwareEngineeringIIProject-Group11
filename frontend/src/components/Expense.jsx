
import { useState } from "react";
import { editExpense, deleteExpense } from "../api/expenseApi";

const styles = `
  .expense-row {
    padding: 12px;
    border: 1px solid #dddddd;
    border-radius: 6px;
    margin-bottom: 8px;
    background: #ffffff;
    color: #222222;
    font-family: Arial, Helvetica, sans-serif;
    color-scheme: light;
    text-align: left;
  }

  .expense-row * {
    box-sizing: border-box;
  }

  .expense-row .expense-display {
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    gap: 15px;
  }

  .expense-row .expense-date {
    color: #555555;
    font-size: 13px;
  }

  .expense-row .expense-description {
    flex: 1;
    min-width: 100px;
    overflow-wrap: anywhere;
  }

  .expense-row .expense-amount {
    font-weight: bold;
    font-variant-numeric: tabular-nums;
  }

  .expense-row .expense-currency {
    color: #555555;
    font-size: 13px;
  }

  .expense-row button {
    background: #333333;
    color: #ffffff;
    border: 1px solid #333333;
    border-radius: 3px;
    padding: 8px 12px;
    cursor: pointer;
    font-size: 13px;
  }

  .expense-row button:hover {
    background: #555555;
  }

  .expense-row button:disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }

  .expense-row .expense-cancel {
    background: #ffffff;
    border: 1px solid #999999;
    color: #222222;
  }

  .expense-row .expense-cancel:hover {
    background: #eeeeee;
  }

  .expense-row .expense-edit-form {
    margin-top: 15px;
    padding-top: 15px;
    border-top: 1px solid #dddddd;
  }

  .expense-row .expense-edit-form h4 {
    margin: 0 0 14px;
    font-size: 15px;
    color: #222222;
  }

  .expense-row .expense-edit-fields {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 12px;
  }

  .expense-row .expense-edit-field {
    display: flex;
    flex-direction: column;
    gap: 6px;
    margin-bottom: 12px;
    color: #444444;
    font-size: 13px;
  }

  .expense-row .expense-edit-field input {
    padding: 10px;
    border: 1px solid #cccccc;
    border-radius: 3px;
    background: #ffffff;
    color: #222222;
    font-size: 13px;
    width: 100%;
  }

  .expense-row .expense-edit-actions {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
    margin-top: 6px;
  }
  
  .expense-row .expense-delete {
    background: #ffffff;
    border: 1px solid #a12626;
    color: #a12626;
  }

  .expense-row .expense-delete:hover {
    background: #a12626;
    color: #ffffff;
  }

  .expense-row .expense-confirm {
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    gap: 10px;
    margin-top: 12px;
    padding-top: 12px;
    border-top: 1px solid #dddddd;
    font-size: 13px;
  }

  .expense-row .expense-confirm-yes {
    background: #a12626;
    border-color: #a12626;
  }

  .expense-row .expense-confirm-yes:hover {
    background: #7d1c1c;
  }

  .expense-row .expense-error {
    color: #a12626;
    background: #fff4f4;
    border: 1px solid #e0bbbb;
    padding: 10px;
    margin: 10px 0;
    font-size: 13px;
  }

  .expense-row .expense-success {
    color: #246138;
    font-size: 12px;
    margin-top: 10px;
  }

  @media (max-width: 550px) {
    .expense-row .expense-edit-fields {
      grid-template-columns: 1fr;
    }
  }
`;

export default function Expense({
  id,
  date,
  dateAdded,
  description,
  amount,
  currency,
  onUpdated,
  onDeleted
}) {
  const expenseDate = date ?? dateAdded ?? "";

  const [editing, setEditing] = useState(false);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [confirmingDelete, setConfirmingDelete] = useState(false);
  const [deleting, setDeleting] = useState(false);
  const [deleteError, setDeleteError] = useState("");

  const [editAmount, setEditAmount] = useState(
    String(amount ?? "")
  );

  const [editDate, setEditDate] = useState(
    String(expenseDate).slice(0, 10)
  );

  const [editDescription, setEditDescription] = useState(
    String(description ?? "")
  );

  function startEditing() {
    setEditAmount(String(amount ?? ""));
    setEditDate(String(expenseDate).slice(0, 10));
    setEditDescription(String(description ?? ""));

    setError("");
    setSuccess("");
    setEditing(true);
  }

  function cancelEditing() {
    setEditing(false);
    setError("");
  }

  async function handleSave(event) {
    event.preventDefault();

    const parsedAmount = Number(editAmount);
    const cleanDescription = editDescription.trim();

    if (
      !Number.isFinite(parsedAmount) ||
      parsedAmount <= 0
    ) {
      setError("Amount must be greater than zero.");
      return;
    }

    if (!editDate) {
      setError("Please select a valid date.");
      return;
    }

    if (!cleanDescription) {
      setError("Description cannot be empty.");
      return;
    }

    if (!id) {
      setError(
        "Expense ID is missing. Refresh the category and try again."
      );
      return;
    }

    setSaving(true);
    setError("");
    setSuccess("");

    try {
      await editExpense(id, {
        amount: parsedAmount,
        date: editDate,
        description: cleanDescription
      });

      // Reload the saved expenses from PostgreSQL
      // through the existing category API.
      if (onUpdated) {
        await onUpdated();
      }

      setEditing(false);
      setSuccess("Expense updated successfully.");
    } catch (saveError) {
      console.error("Error editing expense:", saveError);

      const serverMessage = saveError.response?.data;

      setError(
        typeof serverMessage === "string"
          ? serverMessage
          : serverMessage?.message ||
            "Unable to update this expense. Please try again."
      );
    } finally {
      setSaving(false);
    }
  }

  async function handleDelete() {
    if (!id) {
      setDeleteError(
        "Expense ID is missing. Refresh the category and try again."
      );
      return;
    }

    setDeleting(true);
    setDeleteError("");

    try {
      await deleteExpense(id);

      if (onDeleted) {
        onDeleted(id);
      }
    } 
    catch (deleteFailure) {
      console.error("Error deleting expense:", deleteFailure);

      if (deleteFailure.response?.status === 404 && onDeleted) {
        onDeleted(id);
        return;
      }

      const serverMessage = deleteFailure.response?.data;

      setDeleteError(
        typeof serverMessage === "string" && serverMessage
          ? serverMessage
          : serverMessage?.message ||
            "Unable to delete this expense. Please try again."
      );
      setDeleting(false);
    }
  }

  return (
    <>
      <style>{styles}</style>

      <div className="expense-row">
        <div className="expense-display">

          <div className="expense-date">
            {expenseDate || "No date"}
          </div>

          <div className="expense-description">
            {description}
          </div>

          <div className="expense-amount">
            {Number(amount ?? 0).toFixed(2)}
          </div>

          <div className="expense-currency">
            {currency}
          </div>

          <button
            type="button"
            onClick={startEditing}
            disabled={editing || deleting || !id}
          >
            Edit
          </button>

          <button
            type="button"
            className="expense-delete"
            onClick={() => {
              setDeleteError("");
              setSuccess("");
              setConfirmingDelete(true);
            }}
            disabled={confirmingDelete || deleting || !id}
          >
            Delete
          </button>

        </div>

        {editing && (
          <form
            className="expense-edit-form"
            onSubmit={handleSave}
          >
            <h4>Edit Expense</h4>

            <div className="expense-edit-fields">

              <label className="expense-edit-field">
                Amount
                <input
                  type="number"
                  min="0.01"
                  step="0.01"
                  value={editAmount}
                  onChange={event =>
                    setEditAmount(event.target.value)
                  }
                  disabled={saving}
                  required
                />
              </label>

              <label className="expense-edit-field">
                Date
                <input
                  type="date"
                  value={editDate}
                  onChange={event =>
                    setEditDate(event.target.value)
                  }
                  disabled={saving}
                  required
                />
              </label>

            </div>

            <label className="expense-edit-field">
              Description
              <input
                type="text"
                value={editDescription}
                onChange={event =>
                  setEditDescription(event.target.value)
                }
                disabled={saving}
                required
              />
            </label>

            {error && (
              <div className="expense-error" role="alert">
                {error}
              </div>
            )}

            <div className="expense-edit-actions">
              <button
                type="submit"
                disabled={saving}
              >
                {saving ? "Saving..." : "Save Changes"}
              </button>

              <button
                type="button"
                className="expense-cancel"
                onClick={cancelEditing}
                disabled={saving}
              >
                Cancel
              </button>
            </div>
          </form>
        )}

        {confirmingDelete && (
          <div className="expense-confirm" role="alertdialog">
            <span>Delete this expense? This cannot be undone.</span>

            <button
              type="button"
              className="expense-confirm-yes"
              onClick={handleDelete}
              disabled={deleting}
            >
              {deleting ? "Deleting..." : "Yes, delete"}
            </button>

            <button
              type="button"
              className="expense-cancel"
              onClick={() => {
                setConfirmingDelete(false);
                setDeleteError("");
              }}
              disabled={deleting}
            >
              Keep it
            </button>
          </div>
        )}

        {deleteError && (
          <div className="expense-error" role="alert">
            {deleteError}
          </div>
        )}

        {success && !editing && (
          <p className="expense-success" role="status">
            {success}
          </p>
        )}
      </div>
    </>
  );
}
