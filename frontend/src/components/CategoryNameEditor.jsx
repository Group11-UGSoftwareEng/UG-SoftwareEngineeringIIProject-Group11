import { useState } from "react";
import { renameCategory } from "../api/categoryApi";

export default function CategoryNameEditor({ category, onRenamed }) {
    const [editing, setEditing] = useState(false);
    const [name, setName] = useState(category.name);
    const [saving, setSaving] = useState(false);
    const [error, setError] = useState("");

    function startEditing() {
        setName(category.name);
        setError("");
        setEditing(true);
    }

    async function handleSubmit(event) {
        event.preventDefault();
        if (saving) return;

        const newName = name.trim();

        if (!newName) {
            setError("Please enter a category name.");
            return;
        }

        setSaving(true);
        setError("");

        try {
            await renameCategory(category.id, newName);
            onRenamed(category.id, newName);
            setEditing(false);
        } catch (saveError) {
            const data = saveError.response?.data;
            setError(
                typeof data === "string" && data
                    ? data
                    : data?.message || "Could not update the name. Please try again."
            );
        } finally {
            setSaving(false);
        }
    }

    return (
        <div>
            <h2>{category.name}</h2>

            {editing ? (
                <form onSubmit={handleSubmit}>
                    <label>
                        Category name{" "}
                        <input
                            value={name}
                            onChange={(event) => setName(event.target.value)}
                            disabled={saving}
                            autoFocus
                        />
                    </label>

                    <button type="submit" disabled={saving}>
                        {saving ? "Saving..." : "Save"}
                    </button>

                    <button
                        type="button"
                        disabled={saving}
                        onClick={() => {
                            setEditing(false);
                            setError("");
                        }}
                    >
                        Cancel
                    </button>

                    {error && <p role="alert">{error}</p>}
                </form>
            ) : (
                <button type="button" onClick={startEditing}>
                    Edit name
                </button>
            )}
        </div>
    );
}