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
    padding: theme.spacing(2, 4, 3),
    position: "relative",
    borderRadius: 8,
  },
  formControl: {
    marginTop: theme.spacing(2),
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
  alignFix: {
    marginTop: 0,
    marginBottom: 0,
  },
  cancelButton: {
    marginRight: theme.spacing(2),
    backgroundColor: theme.palette.error.main,
    color: theme.palette.error.contrastText,
  },
  addButton: {
    backgroundColor: theme.palette.success.main,
    color: theme.palette.success.contrastText,
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

  useEffect(() => {
    if (open) {
      fetchLocations();
      fetchDepartments();
      fetchDesignations();
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

  const handleAddEmployee = async () => {
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

        <h2 className={classes.textCenter} style={{ color: "#083A40" }}>
          Add Employee
        </h2>

        <form>
          <div className={classes.formGrid}>
            <TextField
              label="Employee ID"
              value={empId}
              onChange={(e) => setEmpId(e.target.value)}
              fullWidth
            />
            <TextField
              label="First Name"
              value={firstName}
              onChange={(e) => setFirstName(e.target.value)}
              fullWidth
            />
            <TextField
              label="Last Name"
              value={lastName}
              onChange={(e) => setLastName(e.target.value)}
              fullWidth
            />
            <TextField
              label="Email"
              value={mail}
              onChange={(e) => setMail(e.target.value)}
              fullWidth
            />
            <TextField
              label="Mobile"
              value={mobile}
              onChange={(e) => setMobile(e.target.value)}
              fullWidth
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
                  placeholder="Enter Location Name (New if not in list)"
                  variant="outlined"
                  fullWidth
                />
              )}
            />

            <FormControl fullWidth className={classes.formControl}>
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
                  placeholder="Enter Department Name (New if not in list)"
                  variant="outlined"
                  fullWidth
                />
              )}
            />

            <FormControl fullWidth className={classes.formControl}>
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
                  placeholder="Enter Designation Name (New if not in list)"
                  variant="outlined"
                  fullWidth
                />
              )}
            />
          </div>

          <div className={classes.actionsContainer}>
            <Button
              variant="contained"
              className={classes.cancelButton}
              onClick={handleClose}
              sx={{
                backgroundColor: "error.main",
                color: "error.contrastText",
              }}
            >
              Cancel
            </Button>
            <Button
              variant="contained"
              className={classes.addButton}
              onClick={handleAddEmployee}
              sx={{
                backgroundColor: "success.main",
                color: "success.contrastText",
              }}
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
