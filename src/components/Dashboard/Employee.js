import React, { useEffect, useState } from "react";
import { toast } from "react-hot-toast";
import { DataGrid } from "@mui/x-data-grid";
import TextField from "@mui/material/TextField";
import AddEmployeeModal from "./AddEmployeeModal";
import EditEmployeeModal from "./EditEmployeeModal";
import "../Style/Employee.css";
import { getEmployeeList, deleteEmployee } from "../Services/EmployeeService";
import ExportButton from "../Utils/ExportButton";
import FileDownloadIcon from "@mui/icons-material/FileDownload";
import SidebarEmployee from "./SideBarEmployee";
import DeleteIcon from "@mui/icons-material/Delete";
import { Container, Box, IconButton, Tooltip } from "@mui/material";
import VisibilityIcon from "@mui/icons-material/Visibility";
import EditIcon from "@mui/icons-material/Edit";
import { showConfirmAlert } from "../Utils/alerts";
import UploadFileIcon from "@mui/icons-material/UploadFile";
import "../Style/ImportButton.css";
import ImportExcel from "../Utils/ImportExcel";
import AssignmentIndIcon from "@mui/icons-material/AssignmentInd";
import AssignAssetToEmployee from "./AssignAssetToEmployee";

function Employee() {
  const [openEditModal, setOpenEditModal] = useState(false);
  const [employees, setEmployees] = useState([]);
  const [selectedEmployee, setSelectedEmployee] = useState(null);
  const [filterValue, setFilterValue] = useState("");
  const [filteredRows, setFilteredRows] = useState([]);
  const [viewOnly, setViewOnly] = useState(false);
  const [showImportModal, setShowImportModal] = useState(false);
  const [exportType, setExportType] = useState("all");
  const [open, setOpenAddModal] = useState(false);
  const [openAssignAssetDialog, setOpenAssignAssetDialog] = useState(false);
  const [selectedAssignEmployee, setSelectedAssignEmployee] = useState(null);

  const fetchEmployees = async () => {
    try {
      const data = await getEmployeeList();
      setEmployees(data);
      setFilteredRows(data);
    } catch (error) {
      console.error("Error fetching employees:", error);
    }
  };

  useEffect(() => {
    fetchEmployees();
  }, []);

  const handleOpenAddModal = () => {
    setOpenAddModal(true);
  };

  const handleOpenEditModal = (employee, isViewOnly = false) => {
    setSelectedEmployee(employee);
    setViewOnly(isViewOnly);
    setOpenEditModal(true);
  };

  const handleClose = () => {
    setOpenAddModal(false);
    setOpenEditModal(false);
    setSelectedEmployee(null);
    setViewOnly(false);
  };

  const handleSearch = (event) => {
    const value = event.target.value.toLowerCase();
    setFilterValue(value);
    const filtered = employees.filter((employee) =>
      Object.values(employee).some((val) =>
        String(val).toLowerCase().includes(value)
      )
    );
    setFilteredRows(filtered);
  };

  const resetFilters = () => {
    setFilterValue("");
    setFilteredRows(employees);
    setExportType("all");
  };

  const filterByStatus = (status) => {
    setExportType(status);
    const filtered = employees.filter(
      (employee) => employee.status.toLowerCase() === status.toLowerCase()
    );
    setFilteredRows(filtered);
  };

  const handleDelete = async (employee) => {
    const confirmDelete = await showConfirmAlert(
      "Are you sure?",
      `Do you want to Delete this Employee? ${employee.empId}`
    );
    if (!confirmDelete) return;

    try {
      const success = await deleteEmployee(employee.empId);
      if (success) {
        toast.success(`Deleted the Employee with ID: ${employee.empId}`);
        fetchEmployees();
      }
    } catch (error) {
      console.error("Error deleting Employee:", error);
      if (error.status && error.message) {
        toast.error(`Error ${error.message}`);
      } else {
        toast.error(`Cannot delete the Employee with ID: ${employee.empId}`);
      }
    }
  };

  const handleOpenAssignAssets = (employee) => {
    setSelectedAssignEmployee(employee);
    setOpenAssignAssetDialog(true);
  };

  const columns = [
    { field: "empId", headerName: "EmpId", flex: 0.6, minWidth: 80, sortable: true, headerAlign: "center" },
    { field: "name", headerName: "Name", flex: 1, minWidth: 140, sortable: true, headerAlign: "center" },
    { field: "mail", headerName: "Email", flex: 2, minWidth: 200, sortable: true, headerAlign: "center" },
    { field: "designation", headerName: "Designation", flex: 1, minWidth: 150, sortable: true, headerAlign: "center" },
    { field: "department", headerName: "Department", flex: 1, minWidth: 120, sortable: true, headerAlign: "center" },
    { field: "location", headerName: "Location", flex: 1, minWidth: 120, sortable: true, headerAlign: "center" },
    { field: "status", headerName: "Status", flex: 0.6, minWidth: 80, sortable: true, headerAlign: "center" },
    {
      field: "actions",
      headerName: "Actions",
      flex: 1.2,
      minWidth: 200,
      sortable: false,
      headerAlign: "center",
      renderCell: (params) => (
        <Box display="flex" alignItems="center" flexWrap="wrap">
          <Tooltip title="View">
            <IconButton
              sx={{ transition: "transform 0.2s", "&:hover": { transform: "scale(1.3)", color: "primary.main", filter: "drop-shadow(0 0 4px rgba(25, 118, 210, 0.8))" }, mr: 1 }}
              onClick={() => handleOpenEditModal(params.row, true)}
            >
              <VisibilityIcon />
            </IconButton>
          </Tooltip>
          <Tooltip title="Assign Asset">
            <IconButton
              sx={{ transition: "transform 0.2s", "&:hover": { transform: "scale(1.3)", color: "success.main", filter: "drop-shadow(0 0 4px rgba(76, 175, 80, 0.8))" } }}
              onClick={() => handleOpenAssignAssets(params.row)}
            >
              <AssignmentIndIcon />
            </IconButton>
          </Tooltip>
          <Tooltip title="Edit">
            <IconButton
              sx={{ transition: "transform 0.2s", "&:hover": { transform: "scale(1.3)", color: "secondary.main", filter: "drop-shadow(0 0 4px rgba(156, 39, 176, 0.8))" }, mr: 1 }}
              onClick={() => handleOpenEditModal(params.row)}
            >
              <EditIcon />
            </IconButton>
          </Tooltip>
          <Tooltip title="Delete">
            <IconButton
              sx={{ transition: "transform 0.2s", "&:hover": { transform: "scale(1.3)", color: "error.main", filter: "drop-shadow(0 0 4px rgba(211, 47, 47, 0.8))" } }}
              onClick={() => handleDelete(params.row)}
            >
              <DeleteIcon />
            </IconButton>
          </Tooltip>
        </Box>
      ),
    },
  ];

  return (
    <div style={{ width: "100%", display: "flex", justifyContent: "center" }}>
      <main style={{ flexGrow: 1 }}>
        <Container maxWidth={false} sx={{ width: "100%", p: 0 }}>
          <SidebarEmployee
            onAddEmployee={handleOpenAddModal}
            filterByStatus={filterByStatus}
            resetFilters={resetFilters}
          />

          <Box sx={{ display: "flex", justifyContent: "center", width: "100%", mt: 4, mb: 2 }}>
            <Box sx={{ display: "flex", flexWrap: "wrap", justifyContent: "space-between", alignItems: "center", gap: "1rem", width: "80%" }}>
              
              {/* Import Button */}
              <Box sx={{ width: "20%", minWidth: "150px", textAlign: "center" }}>
                <Box
                  className="import-button"
                  sx={{ p: "0.4rem 0.8rem", textAlign: "center", borderRadius: "0.6rem", fontWeight: 600, fontSize: "0.8rem", cursor: "pointer" }}
                  onClick={() => setShowImportModal(true)}
                >
                  <span style={{ display: "flex", alignItems: "center", justifyContent: "center", gap: "0.3rem" }}>
                    <UploadFileIcon fontSize="small" /> IMPORT
                  </span>
                </Box>
              </Box>
              {showImportModal && (
                <ImportExcel
                  importType="employee"
                  onClose={() => setShowImportModal(false)}
                  refreshList={fetchEmployees}
                />
              )}

              {/* Search */}
              <Box sx={{ width: "55%", minWidth: "200px" }}>
                <TextField
                  label="Search"
                  variant="outlined"
                  onChange={handleSearch}
                  value={filterValue}
                  placeholder="Search"
                  fullWidth
                  sx={{
                    "& .MuiOutlinedInput-root": {
                      background: "#ffffff",
                      borderRadius: "0.6rem",
                      fontWeight: 500,
                      boxShadow: "0 0 0.3rem rgba(109, 224, 255, 0.4)",
                      "& fieldset": { border: "1px solid #ccc" },
                      "&:hover fieldset": { borderColor: "#1FCBEA" },
                      "&.Mui-focused fieldset": { borderColor: "#1FCBEA" },
                      "& input": {
                        fontFamily: "'Racing Sans One', sans-serif",
                        fontSize: "0.9rem",
                        color: "#083A40",
                        paddingTop: "20px",
                      },
                    },
                    "& .MuiInputLabel-root": {
                      fontFamily: "'Racing Sans One', sans-serif",
                      fontSize: "1rem",
                      transition: "all 0.2s ease",
                    },
                    "& .MuiInputLabel-root.Mui-focused": {
                      transform: "translate(14px, -25px) scale(0.85)",
                      fontSize: "1.3rem",
                      padding: "0 4px",
                      color: "#fff",
                    },
                  }}
                />
              </Box>

              {/* Export Button */}
              <Box sx={{ width: "20%", minWidth: "150px", textAlign: "center" }}>
                <Box className="export-button" sx={{ p: "0.4rem 0.8rem", textAlign: "center", borderRadius: "0.6rem", fontWeight: 600, fontSize: "0.8rem" }}>
                  <ExportButton
                    type="employee"
                    status={exportType}
                    filter={filterValue}
                    filePrefix="Verinite"
                    buttonLabel={
                      <span style={{ display: "flex", alignItems: "center", justifyContent: "center", gap: "0.3rem" }}>
                        <FileDownloadIcon fontSize="small" /> EXPORT
                      </span>
                    }
                    filteredRows={filteredRows}
                  />
                </Box>
              </Box>
            </Box>
          </Box>

          {/* Data Grid */}
          <Box sx={{ width: "80%", height: "65vh", m: "0 auto", display: "flex", flexDirection: "column", justifyContent: "center", alignItems: "center" }}>
            <Box sx={{ width: "100%", maxWidth: "100%", height: "100%", mb: "1%" }}>
              <DataGrid
                rows={filteredRows}
                columns={columns}
                autoHeight
                sx={{
                  maxHeight: "70vh",
                  overflow: "hidden",
                  borderRadius: "16px",
                  border: "2px solid #020405ff",
                  fontFamily: "'Racing Sans One', sans-serif",
                  color: "#083A40",
                  "& .MuiDataGrid-columnHeaders": {
                    background: "linear-gradient(45deg, #6DE0FF, #2BC4F3)",
                    color: "#083A40",
                    fontSize: "16px",
                    fontWeight: 700,
                  },
                  "& .MuiDataGrid-cell": {
                    background: "#F0FBFF",
                    color: "#083A40",
                    fontSize: "15px",
                    borderBottom: "1px solid #D0F0FF",
                  },
                  "& .MuiDataGrid-footerContainer": {
                    background: "linear-gradient(45deg, #6DE0FF, #2BC4F3)",
                    color: "#083A40",
                    fontWeight: 600,
                  },
                  "& .MuiDataGrid-row:hover": {
                    backgroundColor: "#E0F9FF",
                  },
                }}
              />
            </Box>
          </Box>

          {/* Modals */}
          <AddEmployeeModal open={open} handleClose={() => setOpenAddModal(false)} refreshEmployeeList={fetchEmployees} />
          {selectedEmployee && (
            <EditEmployeeModal open={openEditModal} handleClose={handleClose} employee={selectedEmployee} refreshEmployeeList={fetchEmployees} viewOnly={viewOnly} />
          )}
          {selectedAssignEmployee && (
            <AssignAssetToEmployee open={openAssignAssetDialog} onClose={() => setOpenAssignAssetDialog(false)} employee={selectedAssignEmployee} refresh={fetchEmployees} />
          )}
        </Container>
      </main>
    </div>
  );
}

export default Employee;
