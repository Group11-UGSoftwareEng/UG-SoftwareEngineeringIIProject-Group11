
import { useCallback, useEffect, useState } from 'react'

const API = '/api'

const styles = `
  .ex-page {
    width: 100%;
    max-width: 1100px;
    margin: 20px auto;
    padding: 22px;
    background: #ffffff;
    border: 1px solid #cccccc;
    color: #222222;
    color-scheme: light;
    font-family: Arial, Helvetica, sans-serif;
    text-align: left;
  }

  .ex-page * {
    box-sizing: border-box;
  }

  .ex-page h2,
  .ex-page h3 {
    color: #222222;
    margin: 0 0 8px;
  }

  .ex-page h2 {
    font-size: 21px;
  }

  .ex-page h3 {
    font-size: 16px;
  }

  .ex-muted {
    color: #666666;
    font-size: 13px;
    margin: 0 0 20px;
  }

  .ex-form {
    display: grid;
    grid-template-columns: repeat(2, minmax(0, 1fr));
    gap: 15px;
    margin-bottom: 18px;
  }

  .ex-field {
    display: flex;
    flex-direction: column;
    gap: 7px;
  }

  .ex-field label {
    color: #333333;
    font-size: 13px;
    font-weight: bold;
  }

  .ex-field input,
  .ex-field select {
    width: 100%;
    min-width: 0;
    padding: 11px;
    background: #ffffff;
    color: #222222;
    border: 1px solid #bbbbbb;
    border-radius: 2px;
    font-size: 14px;
    font-family: inherit;
  }

  .ex-field input:focus,
  .ex-field select:focus {
    outline: 2px solid #888888;
    outline-offset: 1px;
  }

  .ex-actions {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
    margin-bottom: 18px;
  }

  .ex-button {
    padding: 11px 17px;
    background: #333333;
    color: #ffffff;
    border: 1px solid #333333;
    border-radius: 2px;
    cursor: pointer;
    font-size: 13px;
  }

  .ex-button.secondary {
    background: #ffffff;
    color: #333333;
    border-color: #aaaaaa;
  }

  .ex-button:disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }

  .ex-message {
    padding: 12px;
    margin-bottom: 15px;
    background: #f4f4f4;
    border: 1px solid #cccccc;
    color: #333333;
    font-size: 13px;
  }

  .ex-message.error {
    border-color: #be8c8c;
    color: #922222;
    background: #fff3f3;
  }

  .ex-divider {
    border-top: 1px solid #dddddd;
    margin: 24px 0;
  }

  .ex-section-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 12px;
    margin-bottom: 15px;
  }

  .ex-table-container {
    overflow-x: auto;
    border: 1px solid #d5d5d5;
  }

  .ex-table {
    width: 100%;
    min-width: 600px;
    border-collapse: collapse;
    font-size: 13px;
    color: #333333;
  }

  .ex-table th {
    padding: 13px;
    background: #eeeeee;
    border-bottom: 1px solid #cccccc;
    color: #333333;
    text-align: left;
  }

  .ex-table td {
    padding: 14px 13px;
    border-bottom: 1px solid #eeeeee;
    color: #333333;
    vertical-align: top;
  }

  .ex-table tbody tr:last-child td {
    border-bottom: none;
  }

  .ex-table td.ex-amount {
    text-align: right;
    white-space: nowrap;
    font-weight: bold;
  }

  .ex-table th:last-child {
    text-align: right;
  }

  .ex-description {
    font-weight: 600;
    color: #222222;
    overflow-wrap: anywhere;
  }

  .ex-empty {
    padding: 18px;
    background: #f5f5f5;
    border: 1px solid #dddddd;
    color: #666666;
    font-size: 13px;
  }

  .ex-total {
    background: #f4f4f4;
    border: 1px solid #dddddd;
    padding: 14px 16px;
    margin-bottom: 15px;
    font-size: 13px;
    color: #333333;
  }

  @media (max-width: 650px) {
    .ex-page {
      padding: 15px;
    }

    .ex-form {
      grid-template-columns: 1fr;
    }
  }
`

