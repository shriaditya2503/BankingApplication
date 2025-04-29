import React, { createContext, useState, useContext, useEffect } from 'react';
import { jwtDecode } from 'jwt-decode';
import api from '../services/api';

interface AuthContextType {
  isAuthenticated: boolean;
  user: any;
  loading: boolean;
  login: (email: string, password: string) => Promise<string>;
  register: (userData: RegisterData) => Promise<void>;
  logout: () => void;
}

interface RegisterData {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  phoneNum: string;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
  const [user, setUser] = useState<any>(null);
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      setIsAuthenticated(true);
      fetchUserDetails();
    } else {
      setLoading(false);
    }
  }, []);

  const fetchUserDetails = async () => {
    try {
      const response = await api.fetchUserDetails();
      setUser(response);
      setLoading(false);
    } catch (error) {
      console.error('Error fetching user details:', error);
      logout();
      setLoading(false);
    }
  };

  const login = async (email: string, password: string): Promise<string> => {
    try {
      const token = await api.login(email, password);
      localStorage.setItem('token', token);
      setIsAuthenticated(true);
  
      try {
        await fetchUserDetails(); // 👈 try fetching details
      } catch (error) {
        console.warn('Fetching user details failed, but staying logged in.', error);
        // Don't logout! Just warn.
      }
  
      return token;
    } catch (error) {
      throw error;
    }
  };
  

  const register = async (userData: RegisterData) => {
    try {
      await api.register(userData);
    } catch (error) {
      throw error;
    }
  };

  const logout = () => {
    localStorage.removeItem('token');
    setIsAuthenticated(false);
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ isAuthenticated, user, loading, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};