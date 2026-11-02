
import { useEffect, useState } from 'react'

const css = `
  .ss {
    font-family: Arial, sans-serif;
    color: #242424;
    color-scheme: light;
    max-width: 1100px;
    margin: 20px auto;
    padding: 22px;
    background: #ffffff;
    border: 1px solid #cccccc;
    text-align: left;
  }

  .ss * {
    box-sizing: border-box;
  }

  .ss h2,
  .ss h3,
  .ss strong {
    color: #242424;
  }

  .ss-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 12px;
    border-bottom: 1px solid #dddddd;
    padding-bottom: 16px;
    margin-bottom: 18px;
  }

  .ss h2 {
    margin: 0 0 6px;
    font-size: 21px;
  }

  .ss h3 {
    font-size: 16px;
    margin: 0 0 14px;
  }

  .ss-muted {
    font-size: 13px;
    color: #666666;
    margin: 0;
    line-height: 1.5;
  }

  .ss button {
    background: #333333;
    color: #ffffff;
    border: 1px solid #333333;
    padding: 9px 16px;
    cursor: pointer;
    border-radius: 2px;
  }

  .ss button:disabled {
    opacity: 0.5;
    cursor: wait;
  }

  .ss-stats {
    display: grid;
    grid-template-columns: repeat(3, minmax(0, 1fr));
    gap: 12px;
    margin-bottom: 22px;
  }

  .ss-stat {
    background: #f4f4f4;
    border: 1px solid #d3d3d3;
    padding: 18px;
    min-width: 0;
  }

  .ss-stat span {
    display: block;
    font-size: 12px;
    color: #666666;
    margin-bottom: 10px;
  }

  .ss-stat strong {
    font-size: 24px;
    overflow-wrap: anywhere;
  }

  .ss-section {
    border: 1px solid #dddddd;
    padding: 18px;
    margin-bottom: 16px;
  }

  .ss-row {
    display: grid;
    grid-template-columns:
      minmax(100px, 1.5fr)
      minmax(80px, 1fr)
      72px
      minmax(100px, 1.5fr);
    gap: 12px;
    align-items: center;
    border-top: 1px solid #eeeeee;
    padding: 13px 0;
    font-size: 13px;
  }

  .ss-row-head {
    font-weight: bold;
    background: #eeeeee;
    border-top: 0;
    padding: 12px;
  }

  .ss-bar {
    background: #e5e5e5;
    height: 13px;
    min-width: 0;
  }

  .ss-fill {
    height: 100%;
    background: #555555;
  }

  .ss-detail {
    width: 100%;
    border-collapse: collapse;
    font-size: 13px;
    min-width: 470px;
  }

  .ss-detail th,
  .ss-detail td {
    padding: 12px 10px;
    border-bottom: 1px solid #dddddd;
    text-align: left;
  }

  .ss-detail th {
    background: #eeeeee;
  }

  .ss-detail td:not(:first-child),
  .ss-detail th:not(:first-child) {
    text-align: right;
  }

  .ss-scroll {
    overflow-x: auto;
  }

  .ss-error {
    background: #f4f4f4;
    border: 1px solid #999999;
    padding: 12px;
    color: #333333;
    margin: 0 0 16px;
    font-size: 13px;
  }

  @media (max-width: 700px) {
    .ss-stats {
      grid-template-columns: 1fr;
    }

    .ss-row {
      grid-template-columns: 1fr 1fr;
    }

    .ss-bar {
      grid-column: 1 / -1;
    }

    .ss-header {
      align-items: flex-start;
    }
  }
`

