import React, { useEffect, useState } from "react";
import { assignAsset } from "../Services/AssetService";
import { getEmployeeList } from "../Services/EmployeeService";
import "animate.css";
import {
  showErrorAlert,
  showSuccessAlert,
  showWarningAlert,
} from "../Utils/alerts";

function AssignAsset({ open, handleClose, asset, fetchAssets }) {
  const assetId = asset?.assetId || asset?.id;
  const [employeeList, setEmployeeList] = useState([]);
  const [selectedEmp, setSelectedEmp] = useState("");
  const user = JSON.parse(localStorage.getItem("user"));

  useEffect(() => {
    if (open) {
      getEmployeeList()
        .then((employees) => {
          setEmployeeList(employees || []);
        })
        .catch(() => {
          return showErrorAlert("Ooopss...!", "Failed to load employee list!");
        });
    }
  }, [open]);

  const handleSubmit = async () => {
    const foundEmp = employeeList.find(
      (emp) =>
        `${emp.empId} - ${emp.firstName} (${emp.designation})` === selectedEmp
    );

    if (!foundEmp) {
      return showWarningAlert(
        "Select a valid employee",
        "Please choose from the list."
      );
    }

    if (!assetId) {
      return showErrorAlert("Asset not found!", "Missing asset information.");
    }

    const assetData = {
      empId: foundEmp.empId,
      serialNumber: asset?.assetSerialNumber || asset?.serialNumber,
      assignedDate: new Date().toISOString().split("T", 1)[0],
      assetName: asset?.assetName || "",
      assignedBy: user?.empId || "admin",
    };

    try {
      const result = await assignAsset([assetData]);
      showSuccessAlert("Asset Assigned!", result);
      fetchAssets();
      handleCloseDialog();
    } catch (error) {
      if (error.status === 406 && typeof error.message === "object") {
        const [serialNo, reason] = Object.entries(error.message)[0] || [];
        return showWarningAlert(
          `Failed to Assign: ${serialNo}`,
          `${reason}`
        );
      } else {
        return showErrorAlert("Assigning Failed", "An error occurred.");
      }
    }

  };

  const handleCloseDialog = () => {
    setSelectedEmp("");
    setEmployeeList([]);
    handleClose();
  };

  if (!open) return null;

  return (
    <div className="import-3d-overlay">
      <div className="import-3d-modal">
        <button className="import-3d-close" onClick={handleCloseDialog}>
          &times;
        </button>
        <div className="import-3d-title">Assign Asset</div>
        <div className="import-3d-file-wrapper">
          <input
            list="employee-options"
            className="import-3d-search"
            placeholder="🔍 Search and Select Employee"
            value={selectedEmp}
            onChange={(e) => setSelectedEmp(e.target.value)}
          />
          <datalist id="employee-options">
            {employeeList.map((emp) => (
              <option
                key={emp.empId}
                value={`${emp.empId} - ${emp.firstName} (${emp.designation})`}
              />
            ))}
          </datalist>

          <button className="import-3d-button" onClick={handleSubmit}>
            Assign Asset
          </button>
        </div>
      </div>
    </div>
  );
}

export default AssignAsset;
