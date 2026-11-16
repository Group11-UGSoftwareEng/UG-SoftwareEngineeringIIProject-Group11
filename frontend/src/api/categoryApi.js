import axios from "axios";

const BASE_URL = "http://localhost:8080/api/categories";

export const getCategories = async () => {
    const response = await axios.get(BASE_URL);
    return response.data.categories;
};

export const getCategoryExpenses = async (categoryId) => {
    const response = await axios.get(
        `${BASE_URL}/${categoryId}/expenses`
    );
    return response.data.expenses;
};

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

export const updateCategory = async (categoryId, categoryData) => {
    const response = await axios.put(`${BASE_URL}/${categoryId}`, categoryData);
    return response.data;
};