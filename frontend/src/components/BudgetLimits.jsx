
import { useCallback, useEffect, useState } from 'react'

const styles = `
  .budget-limits {
    width: 100%;
    margin: 20px 0;
    padding: 22px;
    background: #ffffff;
    border: 1px solid #cccccc;
    color: #222222;
    color-scheme: light;
    font-family: Arial, Helvetica, sans-serif;
    text-align: left;
    box-sizing: border-box;
  }

  .budget-limits * {
    box-sizing: border-box;
  }

  .budget-limits .bl-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    flex-wrap: wrap;
    gap: 15px;
    padding-bottom: 18px;
    border-bottom: 1px solid #cccccc;
    margin-bottom: 20px;
  }

  .budget-limits h2 {
    margin: 0 0 6px;
    font-size: 21px;
    color: #222222;
  }

  .budget-limits h3 {
    font-size: 16px;
    margin: 22px 0 15px;
    color: #222222;
  }

  .budget-limits .bl-subtitle {
    margin: 0;
    color: #666666;
    font-size: 13px;
  }

  .budget-limits button {
    padding: 10px 16px;
    background: #333333;
    border: 1px solid #333333;
    color: #ffffff;
    border-radius: 2px;
    cursor: pointer;
    font-size: 13px;
  }

  .budget-limits button:hover {
    background: #555555;
  }

  .budget-limits button:disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }

  .budget-limits input,
  .budget-limits select {
    padding: 10px;
    border: 1px solid #cccccc;
    background: #ffffff;
    color: #222222;
    border-radius: 2px;
    font-size: 13px;
    min-width: 0;
  }

  .budget-limits .bl-currency {
    display: flex;
    align-items: center;
    gap: 10px;
    margin: 15px 0 20px;
    font-size: 13px;
  }

  .budget-limits .bl-overall {
    border: 1px solid #d5d5d5;
    background: #f5f5f5;
    padding: 20px;
  }

  .budget-limits .bl-overall h3 {
    margin-top: 0;
  }

  .budget-limits .bl-cards {
    display: grid;
    grid-template-columns: repeat(3, minmax(0, 1fr));
    gap: 12px;
    margin: 18px 0;
  }

  .budget-limits .bl-card-label {
    display: block;
    font-size: 13px;
    color: #555555;
    margin-bottom: 9px;
  }

  .budget-limits .bl-card-value {
    display: block;
    font-size: 23px;
    font-weight: bold;
    overflow-wrap: anywhere;
  }

  .budget-limits .bl-form {
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    gap: 10px;
    margin-top: 15px;
  }

  .budget-limits .bl-form label {
    font-size: 13px;
    color: #444444;
  }

  .budget-limits .bl-form input {
    flex: 1;
    max-width: 250px;
  }

  .budget-limits .bl-bar {
    background: #dddddd;
    height: 12px;
    width: 100%;
    margin: 12px 0 8px;
    overflow: hidden;
  }

  .budget-limits .bl-fill {
    background: #555555;
    height: 100%;
    transition: width 0.2s ease;
  }

  .budget-limits .bl-category {
    padding: 18px 0;
    border-bottom: 1px solid #dddddd;
  }

  .budget-limits .bl-category-header {
    display: flex;
    justify-content: space-between;
    flex-wrap: wrap;
    gap: 10px;
  }

  .budget-limits .bl-name {
    font-size: 14px;
    font-weight: bold;
  }

  .budget-limits .bl-amount {
    color: #444444;
    font-size: 13px;
  }

  .budget-limits .bl-status {
    color: #555555;
    font-size: 12px;
    margin-top: 8px;
  }

  .budget-limits .bl-warning {
    font-weight: bold;
    color: #8a5800;
  }

  .budget-limits .bl-exceeded {
    font-weight: bold;
    color: #ab2525;
  }

  .budget-limits .bl-error {
    padding: 13px;
    border: 1px solid #cccccc;
    background: #f5f5f5;
    color: #a12626;
    margin: 12px 0;
    font-size: 13px;
  }

  .budget-limits .bl-message {
    padding: 13px;
    border: 1px solid #cccccc;
    margin: 12px 0;
    background: #f5f5f5;
    font-size: 13px;
  }

  .budget-limits .bl-loading {
    color: #666666;
    font-size: 13px;
  }

  .budget-limits .bl-note {
    margin-top: 20px;
    color: #666666;
    font-size: 12px;
    line-height: 1.6;
  }

  @media (max-width: 700px) {
    .budget-limits .bl-cards {
      grid-template-columns: 1fr;
    }

    .budget-limits {
      padding: 14px;
    }

    .budget-limits .bl-form input {
      width: 100%;
      max-width: none;
    }
  }
`

