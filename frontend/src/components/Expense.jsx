// To show the expense row

export default function Expense({ date, description, amount, currency }) {

    return (
    <div
        style={{
        display: "flex",
        alignItems: "center",
        gap: "20px",
        padding: "10px",
        border: "1px solid #ddd",
        borderRadius: "6px",
        marginBottom: "8px",
        }}
    >
        <div>{new Date(date).toLocaleDateString()}</div>
        <div style={{ flex: 1 }}>{description}</div>
        <div>{Number(amount).toFixed(2)}</div>
        <div>{currency}</div>
    </div>
    );

}