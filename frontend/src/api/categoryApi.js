
import axios from 'axios'

// Use Vite's /api proxy; keep the existing endpoint paths and response shapes.
const BASE_URL = '/api/categories'

export const getCategories = async () => {
  const response = await axios.get(BASE_URL)
  return response.data.categories
}

export const getCategoryExpenses = async (categoryId) => {
  const response = await axios.get(
    `${BASE_URL}/${encodeURIComponent(categoryId)}/expenses`
  )
  return response.data.expenses
}

export const saveExpense = async (categoryId, expense) => {
  const response = await axios.post(
    `${BASE_URL}/${encodeURIComponent(categoryId)}`,
    expense
  )
  return response.data
}

export const renameCategory = async (categoryId, newName) => {
  const response = await axios.put(
    `${BASE_URL}/${encodeURIComponent(categoryId)}`,
    { newName }
  )
  return response.data
}