function BudgetProgress({ budget, formatMoney }) {
  const hasLimit = budget.limit !== null &&
                   budget.limit !== undefined

  const percentage = hasLimit
    ? Number(budget.percentageUsed ?? 0)
    : 0

  const status = budget.status

  let message = 'No budget limit set'

  if (status === 'OK') {
    message = `${formatMoney(budget.remaining)} remaining`
  } else if (status === 'WARNING') {
    message =
      `Approaching limit — ${formatMoney(budget.remaining)} remaining`
  } else if (status === 'EXCEEDED') {
    message = Number(budget.remaining) < 0
      ? `Over budget by ${formatMoney(
          Math.abs(Number(budget.remaining))
        )}`
      : 'Budget limit reached'
  }

  return (
    <>
      {hasLimit && (
        <div
          className="bl-bar"
          role="progressbar"
          aria-label="Budget usage"
          aria-valuenow={Math.min(
            100,
            Math.max(0, percentage)
          )}
          aria-valuemin={0}
          aria-valuemax={100}
        >
          <div
            className="bl-fill"
            style={{
              width: `${Math.min(
                100,
                Math.max(0, percentage)
              )}%`
            }}
          />
        </div>
      )}

      <div
        className={`bl-status ${
          status === 'EXCEEDED'
            ? 'bl-exceeded'
            : status === 'WARNING'
              ? 'bl-warning'
              : ''
        }`}
      >
        {hasLimit && `${percentage.toFixed(1)}% used · `}
        {message}
      </div>
    </>
  )
}

