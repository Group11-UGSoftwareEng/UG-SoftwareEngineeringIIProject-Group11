
import { useState } from 'react';
import './App.css';
import MainPage from './components/MainPage';
import BudgetLimits from './components/BudgetLimits';
import SpendingSummary from './components/SpendingSummary';
import ProjectedSpending from './components/ProjectedSpending';

const VIEWS = [
  { id: 'budget', label: 'Budget Limits' },
  { id: 'summary', label: 'Spending Summary' },
  { id: 'projection', label: 'Projected Spending' },
];

const panelStyle = {
  margin: '32px 0 40px',
  padding: '22px',
  background: '#fff',
  border: '1px solid #d8d8dc',
  borderRadius: '8px',
  color: '#262626',
  textAlign: 'left',
};

export default function App() {
  const [activeView, setActiveView] = useState('budget');
  const [refreshKey, setRefreshKey] = useState(0);

  return (
    <main style={{ width: '100%', minWidth: 0 }}>
      {/* Keep the original expense loading and editing code untouched. */}
      <MainPage onDataChanged={() => setRefreshKey(value => value + 1)} />

      <section style={panelStyle} aria-label="Budget and spending insights">
        <h2 style={{ color: '#262626', fontSize: '23px', fontWeight: 700 }}>
          Budget &amp; Spending Insights
        </h2>
        <p style={{ color: '#626262', fontSize: '14px', marginBottom: '18px' }}>
          Switch between your budget, summary and spending projection.
        </p>

        <div
          role="tablist"
          aria-label="Insights sections"
          style={{
            display: 'flex',
            flexWrap: 'wrap',
            gap: '8px',
            borderBottom: '1px solid #e5e5e5',
            paddingBottom: '16px',
            marginBottom: '18px',
          }}
        >
          {VIEWS.map(view => {
            const selected = activeView === view.id;
            return (
              <button
                key={view.id}
                type="button"
                role="tab"
                id={`tab-${view.id}`}
                aria-selected={selected}
                aria-controls={`panel-${view.id}`}
                onClick={() => setActiveView(view.id)}
                style={{
                  padding: '10px 16px',
                  borderRadius: '6px',
                  fontSize: '14px',
                  fontWeight: 600,
                  cursor: 'pointer',
                  background: selected ? '#30343b' : '#f2f3f5',
                  color: selected ? '#fff' : '#333',
                  border: `1px solid ${selected ? '#30343b' : '#d6d7db'}`,
                }}
              >
                {view.label}
              </button>
            );
          })}
        </div>

        {/* Hide panels rather than unmounting: local form/filter state survives. */}
        <div
          id="panel-budget"
          role="tabpanel"
          aria-labelledby="tab-budget"
          hidden={activeView !== 'budget'}
          style={{ display: activeView === 'budget' ? 'block' : 'none' }}
        >
          <BudgetLimits />
        </div>

        <div
          id="panel-summary"
          role="tabpanel"
          aria-labelledby="tab-summary"
          hidden={activeView !== 'summary'}
          style={{ display: activeView === 'summary' ? 'block' : 'none' }}
        >
          <SpendingSummary refreshKey={refreshKey} />
        </div>

        <div
          id="panel-projection"
          role="tabpanel"
          aria-labelledby="tab-projection"
          hidden={activeView !== 'projection'}
          style={{ display: activeView === 'projection' ? 'block' : 'none' }}
        >
          <ProjectedSpending refreshKey={refreshKey} />
        </div>
      </section>
    </main>
  );
}
