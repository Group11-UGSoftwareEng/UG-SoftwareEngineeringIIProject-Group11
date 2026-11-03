
import { useCallback, useEffect, useState } from 'react'

// All Spending Summary CSS is contained in this file.
// No separate SpendingSummary.css is required.

const styles = `
  .spending-summary {
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

  .spending-summary * {
    box-sizing: border-box;
  }

  .spending-summary .ss-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 15px;
    padding-bottom: 18px;
    margin-bottom: 20px;
    border-bottom: 1px solid #cccccc;
  }

  .spending-summary .ss-header h2 {
    margin: 0 0 6px;
    color: #222222;
    font-size: 21px;
    font-weight: bold;
  }

  .spending-summary .ss-subtitle {
    color: #666666;
    font-size: 13px;
    margin: 0;
  }

  .spending-summary .ss-button {
    padding: 10px 16px;
    background: #333333;
    border: 1px solid #333333;
    color: #ffffff;
    border-radius: 2px;
    font-size: 13px;
    cursor: pointer;
  }

  .spending-summary .ss-button:hover {
    background: #555555;
  }

  .spending-summary .ss-button:disabled {
    opacity: 0.5;
    cursor: wait;
  }

  .spending-summary .ss-filters {
    display: flex;
    flex-wrap: wrap;
    gap: 16px;
    margin-bottom: 22px;
  }

  .spending-summary .ss-filter {
    display: flex;
    flex-direction: column;
    gap: 7px;
    font-size: 13px;
    color: #444444;
    font-weight: bold;
  }

  .spending-summary .ss-filter input,
  .spending-summary .ss-filter select {
    min-width: 155px;
    padding: 10px;
    background: #ffffff;
    border: 1px solid #cccccc;
    border-radius: 2px;
    color: #222222;
    font-family: inherit;
    font-size: 13px;
  }

  .spending-summary .ss-summary {
    display: grid;
    grid-template-columns: repeat(3, minmax(0, 1fr));
    gap: 12px;
    margin-bottom: 25px;
  }

  .spending-summary .ss-card {
    background: #f5f5f5;
    border: 1px solid #d5d5d5;
    padding: 20px;
    min-width: 0;
  }

  .spending-summary .ss-card-label {
    display: block;
    color: #555555;
    font-size: 13px;
    margin-bottom: 12px;
  }

  .spending-summary .ss-card-value {
    display: block;
    color: #222222;
    font-size: 25px;
    font-weight: bold;
    font-variant-numeric: tabular-nums;
    overflow-wrap: anywhere;
  }

  .spending-summary .ss-section-title {
    color: #222222;
    font-size: 16px;
    font-weight: bold;
    margin: 0 0 15px;
  }

  .spending-summary .ss-table {
    border: 1px solid #d5d5d5;
    width: 100%;
  }

  .spending-summary .ss-table-header,
  .spending-summary .ss-table-row {
    display: grid;
    grid-template-columns:
      minmax(110px, 1.3fr)
      minmax(80px, 1fr)
      minmax(65px, 0.7fr)
      minmax(90px, 1.5fr);
    align-items: center;
    gap: 12px;
    padding: 14px;
    font-size: 13px;
  }

  .spending-summary .ss-table-header {
    background: #eeeeee;
    border-bottom: 1px solid #cccccc;
    color: #333333;
    font-weight: bold;
  }

  .spending-summary .ss-table-row {
    background: #ffffff;
    border-bottom: 1px solid #eeeeee;
    color: #333333;
  }

  .spending-summary .ss-table-row:last-child {
    border-bottom: none;
  }

  .spending-summary .ss-category-name {
    color: #222222;
    font-weight: 600;
    overflow-wrap: anywhere;
  }

  .spending-summary .ss-amount {
    font-variant-numeric: tabular-nums;
    overflow-wrap: anywhere;
  }

  .spending-summary .ss-bar-background {
    width: 100%;
    height: 14px;
    background: #e6e6e6;
  }

  .spending-summary .ss-bar-fill {
    height: 100%;
    background: #555555;
    transition: width 0.2s ease;
  }

  .spending-summary .ss-details {
    margin-top: 22px;
  }

  .spending-summary .ss-detail-table {
    width: 100%;
    border-collapse: collapse;
    border: 1px solid #d5d5d5;
    font-size: 13px;
    color: #333333;
  }

  .spending-summary .ss-detail-table th {
    padding: 13px;
    background: #eeeeee;
    border-bottom: 1px solid #cccccc;
    color: #333333;
    text-align: left;
  }

  .spending-summary .ss-detail-table td {
    padding: 13px;
    border-bottom: 1px solid #eeeeee;
    text-align: left;
  }

  .spending-summary .ss-detail-table th:not(:first-child),
  .spending-summary .ss-detail-table td:not(:first-child) {
    text-align: right;
  }

  .spending-summary .ss-detail-scroll,
  .spending-summary .ss-table-scroll {
    overflow-x: auto;
  }

  .spending-summary .ss-note {
    margin-top: 16px;
    color: #666666;
    font-size: 12px;
    line-height: 1.6;
  }

  .spending-summary .ss-error {
    padding: 14px;
    background: #f5f5f5;
    border: 1px solid #aaaaaa;
    color: #333333;
    font-size: 13px;
    margin-bottom: 15px;
  }

  .spending-summary .ss-empty {
    border: 1px solid #d5d5d5;
    padding: 20px;
    background: #f5f5f5;
    color: #555555;
    font-size: 13px;
  }

  .spending-summary .ss-loading {
    color: #666666;
    font-size: 13px;
  }

  @media (max-width: 750px) {
    .spending-summary .ss-summary {
      grid-template-columns: 1fr;
    }

    .spending-summary .ss-table-header,
    .spending-summary .ss-table-row {
      grid-template-columns: 1fr 1fr;
    }

    .spending-summary .ss-bar-background {
      grid-column: 1 / -1;
    }
  }

  @media (max-width: 450px) {
    .spending-summary {
      padding: 14px;
    }

    .spending-summary .ss-header {
      align-items: flex-start;
      flex-direction: column;
    }

    .spending-summary .ss-filter,
    .spending-summary .ss-filter input,
    .spending-summary .ss-filter select {
      width: 100%;
    }
  }
`

