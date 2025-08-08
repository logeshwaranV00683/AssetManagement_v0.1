import React, { useEffect, useState, useCallback } from "react";
import "../Style/Assets.css";
import {
  MenuItem,
  Checkbox,
  ListItemText,
  TextField,
  Menu,
  Button,
  Box,
} from "@mui/material";
import {
  getAssetList,
  assignAsset,
  getAssignedAssetsByEmployee,
  unassignAsset,
} from "../Services/AssetService";
import {
  showErrorAlert,
  showSuccessAlert,
  showWarningAlert,
} from "../Utils/alerts";

function AssignAssetToEmployee({ open, onClose, employee, refresh }) {
  const [assets, setAssets] = useState([]);
  const [selectedAssets, setSelectedAssets] = useState([]);
  const [confirmedAssets, setConfirmedAssets] = useState([]);
  const [alreadyAssignedAssets, setAlreadyAssignedAssets] = useState([]);
  const [selectedUnassignAssets, setSelectedUnassignAssets] = useState([]);
  const [menuOpen, setMenuOpen] = useState(false);
  const [anchorEl, setAnchorEl] = useState(null);

  useEffect(() => {
    if (open && employee?.empId) {
      getAssetList()
        .then((data) => {
          const unassigned = data.filter(
            (asset) => asset.status.toLowerCase() === "unassigned"
          );
          setAssets(unassigned);
        })
        .catch(() =>
          showErrorAlert(
            "Failed to fetch assets",
            "An error occurred while loading assets."
          )
        );

      getAssignedAssetsByEmployee(employee.empId)
        .then((data) => {
          setAlreadyAssignedAssets(data || []);
        })
        .catch(() => {
          setAlreadyAssignedAssets([]);
        });
    }
  }, [open, employee]);

  const handleSelectClick = (event) => {
    setAnchorEl(event.currentTarget);
    setMenuOpen(true);
  };

  const handleOkClick = useCallback(() => {
    if (selectedAssets.length === 0) {
      showWarningAlert("No assets selected", "Please select at least one.");
      return;
    }
    setConfirmedAssets([...selectedAssets]);
    setMenuOpen(false);
  }, [selectedAssets]);

  useEffect(() => {
    const handleKeyDown = (event) => {
      if (menuOpen && event.key === "Enter") {
        event.preventDefault();
        handleOkClick();
      }
    };

    document.addEventListener("keydown", handleKeyDown);
    return () => {
      document.removeEventListener("keydown", handleKeyDown);
    };
  }, [menuOpen, handleOkClick]);

  const handleAssign = async () => {
    const user = JSON.parse(localStorage.getItem("user"));
    const assetData = confirmedAssets.map((serialNumber) => {
      const asset = assets.find((a) => a.serialNumber === serialNumber);
      return {
        empId: employee.empId,
        serialNumber,
        assetName: asset?.assetName || "",
        assignedDate: new Date().toISOString().split("T", 1)[0],
        assignedBy: user?.empId || "admin",
      };
    });

    try {
      const result = await assignAsset(assetData);
      showSuccessAlert("Assets Assigned!", result || "Success");
      resetState();
    } catch (error) {
      if (error.status === 406) {
        showWarningAlert(
          "Asset Already Assigned",
          "Some assets are already assigned."
        );
      } else if (error.status === 404) {
        showWarningAlert("Employee Status Was In Inactive");
      } else {
        showErrorAlert("Assigning Asset Failed", "Could not assign assets.");
      }
    }
  };

  const handleUnassign = async () => {
    try {
      await unassignAsset(selectedUnassignAssets);
      showSuccessAlert(
        "Assets Unassigned!",
        "Selected assets were unassigned."
      );
      resetState();
    } catch (error) {
      showErrorAlert("Unassign Failed", "Could not unassign selected assets.");
    }
  };

  const resetState = () => {
    setSelectedAssets([]);
    setConfirmedAssets([]);
    setSelectedUnassignAssets([]);
    onClose();
    refresh();
  };

  return (
    open && (
      <div className="import-3d-overlay animate__animated animate__fadeIn">
        <div className="import-3d-modal animate__animated animate__zoomIn">
          <button className="import-3d-close" onClick={onClose}>
            &times;
          </button>
          <div className="import-3d-file-wrapper">
            {/* Already assigned assets with unassign option */}
            {alreadyAssignedAssets.length > 0 && (
              <Box className="assigned-assets-box">
                <strong>Already Assigned Assets:</strong>
                <ul>
                  {alreadyAssignedAssets.map((asset) => (
                    <li key={asset.serialNumber}>
                      <Checkbox
                        size="small"
                        checked={selectedUnassignAssets.includes(
                          asset.serialNumber
                        )}
                        onChange={(e) => {
                          const sn = asset.serialNumber;
                          setSelectedUnassignAssets((prev) =>
                            e.target.checked
                              ? [...prev, sn]
                              : prev.filter((v) => v !== sn)
                          );
                        }}
                      />
                      {asset.assetName} ({asset.serialNumber}) - {asset.type}
                    </li>
                  ))}
                </ul>
                {selectedUnassignAssets.length > 0 && (
                  <button className="unassign-button" onClick={handleUnassign}>
                    Unassign
                  </button>
                )}
              </Box>
            )}

            {/* Asset assignment section */}
            <div className="import-3d-title">
              Assign Assets to {employee?.name}
            </div>
            <TextField
              label="Add Assets"
              value={selectedAssets.join(", ")}
              placeholder="Select Assets"
              onClick={handleSelectClick}
              fullWidth
              InputProps={{
                readOnly: true,
              }}
              variant="outlined"
            />

            {/* Dropdown menu */}
            <Menu
              anchorEl={anchorEl}
              open={menuOpen}
              onClose={() => setMenuOpen(false)}
              PaperProps={{
                style: { width: anchorEl?.clientWidth || 300 },
              }}
            >
              {assets.map((asset) => (
                <MenuItem key={asset.serialNumber}>
                  <Checkbox
                    checked={selectedAssets.includes(asset.serialNumber)}
                    onChange={(e) => {
                      const value = asset.serialNumber;
                      setSelectedAssets((prev) =>
                        e.target.checked
                          ? [...prev, value]
                          : prev.filter((v) => v !== value)
                      );
                    }}
                  />
                  <ListItemText
                    primary={`${asset.serialNumber} - ${asset.assetName} - ${asset.type}`}
                  />
                </MenuItem>
              ))}
              <Box textAlign="center" p={1}>
                <Button
                  variant="contained"
                  size="small"
                  onClick={handleOkClick}
                >
                  OK
                </Button>
              </Box>
            </Menu>

            {/* Confirm assignment */}
            {confirmedAssets.length > 0 && (
              <button className="import-3d-button" onClick={handleAssign}>
                Confirm Assigning
              </button>
            )}
          </div>
        </div>
      </div>
    )
  );
}

export default AssignAssetToEmployee;
