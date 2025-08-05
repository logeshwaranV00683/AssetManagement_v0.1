import React, { useState, useEffect } from "react";
import { makeStyles } from "@material-ui/core/styles";
import Modal from "@mui/material/Modal";
import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import Select from "@mui/material/Select";
import MenuItem from "@mui/material/MenuItem";
import FormControl from "@mui/material/FormControl";
import InputLabel from "@mui/material/InputLabel";
import { IconButton } from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import {
  saveEmployee,
  getLocations,
  getDeparatment,
  getDesignation,
} from "../Services/EmployeeService";
import toast from "react-hot-toast";
import Autocomplete from "@mui/material/Autocomplete";

const useStyles = makeStyles((theme) => ({
  modal: {
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
  },
  paper: {
    backgroundColor: theme.palette.background.paper,
    boxShadow: theme.shadows[5],
    padding: theme.spacing(3, 4, 3),
    position: "relative",
    borderRadius: 12,
    width: "60%",
    maxWidth: 700,
    maxHeight: "75%",
    overflowY: "auto",
    scrollbarWidth: "none",
    "&::-webkit-scrollbar": { display: "none" },
  },
  formGrid: {
    display: "grid",
    gridTemplateColumns: "1fr 1fr",
    gap: theme.spacing(2),
    alignItems: "flex-start",
    [theme.breakpoints.down("sm")]: {
      gridTemplateColumns: "1fr",
    },
  },
  errorText: {
    color: "red",
    fontSize: "0.8rem",
    marginBottom: 2,
    minHeight: 18,
  },
  actionsContainer: {
    display: "flex",
    justifyContent: "space-between",
    marginTop: theme.spacing(2),
  },
}));

