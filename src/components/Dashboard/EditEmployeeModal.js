import React, { useState, useEffect, useRef } from "react";
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

function EditEmployeeModal({
  open,
  handleClose,
  employee,
  refreshEmployeeList,
  viewOnly,
}) {
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

  const fieldRefs = {
    firstName: useRef(null),
    lastName: useRef(null),
    role: useRef(null),
    mail: useRef(null),
    mobile: useRef(null),
    location: useRef(null),
    status: useRef(null),
    department: useRef(null),
    designation: useRef(null),
  };

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

  const requiredFields = [
    "firstName",
    "lastName",
    "role",
    "mail",
    "mobile",
    "location",
    "status",
    "department",
    "designation",
  ];

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

  const invalidField = requiredFields.find((f) => !currentValues[f]);

  if (invalidField) {
    setTouched(requiredFields.reduce((acc, key) => ({ ...acc, [key]: true }), {}));

    toast.error("Please fill all required fields!");

    fieldRefs[invalidField]?.current?.scrollIntoView({ behavior: "smooth", block: "center" });
    return;
  }

  const updatedFields = Object.fromEntries(
    Object.entries(currentValues).filter(([key, value]) => value !== (employee[key] || ""))
  );

  if (!Object.keys(updatedFields).length) {
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
    } else if (error.status === 406) {
      toast.error(error.data || "Employee mail or Mobile already exists");
    } else {
      toast.error("Unexpected error occurred while updating.");
    }
  } finally {
    setIsUpdating(false);
  }
};


  const renderFieldWithError = (label, value, setValue, key, type = "text") => (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        animation:
          touched[key] && !value
            ? "shake 0.3s ease-in-out, blink 0.6s step-end 0s 2"
            : "none",
        "@keyframes shake": {
          "0%": { transform: "translateX(0)" },
          "25%": { transform: "translateX(-4px)" },
          "50%": { transform: "translateX(4px)" },
          "75%": { transform: "translateX(-4px)" },
          "100%": { transform: "translateX(0)" },
        },
        "@keyframes blink": {
          "50%": { borderColor: "red" },
        },
      }}
      ref={fieldRefs[key]}
    >
      <span
        style={{
          color: "red",
          fontSize: "0.8rem",
          marginBottom: 2,
          minHeight: 18,
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
      aria-labelledby="edit-employee-modal-title"
      sx={{
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
      }}
    >
      <Box
        sx={{
          backgroundColor: "background.paper",
          boxShadow: 5,
          p: { xs: 2, sm: 3 },
          position: "relative",
          borderRadius: 2,
          width: "60%",
          maxWidth: 700,
          maxHeight: "75%",
          overflowY: "auto",
          scrollbarWidth: "none",
          "&::-webkit-scrollbar": { display: "none" },
        }}
      >
        <IconButton
          edge="end"
          aria-label="close"
          onClick={handleClose}
          sx={{ position: "absolute", top: 8, right: 8 }}
        >
          <CloseIcon />
        </IconButton>

        <h2 style={{ textAlign: "center", color: "#083A40" }}>
          {viewOnly ? "View Employee" : "Edit Employee"}
        </h2>

        <form>
          <Box
            sx={{
              display: "grid",
              gridTemplateColumns: { xs: "1fr", sm: "1fr 1fr" },
              gap: 2,
              alignItems: "flex-start",
            }}
          >
            <TextField
              label="Employee ID"
              value={empId}
              fullWidth
              disabled
              sx={{ mt: 2 }}
            />

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
            <Box
              sx={{
                display: "flex",
                flexDirection: "column",
              }}
              ref={fieldRefs.role}
            >
              <span
                style={{
                  color: "red",
                  fontSize: "0.8rem",
                  marginBottom: 2,
                  minHeight: 18,
                  visibility: touched.role && !role ? "visible" : "hidden",
                }}
              >
                This field is required *
              </span>
              <TextField
                select
                label="Role"
                value={role}
                onChange={(e) => setRole(e.target.value)}
                onBlur={() => setTouched((prev) => ({ ...prev, role: true }))}
                fullWidth
                disabled={viewOnly}
                error={touched.role && !role}
              >
                <MenuItem value="Employee">Employee</MenuItem>
                <MenuItem value="Admin">Admin</MenuItem>
              </TextField>
            </Box>


            {renderFieldWithError("Email", mail, setMail, "mail")}
            {renderFieldWithError("Mobile", mobile, setMobile, "mobile")}

            {/* Location */}
            <Autocomplete
              freeSolo
              disabled={viewOnly}
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
                  error={touched.location && !location}
                />
              )}
            />

            {/* Status */}
            <FormControl
              fullWidth
              size="medium"
              error={touched.status && !status}
              onBlur={() => setTouched((prev) => ({ ...prev, status: true }))}
              disabled={viewOnly}
            >
              <InputLabel>Status</InputLabel>
              <Select
                label="Status"
                value={status}
                onChange={(e) => setStatus(e.target.value)}
              >
                <MenuItem value="Active">Active</MenuItem>
                <MenuItem value="Inactive">Inactive</MenuItem>
              </Select>
              <FormHelperText>
                {touched.status && !status ? "This field is required *" : " "}
              </FormHelperText>
            </FormControl>

            {/* Department */}
            <Autocomplete
              freeSolo
              disabled={viewOnly}
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
                  error={touched.department && !department}
                />
              )}
            />

            {/* Designation */}
            <Autocomplete
              freeSolo
              disabled={viewOnly}
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
                  error={touched.designation && !designation}
                />
              )}
            />
          </Box>

          {!viewOnly && (
            <Box
              sx={{
                display: "flex",
                justifyContent: "space-between",
                mt: 2,
              }}
            >
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
            </Box>
          )}
        </form>
      </Box>
    </Modal>
  );
}

export default EditEmployeeModal;
