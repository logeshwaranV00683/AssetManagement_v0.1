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

const drawerWidth = 80;

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
    { key: 'assethistory', icon: <HistoryIcon fontSize="large" />, label: 'Asset History' },
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
          height: 'auto',
          top: '52%',
          transform: 'translateY(-50%)',
          left: 0,
          bottom: 'auto',
          position: 'fixed',
          boxSizing: 'border-box',
          background: 'linear-gradient(45deg,rgb(78, 203, 219),rgb(120, 218, 233))',
          borderRadius: '0px 40px 40px 0px',
          paddingTop: '12px',
          paddingBottom: '12px',
          boxShadow: '0 0 25px rgb(78, 203, 219), 0 0 50px rgb(120, 218, 233)',
        },
      }}
    >
      <List sx={{ mt: 0 }}>
        {menuItems.map(({ key, icon, label }) => (
          <Tooltip
            key={key}
            title={label}
            placement="right"
            arrow
            componentsProps={{
              tooltip: {
                sx: {
                  fontSize: '18px',
                  fontWeight: 'bold',
                  backgroundColor: 'rgb(78, 203, 219)',
                  color: '#fff',
                  padding: '6px 12px',
                  borderRadius: '6px',
                  boxShadow: '0 2px 8px rgba(0, 0, 0, 0.2)',
                },
              },
              arrow: {
                sx: {
                  color: '#1FCBEA',
                },
              },
            }}
          >
            <ListItem
              button
              onClick={() => handleItemClick(key)}
              sx={{
                justifyContent: 'center',
                backgroundColor: selectedItem === key ? '#1FCBEA' : 'inherit',
                mb: 2.5,
                '&:hover': {
                  backgroundColor: '#1FCBEA',
                  '& .MuiListItemIcon-root': {
                    color: '#fff',
                    textShadow: '0 0 8px #fff, 0 0 16px #1FCBEA, 0 0 24px #1FCBEA',
                  },
                },
              }}
            >
              <ListItemIcon
                sx={{
                  minWidth: 0,
                  color: selectedItem === key ? '#fff' : '#083A40',
                  textShadow:
                    selectedItem === key
                      ? '0 0 8px #fff, 0 0 16px #1FCBEA, 0 0 24px #1FCBEA'
                      : 'none',
                  transition: 'all 0.3s ease-in-out',
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
