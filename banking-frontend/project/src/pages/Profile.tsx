import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import AppLayout from '../components/Layout/AppLayout';
import { Card, CardContent, CardHeader, CardTitle, CardFooter } from '../components/ui/Card';
import Button from '../components/ui/Button';
import Input from '../components/ui/Input';
import api from '../services/api';
import { toast } from 'react-hot-toast';
import { User, Mail, Phone, Lock, Info, Key, Shield } from 'lucide-react';

const Profile: React.FC = () => {
  const { user } = useAuth();
  const [loading, setLoading] = useState(false);
  const [profileData, setProfileData] = useState({
    email: '',
    phoneNum: '',
    password: '',
    confirmPassword: '',
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setProfileData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (profileData.password && profileData.password !== profileData.confirmPassword) {
      toast.error('Passwords do not match');
      return;
    }

    if (!profileData.email && !profileData.phoneNum && !profileData.password) {
      toast.error('No changes to update');
      return;
    }

    setLoading(true);

    // Only include fields that have values
    const updateData: {email?: string; phoneNum?: string; password?: string} = {};
    if (profileData.email) updateData.email = profileData.email;
    if (profileData.phoneNum) updateData.phoneNum = profileData.phoneNum;
    if (profileData.password) updateData.password = profileData.password;

    try {
      await api.updateUserDetails(updateData);
      toast.success('Profile updated successfully');
      // Reset the form
      setProfileData({
        email: '',
        phoneNum: '',
        password: '',
        confirmPassword: '',
      });
    } catch (error: any) {
      toast.error(error.message || 'Failed to update profile');
    } finally {
      setLoading(false);
    }
  };

  return (
    <AppLayout title="Profile">
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="md:col-span-2 space-y-6">
          <Card>
            <CardHeader>
              <CardTitle>Account Information</CardTitle>
            </CardHeader>
            <CardContent className="space-y-6">
              <div className="grid grid-cols-1 gap-6 sm:grid-cols-2">
                <div className="space-y-2">
                  <div className="flex items-center">
                    <User className="mr-2 h-4 w-4 text-gray-500" />
                    <span className="text-sm font-medium text-gray-500">First Name</span>
                  </div>
                  <div className="bg-gray-50 p-3 rounded-md">
                    {user?.firstName || 'Loading...'}
                  </div>
                </div>
                
                <div className="space-y-2">
                  <div className="flex items-center">
                    <User className="mr-2 h-4 w-4 text-gray-500" />
                    <span className="text-sm font-medium text-gray-500">Last Name</span>
                  </div>
                  <div className="bg-gray-50 p-3 rounded-md">
                    {user?.lastName || 'Loading...'}
                  </div>
                </div>
              </div>

              <div className="grid grid-cols-1 gap-6 sm:grid-cols-2">
                <div className="space-y-2">
                  <div className="flex items-center">
                    <Mail className="mr-2 h-4 w-4 text-gray-500" />
                    <span className="text-sm font-medium text-gray-500">Email</span>
                  </div>
                  <div className="bg-gray-50 p-3 rounded-md">
                    {user?.email || 'Loading...'}
                  </div>
                </div>
                
                <div className="space-y-2">
                  <div className="flex items-center">
                    <Phone className="mr-2 h-4 w-4 text-gray-500" />
                    <span className="text-sm font-medium text-gray-500">Phone Number</span>
                  </div>
                  <div className="bg-gray-50 p-3 rounded-md">
                    {user?.phoneNum || 'Loading...'}
                  </div>
                </div>
              </div>

              <div className="space-y-2">
                <div className="flex items-center">
                  <Key className="mr-2 h-4 w-4 text-gray-500" />
                  <span className="text-sm font-medium text-gray-500">Account Number</span>
                </div>
                <div className="bg-gray-50 p-3 rounded-md font-medium">
                  {user?.accountNum || 'Loading...'}
                </div>
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle>Update Profile</CardTitle>
            </CardHeader>
            <CardContent>
              <form onSubmit={handleSubmit} className="space-y-6">
                <div className="space-y-4">
                  <div className="grid grid-cols-1 gap-6 sm:grid-cols-2">
                    <Input
                      label="Email"
                      name="email"
                      type="email"
                      placeholder="Update your email"
                      value={profileData.email}
                      onChange={handleChange}
                    />
                    
                    <Input
                      label="Phone Number"
                      name="phoneNum"
                      placeholder="Update your phone number"
                      value={profileData.phoneNum}
                      onChange={handleChange}
                    />
                  </div>

                  <div className="grid grid-cols-1 gap-6 sm:grid-cols-2">
                    <Input
                      label="New Password"
                      name="password"
                      type="password"
                      placeholder="Set a new password"
                      value={profileData.password}
                      onChange={handleChange}
                    />
                    
                    <Input
                      label="Confirm New Password"
                      name="confirmPassword"
                      type="password"
                      placeholder="Confirm your new password"
                      value={profileData.confirmPassword}
                      onChange={handleChange}
                    />
                  </div>
                </div>

                <div className="bg-blue-50 border-l-4 border-blue-400 p-4 rounded-md">
                  <div className="flex">
                    <div className="flex-shrink-0">
                      <Info className="h-5 w-5 text-blue-400" />
                    </div>
                    <div className="ml-3">
                      <p className="text-sm text-blue-700">
                        Leave fields blank if you don't want to update them.
                      </p>
                    </div>
                  </div>
                </div>

                <Button
                  type="submit"
                  loading={loading}
                  disabled={loading}
                  fullWidth
                >
                  Update Profile
                </Button>
              </form>
            </CardContent>
          </Card>
        </div>

        <div className="space-y-6">
          <Card>
            <CardHeader>
              <CardTitle>Security</CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="flex items-start">
                <div className="flex-shrink-0 mt-0.5">
                  <Shield className="h-5 w-5 text-green-500" />
                </div>
                <div className="ml-3">
                  <h4 className="text-sm font-medium text-gray-900">Two-Factor Authentication</h4>
                  <p className="mt-1 text-sm text-gray-500">
                    Add an extra layer of security to your account
                  </p>
                </div>
              </div>
              <Button variant="outline" fullWidth disabled>
                Enable 2FA
              </Button>
            </CardContent>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle>Account Status</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                <div className="flex items-center">
                  <div className="w-3 h-3 rounded-full bg-green-500 mr-2"></div>
                  <span className="font-medium">Active</span>
                </div>
                <div className="text-sm text-gray-500">
                  <p>Account created on: {user?.creationDate ? new Date(user.creationDate).toLocaleDateString() : 'Loading...'}</p>
                  <p className="mt-1">Last updated: {user?.modificationDate ? new Date(user.modificationDate).toLocaleDateString() : 'Never'}</p>
                </div>
              </div>
            </CardContent>
          </Card>
        </div>
      </div>
    </AppLayout>
  );
};

export default Profile;