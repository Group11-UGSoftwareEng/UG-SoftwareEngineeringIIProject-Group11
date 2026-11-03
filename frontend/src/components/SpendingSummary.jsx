
import { useEffect, useState } from 'react'

const dateInput = (d) => [d.getFullYear(), String(d.getMonth() + 1).padStart(2, '0'), String(d.getDate()).padStart(2, '0')].join('-')
const startingToday = new Date()

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
  }

  .spending-summary * {
    box-sizing: border-box;
  }

  .ss-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 15px;
    padding-bottom: 18px;
    margin-bottom: 20px;
    border-bottom: 1px solid #cccccc;
  }

  .ss-header h2 {
    margin: 0 0 6px;
    color: #222222;
    font-size: 21px;
    font-weight: bold;
  }

  .ss-subtitle {
    color: #666666;
    font-size: 13px;
    margin: 0;
  }

  .ss-button {
    padding: 10px 16px;
    background: #333333;
    border: 1px solid #333333;
    color: #ffffff;
    border-radius: 2px;
    font-size: 13px;
    cursor: pointer;
  }

  .ss-button:hover {
    background: #555555;
  }

  .ss-button:disabled {
    opacity: 0.5;
    cursor: wait;
  }

  .ss-summary {
    display: grid;
    grid-template-columns: repeat(3, minmax(0, 1fr));
    gap: 12px;
    margin-bottom: 25px;
  }

  .ss-card {
    background: #f5f5f5;
    border: 1px solid #d5d5d5;
    padding: 20px;
    min-width: 0;
  }

  .ss-card-label {
    display: block;
    color: #555555;
    font-size: 13px;
    margin-bottom: 12px;
  }

  .ss-card-value {
    display: block;
    color: #222222;
    font-size: 25px;
    font-weight: bold;
    font-variant-numeric: tabular-nums;
    overflow-wrap: anywhere;
  }

  .ss-section-title {
    color: #222222;
    font-size: 16px;
    font-weight: bold;
    margin: 0 0 15px;
  }

  .ss-table {
    border: 1px solid #d5d5d5;
    width: 100%;
  }

  .ss-table-header,
  .ss-table-row {
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

  .ss-table-header {
    background: #eeeeee;
    border-bottom: 1px solid #cccccc;
    color: #333333;
    font-weight: bold;
  }

  .ss-table-row {
    background: #ffffff;
    border-bottom: 1px solid #eeeeee;
    color: #333333;
  }

  .ss-table-row:last-child {
    border-bottom: none;
  }

  .ss-category-name {
    color: #222222;
    font-weight: 600;
    overflow-wrap: anywhere;
  }

  .ss-amount {
    font-variant-numeric: tabular-nums;
    overflow-wrap: anywhere;
  }

  .ss-bar-background {
    width: 100%;
    height: 14px;
    background: #e6e6e6;
  }

  .ss-bar-fill {
    height: 100%;
    background: #555555;
    transition: width 0.2s ease;
  }

  .ss-details {
    margin-top: 22px;
  }

  .ss-detail-table {
    width: 100%;
    border-collapse: collapse;
    border: 1px solid #d5d5d5;
    font-size: 13px;
    color: #333333;
  }

  .ss-detail-table th {
    padding: 13px;
    background: #eeeeee;
    border-bottom: 1px solid #cccccc;
    color: #333333;
    text-align: left;
  }

  .ss-detail-table td {
    padding: 13px;
    border-bottom: 1px solid #eeeeee;
    text-align: left;
  }

  .ss-detail-table th:not(:first-child),
  .ss-detail-table td:not(:first-child) {
    text-align: right;
  }

  .ss-detail-scroll {
    overflow-x: auto;
  }

  .ss-note {
    margin-top: 16px;
    color: #666666;
    font-size: 12px;
    line-height: 1.6;
  }

  .ss-error {
    padding: 14px;
    background: #f5f5f5;
    border: 1px solid #aaaaaa;
    color: #333333;
    font-size: 13px;
    margin-bottom: 15px;
  }

  .ss-empty {
    border: 1px solid #d5d5d5;
    padding: 20px;
    background: #f5f5f5;
    color: #555555;
    font-size: 13px;
  }

  .ss-loading {
    color: #666666;
    font-size: 13px;
  }

  @media (max-width: 750px) {
    .ss-summary {
      grid-template-columns: 1fr;
    }

    .ss-table-header,
    .ss-table-row {
      grid-template-columns: 1fr 1fr;
    }

    .ss-bar-background {
      grid-column: 1 / -1;
    }
  }

  @media (max-width: 450px) {
    .spending-summary {
      padding: 14px;
    }

    .ss-header {
      align-items: flex-start;
      flex-direction: column;
    }
  }
`

export default function SpendingSummary({
  apiBase = '/api',
  refreshKey = 0
}) {
  const [from, setFrom] = useState(dateInput(new Date(startingToday.getFullYear(), startingToday.getMonth(), 1)))
  const [to, setTo] = useState(dateInput(startingToday))
  const [currency, setCurrency] = useState('EUR')
  const [summary, setSummary] = useState(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const [reload, setReload] = useState(0)

  // Load this month's recorded expenses from Spring Boot.
  useEffect(() => {
    const controller = new AbortController()

    async function loadSummary() {
      if (controller.signal.aborted) return
      if (!from || !to || from > to) {
        setError('Choose a valid start and end date.')
        setSummary(null)
        return
      }
      setLoading(true)
      setError('')

      try {
        const response = await fetch(
          `${apiBase}/expenses/summary?${new URLSearchParams({ from, to, currency })}`,
          {
            signal: controller.signal,
            headers: {
              Accept: 'application/json'
            }
          }
        )

        const contentType =
          response.headers.get('content-type') || ''

        if (!contentType.includes('application/json')) {
          throw new Error(
            'The server returned HTML instead of JSON. ' +
            'Check that Spring Boot is running on port 8080 ' +
            'and that the Vite proxy is configured.'
          )
        }

        const result = await response.json()

        if (!response.ok) {
          throw new Error(
            result.error ||
            `Summary request failed: HTTP ${response.status}`
          )
        }

        if (!controller.signal.aborted) {
          setSummary(result)
        }

      } catch (err) {
        if (err.name !== 'AbortError') {
          setError(
            err.message || 'Unable to retrieve spending summary.'
          )
        }

      } finally {
        if (!controller.signal.aborted) {
          setLoading(false)
        }
      }
    }

    loadSummary()

    // Refresh data from Postman or other clients every 15 seconds.
    const interval = setInterval(loadSummary, 15000)

    return () => {
      controller.abort()
      clearInterval(interval)
    }

  }, [apiBase, from, to, currency, refreshKey, reload])

  function formatMoney(value) {
    return new Intl.NumberFormat('en-IE', {
      style: 'currency',
      currency
    }).format(Number(value ?? 0))
  }

  const categories = summary?.categories ?? []

  const totalSpent = Number(summary?.totalSpent ?? 0)

  const transactions = Number(
    summary?.transactionCount ?? 0
  )

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
              {summary?.from
                ? `${summary.from} to ${summary.to}`
                : 'Current Month'}
              {' | '}
              Spending by category
            </p>
          </div>

          <button
            className="ss-button"
            type="button"
            disabled={loading}
            onClick={() =>
              setReload(value => value + 1)
            }
          >
            {loading ? 'Loading...' : 'Refresh'}
          </button>
        </div>

        {/* Date and currency controls */}
        <div className="ss-period-controls" style={{ display: 'flex', flexWrap: 'wrap', gap: 12, marginBottom: 20 }}>
          <label style={{ display: 'grid', gap: 5, color: '#222' }}>
            Start date
            <input type="date" value={from} onChange={e => setFrom(e.target.value)} style={{ color: '#222', background: '#fff', padding: 8 }} />
          </label>
          <label style={{ display: 'grid', gap: 5, color: '#222' }}>
            End date
            <input type="date" value={to} onChange={e => setTo(e.target.value)} style={{ color: '#222', background: '#fff', padding: 8 }} />
          </label>
          <label style={{ display: 'grid', gap: 5, color: '#222' }}>
            Currency
            <select value={currency} onChange={e => setCurrency(e.target.value)} style={{ color: '#222', background: '#fff', padding: 8 }}>
              <option value="EUR">EUR</option>
              <option value="USD">USD</option>
              <option value="GBP">GBP</option>
            </select>
          </label>
        </div>

        {error && (
          <div className="ss-error" role="alert">
            {error}
          </div>
        )}

        {loading && !summary && (
          <p className="ss-loading">
            Loading spending information...
          </p>
        )}

        {summary && (
          <>
            {/* Three summary cards */}
            <div className="ss-summary">

              <div className="ss-card">
                <span className="ss-card-label">
                  Spent This Month
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
                No expenses were recorded in the selected period and currency. Total spending is zero.
              </div>
            ) : (
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
                          `Category ${String(
                            category.categoryId
                          ).slice(0, 8)}`}
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
              </p>
            </div>

          </>
        )}

      </section>
    </>
  )
}
