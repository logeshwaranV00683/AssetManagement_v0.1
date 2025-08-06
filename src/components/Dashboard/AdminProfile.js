import React, { useState, useEffect } from "react";
import { changePassword, getOtherAdmins } from "../Services/AuthService";
import toast from "react-hot-toast";
import { IconButton, InputAdornment } from "@mui/material";
import { Visibility, VisibilityOff } from "@mui/icons-material";
import { motion, AnimatePresence } from "framer-motion";
import CloseIcon from '@mui/icons-material/Close';
import {  Box,  Paper,  Typography,  TextField,  Button,  Grid,} from "@mui/material";

const AdminProfile = ({ onClose }) => {
  // const navigate = useNavigate();
  const [adminData, setAdminData] = useState({    name: "",    email: "",    role: "",    contact: "",    location: "",  });
  const fadeVariant = {
    hidden: { opacity: 0, y: 20, rotateY: -10 },
    visible: { opacity: 1, y: 0, rotateY: 0, transition: { duration: 0.5 } },
    exit: { opacity: 0, y: -20, rotateY: 10, transition: { duration: 0.3 } },
  };

  const [otherAdmins, setOtherAdmins] = useState([]);
  const [showPasswordForm, setShowPasswordForm] = useState(false);
  const [showPasswords, setShowPasswords] = useState({    current: false,    new: false,    confirm: false,  });

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

    getOtherAdmins().then((data) => {
      setOtherAdmins(data);
    });
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
        position: "fixed",
        top: "50%",
        left: "50%",
        transform: "translate(-50%, -50%)", 
        width: {
          xs: "90%",   // mobile
          sm: "80%",   // tablet
          md: "75%",   // small desktop
          lg: "70%",   // large screens
        },
        height: {
          xs: "85%",  // mobile
          sm: "80%",  // tablet
          md: "75%",  // small desktop
          lg: "80%",   // large screens
        },
        backgroundColor: "#2dcfecff",
        borderRadius: 4,
        boxShadow: 24,
        zIndex: 1500,
        overflowY: "auto", 
        p: {
          xs: 2,
          sm: 3,
          md: 4
        },
        "&::-webkit-scrollbar": {
          width: 0,
        },
        "&::-webkit-scrollbar-thumb": {
          backgroundColor: "transparent",
        },
      }}
    >
      {/* Close Button */}
      <IconButton
        onClick={() => {
          if (typeof onClose === "function") {
            onClose();
          }
        }}
        sx={{
          position: "fixed",
          top: 16,
          right: 16,
          zIndex: 1300,
          backgroundColor: "#f2f2f2",
          "&:hover": { backgroundColor: "#e0e0e0" },
        }}
      >
        <CloseIcon />
      </IconButton>

      <Grid container spacing={8}>

        {/* Left side: Your Profile */}
        <Grid item xs={12} md={6}>
          <Paper elevation={4} sx={{ flex: 0.7, maxWidth: "100%", p: 3, height: '90%', minHeight: "520px", overflow: 'hidden', position: "relative", borderRadius: "1rem" }}>
            <Box sx={{ position: "relative", height: "100%" }}>
              <AnimatePresence mode="wait">
                <motion.div
                  key={showPasswordForm ? "passwordForm" : "profileDetails"}
                  layout
                  variants={fadeVariant}
                  initial="hidden"
                  animate="visible"
                  exit="exit"
                  style={{ position: "absolute", width: '100%' }}
                >
                  {showPasswordForm ? (
                    <>
                      <Typography variant="h4" sx={{ color: "#083A40", mb: 3, fontWeight: "bold", marginBottom: "5rem" }}>Change Password</Typography>
                      <form onSubmit={handleSubmit}>
                        <Grid container spacing={5}>
                          {['current', 'new', 'confirm'].map((type, idx) => (
                            <Grid container item justifyContent="center" key={type}>
                              <Grid item xs={12} sm={8} md={8}>
                                <TextField
                                  label={`${type === 'current' ? 'Current' : type === 'new' ? 'New' : 'Confirm New'} Password`}
                                  name={`${type}Password`}
                                  type={showPasswords[type] ? "text" : "password"}
                                  value={passwords[`${type}Password`]}
                                  onChange={handlePasswordChange}
                                  fullWidth
                                  InputProps={{
                                    endAdornment: (
                                      <InputAdornment position="end">
                                        <IconButton
                                          onClick={() => setShowPasswords(prev => ({ ...prev, [type]: !prev[type] }))}
                                          edge="end"
                                        >
                                          {showPasswords[type] ? <VisibilityOff /> : <Visibility />}
                                        </IconButton>
                                      </InputAdornment>
                                    ),
                                  }}
                                />
                              </Grid>
                            </Grid>
                          ))}
                        </Grid>
                        <Box sx={{ textAlign: "center", mt: 8, display: "flex", gap: 2, justifyContent: "center" }}>
                          <Button variant="outlined" color="error" onClick={() => setShowPasswordForm(false)}>Cancel</Button>
                          <Button variant="contained" color="success" type="submit">Change Password</Button>
                        </Box>
                      </form>
                    </>
                  ) : (
                    <>
                      <Typography variant="h4" sx={{ color: "#083A40", fontWeight: "bold", mb: 2 }}>Your Profile</Typography>
                      <Grid container spacing={3.5}>
                        {["empId", "email", "name", "role", "location"].map((field, i) => (
                          <Grid container item justifyContent="center" key={field}>
                            <Grid item xs={12} sm={8} md={8}>
                              <TextField
                                label={field.charAt(0).toUpperCase() + field.slice(1).replace("Id", " ID")}
                                value={adminData[field]}
                                fullWidth
                                InputProps={{ readOnly: true }}
                              />
                            </Grid>
                          </Grid>
                        ))}
                      </Grid>
                      <Box sx={{ textAlign: "center", mt: 3 }}>
                        <Button variant="contained" color="warning" onClick={() => setShowPasswordForm(true)}>Change Password</Button>
                      </Box>
                    </>
                  )}
                </motion.div>
              </AnimatePresence>
            </Box>
          </Paper>
        </Grid>


        {/* Right Section */}
        <Grid item xs={12} md={6}>
          <Paper elevation={4} sx={{ flex: 0.7, maxWidth: "100%", p: 3, height: '90%', minHeight: "520px", overflowY: 'auto', borderRadius: "1rem" }}>
            <Typography variant="h5" sx={{ color: "#083A40", mb: 2, fontWeight: "bold" }}>
              All Admins' Profile
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
        </Grid>

      </Grid>
    </Box>
  );
};

export default AdminProfile;
