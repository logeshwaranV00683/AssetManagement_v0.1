import React, { useEffect, useState } from "react";
import {
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
} from "@mui/material";
import { assignAsset } from "../Services/AssetService";
import { getEmployeeList } from "../Services/EmployeeService";
import toast from "react-hot-toast";

function AssignAsset({ open, handleClose, asset, fetchAssets }) {
  const assetId = asset?.assetId || asset?.id;

  const [employeeList, setEmployeeList] = useState([]);
  const [filteredEmployees, setFilteredEmployees] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [selectedEmp, setSelectedEmp] = useState("");
  const user = JSON.parse(localStorage.getItem('user')); 
  useEffect(() => {
    if (open) {
      getEmployeeList()
        .then((employees) => {
          setEmployeeList(employees || []);
          setFilteredEmployees(employees || []);
        })
        .catch(() => {
          toast.error("Failed to load employee list.");
          setEmployeeList([]);
          setFilteredEmployees([]);
        });
    }
  }, [open]);

  useEffect(() => {
    const filtered = employeeList.filter((emp) => {
      const search = searchTerm.toLowerCase();
      return (
        (emp.empId && emp.empId.toLowerCase().includes(search)) ||
        (emp.firstName && emp.firstName.toLowerCase().includes(search)) ||
        (emp.designation && emp.designation.toLowerCase().includes(search))     
     );
    });
    setFilteredEmployees(filtered);
  }, [searchTerm, employeeList]);

  const handleSubmit = async () => {
  if (!selectedEmp) {
    toast.error("Please select an employee.");
    return;
  }
  if (!assetId) {
    toast.error("Asset ID is missing.");
    return;
  }

const assetData = {
  empId: selectedEmp,
  serialNumber: asset?.assetSerialNumber || asset?.serialNumber,
  assignedDate: new Date().toISOString(),
  assetName: asset?.assetName || '',
  assignedBy: user.empId|| 'admin'
};



  console.log("Sending asset assign data:", assetData);

try {
  const result = await assignAsset([assetData]);
  toast.success(`${result}!`);
  fetchAssets();
  handleCloseDialog();
} catch (error) {
  if (error.response?.status === 403) {
    // Assuming the response is plain text, not JSON
    const errorMessage = error.response.data || "Asset is already assigned.";
    toast.error(errorMessage);
  } else {
    toast.error("Error assigning asset.");
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

  return (
    <Dialog open={open} onClose={handleCloseDialog} fullWidth maxWidth="sm">
      <DialogTitle>Assign Asset</DialogTitle>
      <DialogContent dividers>
        <TextField
          label="Search Employee (ID)"
          variant="outlined"
          fullWidth
          margin="dense"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
        <FormControl fullWidth margin="dense">
          <InputLabel>Select Employee</InputLabel>
          <Select
            value={selectedEmp}
            onChange={(e) => setSelectedEmp(e.target.value)}
            label="Select Employee"
          >
            {filteredEmployees.length > 0 ? (
              filteredEmployees.map((emp) => (
                <MenuItem key={emp.empId} value={emp.empId}>
                  {emp.empId} - {emp.firstName} ({emp.designation})
                </MenuItem>
              ))
            ) : (
              <MenuItem disabled>No employees found</MenuItem>
            )}
          </Select>
        </FormControl>
      </DialogContent>
      <DialogActions>
        <Button onClick={handleCloseDialog}>Cancel</Button>
        <Button variant="contained" color="primary" onClick={handleSubmit}>
          Assign
        </Button>
      </DialogActions>
    </Dialog>
  );
}

export default AssignAsset;
