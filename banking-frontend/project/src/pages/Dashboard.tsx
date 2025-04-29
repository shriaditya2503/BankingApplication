import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import AppLayout from '../components/Layout/AppLayout';
import { Card, CardContent, CardHeader, CardTitle } from '../components/ui/Card';
import api from '../services/api';
import { toast } from 'react-hot-toast';
import { 
  LineChart, 
  CreditCard, 
  ArrowUpRight, 
  ArrowDownRight, 
  MoreHorizontal,
  RefreshCw,
  Clock
} from 'lucide-react';
import { Link } from 'react-router-dom';

interface Transaction {
  id: number;
  accountNum: string;
  amount: number;
  transactionType: 'CREDIT' | 'DEBIT';
  timeStamp: string;
}

const Dashboard: React.FC = () => {
  const { user } = useAuth();
  const [balance, setBalance] = useState<number>(0);
  const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    const fetchDashboardData = async () => {
      if (!user) {
        return; // ❌ Do not fetch if user is not loaded yet
      }
  
      setLoading(true);
      try {
        const balanceData = await api.checkBalance();
        setBalance(balanceData);
  
        if (user.accountNum) {
          const transactionsData = await api.fetchTransactions(user.accountNum);
          setTransactions(transactionsData.slice(0, 5));
        }
      } catch (error) {
        toast.error('Failed to load dashboard data');
        console.error(error);
      } finally {
        setLoading(false);
      }
    };
  
    fetchDashboardData();
  }, [user]); // 👈 Also important: depends on `user`
  

  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat('en-IN', {
      style: 'currency',
      currency: 'INR',
      maximumFractionDigits: 0,
    }).format(amount);
  };

  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return new Intl.DateTimeFormat('en-US', {
      day: '2-digit',
      month: 'short',
      hour: '2-digit',
      minute: '2-digit',
    }).format(date);
  };

  return (
    <AppLayout title="Dashboard">
      <div className="grid grid-cols-1 gap-4 md:grid-cols-2 lg:grid-cols-3 mb-6">
        <Card className="bg-gradient-to-br from-primary to-primary-dark text-white transform transition-all duration-300 hover:scale-105">
          <CardContent className="p-6">
            <div className="flex justify-between items-center mb-4">
              <div className="flex items-center">
                <CreditCard className="h-6 w-6 mr-2" />
                <h3 className="text-lg font-medium">Current Balance</h3>
              </div>
              <RefreshCw className="h-5 w-5 cursor-pointer" onClick={() => window.location.reload()} />
            </div>
            <div className="text-3xl font-bold mb-2">
              {loading ? '...' : formatCurrency(balance)}
            </div>
            <div className="text-sm opacity-80">
              Account: {user?.accountNum || '...'}
            </div>
          </CardContent>
        </Card>

        <Card className="transform transition-all duration-300 hover:scale-105">
          <CardContent className="p-6">
            <div className="flex justify-between items-center mb-4">
              <div className="flex items-center">
                <ArrowUpRight className="h-6 w-6 text-green-500 mr-2" />
                <h3 className="text-lg font-medium">Income</h3>
              </div>
            </div>
            <div className="text-2xl font-bold text-gray-800 mb-2">
              {loading ? '...' : formatCurrency(
                transactions
                  .filter(t => t.transactionType === 'CREDIT')
                  .reduce((sum, t) => sum + t.amount, 0)
              )}
            </div>
            <div className="text-sm text-gray-500">
              Last 5 transactions
            </div>
          </CardContent>
        </Card>

        <Card className="transform transition-all duration-300 hover:scale-105">
          <CardContent className="p-6">
            <div className="flex justify-between items-center mb-4">
              <div className="flex items-center">
                <ArrowDownRight className="h-6 w-6 text-red-500 mr-2" />
                <h3 className="text-lg font-medium">Expenses</h3>
              </div>
            </div>
            <div className="text-2xl font-bold text-gray-800 mb-2">
              {loading ? '...' : formatCurrency(
                transactions
                  .filter(t => t.transactionType === 'DEBIT')
                  .reduce((sum, t) => sum + t.amount, 0)
              )}
            </div>
            <div className="text-sm text-gray-500">
              Last 5 transactions
            </div>
          </CardContent>
        </Card>
      </div>

      <div className="grid grid-cols-1 gap-6 lg:grid-cols-3">
        <Card className="lg:col-span-2">
          <CardHeader className="flex items-center justify-between">
            <CardTitle>Recent Transactions</CardTitle>
            <Link 
              to="/transactions" 
              className="text-sm text-primary hover:underline"
            >
              View All
            </Link>
          </CardHeader>
          <CardContent>
            {loading ? (
              <div className="flex justify-center py-4">
                <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></div>
              </div>
            ) : transactions.length === 0 ? (
              <div className="text-center py-8 text-gray-500">
                <Clock className="h-12 w-12 mx-auto opacity-30 mb-3" />
                <p>No recent transactions found</p>
                <Link to="/transfer" className="mt-2 inline-block text-primary hover:underline">
                  Make your first transaction
                </Link>
              </div>
            ) : (
              <div className="space-y-4">
                {transactions.map((transaction) => (
                  <div key={transaction.id} className="flex items-center justify-between p-3 bg-gray-50 rounded-lg hover:bg-gray-100 transition-colors">
                    <div className="flex items-center">
                      <div className={`p-2 rounded-full ${
                        transaction.transactionType === 'CREDIT' 
                          ? 'bg-green-100 text-green-600' 
                          : 'bg-red-100 text-red-600'
                      }`}>
                        {transaction.transactionType === 'CREDIT' ? (
                          <ArrowUpRight className="h-5 w-5" />
                        ) : (
                          <ArrowDownRight className="h-5 w-5" />
                        )}
                      </div>
                      <div className="ml-3">
                        <p className="font-medium text-gray-800">
                          {transaction.transactionType === 'CREDIT' ? 'Deposit' : 'Withdrawal'}
                        </p>
                        <p className="text-xs text-gray-500">
                          {formatDate(transaction.timeStamp)}
                        </p>
                      </div>
                    </div>
                    <div className={`font-semibold ${
                      transaction.transactionType === 'CREDIT' 
                        ? 'text-green-600' 
                        : 'text-red-600'
                    }`}>
                      {transaction.transactionType === 'CREDIT' ? '+' : '-'} 
                      {formatCurrency(transaction.amount)}
                    </div>
                  </div>
                ))}
              </div>
            )}
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Quick Actions</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="grid grid-cols-1 gap-3">
              <Link
                to="/transfer"
                className="flex items-center p-3 rounded-lg border border-gray-200 hover:bg-gray-50 transition-colors"
              >
                <div className="p-2 bg-primary rounded-lg text-white">
                  <RefreshCw className="h-5 w-5" />
                </div>
                <div className="ml-3">
                  <p className="font-medium">Transfer Funds</p>
                  <p className="text-xs text-gray-500">Send money to other accounts</p>
                </div>
              </Link>
              
              <Link
                to="/transactions"
                className="flex items-center p-3 rounded-lg border border-gray-200 hover:bg-gray-50 transition-colors"
              >
                <div className="p-2 bg-secondary rounded-lg text-white">
                  <LineChart className="h-5 w-5" />
                </div>
                <div className="ml-3">
                  <p className="font-medium">Transaction History</p>
                  <p className="text-xs text-gray-500">View all your transactions</p>
                </div>
              </Link>
              
              <Link
                to="/profile"
                className="flex items-center p-3 rounded-lg border border-gray-200 hover:bg-gray-50 transition-colors"
              >
                <div className="p-2 bg-gray-800 rounded-lg text-white">
                  <CreditCard className="h-5 w-5" />
                </div>
                <div className="ml-3">
                  <p className="font-medium">Account Settings</p>
                  <p className="text-xs text-gray-500">Update your profile details</p>
                </div>
              </Link>
            </div>
          </CardContent>
        </Card>
      </div>
    </AppLayout>
  );
};

export default Dashboard;