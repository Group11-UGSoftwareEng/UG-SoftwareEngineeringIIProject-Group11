import axios from "axios";

const BASE_URL = "http://localhost:8080/api/categories";

// Loads saved categories from the backend.
export const getCategories = async () => {
    const response = await axios.get(BASE_URL);
    return response.data.categories;
};

// Loads expenses belonging to the selected category.
export const getCategoryExpenses = async (categoryId) => {
    const response = await axios.get(
        `${BASE_URL}/${categoryId}/expenses`
    );
    return response.data.expenses;
};

// Saves a new expense in the selected category.
export const saveExpense = async (categoryId, expense) => {
    const response = await axios.post(
        `${BASE_URL}/${categoryId}`,
        expense
    );
    return response.data;
};

export const saveCategory = async (category) => {
    const response = await axios.post(BASE_URL, category);
    return response.data;
};

// Sends the new category name to the backend.
export const renameCategory = async (categoryId, newName) => {
  const response = await axios.put(
    `${BASE_URL}/${encodeURIComponent(categoryId)}`,
    { newName }
  )
  return response.data
}

export const deleteCategory = async (categoryId) => {
  const response = await axios.delete(
    `${BASE_URL}/${encodeURIComponent(categoryId)}`
  )
  return response.data
}

