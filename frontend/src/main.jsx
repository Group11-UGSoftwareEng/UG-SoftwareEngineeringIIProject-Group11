import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import { ExpensesProvider } from './components/contexts/ExpenseContext.jsx'

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <ExpensesProvider>
      <App />
    </ExpensesProvider>
  </StrictMode>,
)
