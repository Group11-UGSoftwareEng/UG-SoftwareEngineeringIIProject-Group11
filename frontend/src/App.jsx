import { useState } from 'react'
import './App.css'
import Expense from './components/Expense'
import BudgetLimits from './components/BudgetLimits'

function App() {
  const [count, setCount] = useState(0)

  return (
    <>
        <Expense />
        <BudgetLimits />
    </>
  )
}

export default App