function formatDate(date) {
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')

  return `${year}-${month}-${day}`
}

const today = new Date()

const initialFrom = formatDate(
  new Date(today.getFullYear(), today.getMonth(), 1)
)

const initialTo = formatDate(today)

export default function SpendingSummary({
  apiBase = '/api',
  refreshKey = 0
}) {
  const [from, setFrom] = useState(initialFrom)
  const [to, setTo] = useState(initialTo)
  const [currency, setCurrency] = useState('EUR')

  const [summary, setSummary] = useState(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const [reload, setReload] = useState(0)

  const loadSummary = useCallback(async (signal) => {
    if (!from || !to || from > to) {
      setError('Please select a valid date range.')
      setSummary(null)
      return
    }

    setLoading(true)
    setError('')

    try {
      const params = new URLSearchParams({
        from,
        to,
        currency
      })

      const response = await fetch(
        `${apiBase}/expenses/summary?${params.toString()}`,
        {
          signal,
          headers: {
            Accept: 'application/json'
          },
          cache: 'no-store'
        }
      )

      const contentType =
        response.headers.get('content-type') || ''

      if (!contentType.includes('application/json')) {
        throw new Error(
          'The server did not return JSON. ' +
          'Check that Spring Boot is running on port 8080 ' +
          'and that the Vite API proxy is configured.'
        )
      }

      const result = await response.json()

      if (!response.ok) {
        throw new Error(
          result.error ||
          `Summary request failed: HTTP ${response.status}`
        )
      }

      if (!signal.aborted) {
        setSummary(result)
      }
    } catch (err) {
      if (err.name !== 'AbortError' && !signal.aborted) {
        setError(
          err.message || 'Unable to retrieve spending summary.'
        )
        setSummary(null)
      }
    } finally {
      if (!signal.aborted) {
        setLoading(false)
      }
    }
  }, [apiBase, from, to, currency])

  useEffect(() => {
    const controller = new AbortController()

    loadSummary(controller.signal)

    const interval = setInterval(() => {
      loadSummary(controller.signal)
    }, 15000)

    return () => {
      controller.abort()
      clearInterval(interval)
    }
  }, [loadSummary, refreshKey, reload])

  function refreshSummary() {
    setReload(value => value + 1)
  }

  function formatMoney(value) {
    return new Intl.NumberFormat('en-IE', {
      style: 'currency',
      currency
    }).format(Number(value ?? 0))
  }

  const categories = summary?.categories ?? []
  const totalSpent = Number(summary?.totalSpent ?? 0)
  const transactions = Number(summary?.transactionCount ?? 0)

  const averageExpense =
    transactions > 0
      ? totalSpent / transactions
      : 0

  return (
    <>
      <style>{styles}</style>

      <section className="spending-summary">

        {/* Header */}
        <div className="ss-header">
          <div>
            <h2>Spending Summary</h2>

            <p className="ss-subtitle">
              {from} to {to}
              {' | '}
              Spending by category
            </p>
          </div>

          <button
            className="ss-button"
            type="button"
            disabled={loading}
            onClick={refreshSummary}
          >
            {loading ? 'Loading...' : 'Refresh'}
          </button>
        </div>

        {/* Date and currency filters */}
        <div className="ss-filters">
          <label className="ss-filter">
            Start Date
            <input
              type="date"
              value={from}
              onChange={event =>
                setFrom(event.target.value)
              }
            />
          </label>

          <label className="ss-filter">
            End Date
            <input
              type="date"
              value={to}
              onChange={event =>
                setTo(event.target.value)
              }
            />
          </label>

          <label className="ss-filter">
            Currency
            <select
              value={currency}
              onChange={event =>
                setCurrency(event.target.value)
              }
            >
              <option value="EUR">EUR</option>
              <option value="USD">USD</option>
              <option value="GBP">GBP</option>
            </select>
          </label>
        </div>

        {/* Error message */}
        {error && (
          <div className="ss-error" role="alert">
            {error}
          </div>
        )}

        {loading && !summary && !error && (
          <p className="ss-loading">
            Loading spending information...
          </p>
        )}

        {/* Summary results */}
        {summary && (
          <>

            {/* Three summary cards */}
            <div className="ss-summary">

              <div className="ss-card">
                <span className="ss-card-label">
                  Total Spent
                </span>

                <strong className="ss-card-value">
                  {formatMoney(totalSpent)}
                </strong>
              </div>

              <div className="ss-card">
                <span className="ss-card-label">
                  Total Transactions
                </span>

                <strong className="ss-card-value">
                  {transactions}
                </strong>
              </div>

              <div className="ss-card">
                <span className="ss-card-label">
                  Average Expense
                </span>

                <strong className="ss-card-value">
                  {formatMoney(averageExpense)}
                </strong>
              </div>

            </div>

            {/* Category breakdown */}
            <h3 className="ss-section-title">
              Spending by Category
            </h3>

            {categories.length === 0 ? (
              <div className="ss-empty">
                No expenses were recorded for the
                selected date range and currency.
                <p>
                  Total expenditure: {formatMoney(0)}
                </p>
              </div>
            ) : (
              <div className="ss-table-scroll">
                <div className="ss-table">

                  <div className="ss-table-header">
                    <span>Category</span>
                    <span>Spent</span>
                    <span>Share</span>
                    <span>Comparison</span>
                  </div>

                  {categories.map(category => {
                    const share = Number(
                      category.percentageOfTotal ?? 0
                    )

                    return (
                      <div
                        className="ss-table-row"
                        key={category.categoryId}
                      >
                        <span className="ss-category-name">
                          {category.categoryName ||
                            'Uncategorized'}
                        </span>

                        <span className="ss-amount">
                          {formatMoney(category.totalSpent)}
                        </span>

                        <span className="ss-amount">
                          {share.toFixed(1)}%
                        </span>

                        <div
                          className="ss-bar-background"
                          title={`${category.categoryName}: ${share.toFixed(1)}%`}
                        >
                          <div
                            className="ss-bar-fill"
                            style={{
                              width: `${Math.max(
                                0,
                                Math.min(100, share)
                              )}%`
                            }}
                          />
                        </div>
                      </div>
                    )
                  })}

                </div>
              </div>
            )}

            {/* Detailed category statistics */}
            {categories.length > 0 && (
              <div className="ss-details">

                <h3 className="ss-section-title">
                  Category Details
                </h3>

                <div className="ss-detail-scroll">
                  <table className="ss-detail-table">
                    <thead>
                      <tr>
                        <th>Category</th>
                        <th>Transactions</th>
                        <th>Average</th>
                        <th>Total</th>
                      </tr>
                    </thead>

                    <tbody>
                      {categories.map(category => (
                        <tr key={category.categoryId}>
                          <td>
                            {category.categoryName ||
                              'Uncategorized'}
                          </td>

                          <td>
                            {category.transactionCount}
                          </td>

                          <td>
                            {formatMoney(
                              category.averageTransaction
                            )}
                          </td>

                          <td>
                            {formatMoney(
                              category.totalSpent
                            )}
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>

              </div>
            )}

            <div className="ss-note">
              <p>
                All figures are calculated from saved
                expenses in the selected currency.
                Different currencies are not combined.
              </p>

              <p>
                Refresh to retrieve the latest expenses.
                The summary also refreshes automatically
                every 15 seconds.
              </p>
            </div>

          </>
        )}

      </section>
    </>
  )
}
