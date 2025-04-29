import axios from 'axios';

const BASE_URL = 'http://localhost:8080';

// Create axios instance with defaults
const axiosInstance = axios.create({
  baseURL: BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add a request interceptor to add token to requests
axiosInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Auth endpoints
const login = async (email: string, password: string) => {
  try {
    const response = await axiosInstance.post('/user/login', { email, password });
    return response.data;
  } catch (error) {
    throw handleApiError(error);
  }
};

const register = async (userData: any) => {
  try {
    const response = await axiosInstance.post('/user/register', userData);
    return response.data;
  } catch (error) {
    throw handleApiError(error);
  }
};

// User data endpoints
const fetchUserDetails = async () => {
  try {
    const response = await axiosInstance.get('/user/fetch-user-details');
    return response.data;
  } catch (error) {
    throw handleApiError(error);
  }
};

const updateUserDetails = async (updateData: any) => {
  try {
    const response = await axiosInstance.post('/user/update-user-details', updateData);
    return response.data;
  } catch (error) {
    throw handleApiError(error);
  }
};

const fetchUserName = async () => {
  try {
    const response = await axiosInstance.get('/user/get-name');
    return response.data;
  } catch (error) {
    throw handleApiError(error);
  }
};

const checkBalance = async () => {
  try {
    const response = await axiosInstance.post('/user/check-balance');
    return response.data;
  } catch (error) {
    throw handleApiError(error);
  }
};

// Transaction endpoints
const fetchTransactions = async (accountNum: string) => {
  try {
    const response = await axiosInstance.post('/transaction/transaction-list', { accountNum });
    return response.data;
  } catch (error) {
    throw handleApiError(error);
  }
};

const transferFunds = async (toAccount: string, amount: number) => {
  try {
    const response = await axiosInstance.post('/user/transfer-fund', { toAccount, amount });
    return response.data;
  } catch (error) {
    throw handleApiError(error);
  }
};

const creditAccount = async (accountNum: string, amount: number) => {
  try {
    const response = await axiosInstance.post('/user/credit', { accountNum, amount });
    return response.data;
  } catch (error) {
    throw handleApiError(error);
  }
};

const debitAccount = async (accountNum: string, amount: number) => {
  try {
    const response = await axiosInstance.post('/user/debit', { accountNum, amount });
    return response.data;
  } catch (error) {
    throw handleApiError(error);
  }
};

// Helper function to handle API errors
const handleApiError = (error: any) => {
  if (error.response) {
    // The request was made and the server responded with a status code
    // that falls out of the range of 2xx
    return {
      status: error.response.status,
      message: error.response.data || 'An error occurred',
    };
  } else if (error.request) {
    // The request was made but no response was received
    return {
      status: 0,
      message: 'No response from server. Please check your connection.',
    };
  } else {
    // Something happened in setting up the request that triggered an Error
    return {
      status: 0,
      message: error.message || 'An unknown error occurred',
    };
  }
};

const api = {
  login,
  register,
  fetchUserDetails,
  updateUserDetails,
  fetchUserName,
  checkBalance,
  fetchTransactions,
  transferFunds,
  creditAccount,
  debitAccount,
};

export default api;