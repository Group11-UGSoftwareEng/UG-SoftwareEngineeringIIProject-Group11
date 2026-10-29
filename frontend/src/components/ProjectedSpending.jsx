
import { useEffect, useState } from 'react';

const todayString = () => {
  const date = new Date();
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
};

const formatMonth = (value) => {
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
};

// All component-specific CSS is embedded here.
const styles = `
  .ps-panel {
    max-width: 1160px;
    margin: 24px auto;
    padding: 28px;
    border-radius: 18px;
    background: #f7f8fc;
    color: #182338;
    font-family: Inter, system-ui, sans-serif;
  }

  .ps-panel * {
    box-sizing: border-box;
  }

  .ps-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    gap: 20px;
    margin-bottom: 24px;
  }

  .ps-eyebrow {
    color: #7564d7;
    font-size: 11px;
    font-weight: 700;
    letter-spacing: 1.2px;
    margin: 0 0 5px;
  }

  .ps-header h2 {
    margin: 0 0 6px;
    font-size: 27px;
    letter-spacing: -0.7px;
  }

  .ps-muted {
    color: #758198;
    font-size: 13px;
    line-height: 1.6;
    margin: 0;
  }

  .ps-button {
    padding: 11px 16px;
    border: 1px solid #dfe3ed;
    border-radius: 9px;
    background: white;
    color: #323e55;
    font-weight: 600;
    cursor: pointer;
  }

  .ps-button:hover {
    background: #f0efff;
  }

  .ps-button:disabled {
    opacity: 0.6;
    cursor: wait;
  }

  .ps-filters {
    display: flex;
    flex-wrap: wrap;
    gap: 14px;
    margin-bottom: 22px;
  }

  .ps-filters label {
    display: flex;
    flex-direction: column;
    gap: 7px;
    font-size: 12px;
    font-weight: 600;
    color: #64718b;
  }

  .ps-filters input,
  .ps-filters select {
    padding: 10px 12px;
    min-width: 155px;
    border: 1px solid #dfe3ed;
    border-radius: 9px;
    background: white;
    color: #23324d;
    font: inherit;
    font-size: 14px;
  }

  .ps-stats {
    display: grid;
    grid-template-columns: repeat(4, minmax(0, 1fr));
    gap: 15px;
    margin-bottom: 18px;
  }

  .ps-stat,
  .ps-card {
    background: white;
    border: 1px solid #e8ebf3;
    border-radius: 14px;
    box-shadow: 0 2px 12px rgba(26,40,71,0.035);
    min-width: 0;
  }

  .ps-stat {
    padding: 20px;
    display: flex;
    flex-direction: column;
    gap: 9px;
  }

  .ps-stat-label {
    font-size: 12px;
    color: #74819b;
    font-weight: 600;
  }

  .ps-stat-value {
    font-size: 24px;
    font-weight: 750;
    letter-spacing: -0.6px;
    overflow-wrap: anywhere;
  }

  .ps-stat-caption {
    font-size: 11px;
    color: #8791a4;
    line-height: 1.5;
  }

  .ps-highlight {
    background: #f3f0ff;
    border-color: #cfc8fb;
  }

  .ps-highlight .ps-stat-value {
    color: #6351cb;
  }

  .ps-content {
    display: grid;
    grid-template-columns: minmax(0, 1.3fr) minmax(0, 0.7fr);
    gap: 17px;
  }

  .ps-card {
    padding: 24px;
  }

  .ps-card h3 {
    font-size: 16px;
    margin: 0 0 8px;
  }

  .ps-chart {
    display: flex;
    flex-direction: column;
    gap: 19px;
    margin-top: 26px;
  }

  .ps-chart-row {
    display: grid;
    grid-template-columns: 90px minmax(0, 1fr) 100px;
    gap: 12px;
    align-items: center;
  }

  .ps-chart-label {
    font-size: 12px;
    color: #64718b;
  }

  .ps-chart-track {
    height: 17px;
    background: #f0f1f6;
    border-radius: 6px;
    overflow: hidden;
  }

  .ps-chart-bar {
    height: 100%;
    border-radius: 6px;
    transition: width 0.25s ease;
  }

  .ps-chart-number {
    text-align: right;
    font-size: 12px;
    font-weight: 700;
    font-variant-numeric: tabular-nums;
  }

  .ps-legend {
    display: flex;
    flex-wrap: wrap;
    gap: 16px;
    margin-top: 28px;
    color: #8791a4;
    font-size: 11px;
  }

  .ps-legend span {
    display: inline-flex;
    align-items: center;
    gap: 7px;
  }

  .ps-legend-dot {
    display: inline-block;
    width: 10px;
    height: 10px;
    border-radius: 3px;
  }

  .ps-projection {
    font-size: 32px;
    font-weight: 750;
    margin: 18px 0 6px;
  }

  .ps-progress-track {
    width: 100%;
    height: 12px;
    background: #eeecfb;
    border-radius: 20px;
    overflow: hidden;
    margin: 25px 0 20px;
  }

  .ps-progress-fill {
    height: 100%;
    background: #7461dd;
    border-radius: 20px;
  }

  .ps-detail {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 12px;
    padding: 12px 0;
    border-bottom: 1px solid #eff1f5;
  }

  .ps-detail span {
    color: #74819b;
    font-size: 12px;
  }

  .ps-detail strong {
    font-size: 13px;
    text-align: right;
    font-variant-numeric: tabular-nums;
  }

  .ps-note {
    margin-top: 18px;
    padding: 18px 22px;
    border-radius: 12px;
    background: white;
    border: 1px solid #e8ebf3;
  }

  .ps-note strong {
    font-size: 13px;
  }

  .ps-note p {
    margin: 8px 0 0;
    color: #74819b;
    font-size: 12px;
    line-height: 1.6;
  }

  .ps-error {
    margin: 0 0 18px;
    padding: 13px 15px;
    background: #fff0f0;
    color: #a52a3c;
    border-radius: 9px;
    font-size: 13px;
  }

  @media (max-width: 1000px) {
    .ps-stats {
      grid-template-columns: repeat(2, minmax(0, 1fr));
    }

    .ps-content {
      grid-template-columns: 1fr;
    }
  }

  @media (max-width: 600px) {
    .ps-panel {
      padding: 16px;
      margin: 10px;
    }

    .ps-header {
      flex-direction: column;
    }

    .ps-stats {
      grid-template-columns: 1fr;
    }

    .ps-card {
      padding: 16px;
    }

    .ps-chart-row {
      grid-template-columns: 78px minmax(0, 1fr);
    }

    .ps-chart-number {
      grid-column: 2;
      text-align: left;
    }

    .ps-filters label,
    .ps-filters input,
    .ps-filters select {
      width: 100%;
    }
  }
`;

