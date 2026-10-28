
import { useEffect, useMemo, useState } from 'react';
import './SpendingSummary.css';

const dateString = (date) => {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
};

const currentMonth = () => {
  const today = new Date();

  return {
    from: dateString(
      new Date(today.getFullYear(), today.getMonth(), 1)
    ),
    to: dateString(
      new Date(today.getFullYear(), today.getMonth() + 1, 0)
    ),
  };
};

const COLORS = [
  '#6b5ce7',
  '#0ea5a4',
  '#f59e0b',
  '#4f86e8',
  '#e76f91',
  '#8f98a8',
];

export default function SpendingSummary({
  apiBase = '/api',
  categoryNames = {},
}) {
  const initialDates = useMemo(currentMonth, []);

  const [from, setFrom] = useState(initialDates.from);
  const [to, setTo] = useState(initialDates.to);
  const [currency, setCurrency] = useState('EUR');

  const [summary, setSummary] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [refreshKey, setRefreshKey] = useState(0);

  useEffect(() => {
    if (!from || !to || from > to) {
      setSummary(null);
      setError('Choose a valid date range.');
      return;
    }

    const controller = new AbortController();

    const load = async () => {
      setLoading(true);
      setError('');

      try {
        const params = new URLSearchParams({
          from,
          to,
          currency,
        });

        const response = await fetch(
          `${apiBase}/expenses/summary?${params}`,
          {
            signal: controller.signal,
            headers: {
              Accept: 'application/json',
            },
          }
        );

        if (!response.ok) {
          let message =
            `Could not load spending summary (HTTP ${response.status}).`;

          try {
            const body = await response.json();
            message = body.error || message;
          } catch {
            // The server did not return JSON.
          }

          throw new Error(message);
        }

        setSummary(await response.json());
      } catch (err) {
        if (err.name !== 'AbortError') {
          setSummary(null);
          setError(
            err.message || 'Unable to connect to the server.'
          );
        }
      } finally {
        if (!controller.signal.aborted) {
          setLoading(false);
        }
      }
    };

    load();

    return () => controller.abort();
  }, [from, to, currency, apiBase, refreshKey]);

  const formatMoney = (value) =>
    new Intl.NumberFormat('en-IE', {
      style: 'currency',
      currency,
    }).format(Number(value ?? 0));

  const categories = summary?.categories ?? [];

  const total = Number(summary?.totalSpent ?? 0);
  const count = Number(summary?.transactionCount ?? 0);

  const average = count > 0 ? total / count : 0;

  const colorFor = (index) =>
    COLORS[index % COLORS.length];

  return (
    <section
      className="ss-panel"
      aria-label="Spending summary with categories"
    >
      {/* Header */}

      <div className="ss-header">
        <div>
          <p className="ss-eyebrow">
            SPENDTRACK / REPORTS
          </p>

          <h2>Spending summary</h2>

          <p className="ss-subtitle">
            See where your money goes, by category.
          </p>
        </div>

        <button
          className="ss-refresh"
          type="button"
          onClick={() =>
            setRefreshKey((key) => key + 1)
          }
          disabled={loading}
        >
          {loading ? 'Loading…' : 'Refresh'}
        </button>
      </div>

      {/* Filters */}

      <div className="ss-filters">
        <label>
          From

          <input
            type="date"
            value={from}
            onChange={(e) => setFrom(e.target.value)}
          />
        </label>

        <label>
          To

          <input
            type="date"
            value={to}
            onChange={(e) => setTo(e.target.value)}
          />
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

      {/* Error display */}

      {error && (
        <p className="ss-error" role="alert">
          {error}
        </p>
      )}

      {/* Summary statistics */}

      <div className="ss-stats">
        <div className="ss-stat">
          <span>Total spent</span>

          <strong>{formatMoney(total)}</strong>
        </div>

        <div className="ss-stat">
          <span>Transactions</span>

          <strong>{count}</strong>
        </div>

        <div className="ss-stat">
          <span>Average expense</span>

          <strong>{formatMoney(average)}</strong>
        </div>
      </div>

      {/* Category information */}

      <div className="ss-content">
        <div className="ss-card">
          <h3>Category breakdown</h3>

          {loading && !summary ? (
            <p className="ss-muted">
              Loading your summary…
            </p>
          ) : categories.length === 0 ? (
            <p className="ss-muted">
              No expenses found for these dates and currency.
            </p>
          ) : (
            <>
              {/* Spending distribution */}

              <div
                className="ss-segments"
                role="img"
                aria-label="Proportion of spending per category"
              >
                {categories.map((item, index) => (
                  <div
                    key={item.categoryId}
                    style={{
                      flexGrow: Math.max(
                        Number(item.totalSpent),
                        0
                      ),
                      background: colorFor(index),
                    }}
                    title={`${
                      categoryNames[item.categoryId] ||
                      `Category ${item.categoryId.slice(0, 8)}`
                    } — ${Number(
                      item.percentageOfTotal
                    ).toFixed(1)}%`}
                  />
                ))}
              </div>

              {/* Category list */}

              <div className="ss-category-list">
                {categories.map((item, index) => {
                  const label =
                    categoryNames[item.categoryId] ||
                    `Category ${item.categoryId.slice(0, 8)}`;

                  return (
                    <div
                      className="ss-category"
                      key={item.categoryId}
                    >
                      <span
                        className="ss-dot"
                        style={{
                          background: colorFor(index),
                        }}
                      />

                      <span
                        className="ss-category-name"
                        title={item.categoryId}
                      >
                        {label}
                      </span>

                      <span className="ss-category-percent">
                        {Number(
                          item.percentageOfTotal
                        ).toFixed(1)}%
                      </span>

                      <strong>
                        {formatMoney(item.totalSpent)}
                      </strong>
                    </div>
                  );
                })}
              </div>
            </>
          )}
        </div>

        {/* Detailed table */}

        <div className="ss-card ss-table-card">
          <h3>Category details</h3>

          <div className="ss-table-scroll">
            <table className="ss-table">
              <thead>
                <tr>
                  <th>Category</th>
                  <th>Transactions</th>
                  <th>Average</th>
                  <th>Total</th>
                </tr>
              </thead>

              <tbody>
                {categories.length ? (
                  categories.map((item) => (
                    <tr key={item.categoryId}>
                      <td title={item.categoryId}>
                        {categoryNames[item.categoryId] ||
                          `Category ${
                            item.categoryId.slice(0, 8)
                          }`}
                      </td>

                      <td>{item.transactionCount}</td>

                      <td>
                        {formatMoney(
                          item.averageTransaction
                        )}
                      </td>

                      <td>
                        {formatMoney(item.totalSpent)}
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td
                      colSpan="4"
                      className="ss-empty"
                    >
                      No category data available.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </section>
  );
}
