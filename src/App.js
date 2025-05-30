// App.js

import React from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import Login from './components/Login';
import Dashboard from './components/Dashboard/Dashboard';
import Assets from './components/Dashboard/Assets';
import Header from './components/Header/Header';
import Sidebar from './components/Dashboard/SideBar';
import Employee from './components/Dashboard/Employee';

function App() {

  return (
    <Router>
      <div className="App">
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/dashboard" element={
          <div className='dashboard-config'>
            <Sidebar/>
            <Header/>
            <Dashboard />
          </div>
          } />
          <Route path="/assets"  element={
          <div className='dashboard-config'>
            <Sidebar/>
            <Header/>
            <Assets />
          </div>
          } />
           <Route path="/employee"  element={
          <div className='dashboard-config'>
            <Sidebar/>
            <Header/>
            <Employee/>
          </div>
          } />
          <Route path="/" element={<Navigate to="/login" />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
