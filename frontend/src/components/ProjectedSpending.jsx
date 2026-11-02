
import { useEffect, useState } from 'react'

const styles = `
  .projected-spending {
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

  .projected-spending * {
    box-sizing: border-box;
  }

  .ps-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 15px;
    padding-bottom: 18px;
    margin-bottom: 20px;
    border-bottom: 1px solid #cccccc;
  }

  .ps-header h2 {
    margin: 0 0 6px;
    color: #222222;
    font-size: 21px;
    font-weight: bold;
  }

  .ps-subtitle {
    color: #666666;
    font-size: 13px;
    margin: 0;
  }

  .ps-button {
    padding: 10px 16px;
    background: #333333;
    border: 1px solid #333333;
    color: white;
    border-radius: 2px;
    font-size: 13px;
    cursor: pointer;
  }

  .ps-button:hover {
    background: #555555;
  }

  .ps-button:disabled {
    opacity: 0.5;
    cursor: wait;
  }

  .ps-summary {
    display: grid;
    grid-template-columns: repeat(3, minmax(0, 1fr));
    gap: 12px;
    margin-bottom: 25px;
  }

  .ps-card {
    background: #f5f5f5;
    border: 1px solid #d5d5d5;
    padding: 20px;
    min-width: 0;
  }

  .ps-card-label {
    display: block;
    color: #555555;
    font-size: 13px;
    margin-bottom: 12px;
  }

  .ps-card-value {
    display: block;
    color: #222222;
    font-size: 25px;
    font-weight: bold;
    font-variant-numeric: tabular-nums;
    overflow-wrap: anywhere;
  }

  .ps-section-title {
    color: #222222;
    font-size: 16px;
    font-weight: bold;
    margin: 0 0 15px;
  }

  .ps-table {
    border: 1px solid #d5d5d5;
    width: 100%;
  }

  .ps-table-header,
  .ps-table-row {
    display: grid;
    grid-template-columns: minmax(110px, 1.3fr)
      minmax(80px, 1fr)
      minmax(80px, 1fr)
      minmax(90px, 1.5fr);
    align-items: center;
    gap: 12px;
    padding: 14px;
    font-size: 13px;
  }

  .ps-table-header {
    background: #eeeeee;
    border-bottom: 1px solid #cccccc;
    color: #333333;
    font-weight: bold;
  }

  .ps-table-row {
    background: #ffffff;
    border-bottom: 1px solid #eeeeee;
    color: #333333;
  }

  .ps-table-row:last-child {
    border-bottom: none;
  }

  .ps-category-name {
    color: #222222;
    font-weight: 600;
    overflow-wrap: anywhere;
  }

  .ps-amount {
    font-variant-numeric: tabular-nums;
    overflow-wrap: anywhere;
  }

  .ps-bar-background {
    width: 100%;
    height: 14px;
    background: #e6e6e6;
  }

  .ps-bar-fill {
    height: 100%;
    background: #555555;
    transition: width 0.2s ease;
  }

  .ps-note {
    margin-top: 16px;
    color: #666666;
    font-size: 12px;
    line-height: 1.6;
  }

  .ps-error {
    padding: 14px;
    background: #f5f5f5;
    border: 1px solid #aaaaaa;
    color: #333333;
    font-size: 13px;
    margin-bottom: 15px;
  }

  .ps-empty {
    border: 1px solid #d5d5d5;
    padding: 20px;
    background: #f5f5f5;
    color: #555555;
    font-size: 13px;
  }

  .ps-loading {
    color: #666666;
    font-size: 13px;
  }

  @media (max-width: 750px) {
    .ps-summary {
      grid-template-columns: 1fr;
    }

    .ps-table-header,
    .ps-table-row {
      grid-template-columns: 1fr 1fr;
    }

    .ps-bar-background {
      grid-column: 1 / -1;
    }
  }

  @media (max-width: 450px) {
    .projected-spending {
      padding: 14px;
    }

    .ps-header {
      align-items: flex-start;
      flex-direction: column;
    }
  }
`

