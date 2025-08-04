import React, { useState, useEffect } from "react";
import { makeStyles } from "@material-ui/core/styles";
import {
  Modal,
  Box,
  TextField,
  Button,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  IconButton,
} from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import Autocomplete from "@mui/material/Autocomplete";
import {
  updateEmployee,
  getLocations,
  getDeparatment,
  getDesignation,
} from "../Services/EmployeeService";
import toast from "react-hot-toast";

const useStyles = makeStyles((theme) => ({
  modal: {
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
  },
  paper: {
    backgroundColor: theme.palette.background.paper,
    boxShadow: theme.shadows[5],
    padding: theme.spacing(2, 4, 3),
    position: "relative",
    borderRadius: 8,
  },
  textCenter: {
    textAlign: "center",
  },
  formGrid: {
    display: "grid",
    gridTemplateColumns: "1fr 1fr",
    gap: theme.spacing(2),
    alignItems: "center",
    [theme.breakpoints.down("sm")]: {
      gridTemplateColumns: "1fr",
    },
  },
  cancelButton: {
    marginRight: theme.spacing(2),
    backgroundColor: theme.palette.error.main,
    color: theme.palette.error.contrastText,
  },
  saveButton: {
    backgroundColor: theme.palette.success.main,
    color: theme.palette.success.contrastText,
  },
  actionsContainer: {
    display: "flex",
    justifyContent: "space-between",
    marginTop: theme.spacing(2),
  },
}));

