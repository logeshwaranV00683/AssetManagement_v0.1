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
    position: 'relative', // Ensure position for close button
  },
  formControl:{
    justifyContent:'center',
    marginTop: '8px !important'
  },
  textCenter: {
    textAlign:'center'
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

function AddAssetModal({ open, handleClose }) {
  const classes = useStyles();
  const [assetName, setAssetName] = useState('');
  const [serialNumber, setSerialNumber] = useState('');
  const [location, setLocation] = useState('');
  const [purchaseDate, setPurchaseDate] = useState('');
  const [warrantyDate, setWarrantyDate] = useState('');
  const [addedBy, setAddedBy] = useState('');

  const handleAddAsset = () => {
    // Implement logic to add asset
    console.log('Asset added:', { assetName, serialNumber, location, purchaseDate, warrantyDate, addedBy });
    // handleClose();
  };

  const handleCls = () => {
    handleClose()
  }
  return (
    <Modal
      open={open}
      onClose={handleClose}
      className={classes.modal}
      aria-labelledby="add-asset-modal-title"
      aria-describedby="add-asset-modal-description"
    >
      <Box className={classes.paper}>
      <IconButton
  edge="end"
  aria-label="close"
  onClick={handleClose} // Use handleClose directly

  sx={{
    position: 'absolute',
    top: '8px',
    right: '8px',
  }}
>
  <CloseIcon />
</IconButton>

        <h2 id="add-asset-modal-title"className={classes.textCenter}>Add Asset</h2>
        <form>
          <div className={classes.formGrid}>
            <FormControl fullWidth className={classes.formControl}>
              <InputLabel className="text-center" htmlFor="asset-name">Asset Name</InputLabel>
              <Select
                id="asset-name"
                value={assetName}
                onChange={(e) => setAssetName(e.target.value)}
                style={{ border: 'none', padding: 0 }} // Override border style
              >
                <MenuItem value="Laptop">Laptop</MenuItem>
                <MenuItem value="Desktop">Desktop</MenuItem>
              </Select>
            </FormControl>
            <TextField
              label="Serial Number"
              value={serialNumber}
              onChange={(e) => setSerialNumber(e.target.value)}
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
            <TextField
              label="Added by"
              value={addedBy}
              onChange={(e) => setAddedBy(e.target.value)}
              fullWidth
              margin="normal"
            />
            <TextField
              label="Purchase Date"
              type="date"
              value={purchaseDate}
              onChange={(e) => setPurchaseDate(e.target.value)}
              fullWidth
              margin="normal"
              InputLabelProps={{
                shrink: true,
              }}
            />
            <TextField
              label="Warranty Date"
              type="date"
              value={warrantyDate}
              onChange={(e) => setWarrantyDate(e.target.value)}
              fullWidth
              margin="normal"
              InputLabelProps={{
                shrink: true,
              }}
            />
          </div>
          <div className={classes.actionsContainer}>
            <Button
              variant="contained"
              color="error"
              className={classes.cancelButton}
              onClick={handleClose}
            >
              Cancel
            </Button>
            <Button
              variant="contained"
              color="success"
              className={classes.addButton}
              onClick={handleAddAsset}
            >
              Add
            </Button>
          </div>
        </form>
      </Box>
    </Modal>
  );
}

export default AddAssetModal;
