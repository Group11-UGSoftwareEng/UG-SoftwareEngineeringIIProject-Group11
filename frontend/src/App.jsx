import { useState } from 'react'
import './App.css'
import MainPage from './components/MainPage'
import ProjectedSpending from './components/ProjectedSpending'

function App() {
  const [count, setCount] = useState(0)

  return (
    <>
        <MainPage />
        <ProjectedSpending />
    </>
  )
}

export default App
