
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
