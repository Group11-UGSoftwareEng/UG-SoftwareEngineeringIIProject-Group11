
import { useState } from 'react'
import Expense from './components/Expense'
import SpendingSummary from './components/SpendingSummary'
import './App.css'

export default function App() {
  const [revision, setRevision] = useState(0)

  function handleExpenseAdded() {
    setRevision(previous => previous + 1)
  }

  return (
    <main>
      <Expense
        onExpenseAdded={handleExpenseAdded}
      />

      <SpendingSummary
        refreshKey={revision}
        currency="EUR"
      />
    </main>
  )
}
