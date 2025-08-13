import toast from "react-hot-toast";
const token = localStorage.getItem("authToken");
const apiUrl = process.env.REACT_APP_API_URL;

export const logIn = (formData) => {
  return fetch(`${apiUrl}/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(formData),
  })
    .then((response) => {
      if (!response.ok) throw new Error("Network response was not ok");
      return response.json();
    })
    .catch((error) => {
      console.error("Error during Sign In:", error);
      throw error;
    });
};

export const ResetPassword = async (
  empId,
  setShowResetPassword,
  setShowForgotPassword,
  setShowLogin
) => {
  const url = `${apiUrl}/reset-password/send-otp?empId=${encodeURIComponent(
    empId
  )}`;
  try {
    const response = await fetch(url, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
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
    console.error("Error during forgot password:", error);
    toast.error("Something went wrong");
  }
};

export const handleResetPassword = async (
  mail,
  otp,
  newPassword,
  confirmPassword,
  setShowLogin,
  setShowResetPassword
) => {
  if (newPassword !== confirmPassword) {
    toast.error("Passwords did not match");
    return;
  }

  try {
    const oldPassword = "#";
    const response = await fetch(`${apiUrl}/reset-password/verify-otp`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ mail, otp, newPassword, oldPassword }),
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

export const changePassword = async (mail, oldPassword, newPassword) => {
  try {
    const response = await fetch(`${apiUrl}/reset-password/change-password-using-oldPassword`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({ mail, oldPassword, newPassword }),
    });

    const text = await response.text();

    let message = '';
    try {
      const data = JSON.parse(text);
      message = data.message || '';
    } catch (e) {
      message = text;
    }

    console.log("STATUS:", response.status);
    console.log("BODY:", message);

    if (response.status === 200 || response.status === 201) {
      toast.success(message || "Password changed successfully");
    } else {
      toast.error(message || "Password change failed");
    }
  } catch (error) {
    console.error("Error changing password:", error);
    toast.error("Something went wrong");
  }
};

export const getOtherAdmins = async () => {
  try {
    const response = await fetch(`${apiUrl}/assetManager/v1/admin/get/all`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      const errorMsg = await response.text();
      throw new Error(errorMsg || 'Failed to fetch admin list');
    }

    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Error fetching other admins:", error);
    throw error;
  }
};



