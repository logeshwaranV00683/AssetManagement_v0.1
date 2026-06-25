import React, { memo } from "react";
import Drawer from "@mui/material/Drawer";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import Tooltip from "@mui/material/Tooltip";
import AddCircleOutlineIcon from "@mui/icons-material/AddCircleOutline";
import AssignmentTurnedInIcon from "@mui/icons-material/AssignmentTurnedIn";
import RemoveCircleOutlineIcon from "@mui/icons-material/RemoveCircleOutline";
import DeleteSweepIcon from "@mui/icons-material/DeleteSweep";
import ReplayIcon from "@mui/icons-material/Replay";
import "../Style/font.css";

const drawerWidth = 80;

const SidebarAssets = ({ onAddAsset, onFilter, onResetFilters }) => {
  const buttons = [
    {
      label: "Add Asset",
      icon: <AddCircleOutlineIcon fontSize="large" />,
      onClick: onAddAsset,
    },
    {
      label: "Assigned",
      icon: <AssignmentTurnedInIcon fontSize="large" />,
      onClick: () => onFilter("assigned"),
    },
    {
      label: "Unassigned",
      icon: <RemoveCircleOutlineIcon fontSize="large" />,
      onClick: () => onFilter("unassigned"),
    },
    {
      label: "Scrap",
      icon: <DeleteSweepIcon fontSize="large" />,
      onClick: () => onFilter("scrap"),
    },
    {
      label: "Reset",
      icon: <ReplayIcon fontSize="large" />,
      onClick: onResetFilters,
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

export default memo(SidebarAssets);
