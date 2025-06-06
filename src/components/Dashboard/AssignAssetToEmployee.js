import React, { useEffect, useState } from "react";
import { showErrorAlert, showSuccessAlert, showWarningAlert } from "../Utils/alerts"; 
import { getAssetList, assignAsset } from "../Services/AssetService";
import "animate.css"; 
import {
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Checkbox,
  ListItemText,
  OutlinedInput,
} from "@mui/material";

function AssignAssetToEmployee({ open, onClose, employee, refresh }) {
  const [assets, setAssets] = useState([]);
  const [selectedAssets, setSelectedAssets] = useState([]);

  useEffect(() => {
    if (open) {
      getAssetList()
        .then((filteredData) => {
          setAssets(filteredData.filter(asset => asset.status.toLowerCase() === 'unassigned'));
        })
        .catch(() => {
          showErrorAlert("Failed to fetch assets", "An error occurred while loading assets.");
        });
    }
  }, [open]);

  const handleAssign = async () => {
    if (selectedAssets.length === 0) {
      showWarningAlert("Select an Asset", "You must select at least one asset to assign.");
      return;
    }

    const user = JSON.parse(localStorage.getItem('user'));

    const assetData = selectedAssets.map((serialNumber) => {
      const asset = assets.find(a => a.serialNumber === serialNumber);
      return {
        empId: employee.empId,
        serialNumber,
        assetName: asset?.assetName || "",
        assignedDate: new Date().toISOString(),
        assignedBy: user?.empId || "admin",
      };
    });

    try {
      const result = await assignAsset(assetData);
      showSuccessAlert("Assets Assigned!", result || "The assets have been successfully assigned.");
      setSelectedAssets([]);
      onClose();
      refresh();
    } catch (error) {
      if (error.status === 406) {
        showWarningAlert("Asset Already Assigned", "This asset has already been assigned.");
      } else {
        showErrorAlert("Assignment Failed", "An error occurred while assigning the assets.");
      }
    }
  };

  return (
    open && (
      <div className="import-3d-overlay animate__animated animate__fadeIn">
        <div className="import-3d-modal animate__animated animate__zoomIn">
          <button 
            className="import-3d-close" 
            onClick={onClose}
          >
            &times;
          </button>
          
          <div className="import-3d-title">Assign Assets to {employee?.name}</div>
          <div className="import-3d-file-wrapper">
            <FormControl fullWidth>
              <InputLabel>Select Assets</InputLabel>
              <Select
                multiple
                value={selectedAssets}
                onChange={(e) => setSelectedAssets(e.target.value)}
                input={<OutlinedInput label="Select Assets" />}
                renderValue={(selected) => selected.join(", ")}
              >
                {assets.map((asset) => (
                  <MenuItem key={asset.serialNumber} value={asset.serialNumber}>
                    <Checkbox checked={selectedAssets.includes(asset.serialNumber)} />
                    <ListItemText primary={`${asset.serialNumber} - ${asset.assetName}`} />
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
            <button className="import-3d-button" onClick={handleAssign}>
              Assign Assets
            </button>
          </div>
        </div>
      </div>
    )
  );
}

export default AssignAssetToEmployee;
