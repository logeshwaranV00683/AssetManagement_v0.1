import React, { memo } from "react";
import Drawer from "@mui/material/Drawer";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import Tooltip from "@mui/material/Tooltip";
import AddCircleOutlineIcon from "@mui/icons-material/AddCircleOutline";
import AssignmentTurnedInIcon from "@mui/icons-material/AssignmentTurnedIn";
import RemoveCircleOutlineIcon from "@mui/icons-material/RemoveCircleOutline";
import ReplayIcon from "@mui/icons-material/Replay";
import "../Style/font.css";

const drawerWidth = 80;

const SidebarEmployee = ({ onAddEmployee, filterByStatus, resetFilters }) => {
  const buttons = [
    {
      label: "Add Employee",
      icon: <AddCircleOutlineIcon fontSize="large" />,
      onClick: onAddEmployee,
    },
    {
      label: "Active",
      icon: <AssignmentTurnedInIcon fontSize="large" />,
      onClick: () => filterByStatus("active"),
    },
    {
      label: "Inactive",
      icon: <RemoveCircleOutlineIcon fontSize="large" />,
      onClick: () => filterByStatus("inactive"),
    },
    {
      label: "Reset",
      icon: <ReplayIcon fontSize="large" />,
      onClick: resetFilters,
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
          top: "50%",
          transform: "translateY(-50%)",
          right: 0,
          left: "auto",
          bottom: "auto",
          position: "fixed",
          boxSizing: "border-box",
          background: "linear-gradient(45deg, #6DE0FF, #2BC4F3)",
          borderRadius: "40px 0px 0px 40px",
          paddingTop: "12px",
          paddingBottom: "12px",
        },
      }}
    >
      <List>
        {buttons.map(({ label, icon, onClick }) => (
          <Tooltip
            key={label}
            title={label}
            placement="left"
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
              button
              onClick={onClick}
              sx={{
                justifyContent: "center",
                mb: 2.5,
                borderRadius: "12px",
                transition: "background-color 0.3s ease",
                "&:hover": {
                  backgroundColor: "#1FCBEA",
                  "& .MuiSvgIcon-root": {
                    color: "#fff",
                    textShadow:
                      "0 0 6px #fff, 0 0 12px #6DE0FF, 0 0 18px #2BC4F3",
                  },
                },
              }}
            >
              {icon}
            </ListItem>
          </Tooltip>
        ))}
      </List>
    </Drawer>
  );
};

export default memo(SidebarEmployee);
