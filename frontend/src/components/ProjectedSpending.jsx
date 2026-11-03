
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

  .projected-spending .ps-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 15px;
    padding-bottom: 18px;
    margin-bottom: 20px;
    border-bottom: 1px solid #cccccc;
  }

  .projected-spending .ps-header h2 {
    margin: 0 0 6px;
    color: #222222;
    font-size: 21px;
    font-weight: bold;
  }

  .projected-spending .ps-subtitle {
    color: #666666;
    font-size: 13px;
    margin: 0;
  }

  .projected-spending .ps-button {
    padding: 10px 16px;
    background: #333333;
    border: 1px solid #333333;
    color: #ffffff;
    border-radius: 2px;
    font-size: 13px;
    cursor: pointer;
  }

  .projected-spending .ps-button:hover {
    background: #555555;
  }

  .projected-spending .ps-button:disabled {
    opacity: 0.5;
    cursor: wait;
  }

  .projected-spending .ps-currency {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-top: 12px;
    color: #444444;
    font-size: 13px;
  }

  .projected-spending .ps-currency select {
    border: 1px solid #cccccc;
    background: white;
    color: #222222;
    padding: 7px;
    border-radius: 2px;
    font-size: 13px;
  }

  .projected-spending .ps-summary {
    display: grid;
    grid-template-columns: repeat(3, minmax(0, 1fr));
    gap: 12px;
    margin-bottom: 25px;
  }

  .projected-spending .ps-card {
    background: #f5f5f5;
    border: 1px solid #d5d5d5;
    padding: 20px;
    min-width: 0;
  }

  .projected-spending .ps-card-label {
    display: block;
    color: #555555;
    font-size: 13px;
    margin-bottom: 12px;
  }

  .projected-spending .ps-card-value {
    display: block;
    color: #222222;
    font-size: 25px;
    font-weight: bold;
    font-variant-numeric: tabular-nums;
    overflow-wrap: anywhere;
  }

  .projected-spending .ps-section-title {
    color: #222222;
    font-size: 16px;
    font-weight: bold;
    margin: 0 0 15px;
  }

  .projected-spending .ps-table {
    border: 1px solid #d5d5d5;
    width: 100%;
  }

  .projected-spending .ps-table-header,
  .projected-spending .ps-table-row {
    display: grid;
    grid-template-columns:
      minmax(110px, 1.3fr)
      minmax(80px, 1fr)
      minmax(80px, 1fr)
      minmax(90px, 1.5fr);
    align-items: center;
    gap: 12px;
    padding: 14px;
    font-size: 13px;
  }

  .projected-spending .ps-table-header {
    background: #eeeeee;
    border-bottom: 1px solid #cccccc;
    color: #333333;
    font-weight: bold;
  }

  .projected-spending .ps-table-row {
    background: #ffffff;
    border-bottom: 1px solid #eeeeee;
    color: #333333;
  }

  .projected-spending .ps-table-row:last-child {
    border-bottom: none;
  }

  .projected-spending .ps-category-name {
    color: #222222;
    font-weight: 600;
    overflow-wrap: anywhere;
  }

  .projected-spending .ps-amount {
    font-variant-numeric: tabular-nums;
    overflow-wrap: anywhere;
  }

  .projected-spending .ps-bar-background {
    width: 100%;
    height: 14px;
    background: #e6e6e6;
  }

  .projected-spending .ps-bar-fill {
    height: 100%;
    background: #555555;
    transition: width 0.2s ease;
  }

  .projected-spending .ps-note {
    margin-top: 16px;
    color: #666666;
    font-size: 12px;
    line-height: 1.6;
  }

  .projected-spending .ps-error {
    padding: 14px;
    background: #f5f5f5;
    border: 1px solid #aaaaaa;
    color: #333333;
    font-size: 13px;
    margin-bottom: 15px;
  }

  .projected-spending .ps-empty {
    border: 1px solid #d5d5d5;
    padding: 20px;
    background: #f5f5f5;
    color: #555555;
    font-size: 13px;
  }

  .projected-spending .ps-loading {
    color: #666666;
    font-size: 13px;
  }

  @media (max-width: 750px) {
    .projected-spending .ps-summary {
      grid-template-columns: 1fr;
    }

    .projected-spending .ps-table-header,
    .projected-spending .ps-table-row {
      grid-template-columns: 1fr 1fr;
    }

    .projected-spending .ps-bar-background {
      grid-column: 1 / -1;
    }
  }

  @media (max-width: 450px) {
    .projected-spending {
      padding: 14px;
    }

    .projected-spending .ps-header {
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

  const [selectedCurrency, setSelectedCurrency] =
    useState(currency)

  // Load the latest forecast from the backend.
  useEffect(() => {

    const controller = new AbortController()

    async function loadForecast() {
      if (controller.signal.aborted) return

      setLoading(true)
      setError('')

      try {
        const response = await fetch(
          `${apiBase}/expenses/forecast?currency=${encodeURIComponent(selectedCurrency)}`,
          {
            signal: controller.signal,
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
            'that the API proxy is configured.'
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

        if (
          err.name !== 'AbortError' &&
          !controller.signal.aborted
        ) {
          setForecast(null)
          setError(
            err.message ||
            'Unable to retrieve spending forecast.'
          )
        }

      } finally {
        if (!controller.signal.aborted) {
          setLoading(false)
        }
      }
    }

    loadForecast()

    // Check the database for updates every 15 seconds.
    const timer = setInterval(loadForecast, 15000)

    return () => {
      clearInterval(timer)
      controller.abort()
    }

  }, [apiBase, selectedCurrency, refreshKey, reload])

  // Format all money amounts in the selected currency.
  function formatMoney(value) {
    return new Intl.NumberFormat('en-IE', {
      style: 'currency',
      currency: selectedCurrency
    }).format(Number(value ?? 0))
  }

  const categories = forecast?.categories ?? []

  const highestProjection = Math.max(
    1,
    ...categories.map(category =>
      Number(category.projectedMonthEnd ?? 0)
    )
  )

  function refreshForecast() {
    setReload(value => value + 1)
  }

  return (
    <>
      <style>{styles}</style>

      <section className="projected-spending">

        {/* Header */}
        <div className="ps-header">

          <div>
            <h2>Projected Spending</h2>

            <p className="ps-subtitle">
              {forecast?.month || 'Current Month'}
              {' | '}
              Category-based spending forecast
            </p>

            <label className="ps-currency">
              Currency:

              <select
                value={selectedCurrency}
                onChange={event =>
                  setSelectedCurrency(event.target.value)
                }
              >
                <option value="EUR">EUR</option>
                <option value="USD">USD</option>
                <option value="GBP">GBP</option>
              </select>
            </label>
          </div>

          <button
            className="ps-button"
            type="button"
            disabled={loading}
            onClick={refreshForecast}
          >
            {loading ? 'Loading...' : 'Refresh'}
          </button>

        </div>

        {/* API errors */}
        {error && (
          <div className="ps-error" role="alert">
            {error}
          </div>
        )}

        {loading && !forecast && !error && (
          <p className="ps-loading" role="status">
            Loading spending forecast...
          </p>
        )}

        {/* Forecast data */}
        {forecast && (
          <>

            {/* Three summary cards */}
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
                No expenses have been recorded this month
                in {selectedCurrency}.

                <p>
                  Add expenses through the existing app
                  to generate a projection.
                </p>
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
                        {category.categoryName ||
                          'Uncategorized'}
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

            {/* Explanation */}
            <div className="ps-note">
              <p>
                {forecast.note}
              </p>

              <p>
              </p>

              <p>
              </p>
            </div>

          </>
        )}

      </section>
    </>
  )
}
