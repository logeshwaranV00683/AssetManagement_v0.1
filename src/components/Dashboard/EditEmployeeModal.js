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
  FormHelperText,
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
  textCenter: {
    textAlign: "center",
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

  const [touched, setTouched] = useState({
    firstName: false,
    lastName: false,
    role: false,
    mail: false,
    mobile: false,
    location: false,
    status: false,
    department: false,
    designation: false,
  });

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

      setTouched({
        firstName: false,
        lastName: false,
        role: false,
        mail: false,
        mobile: false,
        location: false,
        status: false,
        department: false,
        designation: false,
      });
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

  const commitOption = (value, options, setOptions) => {
    if (value && !options.includes(value)) {
      setOptions((prev) => [...prev, value]);
    }
  };

  const handleUpdateEmployee = async () => {
    if (viewOnly || !employee) return;

    if (
      !firstName ||
      !lastName ||
      !role ||
      !mail ||
      !mobile ||
      !location ||
      !status ||
      !department ||
      !designation
    ) {
      setTouched({
        firstName: true,
        lastName: true,
        role: true,
        mail: true,
        mobile: true,
        location: true,
        status: true,
        department: true,
        designation: true,
      });
      toast.error("Please fill all required fields!");
      return;
    }

    const updatedFields = {};
    const currentValues = {
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

  const renderFieldWithError = (label, value, setValue, key, type = "text") => (
    <Box sx={{ display: "flex", flexDirection: "column" }}>
      <span
        className={classes.errorText}
        style={{
          visibility: touched[key] && !value ? "visible" : "hidden",
        }}
      >
        This field is required *
      </span>
      <TextField
        label={label}
        value={value}
        type={type}
        onChange={(e) => setValue(e.target.value)}
        onBlur={() => setTouched((prev) => ({ ...prev, [key]: true }))}
        fullWidth
        disabled={viewOnly}
      />
    </Box>
  );

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
            {/* Employee ID (readonly) */}
            <TextField label="Employee ID" value={empId} fullWidth disabled style={{ marginTop: '19px' }}/>

            {renderFieldWithError(
              "First Name",
              firstName,
              setFirstName,
              "firstName"
            )}
            {renderFieldWithError(
              "Last Name",
              lastName,
              setLastName,
              "lastName"
            )}

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
              <FormControl
                fullWidth
                size="medium"
                error={touched.role && !role}
                onBlur={() => setTouched((prev) => ({ ...prev, role: true }))}
                disabled={viewOnly}
              >
                <InputLabel>Role</InputLabel>
                <Select
                  value={role}
                  onChange={(e) => setRole(e.target.value)}
                  label="Role"
                >
                  <MenuItem value="Employee">Employee</MenuItem>
                  <MenuItem value="Admin">Admin</MenuItem>
                </Select>
                <FormHelperText>
                  {touched.role && !role ? "This field is required *" : " "}
                </FormHelperText>
              </FormControl>
            </Box>

            {renderFieldWithError("Email", mail, setMail, "mail")}
            {renderFieldWithError("Mobile", mobile, setMobile, "mobile")}

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
                  <TextField
                    {...params}
                    label="Location"
                    fullWidth
                    disabled={viewOnly}
                  />
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
              <FormControl
                fullWidth
                size="medium"
                error={touched.status && !status}
                onBlur={() => setTouched((prev) => ({ ...prev, status: true }))}
                disabled={viewOnly}
              >
                <InputLabel>Status</InputLabel>
                <Select
                  value={status}
                  onChange={(e) => setStatus(e.target.value)}
                  label="Status"
                >
                  <MenuItem value="Active">Active</MenuItem>
                  <MenuItem value="Inactive">Inactive</MenuItem>
                </Select>
                <FormHelperText>
                  {touched.status && !status ? "This field is required *" : " "}
                </FormHelperText>
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
                  <TextField
                    {...params}
                    label="Department"
                    fullWidth
                    disabled={viewOnly}
                  />
                )}
              />
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
                  <TextField
                    {...params}
                    label="Designation"
                    fullWidth
                    disabled={viewOnly}
                  />
                )}
              />
            </Box>
          </div>

          {!viewOnly && (
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
                onClick={handleUpdateEmployee}
                disabled={isUpdating}
                sx={{ backgroundColor: "success.main", color: "white" }}
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