export default function ProjectedSpending({
  apiBase = '/api',
  currency = 'EUR',
  refreshKey = 0
}) {
  const [forecast, setForecast] = useState(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const [reload, setReload] = useState(0)

  // Fetch the existing expenses through the forecast API.
  useEffect(() => {
    const controller = new AbortController()

    async function loadForecast() {
      setLoading(true)
      setError('')

      try {
        const response = await fetch(
          `${apiBase}/expenses/forecast?currency=${encodeURIComponent(currency)}`,
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
            'The server returned HTML instead of JSON. Check that Spring Boot is running on port 8080 or 808 and that the Vite proxy points to the correct port.'
          )
        }

        const result = await response.json()

        if (!response.ok) {
          throw new Error(
            result.error ||
            `Forecast request failed: HTTP ${response.status}`
          )
        }

        if (!controller.signal.aborted) {
          setForecast(result)
        }
      } catch (err) {
        if (err.name !== 'AbortError') {
          setForecast(null)
          setError(
            err.message || 'Unable to retrieve spending forecast.'
          )
        }
      } finally {
        if (!controller.signal.aborted) {
          setLoading(false)
        }
      }
    }

    loadForecast()

    return () => controller.abort()
  }, [apiBase, currency, refreshKey, reload])

  // Format values as currency.
  function formatMoney(value) {
    return new Intl.NumberFormat('en-IE', {
      style: 'currency',
      currency
    }).format(Number(value ?? 0))
  }

  const categories = forecast?.categories ?? []

  const highestProjection = Math.max(
    1,
    ...categories.map(category =>
      Number(category.projectedMonthEnd ?? 0)
    )
  )

  return (
    <>
      <style>{styles}</style>

      <section className="projected-spending">

        <div className="ps-header">
          <div>
            <h2>Projected Spending</h2>
            <p className="ps-subtitle">
              {forecast?.month || 'Current Month'} |
              {' '}Category-based spending forecast
            </p>
          </div>

          <button
            className="ps-button"
            type="button"
            disabled={loading}
            onClick={() => setReload(value => value + 1)}
          >
            {loading ? 'Loading...' : 'Refresh'}
          </button>
        </div>

        {error && (
          <div className="ps-error" role="alert">
            {error}
          </div>
        )}

        {loading && !forecast && (
          <p className="ps-loading">
            Loading spending information...
          </p>
        )}

        {forecast && (
          <>
            {/* Overall month projection */}
            <div className="ps-summary">

              <div className="ps-card">
                <span className="ps-card-label">
                  Spent This Month
                </span>
                <strong className="ps-card-value">
                  {formatMoney(forecast.currentMonthSpent)}
                </strong>
              </div>

              <div className="ps-card">
                <span className="ps-card-label">
                  Projected Month-End
                </span>
                <strong className="ps-card-value">
                  {formatMoney(forecast.projectedCurrentMonth)}
                </strong>
              </div>

              <div className="ps-card">
                <span className="ps-card-label">
                  Estimated Remaining Spending
                </span>
                <strong className="ps-card-value">
                  {formatMoney(forecast.estimatedRemaining)}
                </strong>
              </div>

            </div>

            {/* Category breakdown */}
            <h3 className="ps-section-title">
              Spending by Category
            </h3>

            {categories.length === 0 ? (
              <div className="ps-empty">
                No expenses have been recorded this month.
                Add expenses through the existing API to
                generate a projection.
              </div>
            ) : (
              <div className="ps-table">

                <div className="ps-table-header">
                  <span>Category</span>
                  <span>Spent</span>
                  <span>Projected</span>
                  <span>Comparison</span>
                </div>

                {categories.map(category => {
                  const projected = Number(
                    category.projectedMonthEnd ?? 0
                  )

                  const percentage =
                    (projected / highestProjection) * 100

                  return (
                    <div
                      className="ps-table-row"
                      key={category.categoryId}
                    >
                      <span className="ps-category-name">
                        {category.categoryName}
                      </span>

                      <span className="ps-amount">
                        {formatMoney(category.spent)}
                      </span>

                      <span className="ps-amount">
                        {formatMoney(projected)}
                      </span>

                      <div
                        className="ps-bar-background"
                        title={`${category.categoryName}: ${formatMoney(projected)}`}
                      >
                        <div
                          className="ps-bar-fill"
                          style={{
                            width: `${Math.min(
                              100,
                              Math.max(0, percentage)
                            )}%`
                          }}
                        />
                      </div>
                    </div>
                  )
                })}

              </div>
            )}

            <div className="ps-note">
              <p>
                {forecast.note}
              </p>
              <p>
                Based on expenses recorded during the current
                month. Projections use the average amount spent
                per elapsed day to estimate the total at month-end.
              </p>
            </div>

          </>
        )}
      </section>
    </>
  )
}
