import { useState } from 'react'
import './App.css'
import Expense from './components/Expense'
import ProjectedSpending from './components/ProjectedSpending'

function App() {
  const [count, setCount] = useState(0)

  return (
    <>
        <Expense />
        <ProjectedSpending />
    </>
  )
}

export default App
