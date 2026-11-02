
import { useEffect, useState } from 'react';

// ---------- STYLING ----------

const styles = `
  .projected-spending {
    --ps-white: #ffffff;
    --ps-background: #f4f4f4;
    --ps-border: #d4d4d4;
    --ps-text: #222222;
    --ps-muted: #666666;
    --ps-accent: #424b57;

    width: 100%;
    max-width: 1100px;
    margin: 24px auto;
    padding: 24px;
    background: var(--ps-white);
    border: 1px solid var(--ps-border);
    border-radius: 3px;
    color: var(--ps-text);
    color-scheme: light;
    font-family: Arial, Helvetica, sans-serif;
    text-align: left;
  }

  .projected-spending * {
    box-sizing: border-box;
  }

  .projected-spending h2,
  .projected-spending h3,
  .projected-spending strong {
    color: var(--ps-text);
  }

  .projected-spending h2 {
    font-size: 22px;
    font-weight: 700;
    margin: 0 0 6px;
  }

  .projected-spending h3 {
    font-size: 15px;
    font-weight: 700;
    margin: 0 0 16px;
  }

  .ps-description {
    color: var(--ps-muted);
    font-size: 13px;
    margin: 0;
  }

  .ps-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 16px;
    padding-bottom: 20px;
    border-bottom: 1px solid var(--ps-border);
  }

  .ps-button {
    padding: 10px 18px;
    background: var(--ps-accent);
    border: 1px solid var(--ps-accent);
    border-radius: 2px;
    color: #ffffff;
    font-size: 13px;
    font-weight: 600;
    cursor: pointer;
  }

  .ps-button:hover {
    background: #303640;
  }

  .ps-button:disabled {
    opacity: 0.5;
    cursor: wait;
  }

  .ps-inputs {
    display: grid;
    grid-template-columns: repeat(3, minmax(0, 1fr));
    gap: 12px;
    padding: 20px 0;
  }

  .ps-inputs label {
    display: flex;
    flex-direction: column;
    gap: 7px;
    color: var(--ps-text);
    font-size: 12px;
    font-weight: 600;
  }

  .ps-inputs input {
    width: 100%;
    min-width: 0;
    padding: 11px 12px;
    background: #ffffff;
    border: 1px solid #bcbcbc;
    border-radius: 2px;
    color: var(--ps-text);
    font-size: 14px;
    font-family: inherit;
  }

  .ps-inputs input:focus {
    outline: 2px solid #888888;
    outline-offset: 1px;
  }

  .ps-error {
    padding: 12px;
    margin: 0 0 15px;
    border: 1px solid #aaaaaa;
    background: #f4f4f4;
    color: #333333;
    font-size: 13px;
  }

  .ps-status {
    font-size: 13px;
    color: var(--ps-muted);
  }

  .ps-stats {
    display: grid;
    grid-template-columns: repeat(4, minmax(0, 1fr));
    gap: 10px;
    margin-bottom: 20px;
  }

  .ps-stat {
    background: var(--ps-background);
    border: 1px solid var(--ps-border);
    border-radius: 2px;
    padding: 18px 15px;
    min-width: 0;
  }

  .ps-stat-label {
    display: block;
    font-size: 12px;
    color: var(--ps-muted);
    margin-bottom: 13px;
  }

  .ps-stat-value {
    display: block;
    font-size: 23px;
    font-weight: 700;
    overflow-wrap: anywhere;
    font-variant-numeric: tabular-nums;
  }

  .ps-stat-featured {
    border: 2px solid #424b57;
    padding: 17px 14px;
  }

  .ps-section {
    border: 1px solid var(--ps-border);
    border-radius: 2px;
    margin-bottom: 15px;
    padding: 20px;
    background: #ffffff;
  }

  .ps-chart {
    display: flex;
    flex-direction: column;
    gap: 13px;
  }

  .ps-chart-row {
    display: grid;
    grid-template-columns: 105px minmax(0, 1fr) 100px;
    align-items: center;
    gap: 12px;
    font-size: 12px;
  }

  .ps-chart-label {
    color: var(--ps-text);
  }

  .ps-chart-track {
    height: 16px;
    background: #ebebeb;
    border: 1px solid #dddddd;
  }

  .ps-chart-fill {
    height: 100%;
    background: #777777;
  }

  .ps-chart-fill.current {
    background: #424b57;
  }

  .ps-chart-fill.future {
    background: #b3b3b3;
  }

  .ps-chart-value {
    text-align: right;
    font-size: 12px;
    font-weight: 600;
    color: var(--ps-text);
    font-variant-numeric: tabular-nums;
  }

  .ps-legend {
    display: flex;
    flex-wrap: wrap;
    gap: 18px;
    margin-top: 18px;
    font-size: 11px;
    color: var(--ps-muted);
  }

  .ps-legend span {
    display: flex;
    align-items: center;
    gap: 6px;
  }

  .ps-legend-mark {
    display: inline-block;
    width: 10px;
    height: 10px;
    background: #777777;
  }

  .ps-legend-mark.current {
    background: #424b57;
  }

  .ps-legend-mark.future {
    background: #b3b3b3;
  }

  .ps-breakdown {
    display: grid;
    grid-template-columns: repeat(2, minmax(0, 1fr));
    gap: 1px;
    background: var(--ps-border);
    border: 1px solid var(--ps-border);
  }

  .ps-detail {
    background: #ffffff;
    padding: 15px;
  }

  .ps-detail span {
    display: block;
    color: var(--ps-muted);
    font-size: 12px;
    margin-bottom: 8px;
  }

  .ps-detail strong {
    font-size: 18px;
    font-weight: 700;
  }

  .ps-progress {
    height: 10px;
    background: #e5e5e5;
    margin: 18px 0;
    border: 1px solid var(--ps-border);
  }

  .ps-progress-fill {
    height: 100%;
    background: var(--ps-accent);
  }

  .ps-note {
    color: var(--ps-muted);
    font-size: 12px;
    line-height: 1.6;
    padding: 12px 0 0;
  }

  .ps-note p {
    margin: 0;
    color: var(--ps-muted);
  }

  @media (max-width: 800px) {
    .ps-stats {
      grid-template-columns: repeat(2, minmax(0, 1fr));
    }
  }

  @media (max-width: 550px) {
    .projected-spending {
      padding: 14px;
    }

    .ps-header {
      flex-direction: column;
      align-items: flex-start;
    }

    .ps-inputs {
      grid-template-columns: 1fr;
    }

    .ps-stats,
    .ps-breakdown {
      grid-template-columns: 1fr;
    }

    .ps-chart-row {
      grid-template-columns: 75px minmax(0, 1fr);
    }

    .ps-chart-value {
      grid-column: 2;
      text-align: left;
    }
  }
`;

