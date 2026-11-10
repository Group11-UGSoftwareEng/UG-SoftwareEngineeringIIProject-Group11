import { useState } from "react";
import { deleteCategory } from "../api/categoryApi";

export default function CategoryDeleteButton({ category, onDeleted }) {
    const [confirming, setConfirming] = useState(false);
    const [deleting, setDeleting] = useState(false);
    const [error, setError] = useState("");

    async function handleDelete() {
        if (deleting) return;

        setDeleting(true);
        setError("");

        try {
            await deleteCategory(category.id);
            onDeleted(category.id);
        } catch (deleteFailure) {
            const status = deleteFailure.response?.status;
            const data = deleteFailure.response?.data;

            if (status === 404) {
                onDeleted(category.id);
                return;
            }

            if (status === 409) {
                const count = data?.expenseCount;
                setError(
                    data?.message ||
                    `This category still has ${count ?? "some"} expense(s). ` +
                    "Move or delete them before deleting the category."
                );
            } else {
                setError(
                    typeof data === "string" && data
                        ? data
                        : data?.message ||
                          "Could not delete the category. Please try again."
                );
            }

            setConfirming(false);
            setDeleting(false);
        }
    }

    if (confirming) {
        return (
            <div role="alertdialog">
                <p>Delete the category "{category.name}"?</p>

                <button type="button" onClick={handleDelete} disabled={deleting}>
                    {deleting ? "Deleting..." : "Yes, delete"}
                </button>

                <button
                    type="button"
                    onClick={() => setConfirming(false)}
                    disabled={deleting}
                >
                    Keep it
                </button>
            </div>
        );
    }

    return (
        <div>
            <button
                type="button"
                onClick={() => {
                    setError("");
                    setConfirming(true);
                }}
            >
                Delete category
            </button>

            {error && <p role="alert">{error}</p>}
        </div>
    );
}