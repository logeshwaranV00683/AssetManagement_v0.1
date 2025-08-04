import React, { useState, useEffect } from "react";
import { changePassword, getOtherAdmins } from "../Services/AuthService";
import toast from "react-hot-toast";
import { IconButton, InputAdornment } from "@mui/material";
import { Visibility, VisibilityOff } from "@mui/icons-material";
import { motion, AnimatePresence } from "framer-motion";






import {
  Box,
  Paper,
  Typography,
  TextField,
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

  const fadeVariant = {
    hidden: { opacity: 0, y: 20, rotateY: -10 },
    visible: { opacity: 1, y: 0, rotateY: 0, transition: { duration: 0.5 } },
    exit: { opacity: 0, y: -20, rotateY: 10, transition: { duration: 0.3 } },
  };

  const [otherAdmins, setOtherAdmins] = useState([]);
  const [showPasswordForm, setShowPasswordForm] = useState(false);
  const [showPasswords, setShowPasswords] = useState({
    current: false,
    new: false,
    confirm: false,
  });



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
        mx: "auto",
        height: '80vh',
        flexWrap: "wrap",
        width: "80%",

      }}
    >
      {/* Left Section */}
      <Paper elevation={4} sx={{ flex: 0.5, maxWidth: "50%", p: 3, height: '100%', overflowY: 'auto' }}>

        {/* Admin Details */}
        <Typography variant="h4" sx={{ color: "#083A40", mb: 1.5, fontWeight: "bold" }}>
          Your Profile
        </Typography>

        <AnimatePresence mode="wait">
          {!showPasswordForm && (<Grid container spacing={2.5}>
            {/* Employee ID */}
            <Grid container item justifyContent="center">
              <Grid item xs={12} sm={6} sx={{ mb: 1 }}>
                <TextField
                  label="Employee ID"
                  value={adminData.empId}
                  fullWidth
                  InputProps={{ readOnly: true }}
                />
              </Grid>
            </Grid>

            {/* Email */}
            <Grid container item justifyContent="center">
              <Grid item xs={12} sm={6} sx={{ mb: 1 }}>
                <TextField
                  label="Email"
                  value={adminData.email}
                  fullWidth
                  InputProps={{ readOnly: true }}
                />
              </Grid>
            </Grid>

            {/* Name */}
            <Grid container item justifyContent="center">
              <Grid item xs={12} sm={6} sx={{ mb: 1 }}>
                <TextField
                  label="Name"
                  value={adminData.name}
                  fullWidth
                  InputProps={{ readOnly: true }}
                />
              </Grid>
            </Grid>

            {/* Role */}
            <Grid container item justifyContent="center">
              <Grid item xs={12} sm={6} sx={{ mb: 1 }}>
                <TextField
                  label="Role"
                  value={adminData.role}
                  fullWidth
                  InputProps={{ readOnly: true }}
                />
              </Grid>
            </Grid>

            {/* Location */}
            <Grid container item justifyContent="center">
              <Grid item xs={12} sm={6} sx={{ mb: 1 }}>
                <TextField
                  label="Location"
                  value={adminData.location}
                  fullWidth
                  InputProps={{ readOnly: true }}
                />
              </Grid>
            </Grid>
          </Grid>
          )}
        </AnimatePresence>


        {/* password change button */}
        <Box sx={{ textAlign: "center", mt: 5 }}>
          <Button
            variant="contained"
            color={showPasswordForm ? "error" : "primary"}
            onClick={() => setShowPasswordForm(!showPasswordForm)}
          >
            {showPasswordForm ? "Cancel" : "Change Password"}
          </Button>
        </Box>

        <AnimatePresence mode="wait">
          {showPasswordForm && (
            <motion.div
              key="passwordForm"
              variants={fadeVariant}
              initial="hidden"
              animate="visible"
              exit="exit"
              style={{ marginTop: "32px" }}
            >
              <form onSubmit={handleSubmit} style={{ marginTop: "32px" }}>
                <Grid container spacing={2}>
                  {/* Current Password */}
                  <Grid container item justifyContent="center">
                    <Grid item xs={12} sm={6}>
                      <TextField
                        label="Current Password"
                        name="currentPassword"
                        type={showPasswords.current ? "text" : "password"}
                        value={passwords.currentPassword}
                        onChange={handlePasswordChange}
                        fullWidth
                        InputProps={{
                          endAdornment: (
                            <InputAdornment position="end">
                              <IconButton
                                onClick={() =>
                                  setShowPasswords((prev) => ({
                                    ...prev,
                                    current: !prev.current,
                                  }))
                                }
                                edge="end"
                              >
                                {showPasswords.current ? <VisibilityOff /> : <Visibility />}
                              </IconButton>
                            </InputAdornment>
                          ),
                        }}
                      />
                    </Grid>
                  </Grid>

                  {/* New Password */}
                  <Grid container item justifyContent="center">
                    <Grid item xs={12} sm={6}>
                      <TextField
                        label="New Password"
                        name="newPassword"
                        type={showPasswords.new ? "text" : "password"}
                        value={passwords.newPassword}
                        onChange={handlePasswordChange}
                        fullWidth
                        InputProps={{
                          endAdornment: (
                            <InputAdornment position="end">
                              <IconButton
                                onClick={() =>
                                  setShowPasswords((prev) => ({
                                    ...prev,
                                    new: !prev.new,
                                  }))
                                }
                                edge="end"
                              >
                                {showPasswords.new ? <VisibilityOff /> : <Visibility />}
                              </IconButton>
                            </InputAdornment>
                          ),
                        }}
                      />
                    </Grid>
                  </Grid>

                  {/* Confirm New Password */}
                  <Grid container item justifyContent="center">
                    <Grid item xs={12} sm={6}>
                      <TextField
                        label="Confirm New Password"
                        name="confirmPassword"
                        type={showPasswords.confirm ? "text" : "password"}
                        value={passwords.confirmPassword}
                        onChange={handlePasswordChange}
                        fullWidth
                        InputProps={{
                          endAdornment: (
                            <InputAdornment position="end">
                              <IconButton
                                onClick={() =>
                                  setShowPasswords((prev) => ({
                                    ...prev,
                                    confirm: !prev.confirm,
                                  }))
                                }
                                edge="end"
                              >
                                {showPasswords.confirm ? <VisibilityOff /> : <Visibility />}
                              </IconButton>
                            </InputAdornment>
                          ),
                        }}
                      />
                    </Grid>
                  </Grid>
                </Grid>

                <Box sx={{ textAlign: "center", mt: 8 }}>
                  <Button variant="contained" color="success" type="submit">
                    Change Password
                  </Button>
                </Box>
              </form>
            </motion.div>
          )}
        </AnimatePresence>


        {/* <Divider sx={{ my: 4 }} />

        <Typography variant="h6" sx={{ mb: 2, color: "#083A40" }}>
          Change Password
        </Typography> */}

      </Paper>


      {/* Right Section */}
      <Paper elevation={4} sx={{ flex: 0.7, maxWidth: "35%", p: 3, height: '100%', overflowY: 'auto' }}>
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