// ---------- DATE HELPERS ----------

function todayString() {
  const date = new Date();

  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');

  return `${year}-${month}-${day}`;
}

function formatMonth(value) {
  if (!value) return 'Unknown';

  let year;
  let month;

  if (typeof value === 'string') {
    [year, month] = value.split('-').map(Number);
  } else {
    year = value.year;
    month = value.month;
  }

  if (!year || !month) return 'Unknown';

  return new Intl.DateTimeFormat('en-IE', {
    month: 'short',
    year: 'numeric',
  }).format(new Date(year, month - 1, 1));
}

// ---------- COMPONENT ----------

export default function ProjectedSpending({
  apiBase = '/api',
}) {

  // Text inputs.
  const [asOf, setAsOf] = useState(todayString);
  const [historyMonths, setHistoryMonths] = useState('3');
  const [currency, setCurrency] = useState('EUR');

  // API state.
  const [forecast, setForecast] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const [refreshKey, setRefreshKey] = useState(0);

  // Validate values before sending.
  const validDate = /^\\d{4}-\\d{2}-\\d{2}$/.test(asOf)
    && !Number.isNaN(new Date(`${asOf}T12:00:00`).getTime())
    && new Date(`${asOf}T12:00:00`).toISOString().slice(0, 10) === asOf;

  const validMonths =
    /^\\d+$/.test(historyMonths) &&
    Number(historyMonths) >= 1 &&
    Number(historyMonths) <= 24;

  const normalizedCurrency = currency.trim().toUpperCase();

  const validCurrency =
    ['EUR', 'USD', 'GBP'].includes(normalizedCurrency);

  const inputsValid =
    validDate && validMonths && validCurrency;

  // ---------- API REQUEST ----------

  useEffect(() => {

    if (!inputsValid) {
      setForecast(null);
      setError(
        'Enter a valid date (YYYY-MM-DD), historical months (1-24), and currency (EUR, USD or GBP).'
      );
      setLoading(false);
      return;
    }

    const controller = new AbortController();

    async function loadForecast() {
      setLoading(true);
      setError('');

      try {
        const params = new URLSearchParams({
          asOf,
          historyMonths,
          currency: normalizedCurrency,
        });

        const response = await fetch(
          `${apiBase}/expenses/forecast?${params}`,
          {
            signal: controller.signal,
            headers: {
              Accept: 'application/json',
            },
          }
        );

        if (!response.ok) {
          let message =
            `Forecast request failed (HTTP ${response.status}).`;

          try {
            const body = await response.json();
            message = body.error || message;
          } catch {
            // Keep HTTP error.
          }

          throw new Error(message);
        }

        const type = response.headers.get('content-type') || '';

        if (!type.includes('application/json')) {
          throw new Error(
            'The server did not return JSON. Check Spring Boot on port 808 and the Vite proxy.'
          );
        }

        const data = await response.json();
        setForecast(data);

      } catch (err) {
        if (err.name !== 'AbortError') {
          setForecast(null);
          setError(
            err.message || 'Unable to load projected spending.'
          );
        }
      } finally {
        if (!controller.signal.aborted) {
          setLoading(false);
        }
      }
    }

    loadForecast();

    return () => controller.abort();

  }, [
    apiBase,
    asOf,
    historyMonths,
    normalizedCurrency,
    inputsValid,
    refreshKey,
  ]);

  // ---------- CALCULATIONS ----------

  const money = (amount) =>
    new Intl.NumberFormat('en-IE', {
      style: 'currency',
      currency: validCurrency ? normalizedCurrency : 'EUR',
    }).format(Number(amount ?? 0));

  const spent = Number(
    forecast?.currentMonthSpent ?? 0
  );

  const currentProjection = Number(
    forecast?.projectedCurrentMonth ?? 0
  );

  const nextProjection = Number(
    forecast?.projectedNextMonth ?? 0
  );

  const historicalAverage = Number(
    forecast?.historicalMonthlyAverage ?? 0
  );

  const additional = currentProjection - spent;

  const progress = currentProjection > 0
    ? Math.min(100, Math.max(0, (spent / currentProjection) * 100))
    : 0;

  // ---------- CHART DATA ----------

  const chartData = [];

  if (forecast) {

    for (const item of forecast.historicalMonths ?? []) {
      chartData.push({
        month: formatMonth(item.month),
        amount: Number(item.total ?? 0),
        type: 'actual',
      });
    }

    chartData.push({
      month: formatMonth(asOf.slice(0, 7)),
      amount: currentProjection,
      type: 'current',
    });

    const [year, month] = asOf
      .slice(0, 7)
      .split('-')
      .map(Number);

    const nextDate = new Date(year, month, 1);

    const nextMonth = `${nextDate.getFullYear()}-${String(
      nextDate.getMonth() + 1
    ).padStart(2, '0')}`;

    chartData.push({
      month: formatMonth(nextMonth),
      amount: nextProjection,
      type: 'future',
    });
  }

  const maxAmount = Math.max(
    1,
    ...chartData.map((item) => item.amount)
  );

  // ---------- RENDER ----------

  return (
    <>
      <style>{styles}</style>

      <section className="projected-spending">

        <header className="ps-header">
          <div>
            <h2>Projected Spending</h2>
            <p className="ps-description">
              Monthly spending estimates based on previous expenses.
            </p>
          </div>

          <button
            type="button"
            className="ps-button"
            disabled={loading || !inputsValid}
            onClick={() =>
              setRefreshKey((value) => value + 1)
            }
          >
            {loading ? 'Loading...' : 'Refresh'}
          </button>
        </header>

        {/* TEXT INPUTS */}

        <div className="ps-inputs">

          <label>
            Forecast Date
            <input
              type="text"
              placeholder="YYYY-MM-DD"
              value={asOf}
              onChange={(e) => setAsOf(e.target.value)}
            />
          </label>

          <label>
            Historical Months
            <input
              type="text"
              placeholder="3"
              value={historyMonths}
              onChange={(e) =>
                setHistoryMonths(e.target.value)
              }
            />
          </label>

          <label>
            Currency
            <input
              type="text"
              placeholder="EUR"
              maxLength={3}
              value={currency}
              onChange={(e) => setCurrency(e.target.value)}
            />
          </label>

        </div>

        {/* ERRORS */}

        {loading && (
          <p className="ps-status">
            Loading forecast...
          </p>
        )}

        {error && (
          <div className="ps-error" role="alert">
            {error}
          </div>
        )}

        {/* RESULTS */}

        {forecast && (
          <>

            <div className="ps-stats">

              <div className="ps-stat">
                <span className="ps-stat-label">
                  Spent This Month
                </span>
                <strong className="ps-stat-value">
                  {money(spent)}
                </strong>
              </div>

              <div className="ps-stat ps-stat-featured">
                <span className="ps-stat-label">
                  Projected Month-End
                </span>
                <strong className="ps-stat-value">
                  {money(currentProjection)}
                </strong>
              </div>

              <div className="ps-stat">
                <span className="ps-stat-label">
                  Next Month Forecast
                </span>
                <strong className="ps-stat-value">
                  {money(nextProjection)}
                </strong>
              </div>

              <div className="ps-stat">
                <span className="ps-stat-label">
                  Historical Average
                </span>
                <strong className="ps-stat-value">
                  {money(historicalAverage)}
                </strong>
              </div>

            </div>

            {/* MONTHLY COMPARISON */}

            <div className="ps-section">
              <h3>Monthly Spending Comparison</h3>

              <div className="ps-chart">
                {chartData.map((item, index) => (
                  <div
                    className="ps-chart-row"
                    key={`${item.month}-${index}`}
                  >

                    <span className="ps-chart-label">
                      {item.month}
                    </span>

                    <div className="ps-chart-track">
                      <div
                        className={`ps-chart-fill ${
                          item.type === 'current'
                            ? 'current'
                            : item.type === 'future'
                            ? 'future'
                            : ''
                        }`}
                        style={{
                          width: `${
                            (item.amount / maxAmount) * 100
                          }%`,
                        }}
                      />
                    </div>

                    <span className="ps-chart-value">
                      {money(item.amount)}
                    </span>

                  </div>
                ))}
              </div>

              <div className="ps-legend">
                <span>
                  <i className="ps-legend-mark" />
                  Previous Months
                </span>
                <span>
                  <i className="ps-legend-mark current" />
                  Current Projection
                </span>
                <span>
                  <i className="ps-legend-mark future" />
                  Next Month
                </span>
              </div>
            </div>

            {/* CURRENT MONTH BREAKDOWN */}

            <div className="ps-section">
              <h3>Current Month Breakdown</h3>

              <div
                className="ps-progress"
                role="progressbar"
                aria-valuenow={Math.round(progress)}
                aria-valuemin={0}
                aria-valuemax={100}
                aria-label="Spending progress"
              >
                <div
                  className="ps-progress-fill"
                  style={{
                    width: `${progress}%`,
                  }}
                />
              </div>

              <div className="ps-breakdown">

                <div className="ps-detail">
                  <span>Already Spent</span>
                  <strong>{money(spent)}</strong>
                </div>

                <div className="ps-detail">
                  <span>Estimated Remaining Spending</span>
                  <strong>{money(additional)}</strong>
                </div>

                <div className="ps-detail">
                  <span>Current Month Run Rate</span>
                  <strong>
                    {money(forecast.currentMonthRunRate)}
                  </strong>
                </div>

                <div className="ps-detail">
                  <span>Next Month Forecast</span>
                  <strong>{money(nextProjection)}</strong>
                </div>

              </div>
            </div>

            {/* EXPLANATION */}

            <div className="ps-note">
              <p>
                {forecast.note ||
                  'Forecast based on recorded spending and previous monthly averages.'}
              </p>
            </div>

          </>
        )}

      </section>
    </>
  );
}