function EditEmployeeModal({
  open,
  handleClose,
  employee,
  refreshEmployeeList,
  viewOnly,
}) {
  const classes = useStyles();

  const [empId, setEmpId] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [role, setRole] = useState("");
  const [mail, setMail] = useState("");
  const [mobile, setMobile] = useState("");
  const [location, setLocation] = useState("");
  const [status, setStatus] = useState("Active");
  const [department, setDepartment] = useState("");
  const [designation, setDesignation] = useState("");

  const [locationOptions, setLocationOptions] = useState([]);
  const [departmentOptions, setDepartmentOptions] = useState([]);
  const [designationOptions, setDesignationOptions] = useState([]);

  const [isUpdating, setIsUpdating] = useState(false);

  useEffect(() => {
    if (open) {
      fetchLocations();
      fetchDepartments();
      fetchDesignations();

      if (employee) {
        setEmpId(employee.empId || "");
        setFirstName(employee.firstName || "");
        setLastName(employee.lastName || "");
        setRole(employee.role || "");
        setMail(employee.mail || "");
        setMobile(employee.mobile || "");
        setLocation(employee.location || "");
        setStatus(employee.status?.trim() || "Active");
        setDepartment(employee.department || "");
        setDesignation(employee.designation || "");
      }
    }
  }, [open, employee]);

  const fetchLocations = async () => {
    try {
      const locations = await getLocations();
      setLocationOptions(locations || []);
    } catch (err) {
      console.error("Error fetching locations:", err);
    }
  };

  const fetchDepartments = async () => {
    try {
      const departments = await getDeparatment();
      setDepartmentOptions(departments || []);
    } catch (err) {
      console.error("Error fetching departments:", err);
    }
  };

  const fetchDesignations = async () => {
    try {
      const designations = await getDesignation();
      setDesignationOptions(designations || []);
    } catch (err) {
      console.error("Error fetching designations:", err);
    }
  };

  // Add new options dynamically
  const commitLocationOption = (value) => {
    if (value && !locationOptions.includes(value)) {
      setLocationOptions((prev) => [...prev, value]);
    }
  };

  const commitDepartmentOption = (value) => {
    if (value && !departmentOptions.includes(value)) {
      setDepartmentOptions((prev) => [...prev, value]);
    }
  };

  const commitDesignationOption = (value) => {
    if (value && !designationOptions.includes(value)) {
      setDesignationOptions((prev) => [...prev, value]);
    }
  };

  const handleUpdateEmployee = async () => {
    if (!employee) return;

    const updatedFields = {};
    const currentValues = {
      empId,
      firstName,
      lastName,
      role,
      mail,
      mobile,
      location,
      status,
      department,
      designation,
    };

    Object.entries(currentValues).forEach(([key, value]) => {
      if (value !== (employee[key] || "")) {
        updatedFields[key] = value;
      }
    });

    if (Object.keys(updatedFields).length === 0) {
      toast.error("No changes detected.");
      return;
    }

    setIsUpdating(true);
    try {
      await updateEmployee(empId, updatedFields);
      toast.success(`Employee ${empId} updated successfully`);
      refreshEmployeeList();
      handleClose();
    } catch (error) {
      console.error("Error updating employee:", error);
      if (error.status === 400 && typeof error.data === "object") {
        Object.entries(error.data).forEach(([field, message]) => {
          toast.error(`${field}: ${message}`);
        });
      } else if (error.status === 409) {
        toast.error(error.data || "Employee mail or Mobile already exists");
      } else {
        toast.error("Unexpected error occurred while updating.");
      }
    } finally {
      setIsUpdating(false);
    }
  };

  return (
    <Modal
      open={open}
      onClose={handleClose}
      className={classes.modal}
      aria-labelledby="edit-employee-modal-title"
    >
      <Box className={classes.paper}>
        <IconButton
          edge="end"
          aria-label="close"
          onClick={handleClose}
          sx={{ position: "absolute", top: 8, right: 8 }}
        >
          <CloseIcon />
        </IconButton>

        <h2 className={classes.textCenter} style={{ color: "#083A40" }}>
          {viewOnly ? "View Employee" : "Edit Employee"}
        </h2>

        <form>
          <div className={classes.formGrid}>
            <TextField label="Employee ID" value={empId} fullWidth disabled />

            <TextField
              label="First Name"
              value={firstName}
              onChange={(e) => setFirstName(e.target.value)}
              fullWidth
              disabled={viewOnly}
            />
            <TextField
              label="Last Name"
              value={lastName}
              onChange={(e) => setLastName(e.target.value)}
              fullWidth
              disabled={viewOnly}
            />

            <FormControl fullWidth disabled={viewOnly}>
              <InputLabel htmlFor="role">Role</InputLabel>
              <Select
                value={role}
                onChange={(e) => setRole(e.target.value)}
                inputProps={{ name: "role", id: "role" }}
              >
                <MenuItem value="Employee">Employee</MenuItem>
                <MenuItem value="Admin">Admin</MenuItem>
              </Select>
            </FormControl>

            <TextField
              label="Email"
              value={mail}
              onChange={(e) => setMail(e.target.value)}
              fullWidth
              disabled={viewOnly}
            />
            <TextField
              label="Mobile"
              value={mobile}
              onChange={(e) => setMobile(e.target.value)}
              fullWidth
              disabled={viewOnly}
            />

            {/* Location Autocomplete */}
            <Autocomplete
              freeSolo
              options={locationOptions}
              value={location}
              onChange={(event, newValue) => {
                setLocation(newValue || "");
                commitLocationOption(newValue);
              }}
              onInputChange={(event, newInputValue) => {
                setLocation(newInputValue || "");
              }}
              onBlur={() => commitLocationOption(location)}
              renderInput={(params) => (
                <TextField
                  {...params}
                  label="Location"
                  fullWidth
                  disabled={viewOnly}
                />
              )}
            />

            <FormControl fullWidth disabled={viewOnly}>
              <InputLabel htmlFor="status">Status</InputLabel>
              <Select
                value={status}
                onChange={(e) => setStatus(e.target.value)}
                inputProps={{ name: "status", id: "status" }}
              >
                <MenuItem value="Active">Active</MenuItem>
                <MenuItem value="Inactive">Inactive</MenuItem>
              </Select>
            </FormControl>

            {/* Department Autocomplete */}
            <Autocomplete
              freeSolo
              options={departmentOptions}
              value={department}
              onChange={(event, newValue) => {
                setDepartment(newValue || "");
                commitDepartmentOption(newValue);
              }}
              onInputChange={(event, newInputValue) => {
                setDepartment(newInputValue || "");
              }}
              onBlur={() => commitDepartmentOption(department)}
              renderInput={(params) => (
                <TextField
                  {...params}
                  label="Department"
                  fullWidth
                  disabled={viewOnly}
                />
              )}
            />

            {/* Designation Autocomplete */}
            <Autocomplete
              freeSolo
              options={designationOptions}
              value={designation}
              onChange={(event, newValue) => {
                setDesignation(newValue || "");
                commitDesignationOption(newValue);
              }}
              onInputChange={(event, newInputValue) => {
                setDesignation(newInputValue || "");
              }}
              onBlur={() => commitDesignationOption(designation)}
              renderInput={(params) => (
                <TextField
                  {...params}
                  label="Designation"
                  fullWidth
                  disabled={viewOnly}
                />
              )}
            />
          </div>

          {!viewOnly && (
            <div className={classes.actionsContainer}>
              <Button
                variant="contained"
                className={classes.cancelButton}
                onClick={handleClose}
              >
                Cancel
              </Button>
              <Button
                variant="contained"
                className={classes.saveButton}
                onClick={handleUpdateEmployee}
                disabled={isUpdating}
              >
                {isUpdating ? "Updating..." : "Update"}
              </Button>
            </div>
          )}
        </form>
      </Box>
    </Modal>
  );
}

export default EditEmployeeModal;
