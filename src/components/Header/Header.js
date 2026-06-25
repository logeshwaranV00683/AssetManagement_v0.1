import React, { useState, useEffect } from "react";
import {
  AppBar,
  Toolbar,
  Typography,
  IconButton,
  Menu,
  MenuItem,
} from "@mui/material";
import LockResetIcon from "@mui/icons-material/LockReset";
import AccountCircleIcon from "@mui/icons-material/AccountCircle";
import LogoutIcon from "@mui/icons-material/Logout";
import { useNavigate } from "react-router-dom";
import "../Style/font.css";
import Dialog from "@mui/material/Dialog";
import DialogContent from "@mui/material/DialogContent";
import AdminProfile from "../Dashboard/AdminProfile";


const Header = ({ onLogout }) => {
  const [anchorEl, setAnchorEl] = useState(null);
  const [userInfo, setUserInfo] = useState({ name: "" });
  const navigate = useNavigate();

  useEffect(() => {
    const user = JSON.parse(localStorage.getItem("user"));
    if (user) {
      setUserInfo({ name: user.firstName });
    }
  }, []);

  const handleMenuOpen = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };
  const handleLogout = () => {
    handleMenuClose();
    localStorage.removeItem("authToken");
    localStorage.removeItem("isLogin");
    localStorage.removeItem("user");
    navigate("/login");
  };

  const [openAdminProfile, setOpenAdminProfile] = useState(false);

  const handleAdminProfile = () => {
    // handleMenuClose();
    setOpenAdminProfile(true);
  };


  const handleProfileClose = () => {
    setOpenAdminProfile(false);
  };


  return (
    <>
      <AppBar
        position="fixed"
        elevation={0}
        style={{
          background: "linear-gradient(45deg, #6DE0FF, #2BC4F3)",
          height: "14vh",
          width: "90%",
          margin: "0 auto",
          top: 0,
          left: 0,
          right: 0,
          borderRadius: "0 0 60px 60px",
          zIndex: 1300,
          display: "flex",
          justifyContent: "center",
        }}
      >
        <Toolbar
          style={{
            width: "100%",
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
            padding: "0 32px",
          }}
        >
          {/* Left: Title */}
          <Typography
            variant="h5"
            style={{
              fontSize: "32px",
              color: "#083A40",
              fontFamily: "'Racing Sans One', sans-serif",
              textShadow: "0 0 4px #B2F1FF, 0 0 8px #2BC4F3",
              letterSpacing: "2px",
              marginLeft: "30px",
            }}
          >
            Verinite - Assets Management
          </Typography>

          {/* Right: Welcome + Icon */}
          <div style={{ display: "flex", alignItems: "center", gap: "12px" }}>
            <span
              style={{
                fontSize: "18px",
                color: "#083A40",
                fontFamily: "'Racing Sans One', sans-serif",
                textShadow: "0 0 3px #B2F1FF, 0 0 6px #2BC4F3",
                marginLeft: "30px",
              }}
            >
              {userInfo.name ? `Welcome! - ${userInfo.name}` : "Loading..."}
            </span>

            <IconButton
              aria-label="account"
              onClick={handleMenuOpen}
              color="inherit"
              style={{
                transform: "scale(1.8)",
                color: "#083A40",
                textShadow: "0 0 4px #B2F1FF, 0 0 6px #2BC4F3",
                marginRight: "60px",
              }}
            >
              <AccountCircleIcon />
            </IconButton>

            <Menu
              id="menu-appbar"
              anchorEl={anchorEl}
              anchorOrigin={{ vertical: "bottom", horizontal: "right" }}
              transformOrigin={{ vertical: "top", horizontal: "right" }}
              open={Boolean(anchorEl)}
              onClose={handleMenuClose}
            >
              <MenuItem
                onClick={() => {
                  handleMenuClose(); // close menu first
                  handleAdminProfile(); // then open dialog
                }}
                style={{
                  color: "green",
                  fontWeight: "bold",
                  textShadow: "0 0 3px #fff, 0 0 6px #4dff5cff",
                }}
              >
                <LockResetIcon style={{ marginRight: "8px" }} />
                Profile
              </MenuItem>

              <MenuItem
                onClick={handleLogout}
                style={{
                  color: "red",
                  fontWeight: "bold",
                  textShadow: "0 0 3px #fff, 0 0 6px #FF4D4D",
                }}
              >
                <LogoutIcon style={{ marginRight: "8px" }} />
                Logout
              </MenuItem>
            </Menu>
            <Dialog
              open={openAdminProfile}
              onClose={handleProfileClose}
              fullWidth
              maxWidth="md"
            >
              <DialogContent sx={{ p: 0 }}>
                <AdminProfile onClose={handleProfileClose} />
              </DialogContent>
            </Dialog>

          </div>
        </Toolbar>
      </AppBar>

      <div style={{ height: "14vh" }} />
    </>
  );
};

export default Header;
