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
import SidebarAssets from "./components/Dashboard/SideBarAssets";
import SidebarEmployee from "./components/Dashboard/SideBarEmployee";
import PrivateRoute from "./components/Dashboard/PrivateRoute";
import MorePage from "./components/Dashboard/MorePage";

function App() {
  return (
    <Router>
      <div className="App">
        <Routes>
          {/* Public Route */}
          <Route path="/login" element={<Login />} />

          {/* Protected Routes */}
          <Route
            path="/dashboard"
            element={
              <PrivateRoute>
                <div className="dashboard-config">
                  <Sidebar />
                  <Header />
                  <Dashboard />
                </div>
              </PrivateRoute>
            }
          />

          <Route
            path="/assets"
            element={
              <PrivateRoute>
                <div className="dashboard-config">
                  <Sidebar />
                  <SidebarAssets />
                  <Header />
                  <Assets />
                </div>
              </PrivateRoute>
            }
          />

          <Route
            path="/employee"
            element={
              <PrivateRoute>
                <div className="dashboard-config">
                  <Sidebar />
                  <SidebarEmployee />
                  <Header />
                  <Employee />
                </div>
              </PrivateRoute>
            }
          />
          <Route
            path="/more"
            element={
              <PrivateRoute>
                <div className="dashboard-config">
                  <Sidebar />
                  <Header />
                  <MorePage />
                </div>
              </PrivateRoute>
            }
          />
          <Route
            path="/adminprofile"
            element={
              <PrivateRoute>
                <AdminProfile />
              </PrivateRoute>
            }
          />

          {/* Default redirect */}
          <Route path="/" element={<Navigate to="/login" />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
