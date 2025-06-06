import React, { useEffect, useState } from "react";
import { assignAsset } from "../Services/AssetService";
import { getEmployeeList } from "../Services/EmployeeService";
import "animate.css";
import { showErrorAlert, showSuccessAlert , showWarningAlert} from "../Utils/alerts";

function AssignAsset({ open, handleClose, asset, fetchAssets }) {
  const assetId = asset?.assetId || asset?.id;
  const [employeeList, setEmployeeList] = useState([]);
  const [filteredEmployees, setFilteredEmployees] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [selectedEmp, setSelectedEmp] = useState("");
  const user = JSON.parse(localStorage.getItem("user"));

  useEffect(() => {
    if (open) {
      getEmployeeList()
        .then((employees) => {
          setEmployeeList(employees || []);
          setFilteredEmployees(employees || []);
        })
        .catch(() => {
         return showErrorAlert("Ooopss...!","Failed to load employee list!");
        });
    }
  }, [open]);

  useEffect(() => {
    const filtered = employeeList.filter((emp) => {
      const search = searchTerm.toLowerCase();
      return (
        emp.empId?.toLowerCase().includes(search) ||
        emp.firstName?.toLowerCase().includes(search) ||
        emp.designation?.toLowerCase().includes(search)
      );
    });
    setFilteredEmployees(filtered);
  }, [searchTerm, employeeList]);

  const handleSubmit = async () => {
    if (!selectedEmp) {
    return  showWarningAlert("Select an employee","You must choose someone to assign this asset.");
    }

    if (!assetId) {
    return  showErrorAlert("Asset not found!","Missing asset information.");
    }

    const assetData = {
      empId: selectedEmp,
      serialNumber: asset?.assetSerialNumber || asset?.serialNumber,
      assignedDate: new Date().toISOString(),
      assetName: asset?.assetName || "",
      assignedBy: user?.empId || "admin",
    };

    try {
      const result = await assignAsset([assetData]);
      showSuccessAlert("Asset Assigned!",result);
      fetchAssets();
      handleCloseDialog();
    } catch (error) {
      if (error.status === 406) {
       return showWarningAlert("Already Assigned","This asset has already been assigned.");
      } else {
       return showErrorAlert("Assigning Failed", "An error occurred.");
      }
    }
  };

  const handleCloseDialog = () => {
    setSelectedEmp("");
    setSearchTerm("");
    setEmployeeList([]);
    setFilteredEmployees([]);
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
            type="text"
            placeholder="🔍 Search Employee by ID"
            className="import-3d-search"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />


          <select
            className="import-3d-file-button"
            value={selectedEmp}
            onChange={(e) => setSelectedEmp(e.target.value)}
          >
            <option value="" disabled>
              Select Employee
            </option>
            {filteredEmployees.length > 0 ? (
              filteredEmployees.map((emp) => (
                <option key={emp.empId} value={emp.empId}>
                  {emp.empId} - {emp.firstName} ({emp.designation})
                </option>
              ))
            ) : (
              <option disabled>No employees found</option>
            )}
          </select>

          <button className="import-3d-button" onClick={handleSubmit}>
            Assign Asset
          </button>
        </div>
      </div>
    </div>
  );
}

export default AssignAsset;
