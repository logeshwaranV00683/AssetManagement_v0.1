import React, { useState, useEffect } from "react";
import { changePassword, getOtherAdmins  } from "../Services/AuthService";
import toast from "react-hot-toast";


import {
  Box,
  Paper,
  Typography,
  TextField,
  Divider,
  Button,
  Grid,
} from "@mui/material";

const AdminProfile = () => {
  const [adminData, setAdminData] = useState({
    name: "",
    email: "",
    role: "",
    contact: "",
    location: "",
  });

  const [otherAdmins, setOtherAdmins] = useState([]);

  getOtherAdmins().then((data) => {
  setOtherAdmins(data);
});


  useEffect(() => {
    const userData = JSON.parse(localStorage.getItem("user"));
    if (userData) {
      setAdminData({
        name: (userData.firstName || "") + " " + (userData.lastName || ""),
        email: userData.mail || "",
        role: userData.role || "",
        empId: userData.empId || "",
        location: userData.location || "",
      });
    }
  }, []);

  const [passwords, setPasswords] = useState({
    currentPassword: "",
    newPassword: "",
    confirmPassword: "",
  });

  const handlePasswordChange = (e) => {
    setPasswords({ ...passwords, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
  e.preventDefault();

  const { currentPassword, newPassword, confirmPassword } = passwords;

  if (!currentPassword || !newPassword || !confirmPassword) {
    toast.error("All password fields are required");
    return;
  }

  if (newPassword !== confirmPassword) {
    toast.error("New password and confirm password do not match");
    return;
  }

  try {
    await changePassword(adminData.email, currentPassword, newPassword);
    setPasswords({
      currentPassword: "",
      newPassword: "",
      confirmPassword: "",
    });
  } catch (error) {
  }
};


  return (
    <Box
      sx={{
        display: "flex",
        justifyContent: "space-between",
        px: 3,
        mt: 5,
        gap: 3,
      }}
    >
      {/* Left Section */}
      <Paper elevation={4} sx={{ flex: 3, maxWidth: "70%", p: 4 }}>
        {/* Admin Details */}
        <Typography variant="h4" sx={{ color: "#083A40", mb: 2, fontWeight: "bold" }}>
          Your Profile
        </Typography>

        <Grid container spacing={2}>
          <Grid item xs={12} sm={6}>
            <TextField
              label="Employee ID"
              value={adminData.empId}
              fullWidth
              InputProps={{ readOnly: true }}
            />
          </Grid>
          <Grid item xs={12} sm={6}>
            <TextField
              label="Email"
              value={adminData.email}
              fullWidth
              InputProps={{ readOnly: true }}
            />
          </Grid>
          <Grid item xs={12} sm={6}>
            <TextField
              label="Name"
              value={adminData.name}
              fullWidth
              InputProps={{ readOnly: true }}
            />
          </Grid>
          <Grid item xs={12} sm={6}>
            <TextField
              label="Role"
              value={adminData.role}
              fullWidth
              InputProps={{ readOnly: true }}
            />
          </Grid>
          <Grid item xs={12} sm={6}>
            <TextField
              label="Location"
              value={adminData.location}
              fullWidth
              InputProps={{ readOnly: true }}
            />
          </Grid>
        </Grid>

        <Divider sx={{ my: 4 }} />

        <Typography variant="h6" sx={{ mb: 2, color: "#083A40" }}>
          Change Password
        </Typography>

        <form onSubmit={handleSubmit}>
          <TextField
            label="Current Password"
            name="currentPassword"
            type="password"
            value={passwords.currentPassword}
            onChange={handlePasswordChange}
            fullWidth
            margin="normal"
          />
          <TextField
            label="New Password"
            name="newPassword"
            type="password"
            value={passwords.newPassword}
            onChange={handlePasswordChange}
            fullWidth
            margin="normal"
          />
          <TextField
            label="Confirm New Password"
            name="confirmPassword"
            type="password"
            value={passwords.confirmPassword}
            onChange={handlePasswordChange}
            fullWidth
            margin="normal"
          />

          {/*
          {error && (
            <Typography color="error" sx={{ mt: 1 }}>
              {error}
            </Typography>
          )}
          {success && (
            <Typography sx={{ mt: 1, color: "green" }}>{success}</Typography>
          )}

          */}
          <Box sx={{ textAlign: "right", mt: 3 }}>
            <Button variant="contained" color="primary" type="submit">
              Change Password
            </Button>
          </Box>
        </form>
      </Paper>

      {/* Right Section */}
      <Paper elevation={4} sx={{ flex: 1, maxWidth: "30%", p: 4, maxHeight: '80vh', overflowY: 'auto' }}>
        <Typography variant="h5" sx={{ color: "#083A40", mb: 2, fontWeight: "bold" }}>
          Other Admins' Profile
        </Typography>

        {otherAdmins.length > 0 ? (
          otherAdmins.map((admin, index) => (
            <Box key={index} sx={{ mb: 1, p: 2, border: '0.2px solid #ccc', borderRadius: '12px' }}>
              <Typography><strong>Name:</strong> {admin.firstName} {admin.lastName}</Typography>
              <Typography><strong>Email:</strong> {admin.mail}</Typography>
              <Typography><strong>Location:</strong> {admin.location}</Typography>
            </Box>
          ))
        ) : (
          <Typography variant="body2">No other admins found.</Typography>
        )}
      </Paper>

    </Box>
  );
};

export default AdminProfile;
