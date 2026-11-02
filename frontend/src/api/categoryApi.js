import axios from "axios";

const BASE_URL =`http://localhost:8080/api/categories`;

export const saveExpense = async (categoryId, expense) => {
    const res = await axios.post(`${BASE_URL}/${categoryId}`, expense);
    return res.data;
}