
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
    gap: 15px;
    border-bottom: 1px solid #cccccc;
    padding-bottom: 18px;
    margin-bottom: 20px;
  }

  .budget-limits h2 {
  margin: 0 0 6px;
  font-size: 21px;
  font-weight: 700;
  color: #222222 !important;
  }

  .budget-limits h3 {
    margin: 0 0 15px;
    font-size: 16px;
  }

  .budget-limits .bl-subtitle {
    color: #666666;
    font-size: 13px;
    margin: 0;
  }

  .budget-limits button {
    padding: 10px 16px;
    background: #333333;
    color: white;
    border: 1px solid #333333;
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
    background: white;
    color: #222222;
    border: 1px solid #cccccc;
    border-radius: 2px;
    font-size: 13px;
  }

  .budget-limits .bl-filters {
    display: flex;
    flex-wrap: wrap;
    gap: 15px;
    margin-bottom: 22px;
  }

  .budget-limits .bl-filter {
    display: flex;
    flex-direction: column;
    gap: 7px;
    font-size: 13px;
  }

  .budget-limits .bl-panel {
    padding: 20px;
    border: 1px solid #d5d5d5;
    background: #f5f5f5;
    margin-bottom: 22px;
  }

  .budget-limits .bl-cards {
    display: grid;
    grid-template-columns: repeat(3, minmax(0, 1fr));
    gap: 12px;
    margin: 18px 0;
  }

  .budget-limits .bl-card-label {
    display: block;
    color: #555555;
    font-size: 13px;
    margin-bottom: 10px;
  }

  .budget-limits .bl-card-value {
    display: block;
    font-size: 23px;
    font-weight: bold;
    overflow-wrap: anywhere;
  }

  .budget-limits .bl-form {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    gap: 10px;
    margin-top: 18px;
  }

  .budget-limits .bl-form input {
    flex: 1;
    min-width: 120px;
    max-width: 250px;
  }

  .budget-limits .bl-bar {
    height: 12px;
    background: #dddddd;
    margin: 12px 0 8px;
    overflow: hidden;
  }

  .budget-limits .bl-fill {
    height: 100%;
    background: #555555;
    transition: width 0.2s ease;
  }

  .budget-limits .bl-status {
    color: #555555;
    font-size: 12px;
    margin-top: 8px;
  }

  .budget-limits .bl-warning {
    color: #8a5800;
    font-weight: bold;
  }

  .budget-limits .bl-exceeded {
    color: #ab2525;
    font-weight: bold;
  }

  .budget-limits .bl-message {
    padding: 13px;
    border: 1px solid #cccccc;
    margin: 12px 0;
    background: #f5f5f5;
    font-size: 13px;
  }

  .budget-limits .bl-error {
    color: #a12626;
  }

  .budget-limits .bl-note {
    color: #666666;
    font-size: 12px;
    line-height: 1.6;
  }

  @media (max-width: 700px) {
    .budget-limits .bl-cards {
      grid-template-columns: 1fr;
    }

    .budget-limits .bl-header {
      flex-direction: column;
      align-items: flex-start;
    }

    .budget-limits {
      padding: 14px;
    }
  }
