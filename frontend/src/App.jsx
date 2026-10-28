
import { useState } from 'react'
import heroImg from './assets/hero.png'
import reactLogo from './assets/react.svg'
import viteLogo from './assets/vite.svg'
import './App.css'

import Expense from './components/Expense'
import SpendingSummary from './components/SpendingSummary'

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
