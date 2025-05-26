import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-hot-toast';
import { FaEye, FaEyeSlash } from 'react-icons/fa';
import { logIn, ResetPassword, handleResetPassword } from './Services/AuthService';
import './Style/Login.css';
import { motion, AnimatePresence } from 'framer-motion';
import 'animate.css';

const CustomInput = ({ type, name, placeholder, value, onChange, icon }) => (
  <div className="input-container">
    <input
      type={type}
      name={name}
      placeholder={placeholder}
      value={value}
      onChange={onChange}
    />
    <div className="icon">{icon}</div>
  </div>
);

const fadeVariant = {
  hidden: { opacity: 0, y: 20, rotateY: -10 },
  visible: { opacity: 1, y: 0, rotateY: 0, transition: { duration: 0.5 } },
  exit: { opacity: 0, y: -20, rotateY: 10, transition: { duration: 0.3 } }
};

const Login = () => {
  const navigate = useNavigate();
  const [showLogin, setShowLogin] = useState(true);
  const [showForgotPassword, setShowForgotPassword] = useState(false);
  const [showResetPassword, setShowResetPassword] = useState(false);

  const [mail, setMail] = useState('');
  const [empId, setEmpId] = useState('');
  const [otp, setOtp] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [isLogin, setIsLogin] = useState(false);

  const [formData, setFormData] = useState({ employeeId: '', password: '' });
  const [showPassword, setShowPassword] = useState(false);

  useEffect(() => {
    localStorage.removeItem('isLogin');
  }, []);

  const passwordIcon = showPassword ? (
    <FaEyeSlash onClick={() => setShowPassword(false)} />
  ) : (
    <FaEye onClick={() => setShowPassword(true)} />
  );

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setIsLogin(true);
      const data = await logIn({ empId: formData.employeeId, password: formData.password });

      if (data?.token) {
        localStorage.setItem('authToken', data.token);
        localStorage.setItem('isLogin', 'yes');
        localStorage.setItem('user', JSON.stringify(data.data));
        navigate('/dashboard');
        toast.success('Logged in successfully');
      } else {
        throw new Error('Authentication failed');
      }
    } catch (error) {
      toast.error('Login Failed');
    } finally {
      setIsLogin(false);
    }
  };

  const handleForgotPassword = () => {
    setShowLogin(false);
    setShowForgotPassword(true);
  };

  const handleCancel = () => {
    setShowLogin(true);
    setShowForgotPassword(false);
    setShowResetPassword(false);
  };

  return (
    <motion.div className="login-page" initial="hidden" animate="visible" exit="exit" variants={fadeVariant}>
      <div className="container">
        <motion.div
          className="login-form-container form-container animate__animated animate__fadeInUp"
          variants={fadeVariant}
        >
          <img src="../assets/logo2.png" alt="Logo" width={90} className="mb-4" />
          <h1 style={{ color: '#41b5f0' }}>Asset Manager</h1>
          <form className="login-form">
            <AnimatePresence mode="wait">
              {showLogin && (
                <motion.div key="login" variants={fadeVariant} initial="hidden" animate="visible" exit="exit">
                  <input
                    type="text"
                    name="employeeId"
                    placeholder="Employee Id"
                    value={formData.employeeId}
                    onChange={handleChange}
                  />
                  <CustomInput
                    type={showPassword ? 'text' : 'password'}
                    name="password"
                    placeholder="Password"
                    value={formData.password}
                    onChange={handleChange}
                    icon={passwordIcon}
                  />
                  <button type="submit" className="submit-btn" disabled={isLogin} onClick={handleSubmit}>
                    <b>{isLogin ? 'Logging...' : 'Login'}</b>
                  </button>
                  <div className="text-label" style={{ textAlign: 'center', width: '100%' }}>
                    <h5
                      onClick={handleForgotPassword}
                      style={{ color: 'blue', cursor: 'pointer', display: 'inline-block' }}
                    >
                      Forgot Password
                    </h5>
                  </div>
                </motion.div>
              )}

              {showForgotPassword && (
                <motion.div key="forgot" variants={fadeVariant} initial="hidden" animate="visible" exit="exit">
                  <h3>Forgot Password</h3>
                  <input
                    type="textBox"
                    placeholder="Enter your Employee Id"
                    value={empId}
                    onChange={(e) => setEmpId(e.target.value)}
                  />
                  <button
                    type="button"
                    className="submit-btn"
                    onClick={() => ResetPassword(empId, setShowResetPassword, setShowForgotPassword, setShowLogin)}
                  >
                    Send OTP
                  </button>
                  <button type="button" style={{ marginTop: '15px' }} onClick={handleCancel}>
                    Cancel
                  </button>
                </motion.div>
              )}

              {showResetPassword && (
                <motion.div key="reset" variants={fadeVariant} initial="hidden" animate="visible" exit="exit">
                  <h3>Reset Password</h3>
                  <input
                    type="email"
                    placeholder="Enter your email"
                    value={mail}
                    onChange={(e) => setMail(e.target.value)}
                  />
                  <input
                    type="number"
                    placeholder="Enter your OTP"
                    value={otp}
                    onChange={(e) => setOtp(e.target.value)}
                  />
                  <input
                    type="password"
                    placeholder="Enter New Password"
                    value={newPassword}
                    onChange={(e) => setNewPassword(e.target.value)}
                  />
                  <input
                    type="password"
                    placeholder="Confirm Password"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                  />
                  <button
                    type="button"
                    className="submit-btn"
                    onClick={() =>
                      handleResetPassword(mail,otp, newPassword, confirmPassword, setShowLogin, setShowResetPassword)
                    }
                  >
                    Reset Password
                  </button>
                </motion.div>
              )}
            </AnimatePresence>
          </form>
        </motion.div>
      </div>
    </motion.div>
  );
};

export default Login;
