import { useState } from 'react'
import './App.css'
import MainPage from './components/MainPage'
import SpendingSummary from './components/SpendingSummary'
import ProjectedSpending from './components/ProjectedSpending'

export default function App() {
  const [refreshKey, setRefreshKey] = useState(0)
  return (
    <>
      <MainPage onDataChanged={() => setRefreshKey(value => value + 1)}/>
      <SpendingSummary refreshKey={refreshKey}/>
      <ProjectedSpending refreshKey={refreshKey}/>
    </>
  )
}