`

function BudgetProgress({ budget, money }) {
  if (!budget || budget.limit == null) {
    return (
      <p className="bl-status">
        No budget limit has been set.
      </p>
    )
  }

  const percentage = Number(budget.percentageUsed ?? 0)
  const remaining = Number(budget.remaining ?? 0)

  let message = `${money(remaining)} remaining`

  if (budget.status === 'WARNING') {
    message = `Approaching limit — ${money(remaining)} remaining`
  }

  if (budget.status === 'EXCEEDED') {
    message = remaining < 0
      ? `Over budget by ${money(Math.abs(remaining))}`
      : 'Budget limit reached'
  }

  return (
    <>
      <div
        className="bl-bar"
        role="progressbar"
        aria-label="Budget usage"
        aria-valuenow={Math.max(0, Math.min(100, percentage))}
        aria-valuemin={0}
        aria-valuemax={100}
      >
        <div
          className="bl-fill"
          style={{
            width: `${Math.max(
              0,
              Math.min(100, percentage)
            )}%`
          }}
        />
      </div>

      <p className={`bl-status ${
        budget.status === 'EXCEEDED'
          ? 'bl-exceeded'
          : budget.status === 'WARNING'
            ? 'bl-warning'
            : ''
      }`}>
        {percentage.toFixed(1)}% used — {message}
      </p>
    </>
  )
}

export default function BudgetLimits({
  apiBase = '/api'
}) {
  const [currency, setCurrency] = useState('EUR')
  const [data, setData] = useState(null)

  const [selectedCategoryId, setSelectedCategoryId] =
    useState('')

  const [overallInput, setOverallInput] = useState('')
  const [categoryInput, setCategoryInput] = useState('')

  const [error, setError] = useState('')
  const [message, setMessage] = useState('')
  const [loading, setLoading] = useState(false)
  const [saving, setSaving] = useState(false)
  const [refresh, setRefresh] = useState(0)

  const loadBudgets = useCallback(async (signal) => {
    const response = await fetch(
      `${apiBase}/budgets/monthly?currency=${
        encodeURIComponent(currency)
      }`,
      {
        signal,
        cache: 'no-store',
        headers: { Accept: 'application/json' }
      }
    )

    const result = await response.json()

    if (!response.ok) {
      throw new Error(result.error || `HTTP ${response.status}`)
    }

    return result
  }, [apiBase, currency])

  useEffect(() => {
    const controller = new AbortController()
    let requestNumber = 0

    async function update() {
      if (controller.signal.aborted) return

      const currentRequest = ++requestNumber
      setLoading(true)

      try {
        const result = await loadBudgets(controller.signal)

        if (!controller.signal.aborted &&
            currentRequest === requestNumber) {
          setData(result)
          setError('')
        }
      } catch (err) {
        if (!controller.signal.aborted &&
            currentRequest === requestNumber) {
          setError(err.message || 'Could not load budgets.')
        }
      } finally {
        if (!controller.signal.aborted &&
            currentRequest === requestNumber) {
          setLoading(false)
        }
      }
    }

    update()

    const timer = setInterval(update, 15000)

    return () => {
      controller.abort()
      clearInterval(timer)
    }
  }, [loadBudgets, refresh])

  const categories = data?.categories ?? []

  // Use existing category IDs; do not create new categories.
  const activeCategoryId = categories.some(
    category => category.categoryId === selectedCategoryId
  )
    ? selectedCategoryId
    : (categories[0]?.categoryId ?? '')

  const activeCategory = categories.find(
    category => category.categoryId === activeCategoryId
  )

  const overall = data?.overall

  function money(value) {
    return new Intl.NumberFormat('en-IE', {
      style: 'currency',
      currency
    }).format(Number(value ?? 0))
  }

  async function saveBudget(url, input) {
    const raw = String(input).trim()

    if (!/^\d+(\.\d{1,2})?$/.test(raw) ||
        Number(raw) <= 0 ||
        !Number.isFinite(Number(raw))) {
      setError(
        'Enter a positive amount with at most 2 decimal places.'
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
          limit: Number(raw)
        })
      })

      const result = await response.json()

      if (!response.ok) {
        throw new Error(result.error || `HTTP ${response.status}`)
      }

      setData(result)
      setMessage('Budget saved successfully.')
      setRefresh(value => value + 1)
    } catch (err) {
      setError(err.message || 'Unable to save budget.')
    } finally {
      setSaving(false)
    }
  }

  return (
    <>
      <style>{styles}</style>

      <section className="budget-limits">
        <div className="bl-header">
          <div>
            <h2>Budget Limits</h2>
            <p className="bl-subtitle">
              {data?.month || 'Current Month'}
              {' | '}
              Overall and category limits
            </p>
          </div>

          <button
            type="button"
            disabled={loading || saving}
            onClick={() => setRefresh(v => v + 1)}
          >
            {loading ? 'Refreshing...' : 'Refresh'}
          </button>
        </div>

        <div className="bl-filters">
          <label className="bl-filter">
            Currency
            <select
              value={currency}
              disabled={saving}
              onChange={e => {
                setCurrency(e.target.value)
                setData(null)
                setMessage('')
                setError('')
                setOverallInput('')
                setCategoryInput('')
              }}
            >
              <option value="EUR">EUR</option>
              <option value="USD">USD</option>
              <option value="GBP">GBP</option>
            </select>
          </label>
        </div>

        {error && (
          <div className="bl-message bl-error" role="alert">
            {error}
          </div>
        )}

        {message && (
          <div className="bl-message" role="status">
            {message}
          </div>
        )}

        {loading && !data && (
          <p>Loading budget information...</p>
        )}

        {overall && (
          <>
            <div className="bl-panel">
              <h3>Overall Monthly Budget</h3>

              <div className="bl-cards">
                <div>
                  <span className="bl-card-label">
                    Monthly Limit
                  </span>
                  <strong className="bl-card-value">
                    {overall.limit == null
                      ? 'Not Set'
                      : money(overall.limit)}
                  </strong>
                </div>

                <div>
                  <span className="bl-card-label">
                    Spent So Far
                  </span>
                  <strong className="bl-card-value">
                    {money(overall.spent)}
                  </strong>
                </div>

                <div>
                  <span className="bl-card-label">
                    Remaining
                  </span>
                  <strong className="bl-card-value">
                    {overall.remaining == null
                      ? '—'
                      : money(overall.remaining)}
                  </strong>
                </div>
              </div>

              <BudgetProgress budget={overall} money={money} />

              <form
                className="bl-form"
                onSubmit={e => {
                  e.preventDefault()
                  saveBudget(
                    `${apiBase}/budgets/monthly/overall`,
                    overallInput
                  )
                }}
              >
                <label htmlFor="overall-limit">
                  Overall limit
                </label>

                <input
                  id="overall-limit"
                  type="number"
                  min="0.01"
                  step="0.01"
                  placeholder={
                    overall.limit == null
                      ? 'Enter limit'
                      : String(overall.limit)
                  }
                  value={overallInput}
                  onChange={e => setOverallInput(e.target.value)}
                  disabled={saving}
                  required
                />

                <button type="submit" disabled={saving}>
                  Save Overall Budget
                </button>
              </form>
            </div>

            <div className="bl-panel">
              <h3>Individual Category Budget</h3>

              {categories.length === 0 ? (
                <p>
                  No categories exist. Add categories
                  using the expense tracker above.
                </p>
              ) : (
                <>
                  <label className="bl-filter">
                    Select an existing category
                    <select
                      value={activeCategoryId}
                      disabled={saving}
                      onChange={e => {
                        setSelectedCategoryId(e.target.value)
                        setCategoryInput('')
                        setMessage('')
                      }}
                    >
                      {categories.map(category => (
                        <option
                          key={category.categoryId}
                          value={category.categoryId}
                        >
                          {category.categoryName}
                        </option>
                      ))}
                    </select>
                  </label>

                  {activeCategory && (
                    <>
                      <div className="bl-cards">
                        <div>
                          <span className="bl-card-label">
                            Category Limit
                          </span>
                          <strong className="bl-card-value">
                            {activeCategory.budget.limit == null
                              ? 'Not Set'
                              : money(activeCategory.budget.limit)}
                          </strong>
                        </div>

                        <div>
                          <span className="bl-card-label">
                            Spent So Far
                          </span>
                          <strong className="bl-card-value">
                            {money(activeCategory.budget.spent)}
                          </strong>
                        </div>

                        <div>
                          <span className="bl-card-label">
                            Remaining
                          </span>
                          <strong className="bl-card-value">
                            {activeCategory.budget.remaining == null
                              ? '—'
                              : money(activeCategory.budget.remaining)}
                          </strong>
                        </div>
                      </div>

                      <BudgetProgress
                        budget={activeCategory.budget}
                        money={money}
                      />

                      <form
                        className="bl-form"
                        onSubmit={e => {
                          e.preventDefault()
                          saveBudget(
                            `${apiBase}/budgets/monthly/categories/${
                              activeCategory.categoryId
                            }`,
                            categoryInput
                          )
                        }}
                      >
                        <label htmlFor="category-limit">
                          Set category limit
                        </label>

                        <input
                          id="category-limit"
                          type="number"
                          min="0.01"
                          step="0.01"
                          placeholder={
                            activeCategory.budget.limit == null
                              ? 'Enter limit'
                              : String(activeCategory.budget.limit)
                          }
                          value={categoryInput}
                          onChange={e =>
                            setCategoryInput(e.target.value)
                          }
                          disabled={saving}
                          required
                        />

                        <button type="submit" disabled={saving}>
                          Save Category Budget
                        </button>
                      </form>
                    </>
                  )}
                </>
              )}
            </div>
          </>
        )}

        <p className="bl-note">
        </p>
      </section>
    </>
  )
}
