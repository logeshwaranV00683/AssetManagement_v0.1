import React, { useState,useEffect } from "react";
import {
  AppBar,
  Toolbar,
  Typography,
  IconButton,
  Menu,
  MenuItem,
} from "@material-ui/core";
import AccountCircleIcon from "@material-ui/icons/AccountCircle";
import LogoutIcon from '@mui/icons-material/Logout';
import { useNavigate } from "react-router-dom";
import { colors } from "@mui/material";

const Header = ({ onLogout }) => {
  const [anchorEl, setAnchorEl] = useState(null);
  const [userInfo, setUserInfo] = useState({ name: ''})
  const navigate = useNavigate();
  onLogout = () => {
    navigate("/login");
  };
  const handleMenuOpen = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };
   useEffect(() => {
      const user = JSON.parse(localStorage.getItem('user'));
      if (user) {
        setUserInfo({ name: user.firstName});
      }
    }, []);

  const handleLogout = () => {
    handleMenuClose();
    if (onLogout) {
      onLogout();
    } else {
      navigate('/login'); // fallback
    }
  };

  return (

    <AppBar position="static" style={{ backgroundColor: '#ebf7fd' }} color='black'>

      <Toolbar>
        {/* Logo 
        <div style={{ display: 'flex', alignItems: 'center', marginLeft: 100 }}>
          <img
            src="/verinite-open-graph-logo.jpeg"
            alt="Logo"
            style={{ width: '180px', height: 'auto' }}
          />
        </div>
          */}
        {/* Title */}
        <Typography variant="h6" style={{ flexGrow: 1, marginLeft: 100 }}>
          Asset Management
        </Typography>

        {/* User Menu */}
        <div>
          <span className='title-val'>{"Welcome! "+userInfo.name+" "|| 'Loading...'}</span>
          <IconButton
            aria-label="account of current user"
            aria-controls="menu-appbar"
            aria-haspopup="true"
            onClick={handleMenuOpen}
            color="inherit"
            style={{
              transform: "scale(2.0)", // 1.5x larger (50% bigger)
            }}
          >
            <AccountCircleIcon />
          </IconButton>
          <Menu
            id="menu-appbar"
            anchorEl={anchorEl}
            anchorOrigin={{
              vertical: "top",
              horizontal: "right",
            }}
            keepMounted
            transformOrigin={{
              vertical: "top",
              horizontal: "right",
            }}
            open={Boolean(anchorEl)}
            onClose={handleMenuClose}
          >
            <MenuItem onClick={onLogout} style={{color:'red'}}>
            <LogoutIcon style={{ marginRight: '8px' }} />
            Logout</MenuItem>
            {/* <MenuItem onClick={}>Profile</MenuItem> */}
          </Menu>
        </div>
      </Toolbar>
    </AppBar>
  );
};

export default Header;
