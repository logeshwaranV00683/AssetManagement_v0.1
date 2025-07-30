// App.js

import React from "react";
import {
  BrowserRouter as Router,
  Route,
  Routes,
  Navigate,
} from "react-router-dom";
import Login from "./components/Login";
import Dashboard from "./components/Dashboard/Dashboard";
import Assets from "./components/Dashboard/Assets";
import AdminProfile from "./components/Dashboard/AdminProfile";
import Header from "./components/Header/Header";
import Sidebar from "./components/Dashboard/SideBar";
import Employee from "./components/Dashboard/Employee";
import AssetsToEmployee from "./components/Dashboard/AssetsToEmployee";
import SidebarAssets from "./components/Dashboard/SideBarAssets";
import SidebarEmployee from "./components/Dashboard/SideBarEmployee";

function App() {
  return (
    <Router>
      <div className="App">
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route
            path="/dashboard"
            element={
              <div className="dashboard-config">
                <Sidebar />
                <Header />
                <Dashboard />
              </div>
            }
          />
          <Route
            path="/assets"
            element={
              <div className="dashboard-config">
                <Sidebar />
                <SidebarAssets />
                <Header />
                <Assets />
              </div>
            }
          />
          <Route
            path="/employee"
            element={
              <div className="dashboard-config">
                <Sidebar />
                <SidebarEmployee />
                <Header />
                <Employee />
              </div>
            }
          />

          <Route
            path="/assetstoemployee"
            element={
              <div className="dashboard-config">
                <Sidebar />
                <Header />
                <AssetsToEmployee />
              </div>
            }
          />
          <Route path="/" element={<Navigate to="/login" />} />
          <Route path="/adminprofile" element={<AdminProfile />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