function formatMoney(value, currency) {
  const code = ['EUR', 'USD', 'GBP'].includes(currency)
    ? currency
    : 'EUR'

  return new Intl.NumberFormat('en-IE', {
    style: 'currency',
    currency: code
  }).format(Number(value ?? 0))
}

function formatDate(value) {
  if (!value) return 'Unknown'

  const [year, month, day] = value.split('-')

  if (!year || !month || !day) return value

  return `${day}/${month}/${year}`
}

export default function Expense({
  onExpenseAdded,
  refreshKey = 0
}) {
  const [categories, setCategories] = useState([])
  const [categoryId, setCategoryId] = useState('')
  const [amount, setAmount] = useState('')
  const [description, setDescription] = useState('')
  const [currency, setCurrency] = useState('EUR')

  const [expenses, setExpenses] = useState([])
  const [loading, setLoading] = useState(false)
  const [saving, setSaving] = useState(false)

  const [message, setMessage] = useState('')
  const [error, setError] = useState('')

  // Fetch structured JSON instead of Java toString() output.
  const loadExpenses = useCallback(async () => {
    setLoading(true)

    try {
      const response = await fetch(
        `${API}/expenses/list`,
        { cache: 'no-store' }
      )

      if (!response.ok) {
        throw new Error(
          `Unable to load expenses (HTTP ${response.status})`
        )
      }

      const result = await response.json()

      if (!Array.isArray(result)) {
        throw new Error('Expected a JSON list of expenses.')
      }

      setExpenses(result)
      setError('')

    } catch (err) {
      setError(err.message || 'Unable to load expenses.')
    } finally {
      setLoading(false)
    }
  }, [])

  // Load categories from the existing database.
  const loadCategories = useCallback(async () => {
    try {
      const response = await fetch(
        `${API}/expenses/categories`
      )

      if (!response.ok) {
        throw new Error(
          `Unable to load categories (HTTP ${response.status})`
        )
      }

      const result = await response.json()

      setCategories(result)

      setCategoryId(current =>
        result.some(category => category.id === current)
          ? current
          : (result[0]?.id || '')
      )

    } catch (err) {
      setError(err.message || 'Unable to load categories.')
    }
  }, [])

  useEffect(() => {
    loadCategories()
  }, [loadCategories])

  useEffect(() => {
    loadExpenses()
  }, [loadExpenses, refreshKey])

  // Add a new expense using the ORIGINAL API.
  async function addExpense(event) {
    event.preventDefault()
    setMessage('')
    setError('')

    if (!categoryId) {
      setError('Select a category.')
      return
    }

    if (
      !Number.isFinite(Number(amount)) ||
      Number(amount) <= 0
    ) {
      setError('Enter an amount greater than zero.')
      return
    }

    if (!description.trim()) {
      setError('Enter a description.')
      return
    }

    setSaving(true)

    try {
      const response = await fetch(
        `${API}/categories/${categoryId}`,
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            amount: Number(amount),
            description: description.trim(),
            currency
          })
        }
      )

      if (!response.ok) {
        const body = await response.text()

        throw new Error(
          `Unable to save expense (HTTP ${response.status}): ` +
          body.slice(0, 200)
        )
      }

      const savedExpense = await response.json()

      setAmount('')
      setDescription('')

      setMessage('Expense saved successfully.')

      // Reload from PostgreSQL so the new record is visible.
      await loadExpenses()

      // Tell other dashboard components to refresh.
      if (typeof onExpenseAdded === 'function') {
        onExpenseAdded(savedExpense)
      }

    } catch (err) {
      setError(err.message || 'Unable to save expense.')
    } finally {
      setSaving(false)
    }
  }

  const currentMonth = new Date()
  const currentYear = currentMonth.getFullYear()
  const currentMonthNumber = currentMonth.getMonth() + 1

  // Display all stored expenses; summarize the current month.
  const monthlyExpenses = expenses.filter(expense => {
    if (!expense.date) return false

    const [year, month] = expense.date
      .split('-')
      .map(Number)

    return year === currentYear && month === currentMonthNumber
  })

  const monthlyTotals = monthlyExpenses
    .filter(expense => expense.currency === currency)
    .reduce(
      (total, expense) => total + Number(expense.amount ?? 0),
      0
    )

  return (
    <>
      <style>{styles}</style>

      <section className="ex-page">

        <h2>Expense Management</h2>

        <p className="ex-muted">
          Add expenses and view your saved records.
          All expenses are stored through the backend API.
        </p>

        <form onSubmit={addExpense}>
          <div className="ex-form">

            <div className="ex-field">
              <label htmlFor="expense-category">
                Category
              </label>

              <select
                id="expense-category"
                value={categoryId}
                onChange={event =>
                  setCategoryId(event.target.value)
                }
                required
              >
                <option value="">Select a category</option>

                {categories.map(category => (
                  <option
                    key={category.id}
                    value={category.id}
                  >
                    {category.name}
                  </option>
                ))}
              </select>
            </div>

            <div className="ex-field">
              <label htmlFor="expense-amount">
                Amount
              </label>

              <input
                id="expense-amount"
                type="number"
                min="0.01"
                step="0.01"
                placeholder="0.00"
                value={amount}
                onChange={event =>
                  setAmount(event.target.value)
                }
                required
              />
            </div>

            <div className="ex-field">
              <label htmlFor="expense-description">
                Description
              </label>

              <input
                id="expense-description"
                type="text"
                maxLength={200}
                placeholder="e.g. Groceries at Tesco"
                value={description}
                onChange={event =>
                  setDescription(event.target.value)
                }
                required
              />
            </div>

            <div className="ex-field">
              <label htmlFor="expense-currency">
                Currency
              </label>

              <select
                id="expense-currency"
                value={currency}
                onChange={event =>
                  setCurrency(event.target.value)
                }
              >
                <option value="EUR">EUR (€)</option>
                <option value="USD">USD ($)</option>
                <option value="GBP">GBP (£)</option>
              </select>
            </div>

          </div>

          <div className="ex-actions">
            <button
              type="submit"
              className="ex-button"
              disabled={saving || !categoryId}
            >
              {saving ? 'Saving...' : 'Add Expense'}
            </button>

            <button
              type="button"
              className="ex-button secondary"
              disabled={loading}
              onClick={loadExpenses}
            >
              {loading ? 'Loading...' : 'Refresh Expenses'}
            </button>
          </div>

        </form>

        {message && (
          <div className="ex-message" role="status">
            {message}
          </div>
        )}

        {error && (
          <div
            className="ex-message error"
            role="alert"
          >
            {error}
          </div>
        )}

        <div className="ex-divider" />

        <div className="ex-section-header">
          <h3>Saved Expenses ({expenses.length})</h3>
        </div>

        <div className="ex-total">
          <strong>Spent this month ({currency}): </strong>
          {formatMoney(monthlyTotals, currency)}
        </div>

        {expenses.length === 0 ? (
          <div className="ex-empty">
            {loading
              ? 'Loading saved expenses...'
              : 'No saved expenses found.'}
          </div>
        ) : (
          <div className="ex-table-container">
            <table className="ex-table">
              <thead>
                <tr>
                  <th>Date</th>
                  <th>Description</th>
                  <th>Category</th>
                  <th>Amount</th>
                </tr>
              </thead>

              <tbody>
                {expenses.map(expense => (
                  <tr key={expense.id}>

                    <td>
                      {formatDate(expense.date)}
                    </td>

                    <td>
                      <span className="ex-description">
                        {expense.description || 'No description'}
                      </span>
                    </td>

                    <td>
                      {expense.categoryName || 'Uncategorized'}
                    </td>

                    <td className="ex-amount">
                      {formatMoney(
                        expense.amount,
                        expense.currency
                      )}
                    </td>

                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        <p className="ex-muted" style={{ marginTop: 15 }}>
          Records are loaded from PostgreSQL through the
          Spring Boot API, including the saved description,
          date, category, and amount.
        </p>

      </section>
    </>
  )
}