const BAR_COLORS = {
  actual: '#a9a1e5',
  current: '#7461dd',
  forecast: '#20a19c',
};

export default function ProjectedSpending({
  apiBase = '/api',
}) {
  const [asOf, setAsOf] = useState(todayString);
  const [historyMonths, setHistoryMonths] = useState(3);
  const [currency, setCurrency] = useState('EUR');

  const [forecast, setForecast] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [refreshKey, setRefreshKey] = useState(0);

  useEffect(() => {
    const controller = new AbortController();

    async function loadForecast() {
      setLoading(true);
      setError('');

      try {
        const params = new URLSearchParams({
          asOf,
          historyMonths: String(historyMonths),
          currency,
        });

        const response = await fetch(
          `${apiBase}/expenses/forecast?${params}`,
          { signal: controller.signal }
        );

        if (!response.ok) {
          let message = `Server error: ${response.status}`;

          try {
            const body = await response.json();
            message = body.error || message;
          } catch {
            // Keep the HTTP error message.
          }

          throw new Error(message);
        }

        setForecast(await response.json());
      } catch (err) {
        if (err.name !== 'AbortError') {
          setForecast(null);
          setError(err.message || 'Unable to load forecast.');
        }
      } finally {
        if (!controller.signal.aborted) {
          setLoading(false);
        }
      }
    }

    loadForecast();

    return () => controller.abort();
  }, [apiBase, asOf, historyMonths, currency, refreshKey]);

  const money = (value) =>
    new Intl.NumberFormat('en-IE', {
      style: 'currency',
      currency,
    }).format(Number(value ?? 0));

  const currentSpent = Number(
    forecast?.currentMonthSpent ?? 0
  );

  const currentProjected = Number(
    forecast?.projectedCurrentMonth ?? 0
  );

  const nextProjected = Number(
    forecast?.projectedNextMonth ?? 0
  );

  const historicalAverage = Number(
    forecast?.historicalMonthlyAverage ?? 0
  );

  const additionalSpending = Math.max(
    0,
    currentProjected - currentSpent
  );

  const progress =
    currentProjected > 0
      ? Math.min(
          100,
          (currentSpent / currentProjected) * 100
        )
      : 0;

  const chartData = [];

  if (forecast) {
    for (const item of forecast.historicalMonths ?? []) {
      chartData.push({
        label: formatMonth(item.month),
        amount: Number(item.total ?? 0),
        type: 'actual',
      });
    }

    chartData.push({
      label: formatMonth(asOf.slice(0, 7)),
      amount: currentProjected,
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
      label: formatMonth(nextMonth),
      amount: nextProjected,
      type: 'forecast',
    });
  }

  const maxAmount = Math.max(
    1,
    ...chartData.map((item) => item.amount)
  );

  const StatCard = ({ label, value, caption, highlight }) => (
    <div className={`ps-stat ${highlight ? 'ps-highlight' : ''}`}>
      <span className="ps-stat-label">{label}</span>
      <strong className="ps-stat-value">{money(value)}</strong>
      <span className="ps-stat-caption">{caption}</span>
    </div>
  );

  return (
    <>
      <style>{styles}</style>

      <section className="ps-panel">
        <header className="ps-header">
          <div>
            <p className="ps-eyebrow">
              SPENDTRACK / FORECASTING
            </p>

            <h2>Projected spending</h2>

            <p className="ps-muted">
              Predict upcoming expenses using your spending history.
            </p>
          </div>

          <button
            type="button"
            className="ps-button"
            disabled={loading}
            onClick={() => setRefreshKey((key) => key + 1)}
          >
            {loading ? 'Loading...' : 'Refresh forecast'}
          </button>
        </header>

        <div className="ps-filters">
          <label>
            Forecast date
            <input
              type="date"
              value={asOf}
              onChange={(e) => setAsOf(e.target.value)}
            />
          </label>

          <label>
            Historical months
            <select
              value={historyMonths}
              onChange={(e) =>
                setHistoryMonths(Number(e.target.value))
              }
            >
              {Array.from({ length: 24 }, (_, i) => i + 1).map(
                (count) => (
                  <option key={count} value={count}>
                    {count} {count === 1 ? 'month' : 'months'}
                  </option>
                )
              )}
            </select>
          </label>

          <label>
            Currency
            <select
              value={currency}
              onChange={(e) => setCurrency(e.target.value)}
            >
              <option value="EUR">EUR (€)</option>
              <option value="USD">USD ($)</option>
              <option value="GBP">GBP (£)</option>
            </select>
          </label>
        </div>

        {error && (
          <div className="ps-error" role="alert">
            {error}
          </div>
        )}

        {loading && (
          <p className="ps-muted" role="status">
            Calculating forecast...
          </p>
        )}

        {forecast && (
          <>
            <div className="ps-stats">
              <StatCard
                label="Spent this month"
                value={currentSpent}
                caption="Actual recorded expenditure"
              />

              <StatCard
                label="Projected end of month"
                value={currentProjected}
                caption="Estimated month-end total"
                highlight
              />

              <StatCard
                label="Next month forecast"
                value={nextProjected}
                caption="Expected spending next month"
              />

              <StatCard
                label="Historical monthly average"
                value={historicalAverage}
                caption={`Based on ${historyMonths} prior months`}
              />
            </div>

            <div className="ps-content">
              <article className="ps-card">
                <h3>Monthly spending comparison</h3>

                <p className="ps-muted">
                  Previous expenditure and future estimates
                </p>

                <div className="ps-chart">
                  {chartData.map((item, index) => (
                    <div
                      className="ps-chart-row"
                      key={`${item.label}-${index}`}
                    >
                      <span className="ps-chart-label">
                        {item.label}
                      </span>

                      <div className="ps-chart-track">
                        <div
                          className="ps-chart-bar"
                          style={{
                            width: `${
                              (item.amount / maxAmount) * 100
                            }%`,
                            background: BAR_COLORS[item.type],
                          }}
                        />
                      </div>

                      <strong className="ps-chart-number">
                        {money(item.amount)}
                      </strong>
                    </div>
                  ))}
                </div>

                <div className="ps-legend">
                  {[
                    ['actual', 'Historical'],
                    ['current', 'Current projection'],
                    ['forecast', 'Next month'],
                  ].map(([key, label]) => (
                    <span key={key}>
                      <i
                        className="ps-legend-dot"
                        style={{
                          background: BAR_COLORS[key],
                        }}
                      />
                      {label}
                    </span>
                  ))}
                </div>
              </article>

              <article className="ps-card">
                <h3>Current month projection</h3>

                <div className="ps-projection">
                  {money(currentProjected)}
                </div>

                <p className="ps-muted">
                  Estimated total by month-end
                </p>

                <div
                  className="ps-progress-track"
                  role="progressbar"
                  aria-label="Actual spending as a proportion of projected spending"
                  aria-valuenow={Math.round(progress)}
                  aria-valuemin={0}
                  aria-valuemax={100}
                >
                  <div
                    className="ps-progress-fill"
                    style={{ width: `${progress}%` }}
                  />
                </div>

                {[
                  ['Already spent', currentSpent],
                  ['Estimated additional spending', additionalSpending],
                  [
                    'Current monthly run rate',
                    forecast.currentMonthRunRate,
                  ],
                  ['Next month forecast', nextProjected],
                ].map(([label, value]) => (
                  <div className="ps-detail" key={label}>
                    <span>{label}</span>
                    <strong>{money(value)}</strong>
                  </div>
                ))}
              </article>
            </div>

            <div className="ps-note">
              <strong>About this forecast</strong>

              <p>{forecast.note}</p>

              <p>
                These projections are based on recorded expenses
                and historical monthly averages. Actual future
                expenditure may differ.
              </p>
            </div>
          </>
        )}
      </section>
    </>
  );
}
