import React, { memo, useEffect, useState } from "react";
import Drawer from "@mui/material/Drawer";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import ListItemIcon from "@mui/material/ListItemIcon";
import Tooltip from "@mui/material/Tooltip";
import DashboardIcon from "@mui/icons-material/Dashboard";
import PeopleIcon from "@mui/icons-material/People";
import MonitorIcon from "@mui/icons-material/Monitor";
import AppsIcon from "@mui/icons-material/Apps";
import { useNavigate } from "react-router-dom";
import "../Style/font.css";

const drawerWidth = 80;

const Sidebar = () => {
  const [selectedItem, setSelectedItem] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    const path = window.location.pathname.split("/")[1];
    setSelectedItem(path);
  }, []);

  const handleItemClick = (item) => {
    setSelectedItem(item);
    navigate(`/${item}`);
  };

  const menuItems = [
    {
      key: "dashboard",
      icon: <DashboardIcon fontSize="large" />,
      label: "Dashboard",
    },
    { key: "assets", icon: <MonitorIcon fontSize="large" />, label: "Assets" },
    {
      key: "employee",
      icon: <PeopleIcon fontSize="large" />,
      label: "Employees",
    },
    {
      key: "more",
      icon: <AppsIcon fontSize="large" />,
      label: "More",
    },
  ];

  return (
    <Drawer
      variant="permanent"
      sx={{
        width: drawerWidth,
        flexShrink: 0,
        [`& .MuiDrawer-paper`]: {
          width: drawerWidth,
          height: "auto",
          top: "52%",
          transform: "translateY(-50%)",
          left: 0,
          bottom: "auto",
          position: "fixed",
          boxSizing: "border-box",
          background: "linear-gradient(45deg, #6DE0FF, #2BC4F3)",
          borderRadius: "0px 40px 40px 0px",
          paddingTop: "12px",
          paddingBottom: "12px",
        },
      }}
    >
      <List sx={{ mt: 0 }}>
        {menuItems.map(({ key, icon, label }) => (
          <Tooltip
            key={key}
            title={label}
            placement="right"
            arrow
            componentsProps={{
              tooltip: {
                sx: {
                  fontSize: "22px",
                  backgroundColor: "#2BC4F3",
                  color: "#083A40",
                  padding: "6px 12px",
                  borderRadius: "6px",
                  fontFamily: "'Racing Sans One', sans-serif",
                },
              },
              arrow: {
                sx: {
                  color: "#2BC4F3",
                },
              },
            }}
          >
            <ListItem
              onClick={() => handleItemClick(key)}
              sx={{
                justifyContent: "center",
                backgroundColor: selectedItem === key ? "#1FCBEA" : "inherit",
                mb: 2.5,
                borderRadius: "12px",
                transition: "background-color 0.3s ease",
                "&:hover": {
                  backgroundColor: "#1FCBEA",
                  "& .MuiListItemIcon-root": {
                    color: "#ffffff",
                    textShadow:
                      "0 0 6px #ffffff, 0 0 12px #6DE0FF, 0 0 18px #2BC4F3",
                  },
                },
              }}
            >
              <ListItemIcon
                sx={{
                  minWidth: 0,
                  color: selectedItem === key ? "#ffffff" : "#083A40",
                  textShadow:
                    selectedItem === key
                      ? "0 0 6px #ffffff, 0 0 12px #6DE0FF, 0 0 18px #2BC4F3"
                      : "none",
                  transition: "all 0.3s ease-in-out",
                }}
              >
                {icon}
              </ListItemIcon>
            </ListItem>
          </Tooltip>
        ))}
      </List>
    </Drawer>
  );
};

export default memo(Sidebar);
