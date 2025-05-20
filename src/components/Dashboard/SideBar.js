import React, { useEffect, useState } from 'react';
import Drawer from '@mui/material/Drawer';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemIcon from '@mui/material/ListItemIcon';
import Tooltip from '@mui/material/Tooltip';
import DashboardIcon from '@mui/icons-material/Dashboard';
import PeopleIcon from '@mui/icons-material/People';
import HistoryIcon from '@mui/icons-material/History';
import MonitorIcon from '@mui/icons-material/Monitor';
import AppsIcon from '@mui/icons-material/Apps';
import { useNavigate } from 'react-router-dom';



const drawerWidth = 90;

const Sidebar = () => {
  const [selectedItem, setSelectedItem] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const path = window.location.pathname.split('/')[1];
    setSelectedItem(path);
  }, []);

  const handleItemClick = (item) => {
    setSelectedItem(item);
    navigate(`/${item}`);
  };

  const menuItems = [
    { key: 'dashboard', icon: <DashboardIcon fontSize="large" />, label: 'Dashboard' },
    { key: 'assets', icon: <MonitorIcon fontSize="large" />, label: 'Assets' },
    { key: 'employee', icon: <PeopleIcon fontSize="large" />, label: 'Employees' },
    { key: 'history', icon: <HistoryIcon fontSize="large" />, label: 'Asset History' },
    { key: 'services', icon: <AppsIcon fontSize="large" />, label: 'Services' },
    { key: 'report', icon: <HistoryIcon fontSize="large" />, label: 'Report' },
  ];

  return (
    <Drawer
      variant="permanent"
      sx={{
        width: drawerWidth,
        flexShrink: 0,
        [`& .MuiDrawer-paper`]: {
          width: drawerWidth,
          boxSizing: 'border-box',
          backgroundColor: '#ebf7fd',
        },
      }}
    >
      <div
        onClick={() => handleItemClick('dashboard')}
        style={{ padding: '16px 0', display: 'flex', justifyContent: 'center', cursor: 'pointer' }}
      >
        <img
          src="/Verinite-Logo.png"
          alt="Logo"
          style={{ width: '55px', height: 'auto', marginTop: '8px' }}
        />
      </div>

      <List sx={{ mt: 6 }}>
        {menuItems.map(({ key, icon, label }) => (
          <Tooltip key={key} title={label} placement="right" arrow
           componentsProps={{
                            tooltip: {
                              sx: {
                                fontSize: '18px',        // Larger font
                                fontWeight: 'bold',      // Bold text
                                backgroundColor: '#1FCBEA', // Custom background
                                color: '#fff',           // White text
                                padding: '6px 12px',     // More padding
                                borderRadius: '6px',     // Rounded corners
                                boxShadow: '0 2px 8px rgba(0, 0, 0, 0.2)', // Subtle shadow
                              },
                            },
                            arrow: {
                              sx: {
                                color: '#1FCBEA', // Match arrow color with tooltip background
                              },
                            },
                          }}>
            <ListItem
              button
              onClick={() => handleItemClick(key)}
              sx={{
                justifyContent: 'center',
                backgroundColor: selectedItem === key ? '#1FCBEA' : 'inherit', mb: 4,
                '&:hover': {
                  backgroundColor: '#1FCBEA',
                },
              }}
            >
              <ListItemIcon
                sx={{
                  minWidth: 0,
                  color: selectedItem === key ? 'white' : '#2196F3',
                }}
              >
                {icon}
              </ListItemIcon>
            </ListItem>
          </Tooltip>
        ))}
      </List>
    </Drawer>
  );
};

export default Sidebar;
