import React, { useEffect, useState } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Drawer from '@material-ui/core/Drawer';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import DashboardIcon from '@material-ui/icons/Dashboard';
import PeopleIcon from '@material-ui/icons/People';
import HistoryIcon from '@mui/icons-material/History';
import MonitorIcon from '@mui/icons-material/Monitor';
import AppsIcon from '@mui/icons-material/Apps';
import './SideBar.css'
import { useNavigate } from 'react-router-dom';

const drawerWidth = 240;

const useStyles = makeStyles((theme) => ({
  drawer: {
    width: drawerWidth,
    flexShrink: 0,
  },
  drawerPaper: {
    width: drawerWidth,
  },
  listItem: {
    '&:hover': {
      backgroundColor: '#1FCBEA', // Background color on hover
    },
  },
  selected: {
    backgroundColor: '#1FCBEA',
    color: 'white', // Text color of selected item
    '&:hover': {
      backgroundColor: '#1FCBEA', // Background color on hover
    },
  },
  blueIcon: {
    color: '#2196F3', // Blue color for icon
  },
  toolbar: theme.mixins.toolbar,
}));

const Sidebar = () => {
  useEffect(() => {
    console.log('loc', window.location.pathname);
    if (window.location.pathname === '/dashboard') {
      setSelectedItem('dashboard')
    }
  }, [])
  const navigate = useNavigate();

  const classes = useStyles();
  const [selectedItem, setSelectedItem] = useState('');

  const handleItemClick = (item) => {
    console.log('item', item);

    let routeVal = `/${item}`
    setSelectedItem(item);
    navigate(routeVal)
  };

  return (
    <Drawer
      className={classes.drawer}
      variant="permanent"
      classes={{
        paper: classes.drawerPaper,
      }}
    >
      <div className={`${classes.toolbar} align`}>
        <div className='admin'>
          <span className='title'>Admin: </span>
          <span className='title-val'>ABCD</span>
        </div>
        <div className='emp'>
          <span className='title'>Emp Id: </span>
          <span className='title-val'>1222332</span>
        </div>
      </div>
      <List>
        <ListItem button className={`${classes.listItem} ${selectedItem === 'dashboard' ? classes.selected : ''}`}
          onClick={(e) => {
            e.preventDefault();
            handleItemClick('dashboard')
          }}
        >
          <ListItemIcon className={selectedItem === 'dashboard' ? classes.selected : classes.blueIcon}><DashboardIcon /></ListItemIcon>
          <ListItemText className={classes.listItemText} primary="Dashboard" />
        </ListItem>

        <ListItem button className={`${classes.listItem} ${selectedItem === 'assets' ? classes.selected : ''}`}
          onClick={() => {
            handleItemClick('assets')
          }
          }>
          <ListItemIcon><MonitorIcon /></ListItemIcon>
          <ListItemText className={classes.listItemText} primary="Assets" />
        </ListItem>
        <ListItem className={`${classes.listItem} ${selectedItem === 'employee' ? classes.selected : ''}`} button  onClick={() => {
            handleItemClick('employee')
          }}>          
        <ListItemIcon><PeopleIcon /></ListItemIcon>
          <ListItemText className={classes.listItemText} primary="Employees" />
        </ListItem>
        <ListItem button className={classes.listItem}>
          <ListItemIcon>< HistoryIcon /></ListItemIcon>
          <ListItemText className={classes.listItemText} primary="Asset History" />
        </ListItem>
        <ListItem button className={classes.listItem}>
          <ListItemIcon><AppsIcon /></ListItemIcon>
          <ListItemText className={classes.listItemText} primary="Services" />
        </ListItem>
        <ListItem button className={classes.listItem}>
          <ListItemIcon>< HistoryIcon /></ListItemIcon>
          <ListItemText className={classes.listItemText} primary="Report" />
        </ListItem>
      </List>
    </Drawer>
  );
}

export default Sidebar;
