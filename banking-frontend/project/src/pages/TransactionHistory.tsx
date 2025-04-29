import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import AppLayout from '../components/Layout/AppLayout';
import { Card, CardContent, CardHeader, CardTitle } from '../components/ui/Card';
import api from '../services/api';
import { toast } from 'react-hot-toast';
import { ArrowUpRight, ArrowDownRight, Search, Filter, Calendar } from 'lucide-react';
import Input from '../components/ui/Input';

interface Transaction {
  id: number;
  accountNum: string;
  amount: number;
  transactionType: 'CREDIT' | 'DEBIT';
  timeStamp: string;
}

const TransactionHistory: React.FC = () => {
  const { user } = useAuth();
  const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [filteredTransactions, setFilteredTransactions] = useState<Transaction[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [searchTerm, setSearchTerm] = useState<string>('');
  const [filter, setFilter] = useState<string>('all');

  useEffect(() => {
    const fetchTransactions = async () => {
      if (!user?.accountNum) return;

      setLoading(true);
      try {
        const data = await api.fetchTransactions(user.accountNum);
        setTransactions(data);
        setFilteredTransactions(data);
      } catch (error) {
        toast.error('Failed to load transactions');
        console.error(error);
      } finally {
        setLoading(false);
      }
    };

    fetchTransactions();
  }, [user]);

  useEffect(() => {
    // Apply filters and search
    let result = transactions;

    // Apply transaction type filter
    if (filter !== 'all') {
      result = result.filter(t => t.transactionType === filter.toUpperCase());
    }

    // Apply search term (search by amount)
    if (searchTerm) {
      result = result.filter(t => 
        t.amount.toString().includes(searchTerm)
      );
    }

    setFilteredTransactions(result);
  }, [transactions, filter, searchTerm]);

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
      year: 'numeric',
      month: 'short',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
    }).format(date);
  };

  return (
    <AppLayout title="Transaction History">
      <Card>
        <CardHeader className="flex flex-col space-y-1.5">
          <CardTitle>Transaction History</CardTitle>
          <div className="flex flex-col md:flex-row gap-4 mt-4">
            <div className="relative flex-grow">
              <Search className="absolute left-3 top-2.5 h-5 w-5 text-gray-400" />
              <Input
                placeholder="Search by amount"
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10"
              />
            </div>
            <div className="flex gap-2">
              <div className="relative inline-block">
                <Filter className="absolute left-3 top-2.5 h-5 w-5 text-gray-400" />
                <select
                  className="pl-10 h-full rounded-md border border-gray-300 bg-white py-2 px-4 text-gray-900 focus:ring-2 focus:ring-primary focus:border-primary"
                  value={filter}
                  onChange={(e) => setFilter(e.target.value)}
                >
                  <option value="all">All Transactions</option>
                  <option value="credit">Deposits Only</option>
                  <option value="debit">Withdrawals Only</option>
                </select>
              </div>
            </div>
          </div>
        </CardHeader>
        <CardContent>
          {loading ? (
            <div className="flex justify-center py-10">
              <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
            </div>
          ) : filteredTransactions.length === 0 ? (
            <div className="text-center py-12 text-gray-500">
              <Calendar className="h-12 w-12 mx-auto opacity-30 mb-3" />
              <p className="text-xl font-medium mb-2">No transactions found</p>
              <p>Try changing your filters or search criteria</p>
            </div>
          ) : (
            <div className="rounded-md border">
              <div className="grid grid-cols-4 font-semibold bg-gray-50 p-4 border-b">
                <div className="col-span-2">Transaction</div>
                <div className="text-right">Amount</div>
                <div className="text-right">Date</div>
              </div>

              <div className="divide-y">
                {filteredTransactions.map((transaction) => (
                  <div key={transaction.id} className="grid grid-cols-4 p-4 hover:bg-gray-50 transition-colors">
                    <div className="col-span-2 flex items-center">
                      <div className={`p-2 rounded-full mr-3 ${
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
                      <div>
                        <p className="font-medium">
                          {transaction.transactionType === 'CREDIT' ? 'Deposit' : 'Withdrawal'}
                        </p>
                        <p className="text-xs text-gray-500">
                          {transaction.accountNum}
                        </p>
                      </div>
                    </div>
                    <div className={`text-right font-semibold ${
                      transaction.transactionType === 'CREDIT' 
                        ? 'text-green-600' 
                        : 'text-red-600'
                    }`}>
                      {transaction.transactionType === 'CREDIT' ? '+' : '-'} 
                      {formatCurrency(transaction.amount)}
                    </div>
                    <div className="text-right text-gray-500">
                      {formatDate(transaction.timeStamp)}
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}
        </CardContent>
      </Card>
    </AppLayout>
  );
};

export default TransactionHistory;