export default function BudgetLimits({
  apiBase = '/api'
}) {
  const [currency, setCurrency] = useState('EUR')
  const [data, setData] = useState(null)

  const [overallInput, setOverallInput] = useState('')
  const [categoryInputs, setCategoryInputs] = useState({})

  const [error, setError] = useState('')
  const [message, setMessage] = useState('')

  const [loading, setLoading] = useState(false)
  const [saving, setSaving] = useState(false)
  const [revision, setRevision] = useState(0)

  // Retrieve saved limits and expenses from PostgreSQL
  // through the Spring Boot REST API.
  const loadBudgets = useCallback(async (signal) => {
    try {
      const response = await fetch(
        `${apiBase}/budgets/monthly?currency=${
          encodeURIComponent(currency)
        }`,
        {
          signal,
          cache: 'no-store',
          headers: {
            Accept: 'application/json'
          }
        }
      )

      const contentType =
        response.headers.get('content-type') || ''

      if (!contentType.includes('application/json')) {
        throw new Error(
          'The server did not return JSON. ' +
          'Check that Spring Boot is running and ' +
          'the API proxy is configured.'
        )
      }

      const result = await response.json()

      if (!response.ok) {
        throw new Error(
          result.error || `HTTP ${response.status}`
        )
      }

      if (!signal.aborted) {
        setData(result)
        setError('')
      }

    } catch (err) {
      if (err.name !== 'AbortError' && !signal.aborted) {
        setError(
          err.message || 'Could not load budgets.'
        )
      }
    }
  }, [apiBase, currency])

  // Initial load and automatic updates.
  useEffect(() => {
    const controller = new AbortController()

    async function refresh() {
      if (controller.signal.aborted) return

      setLoading(true)

      try {
        await loadBudgets(controller.signal)
      } finally {
        if (!controller.signal.aborted) {
          setLoading(false)
        }
      }
    }

    refresh()

    const timer = setInterval(refresh, 15000)

    return () => {
      controller.abort()
      clearInterval(timer)
    }
  }, [loadBudgets, revision])

  // Save overall or category budgets.
  async function saveBudget(url, value) {
    const raw = String(value).trim()
    const amount = Number(raw)

    if (
      !/^\d+(\.\d{1,2})?$/.test(raw) ||
      !Number.isFinite(amount) ||
      amount <= 0
    ) {
      setError(
        'Enter a positive budget amount with at most 2 decimal places.'
      )
      return
    }

    setSaving(true)
    setError('')
    setMessage('')

    try {
      const response = await fetch(url, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          Accept: 'application/json'
        },
        body: JSON.stringify({
          currency,
          limit: amount
        })
      })

      const result = await response.json()

      if (!response.ok) {
        throw new Error(
          result.error || `HTTP ${response.status}`
        )
      }

      setData(result)
      setMessage('Budget limit saved successfully.')

      // Fetch latest expenses and limits.
      setRevision(current => current + 1)

    } catch (err) {
      setError(
        err.message || 'Unable to save budget.'
      )
    } finally {
      setSaving(false)
    }
  }

  function formatMoney(amount) {
    return new Intl.NumberFormat('en-IE', {
      style: 'currency',
      currency
    }).format(Number(amount ?? 0))
  }

  function refreshBudgets() {
    setMessage('')
    setRevision(current => current + 1)
  }

  const overall = data?.overall
  const categories = data?.categories ?? []

  return (
    <>
      <style>{styles}</style>

      <section className="budget-limits">

        {/* Page heading */}
        <div className="bl-header">
          <div>
            <h2>Budget Limits</h2>

            <p className="bl-subtitle">
              {data?.month || 'Current Month'}
              {' | '}
              Overall and category spending limits
            </p>
          </div>

          <button
            type="button"
            onClick={refreshBudgets}
            disabled={loading || saving}
          >
            {loading ? 'Refreshing...' : 'Refresh'}
          </button>
        </div>

        {/* Currency selection */}
        <label className="bl-currency">
          Currency

          <select
            value={currency}
            disabled={saving}
            onChange={event => {
              setCurrency(event.target.value)
              setData(null)
              setOverallInput('')
              setCategoryInputs({})
              setError('')
              setMessage('')
            }}
          >
            <option value="EUR">EUR</option>
            <option value="USD">USD</option>
            <option value="GBP">GBP</option>
          </select>
        </label>

        {/* Messages */}
        {error && (
          <div className="bl-error" role="alert">
            {error}
          </div>
        )}

        {message && (
          <div className="bl-message" role="status">
            {message}
          </div>
        )}

        {loading && !data && (
          <p className="bl-loading" role="status">
            Loading budget information...
          </p>
        )}

        {/* Overall monthly budget */}
        {overall && (
          <>
            <div className="bl-overall">

              <h3>Overall Monthly Budget</h3>

              <div className="bl-cards">
                <div>
                  <span className="bl-card-label">
                    Monthly Limit
                  </span>

                  <strong className="bl-card-value">
                    {overall.limit === null
                      ? 'Not Set'
                      : formatMoney(overall.limit)}
                  </strong>
                </div>

                <div>
                  <span className="bl-card-label">
                    Spent So Far
                  </span>

                  <strong className="bl-card-value">
                    {formatMoney(overall.spent)}
                  </strong>
                </div>

                <div>
                  <span className="bl-card-label">
                    Remaining
                  </span>

                  <strong className="bl-card-value">
                    {overall.remaining === null
                      ? '—'
                      : formatMoney(overall.remaining)}
                  </strong>
                </div>
              </div>

              <BudgetProgress
                budget={overall}
                formatMoney={formatMoney}
              />

              {/* Set overall limit */}
              <form
                className="bl-form"
                onSubmit={event => {
                  event.preventDefault()

                  saveBudget(
                    `${apiBase}/budgets/monthly/overall`,
                    overallInput
                  )
                }}
              >
                <label htmlFor="overall-budget-input">
                  Set overall limit
                </label>

                <input
                  id="overall-budget-input"
                  type="number"
                  min="0.01"
                  step="0.01"
                  placeholder={
                    overall.limit === null
                      ? 'Enter monthly limit'
                      : String(overall.limit)
                  }
                  value={overallInput}
                  onChange={event =>
                    setOverallInput(event.target.value)
                  }
                  disabled={saving}
                  required
                />

                <button
                  type="submit"
                  disabled={saving}
                >
                  {saving ? 'Saving...' : 'Save Overall Budget'}
                </button>
              </form>

            </div>

            {/* Individual category limits */}
            <h3>Category Budget Limits</h3>

            {categories.length === 0 && (
              <div className="bl-message">
                No categories are available.
                Create categories using the existing application.
              </div>
            )}

            {categories.map(category => (
              <div
                className="bl-category"
                key={category.categoryId}
              >

                <div className="bl-category-header">
                  <span className="bl-name">
                    {category.categoryName}
                  </span>

                  <span className="bl-amount">
                    {formatMoney(category.budget.spent)}
                    {' / '}
                    {category.budget.limit === null
                      ? 'No limit'
                      : formatMoney(category.budget.limit)}
                  </span>
                </div>

                <BudgetProgress
                  budget={category.budget}
                  formatMoney={formatMoney}
                />

                {/* Category limit form */}
                <form
                  className="bl-form"
                  onSubmit={event => {
                    event.preventDefault()

                    saveBudget(
                      `${apiBase}/budgets/monthly/categories/${
                        category.categoryId
                      }`,
                      categoryInputs[
                        category.categoryId
                      ] || ''
                    )
                  }}
                >
                  <label
                    htmlFor={`budget-${category.categoryId}`}
                  >
                    Category limit
                  </label>

                  <input
                    id={`budget-${category.categoryId}`}
                    type="number"
                    min="0.01"
                    step="0.01"
                    placeholder={
                      category.budget.limit === null
                        ? 'Enter limit'
                        : String(category.budget.limit)
                    }
                    value={
                      categoryInputs[
                        category.categoryId
                      ] ?? ''
                    }
                    onChange={event => {
                      setCategoryInputs(current => ({
                        ...current,
                        [category.categoryId]:
                          event.target.value
                      }))
                    }}
                    disabled={saving}
                    required
                  />

                  <button
                    type="submit"
                    disabled={saving}
                  >
                    {saving ? 'Saving...' : 'Save Limit'}
                  </button>
                </form>

              </div>
            ))}
          </>
        )}

        {/* Explanation */}
        <div className="bl-note">

        </div>

      </section>
    </>
  )
}
