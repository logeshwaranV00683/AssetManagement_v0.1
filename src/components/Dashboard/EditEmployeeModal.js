import React, { useState, useEffect } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Modal from '@mui/material/Modal';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Select from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import InputLabel from '@mui/material/InputLabel';
import { IconButton } from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';
import { updateEmployee } from '../Services/EmployeeService';

const useStyles = makeStyles((theme) => ({
  modal: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
  },
  paper: {
    backgroundColor: theme.palette.background.paper,
    boxShadow: theme.shadows[5],
    padding: theme.spacing(2, 4, 3),
    position: 'relative',
  },
  formControl: {
    marginTop: theme.spacing(2),
  },
  textCenter: {
    textAlign: 'center',
  },
  formGrid: {
    display: 'grid',
    gridTemplateColumns: '1fr 1fr',
    gap: theme.spacing(2),
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
    display: 'flex',
    justifyContent: 'space-between',
    marginTop: theme.spacing(2),
  },
}));

function EditEmployeeModal({ open, handleClose, employee, refreshEmployeeList, viewOnly }) {
  const classes = useStyles();
  const [empId, setEmpId] = useState(employee.empId);
  const [firstName, setFirstName] = useState(employee.firstName);
  const [lastName, setLastName] = useState(employee.lastName);
  const [role, setRole] = useState(employee.role);
  const [mail, setMail] = useState(employee.mail);
  const [mobile, setMobile] = useState(employee.mobile);
  const [location, setLocation] = useState(employee.location);
  const [status, setStatus] = useState(employee.status);
  const [department, setDepartment] = useState(employee.department);
  const [designation, setDesignation] = useState(employee.designation);

  const handleSaveEmployee = async () => {
    const updatedFields = {};
    if (firstName !== employee.firstName) updatedFields.firstName = firstName;
    if (lastName !== employee.lastName) updatedFields.lastName = lastName;
    if (role !== employee.role) updatedFields.role = role;
    if (mail !== employee.mail) updatedFields.mail = mail;
    if (mobile !== employee.mobile) updatedFields.mobile = mobile;
    if (location !== employee.location) updatedFields.location = location;
    if (status !== employee.status) updatedFields.status = status;
    if (department !== employee.department) updatedFields.department = department;
    if (designation !== employee.designation) updatedFields.designation = designation;

    try {
      await updateEmployee(empId, updatedFields);
      console.log('Employee updated:', updatedFields);
      refreshEmployeeList();
      handleClose();
    } catch (error) {
      console.error('Error updating employee:', error);
    }
  };

  useEffect(() => {
    if (employee) {
      setEmpId(employee.empId);
      setFirstName(employee.firstName);
      setLastName(employee.lastName);
      setRole(employee.role);
      setMail(employee.mail);
      setMobile(employee.mobile);
      setLocation(employee.location);
      setStatus(employee.status);
      setDepartment(employee.department);
      setDesignation(employee.designation);
    }
  }, [employee]);

  return (
    <Modal
      open={open}
      onClose={handleClose}
      className={classes.modal}
      aria-labelledby="edit-employee-modal-title"
      aria-describedby="edit-employee-modal-description"
    >
      <Box className={classes.paper}>
        <IconButton
          edge="end"
          aria-label="close"
          onClick={handleClose}
          sx={{
            position: 'absolute',
            top: '8px',
            right: '8px',
          }}
        >
          <CloseIcon />
        </IconButton>
        <h2 id="edit-employee-modal-title" className={classes.textCenter}>
          {viewOnly ? 'View Employee' : 'Edit Employee'}
        </h2>
        <form>
          <div className={classes.formGrid}>
            <TextField
              label="Employee ID"
              value={empId}
              fullWidth
              margin="normal"
              disabled
            />
            <TextField
              label="First Name"
              value={firstName}
              onChange={(e) => setFirstName(e.target.value)}
              fullWidth
              margin="normal"
              disabled={viewOnly}
            />
            <TextField
              label="Last Name"
              value={lastName}
              onChange={(e) => setLastName(e.target.value)}
              fullWidth
              margin="normal"
              disabled={viewOnly}
            />
            <FormControl fullWidth className={classes.formControl} disabled={viewOnly}>
              <InputLabel htmlFor="role">Role</InputLabel>
              <Select
                label="Role"
                value={role}
                onChange={(e) => setRole(e.target.value)}
                inputProps={{
                name: 'role',
                id: 'role',
               }}
              >
                <MenuItem value="User">User</MenuItem>
                <MenuItem value="Admin">Admin</MenuItem>
              </Select>
            </FormControl>
            <TextField
              label="Email"
              value={mail}
              onChange={(e) => setMail(e.target.value)}
              fullWidth
              margin="normal"
              disabled={viewOnly}
            />
            <TextField
              label="Mobile"
              value={mobile}
              onChange={(e) => setMobile(e.target.value)}
              fullWidth
              margin="normal"
              disabled={viewOnly}
            />
            <TextField
              label="Location"
              value={location}
              onChange={(e) => setLocation(e.target.value)}
              fullWidth
              margin="normal"
              disabled={viewOnly}
            />
            <FormControl fullWidth className={classes.formControl} disabled={viewOnly}>
              <InputLabel htmlFor="status">Status</InputLabel>
              <Select
                label="Status"
                value={status}
                onChange={(e) => setStatus(e.target.value)}
                inputProps={{
                name: 'status',
                id: 'status',
               }}
              >
                <MenuItem value="Active">Active</MenuItem>
                <MenuItem value="Inactive">Inactive</MenuItem>
              </Select>
            </FormControl>
            <TextField
              label="Department"
              value={department}
              onChange={(e) => setDepartment(e.target.value)}
              fullWidth
              margin="normal"
              disabled={viewOnly}
            />
            <TextField
              label="Designation"
              value={designation}
              onChange={(e) => setDesignation(e.target.value)}
              fullWidth
              margin="normal"
              disabled={viewOnly}
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
                onClick={handleSaveEmployee}
              >
                Save
              </Button>
            </div>
          )}
        </form>
      </Box>
    </Modal>
  );
}

export default EditEmployeeModal;