export default function SpendingSummary({
  apiBase = '/api',
  currency = 'EUR',
  refreshKey = 0
}) {
  const [summary, setSummary] = useState(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const [refresh, setRefresh] = useState(0)

  useEffect(() => {
    const controller = new AbortController()

    async function load() {
      setLoading(true)
      setError('')

      try {
        const response = await fetch(
          `${apiBase}/expenses/summary?currency=${encodeURIComponent(currency)}`,
          {
            signal: controller.signal,
            headers: {
              Accept: 'application/json'
            }
          }
        )

        const type =
          response.headers.get('content-type') || ''

        if (!type.includes('application/json')) {
          throw new Error(
            'Expected JSON. Check the backend and Vite proxy.'
          )
        }

        const body = await response.json()

        if (!response.ok) {
          throw new Error(
            body.error || `HTTP ${response.status}`
          )
        }

        if (!controller.signal.aborted) {
          setSummary(body)
        }

      } catch (err) {
        if (err.name !== 'AbortError') {
          setError(err.message)
          setSummary(null)
        }
      } finally {
        if (!controller.signal.aborted) {
          setLoading(false)
        }
      }
    }

    // Initial load.
    load()

    const interval = setInterval(load, 15000)

    return () => {
      controller.abort()
      clearInterval(interval)
    }

  }, [apiBase, currency, refreshKey, refresh])

  // Format money values.
  const money = value =>
    new Intl.NumberFormat('en-IE', {
      style: 'currency',
      currency
    }).format(Number(value ?? 0))

  const items = summary?.categories ?? []

  const total = Number(
    summary?.totalSpent ?? 0
  )

  const count = Number(
    summary?.transactionCount ?? 0
  )

  return (
    <>
      <style>{css}</style>

      <section className="ss">

        {/* Header */}
        <div className="ss-header">
          <div>
            <h2>Current Month Spending Summary</h2>

            <p className="ss-muted">
              {summary
                ? `${summary.from} to ${summary.to}`
                : 'Current calendar month'
              }
              {' '}· Actual recorded expenses
            </p>
          </div>

          <button
            type="button"
            disabled={loading}
            onClick={() =>
              setRefresh(value => value + 1)
            }
          >
            {loading ? 'Loading...' : 'Refresh'}
          </button>
        </div>

        {/* Errors */}
        {error && (
          <p
            className="ss-error"
            role="alert"
          >
            {error}
          </p>
        )}

        {/* Summary statistics */}
        <div className="ss-stats">

          <div className="ss-stat">
            <span>Total Spent</span>
            <strong>{money(total)}</strong>
          </div>

          <div className="ss-stat">
            <span>Transactions</span>
            <strong>{count}</strong>
          </div>

          <div className="ss-stat">
            <span>Average Expense</span>
            <strong>
              {money(count ? total / count : 0)}
            </strong>
          </div>

        </div>

        {/* Category breakdown */}
        <div className="ss-section">

          <h3>Spending by Category</h3>

          {items.length === 0 ? (
            <p className="ss-muted">
              No expenses recorded this month in {currency}.
            </p>
          ) : (
            <>
              <div className="ss-row ss-row-head">
                <span>Category</span>
                <span>Total</span>
                <span>Share</span>
                <span>Comparison</span>
              </div>

              {items.map(item => (
                <div
                  className="ss-row"
                  key={item.categoryId}
                >
                  <strong>
                    {item.categoryName ||
                      `Category ${item.categoryId.slice(0, 8)}`
                    }
                  </strong>

                  <span>
                    {money(item.totalSpent)}
                  </span>

                  <span>
                    {Number(
                      item.percentageOfTotal
                    ).toFixed(1)}%
                  </span>

                  <div className="ss-bar">
                    <div
                      className="ss-fill"
                      style={{
                        width: `${
                          Math.max(
                            0,
                            Math.min(
                              100,
                              Number(item.percentageOfTotal)
                            )
                          )
                        }%`
                      }}
                    />
                  </div>
                </div>
              ))}
            </>
          )}

        </div>

        {/* Detailed category information */}
        <div className="ss-section">

          <h3>Category Details</h3>

          <div className="ss-scroll">

            <table className="ss-detail">
              <thead>
                <tr>
                  <th>Category</th>
                  <th>Transactions</th>
                  <th>Average</th>
                  <th>Total</th>
                </tr>
              </thead>

              <tbody>
                {items.map(item => (
                  <tr key={item.categoryId}>
                    <td>
                      {item.categoryName ||
                        item.categoryId}
                    </td>

                    <td>
                      {item.transactionCount}
                    </td>

                    <td>
                      {money(item.averageTransaction)}
                    </td>

                    <td>
                      {money(item.totalSpent)}
                    </td>
                  </tr>
                ))}

                {items.length === 0 && (
                  <tr>
                    <td colSpan="4">
                      No category data.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>

          </div>

        </div>

        <p className="ss-muted">

        </p>

      </section>
    </>
  )
}
