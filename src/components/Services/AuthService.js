import toast from "react-hot-toast";

const token = localStorage.getItem('authToken');
const apiUrl = process.env.REACT_APP_API_URL;

export const logIn = (formData) => {
  return fetch(`${apiUrl}/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(formData),
  })
    .then((response) => {
      if (!response.ok) throw new Error('Network response was not ok');
      return response.json();
    })
    .catch((error) => {
      console.error('Error during Sign In:', error);
      throw error;
    });
};

export const ResetPassword = async (empId, setShowResetPassword, setShowForgotPassword, setShowLogin) => {
  const url = `${apiUrl}/forget_password/validation?empId=${encodeURIComponent(empId)}`;
  try {
    const response = await fetch(url, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
    });

    if (response.ok) {
      toast.success("OTP sent to your Mail Id");
      setShowResetPassword(true);
      setShowForgotPassword(false);
      setShowLogin(false);
    } else {
      toast.error("Invalid empId");
    }
  } catch (error) {
    console.error('Error during forgot password:', error);
    toast.error("Something went wrong");
  }
};

export const handleResetPassword = async (mail,otp, newPassword, confirmPassword, setShowLogin, setShowResetPassword) => {
  if (newPassword !== confirmPassword) {
    toast.error("Passwords do not match");
    return;
  }

  try {
    const response = await fetch(`${apiUrl}/forget_password/saveNewPassword`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({mail, otp, newPassword }),
    });

    if (response.ok) {
      toast.success("Password reset successful");
      setShowLogin(true);
      setShowResetPassword(false);
    } else {
      toast.error("Reset failed. Check OTP & Mail Id or try again.");
    }
  } catch (error) {
    toast.error("Something went wrong");
  }
};
