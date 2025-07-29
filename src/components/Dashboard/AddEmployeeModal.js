import React, { useState } from 'react';
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
import { saveEmployee } from '../Services/EmployeeService';

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
  addButton: {
    backgroundColor: theme.palette.success.main,
    color: theme.palette.success.contrastText,
  },
  actionsContainer: {
    display: 'flex',
    justifyContent: 'space-between',
    marginTop: theme.spacing(2),
  },
}));

function AddEmployeeModal({ open, handleClose, refreshEmployeeList }) {
  const classes = useStyles();
  const [empId, setEmpId] = useState('');
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [role] = useState('User');
  const [mail, setMail] = useState('');
  const [mobile, setMobile] = useState('');
  const [location, setLocation] = useState('');
  const [status, setStatus] = useState('');
  const [department, setDepartment] = useState('');
  const [designation, setDesignation] = useState('');

  const handleAddEmployee = async() => {
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
    console.log('Employee added:', newEmployee);
    try {
      await saveEmployee(newEmployee);
      refreshEmployeeList(); 
      handleClose();
    } catch (error) {
      console.error('Error adding employee:', error);
    }
  };

  return (
    <Modal
      open={open}
      onClose={handleClose}
      className={classes.modal}
      aria-labelledby="add-employee-modal-title"
      aria-describedby="add-employee-modal-description"
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
        <h2 id="add-employee-modal-title" className={classes.textCenter} style={{ textAlign: 'center', color: '#083A40' }}>Add Employee</h2>
        <form>
          <div className={classes.formGrid}>
            <TextField
              label="Employee ID"
              value={empId}
              onChange={(e) => setEmpId(e.target.value)}
              fullWidth
              margin="normal"
            />
            <TextField
              label="First Name"
              value={firstName}
              onChange={(e) => setFirstName(e.target.value)}
              fullWidth
              margin="normal"
            />
            <TextField
              label="Last Name"
              value={lastName}
              onChange={(e) => setLastName(e.target.value)}
              fullWidth
              margin="normal"
            />
            <TextField
              label="Email"
              value={mail}
              onChange={(e) => setMail(e.target.value)}
              fullWidth
              margin="normal"
            />
            <TextField
              label="Mobile"
              value={mobile}
              onChange={(e) => setMobile(e.target.value)}
              fullWidth
              margin="normal"
            />
            <TextField
              label="Location"
              value={location}
              onChange={(e) => setLocation(e.target.value)}
              fullWidth
              margin="normal"
            />
            <FormControl fullWidth className={classes.formControl}>
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
            />
            <TextField
              label="Designation"
              value={designation}
              onChange={(e) => setDesignation(e.target.value)}
              fullWidth
              margin="normal"
            />
          </div>
          <div className={classes.actionsContainer}>
            <Button
             type="button" 
              variant="contained"
              className={classes.cancelButton}
              onClick={handleClose}
              sx={{ backgroundColor: 'error.main', color: 'error.contrastText' }}
            >
              Cancel
            </Button>
            <Button
             type="button" 
              data-id="add-employee-button"
              variant="contained"
              className={classes.addButton}
              onClick={handleAddEmployee}
              sx={{ backgroundColor: 'success.main', color: 'success.contrastText' }}
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
