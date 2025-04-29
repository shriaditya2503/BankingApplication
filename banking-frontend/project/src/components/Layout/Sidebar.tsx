import React from 'react';
import { NavLink } from 'react-router-dom';
import { Home, History, RefreshCw, User, X, LayoutDashboard, BanknoteIcon } from 'lucide-react';

interface SidebarProps {
  closeSidebar?: () => void;
}

const Sidebar: React.FC<SidebarProps> = ({ closeSidebar }) => {
  const navItems = [
    { name: 'Dashboard', to: '/dashboard', icon: <LayoutDashboard className="h-5 w-5" /> },
    { name: 'Transactions', to: '/transactions', icon: <History className="h-5 w-5" /> },
    { name: 'Transfer Funds', to: '/transfer', icon: <RefreshCw className="h-5 w-5" /> },
    { name: 'Profile', to: '/profile', icon: <User className="h-5 w-5" /> },
  ];

  return (
    <div className="h-full flex flex-col bg-primary text-white">
      <div className="flex items-center justify-between h-16 px-6 bg-primary-dark">
        <div className="flex items-center">
          <BanknoteIcon className="h-8 w-8" />
          <span className="ml-2 text-xl font-bold">BankSecure</span>
        </div>
        {closeSidebar && (
          <button 
            className="lg:hidden" 
            onClick={closeSidebar}
          >
            <X className="h-6 w-6" />
          </button>
        )}
      </div>
      <nav className="mt-6 px-4 flex-1">
        <ul className="space-y-1">
          {navItems.map((item) => (
            <li key={item.name}>
              <NavLink
                to={item.to}
                className={({ isActive }) =>
                  `flex items-center px-4 py-3 text-sm font-medium rounded-md transition-colors ${
                    isActive 
                      ? 'bg-primary-dark text-white' 
                      : 'text-primary-100 hover:bg-primary-dark/50'
                  }`
                }
                onClick={closeSidebar}
              >
                {item.icon}
                <span className="ml-3">{item.name}</span>
              </NavLink>
            </li>
          ))}
        </ul>
      </nav>
      <div className="p-4 border-t border-primary-dark text-xs text-primary-100">
        <p>© 2025 BankSecure</p>
        <p className="mt-1">Secure Banking Solutions</p>
      </div>
    </div>
  );
};

export default Sidebar;