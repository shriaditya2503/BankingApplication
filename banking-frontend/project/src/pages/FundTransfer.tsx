import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import AppLayout from '../components/Layout/AppLayout';
import { Card, CardContent, CardHeader, CardTitle, CardFooter } from '../components/ui/Card';
import Button from '../components/ui/Button';
import Input from '../components/ui/Input';
import api from '../services/api';
import { toast } from 'react-hot-toast';
import { RefreshCw, ArrowRightLeft, AlertCircle, CheckCircle2 } from 'lucide-react';

const FundTransfer: React.FC = () => {
  const { user } = useAuth();
  const [toAccount, setToAccount] = useState('');
  const [amount, setAmount] = useState('');
  const [loading, setLoading] = useState(false);
  const [transferComplete, setTransferComplete] = useState(false);
  const [transferDetails, setTransferDetails] = useState({
    amount: 0,
    account: '',
  });

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!toAccount || !amount) {
      toast.error('Please fill in all required fields');
      return;
    }

    if (parseFloat(amount) <= 0) {
      toast.error('Amount must be greater than 0');
      return;
    }

    if (toAccount === user?.accountNum) {
      toast.error('You cannot transfer funds to your own account');
      return;
    }

    setLoading(true);

    try {
      await api.transferFunds(toAccount, parseFloat(amount));
      setTransferComplete(true);
      setTransferDetails({
        amount: parseFloat(amount),
        account: toAccount,
      });
      toast.success('Funds transferred successfully');
    } catch (error: any) {
      toast.error(error.message || 'Failed to transfer funds');
    } finally {
      setLoading(false);
    }
  };

  const resetForm = () => {
    setToAccount('');
    setAmount('');
    setTransferComplete(false);
  };

  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat('en-IN', {
      style: 'currency',
      currency: 'INR',
      maximumFractionDigits: 0,
    }).format(amount);
  };

  return (
    <AppLayout title="Transfer Funds">
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <Card className="md:col-span-2">
          <CardHeader>
            <CardTitle>Transfer Money</CardTitle>
          </CardHeader>
          <CardContent>
            {transferComplete ? (
              <div className="text-center py-8">
                <div className="mb-4 flex justify-center">
                  <div className="h-20 w-20 rounded-full bg-green-100 flex items-center justify-center">
                    <CheckCircle2 className="h-10 w-10 text-green-600" />
                  </div>
                </div>
                <h3 className="text-2xl font-bold mb-2">Transfer Successful</h3>
                <p className="text-gray-600 mb-6">
                  You have successfully transferred {formatCurrency(transferDetails.amount)} to account {transferDetails.account}
                </p>
                <div className="flex justify-center">
                  <Button onClick={resetForm} variant="outline" className="mr-2">
                    Make Another Transfer
                  </Button>
                </div>
              </div>
            ) : (
              <form onSubmit={handleSubmit} className="space-y-6">
                <Input
                  label="Recipient Account Number"
                  placeholder="Enter 10-digit account number"
                  value={toAccount}
                  onChange={(e) => setToAccount(e.target.value)}
                  required
                />

                <Input
                  label="Amount (₹)"
                  type="number"
                  placeholder="Enter amount to transfer"
                  value={amount}
                  onChange={(e) => setAmount(e.target.value)}
                  required
                  min="1"
                />

                <div className="bg-yellow-50 border-l-4 border-yellow-400 p-4 rounded-md">
                  <div className="flex">
                    <div className="flex-shrink-0">
                      <AlertCircle className="h-5 w-5 text-yellow-400" />
                    </div>
                    <div className="ml-3">
                      <p className="text-sm text-yellow-700">
                        Please verify the account number before transferring. Transfers cannot be reversed once completed.
                      </p>
                    </div>
                  </div>
                </div>

                <Button
                  type="submit"
                  loading={loading}
                  disabled={loading}
                  fullWidth
                  className="mt-6"
                >
                  <ArrowRightLeft className="mr-2 h-4 w-4" />
                  Transfer Funds
                </Button>
              </form>
            )}
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Transfer Information</CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            <div>
              <h4 className="font-medium text-gray-700 mb-2">Your Account</h4>
              <p className="text-sm bg-gray-50 p-3 rounded-md">{user?.accountNum || 'Loading...'}</p>
            </div>
            
            <div>
              <h4 className="font-medium text-gray-700 mb-2">Daily Limit</h4>
              <p className="text-sm bg-gray-50 p-3 rounded-md">₹50,000</p>
            </div>
            
            <div>
              <h4 className="font-medium text-gray-700 mb-2">Transfer Fee</h4>
              <p className="text-sm bg-gray-50 p-3 rounded-md text-green-600">Free</p>
            </div>
            
            <div>
              <h4 className="font-medium text-gray-700 mb-2">Processing Time</h4>
              <p className="text-sm bg-gray-50 p-3 rounded-md">Instant</p>
            </div>
          </CardContent>
          <CardFooter className="bg-gray-50 border-t border-gray-100">
            <div className="w-full text-center">
              <p className="text-xs text-gray-500">
                Transfers are processed instantly and cannot be reversed. Please verify all details before confirming.
              </p>
            </div>
          </CardFooter>
        </Card>
      </div>
    </AppLayout>
  );
};

export default FundTransfer;