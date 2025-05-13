// useLoginStatus.js

import { useState, useEffect } from 'react';

function useLoginStatus() {
  const [isLogin, setIsLogin] = useState(false);

  useEffect(() => {
    const loginStatus = localStorage.getItem('isLogin');
    setIsLogin(loginStatus === 'true');

    const handleStorageChange = () => {
      const updatedLoginStatus = localStorage.getItem('isLogin');
      setIsLogin(updatedLoginStatus === 'true');
    };

    window.addEventListener('storage', handleStorageChange);

    return () => {
      window.removeEventListener('storage', handleStorageChange);
    };
  }, []);

  return isLogin;
}

export default useLoginStatus;
