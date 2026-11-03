export default function Expense({
                                    date,
                                    dateAdded,
                                    description,
                                    amount,
                                    currency
                                }) {
    const expenseDate = date ?? dateAdded;

    return (
        <div
            style={{
                display: "flex",
                alignItems: "center",
                gap: "20px",
                padding: "10px",
                border: "1px solid #ddd",
                borderRadius: "6px",
                marginBottom: "8px"
            }}
        >
            <div>{expenseDate ?? "No date"}</div>
            <div style={{ flex: 1 }}>{description}</div>
            <div>{Number(amount).toFixed(2)}</div>
            <div>{currency}</div>
        </div>
    );
}