function AddEmployeeModal({ open, handleClose, refreshEmployeeList }) {
  const classes = useStyles();

  const [empId, setEmpId] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [role, setRole] = useState("");
  const [mail, setMail] = useState("");
  const [mobile, setMobile] = useState("");
  const [location, setLocation] = useState("");
  const [locationOptions, setLocationOptions] = useState([]);
  const [status, setStatus] = useState("");

  const [department, setDepartment] = useState("");
  const [departmentOptions, setDepartmentOptions] = useState([]);

  const [designation, setDesignation] = useState("");
  const [designationOptions, setDesignationOptions] = useState([]);

  const [touched, setTouched] = useState({
    empId: false,
    firstName: false,
    lastName: false,
    mail: false,
    mobile: false,
    location: false,
    status: false,
    department: false,
    role: false,
    designation: false,
  });

  useEffect(() => {
    if (open) {
      fetchLocations();
      fetchDepartments();
      fetchDesignations();
      setTouched({
        empId: false,
        firstName: false,
        lastName: false,
        mail: false,
        mobile: false,
        location: false,
        status: false,
        department: false,
        role: false,
        designation: false,
      });
    }
  }, [open]);

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

  const commitOption = (value, options, setOptions) => {
    if (value && !options.includes(value)) {
      setOptions((prev) => [...prev, value]);
    }
  };

  const handleAddEmployee = async () => {
    if (
      !empId ||
      !firstName ||
      !lastName ||
      !mail ||
      !mobile ||
      !location ||
      !status ||
      !department ||
      !role ||
      !designation
    ) {
      setTouched({
        empId: true,
        firstName: true,
        lastName: true,
        mail: true,
        mobile: true,
        location: true,
        status: true,
        department: true,
        role: true,
        designation: true,
      });
      toast.error("Please fill all required fields!");
      return;
    }

    const newEmployee = {
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

    try {
      await saveEmployee(newEmployee);
      toast.success("Employee added successfully!");
      refreshEmployeeList();

      setEmpId("");
      setFirstName("");
      setLastName("");
      setMail("");
      setMobile("");
      setLocation("");
      setStatus("");
      setRole("");
      setDepartment("");
      setDesignation("");

      handleClose();
    } catch (error) {
      if (error.status === 400 && typeof error.data === "object") {
        Object.entries(error.data).forEach(([field, message]) => {
          toast.error(`${field}: ${message}`);
        });
      } else if (error.status === 409) {
        toast.error(error.data || "Employee mail or Mobile already exists");
      } else {
        toast.error("Unexpected error occurred!");
        console.error(error);
      }
    }
  };

  return (
    <Modal
      open={open}
      onClose={handleClose}
      className={classes.modal}
      aria-labelledby="add-employee-modal-title"
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

        <h2 style={{ textAlign: "center", color: "#083A40" }}>Add Employee</h2>

        <form>
          <div className={classes.formGrid}>
            {/* Employee ID */}
            <Box sx={{ display: "flex", flexDirection: "column" }}>
              <span
                className={classes.errorText}
                style={{
                  visibility: touched.empId && !empId ? "visible" : "hidden",
                }}
              >
                This field is required *
              </span>
              <TextField
                label="Employee ID"
                value={empId}
                onChange={(e) => setEmpId(e.target.value)}
                onBlur={() => setTouched((prev) => ({ ...prev, empId: true }))}
                fullWidth
              />
            </Box>

            {/* First Name */}
            <Box sx={{ display: "flex", flexDirection: "column" }}>
              <span
                className={classes.errorText}
                style={{
                  visibility:
                    touched.firstName && !firstName ? "visible" : "hidden",
                }}
              >
                This field is required *
              </span>
              <TextField
                label="First Name"
                value={firstName}
                onChange={(e) => setFirstName(e.target.value)}
                onBlur={() =>
                  setTouched((prev) => ({ ...prev, firstName: true }))
                }
                fullWidth
              />
            </Box>

            {/* Last Name */}
            <Box sx={{ display: "flex", flexDirection: "column" }}>
              <span
                className={classes.errorText}
                style={{
                  visibility:
                    touched.lastName && !lastName ? "visible" : "hidden",
                }}
              >
                This field is required *
              </span>
              <TextField
                label="Last Name"
                value={lastName}
                onChange={(e) => setLastName(e.target.value)}
                onBlur={() =>
                  setTouched((prev) => ({ ...prev, lastName: true }))
                }
                fullWidth
              />
            </Box>

            {/* Email */}
            <Box sx={{ display: "flex", flexDirection: "column" }}>
              <span
                className={classes.errorText}
                style={{
                  visibility: touched.mail && !mail ? "visible" : "hidden",
                }}
              >
                This field is required *
              </span>
              <TextField
                label="Email"
                value={mail}
                onChange={(e) => setMail(e.target.value)}
                onBlur={() => setTouched((prev) => ({ ...prev, mail: true }))}
                fullWidth
              />
            </Box>

            {/* Mobile */}
            <Box sx={{ display: "flex", flexDirection: "column" }}>
              <span
                className={classes.errorText}
                style={{
                  visibility: touched.mobile && !mobile ? "visible" : "hidden",
                }}
              >
                This field is required *
              </span>
              <TextField
                label="Mobile"
                value={mobile}
                onChange={(e) => setMobile(e.target.value)}
                onBlur={() => setTouched((prev) => ({ ...prev, mobile: true }))}
                fullWidth
              />
            </Box>

            {/* Location */}
            <Box sx={{ display: "flex", flexDirection: "column" }}>
              <span
                className={classes.errorText}
                style={{
                  visibility:
                    touched.location && !location ? "visible" : "hidden",
                }}
              >
                This field is required *
              </span>
              <Autocomplete
                freeSolo
                options={locationOptions}
                value={location}
                onChange={(event, newValue) => {
                  setLocation(newValue || "");
                  commitOption(newValue, locationOptions, setLocationOptions);
                }}
                onInputChange={(event, newInputValue) =>
                  setLocation(newInputValue || "")
                }
                onBlur={() =>
                  setTouched((prev) => ({ ...prev, location: true }))
                }
                renderInput={(params) => (
                  <TextField {...params} label="Location" fullWidth />
                )}
              />
            </Box>

            {/* Status */}
            <Box sx={{ display: "flex", flexDirection: "column" }}>
              <span
                className={classes.errorText}
                style={{
                  visibility: touched.status && !status ? "visible" : "hidden",
                }}
              >
                This field is required *
              </span>
              <FormControl fullWidth>
                <InputLabel>Status</InputLabel>
                <Select
                  value={status}
                  onChange={(e) => setStatus(e.target.value)}
                  onBlur={() =>
                    setTouched((prev) => ({ ...prev, status: true }))
                  }
                >
                  <MenuItem value="Active">Active</MenuItem>
                  <MenuItem value="Inactive">Inactive</MenuItem>
                </Select>
              </FormControl>
            </Box>

            {/* Department */}
            <Box sx={{ display: "flex", flexDirection: "column" }}>
              <span
                className={classes.errorText}
                style={{
                  visibility:
                    touched.department && !department ? "visible" : "hidden",
                }}
              >
                This field is required *
              </span>
              <Autocomplete
                freeSolo
                options={departmentOptions}
                value={department}
                onChange={(event, newValue) => {
                  setDepartment(newValue || "");
                  commitOption(
                    newValue,
                    departmentOptions,
                    setDepartmentOptions
                  );
                }}
                onInputChange={(event, newInputValue) =>
                  setDepartment(newInputValue || "")
                }
                onBlur={() =>
                  setTouched((prev) => ({ ...prev, department: true }))
                }
                renderInput={(params) => (
                  <TextField {...params} label="Department" fullWidth />
                )}
              />
            </Box>

            {/* Role */}
            <Box sx={{ display: "flex", flexDirection: "column" }}>
              <span
                className={classes.errorText}
                style={{
                  visibility: touched.role && !role ? "visible" : "hidden",
                }}
              >
                This field is required *
              </span>
              <FormControl fullWidth>
                <InputLabel>Role</InputLabel>
                <Select
                  value={role}
                  onChange={(e) => setRole(e.target.value)}
                  onBlur={() => setTouched((prev) => ({ ...prev, role: true }))}
                >
                  <MenuItem value="Employee">Employee</MenuItem>
                  <MenuItem value="Admin">Admin</MenuItem>
                </Select>
              </FormControl>
            </Box>

            {/* Designation */}
            <Box sx={{ display: "flex", flexDirection: "column" }}>
              <span
                className={classes.errorText}
                style={{
                  visibility:
                    touched.designation && !designation ? "visible" : "hidden",
                }}
              >
                This field is required *
              </span>
              <Autocomplete
                freeSolo
                options={designationOptions}
                value={designation}
                onChange={(event, newValue) => {
                  setDesignation(newValue || "");
                  commitOption(
                    newValue,
                    designationOptions,
                    setDesignationOptions
                  );
                }}
                onInputChange={(event, newInputValue) =>
                  setDesignation(newInputValue || "")
                }
                onBlur={() =>
                  setTouched((prev) => ({ ...prev, designation: true }))
                }
                renderInput={(params) => (
                  <TextField {...params} label="Designation" fullWidth />
                )}
              />
            </Box>
          </div>

          {/* Buttons */}
          <div className={classes.actionsContainer}>
            <Button
              variant="contained"
              onClick={handleClose}
              sx={{ backgroundColor: "error.main", color: "white" }}
            >
              Cancel
            </Button>
            <Button
              variant="contained"
              onClick={handleAddEmployee}
              sx={{ backgroundColor: "success.main", color: "white" }}
            >
              Add
            </Button>
          </div>
        </form>
      </Box>
    </Modal>
  );
}

export default AddEmployeeModal;
