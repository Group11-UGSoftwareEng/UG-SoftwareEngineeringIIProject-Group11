
import { useState } from "react";
import "./App.css";

import MainPage from "./components/MainPage";
import BudgetLimits from "./components/BudgetLimits";
import SpendingSummary from "./components/SpendingSummary";
import ProjectedSpending from "./components/ProjectedSpending";

const SECTIONS = [
  { id: "budget", label: "Budget Limits" },
  { id: "summary", label: "Spending Summary" },
  { id: "projection", label: "Projected Spending" },
];

export default function App() {
  const [activeSection, setActiveSection] = useState("budget");
  const [refreshKey, setRefreshKey] = useState(0);

  function refreshInsights() {
    setRefreshKey((previous) => previous + 1);
  }

  return (
    <main className="spendtrack-app">
      <section className="tracker-panel">
        <MainPage onDataChanged={refreshInsights} />
      </section>

      <section className="insights-area">
        <header className="insights-heading">
          <h2>Budget &amp; Spending Insights</h2>
          <p>
            View your budgets, spending summary,
            and projected spending.
          </p>
        </header>

        <nav className="insights-tabs" aria-label="Spending views">
          {SECTIONS.map((section) => (
            <button
              key={section.id}
              type="button"
              className={
                activeSection === section.id
                  ? "insights-tab active"
                  : "insights-tab"
              }
              aria-pressed={activeSection === section.id}
              onClick={() => setActiveSection(section.id)}
            >
              {section.label}
            </button>
          ))}
        </nav>

        {/* Keep components mounted so their state is preserved. */}
        <div hidden={activeSection !== "budget"}>
          <BudgetLimits refreshKey={refreshKey} />
        </div>

        <div hidden={activeSection !== "summary"}>
          <SpendingSummary refreshKey={refreshKey} />
        </div>

        <div hidden={activeSection !== "projection"}>
          <ProjectedSpending refreshKey={refreshKey} />
        </div>
      </section>
    </main>
  );
}
