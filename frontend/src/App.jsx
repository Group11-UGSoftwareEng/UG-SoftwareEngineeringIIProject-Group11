
import { useState } from 'react'
import './App.css'
import Expense from './components/Expense.jsx'
import SpendingSummary from './components/SpendingSummary.jsx'

function App() {
  const [count, setCount] = useState(0)

  return (
    <>
      <Expense />
      <SpendingSummary />
    </>
  )
}

export default App
