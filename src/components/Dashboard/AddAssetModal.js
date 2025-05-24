import React, { useState } from 'react';
import Modal from '@mui/material/Modal';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Select from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import InputLabel from '@mui/material/InputLabel';
import IconButton from '@mui/material/IconButton';
import CloseIcon from '@mui/icons-material/Close';
import { saveAsset } from '../Services/AssetService';
import { toast } from 'react-hot-toast';


function AddAssetModal({ open, handleClose, refreshAssetList }) {

  const user = JSON.parse(localStorage.getItem('user'));


  const [assetName, setAssetName] = useState('');
  const [serialNumber, setSerialNumber] = useState('');
  const [location, setLocation] = useState('');
  const [locCode, setLocCode] = useState('');
  const [operatingSystem, setOperatingSystem] = useState('');
  const [modelName, setModelName] = useState('');
  const [purchaseDate, setPurchaseDate] = useState('');
  const [warrantyDate, setWarrantyDate] = useState('');
  const [addedBy, setAddedBy] = useState(user.empId);
  const [type, setType] = useState('');
  const [assertSourcedBy, setAssertSourcedBy] = useState('Verinite');

  const handleAddAsset = async () => {
    const newAsset = {
      assetName,
      serialNumber,
      location,
      locCode,
      type,
      operatingSystem,
      modelName,
      purchaseDate,
      warrantyDate,
      addedBy,
      assertSourcedBy,
    };
    console.log('Asset added:', newAsset);
           try {
             await saveAsset(newAsset);
             console.log('Asset added:', newAsset);
             refreshAssetList();
             toast.success(`${newAsset.serialNumber} Asset Added Successfully`); 
             handleCloseModal();
           } catch (error) {
             console.error('Error adding Asset:', error);
             toast.error(`Adding ${newAsset.serialNumber} Asset Failed`); 
           }
  };

  const resetForm = () => {
    setAssetName('');
    setSerialNumber('');
    setLocation('');
    setLocCode('');
    setOperatingSystem('');
    setModelName('');
    setType('');
    setPurchaseDate('');
    setWarrantyDate('');
    setAddedBy(user.empId);
    setAssertSourcedBy('');
  };

  const handleCloseModal = () => {
    resetForm();
    handleClose();
  };

  return (
    <Modal
      open={open}
      onClose={handleCloseModal}
      aria-labelledby="add-asset-modal-title"
      aria-describedby="add-asset-modal-description"
    >
      <Box
        sx={{
          backgroundColor: 'background.paper',
          boxShadow: 24,
          p: 4,
          width: '60%',
          maxWidth: 700,
          mx: 'auto',
          my: '10%',
          borderRadius: 4,
          position: 'relative',
        }}
      >
        <IconButton
          aria-label="close"
          onClick={handleCloseModal}
          sx={{ position: 'absolute', top: 8, right: 8 }}
        >
          <CloseIcon />
        </IconButton>

        <h2 id="add-asset-modal-title" style={{ textAlign: 'center', color: '#083A40' }}>
          Add Asset
        </h2>

        <Box
          component="form"
          noValidate
          autoComplete="off"
          sx={{
            display: 'grid',
            gridTemplateColumns: { xs: '1fr', sm: '1fr 1fr' },
            gap: 2,
          }}
        >
          <FormControl fullWidth>
            <InputLabel id="asset-name-label">Asset Name</InputLabel>
            <Select
              labelId="asset-name-label"
              id="asset-name"
              value={assetName}
              label="Asset Name"
              onChange={(e) => setAssetName(e.target.value)}
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
          />
          <TextField
                                label="Location"
                                value={location}
                                onChange={(e) => setLocation(e.target.value)}
                                fullWidth
                    />

                    <TextField
                                label="Location Code"
                                value={locCode}
                                onChange={(e) => setLocCode(e.target.value)}
                                fullWidth
                    />
                    <TextField
                                          label="Operating System"
                                          value={operatingSystem}
                                          onChange={(e) => setOperatingSystem(e.target.value)}
                                          fullWidth
                    />
                    <TextField
                                          label="Variant"
                                          value={modelName}
                                          onChange={(e) => setModelName(e.target.value)}
                                          fullWidth
                    />
                    <TextField
                                                    label="Type"
                                                    value={type}
                                                    onChange={(e) => setType(e.target.value)}
                                                    fullWidth
                    />
          <TextField
            label="Purchase Date"
            type="date"
            value={purchaseDate}
            onChange={(e) => setPurchaseDate(e.target.value)}
            InputLabelProps={{ shrink: true }}
            fullWidth
          />

          <TextField
            label="Warranty Date"
            type="date"
            value={warrantyDate}
            onChange={(e) => setWarrantyDate(e.target.value)}
            InputLabelProps={{ shrink: true }}
            fullWidth
          />

          <FormControl fullWidth>
            <InputLabel id="assert-sourced-by-label">Assert Sourced By</InputLabel>
            <Select
              labelId="assert-sourced-by-label"
              id="assert-sourced-by"
              value={
                ['Verinite', 'Client Company'].includes(assertSourcedBy)
                  ? assertSourcedBy
                  : 'Client Company'
              }
              label="Assert Sourced By"
              onChange={(e) => {
                const value = e.target.value;
                if (value === 'Verinite') {
                  setAssertSourcedBy('Verinite');
                } else {
                  setAssertSourcedBy('');
                }
              }}
            >
              <MenuItem value="Verinite">Verinite</MenuItem>
              <MenuItem value="Client Company">Client Company</MenuItem>
            </Select>
          </FormControl>

          {assertSourcedBy !== 'Verinite' && (
            <TextField
              label="Enter Client Company Name"
              value={assertSourcedBy}
              onChange={(e) => setAssertSourcedBy(e.target.value)}
              fullWidth
            />
          )}
        </Box>

        <Box
          sx={{
            display: 'flex',
            justifyContent: 'space-between',
            mt: 3,
          }}
        >
          <Button
            variant="contained"
            onClick={handleCloseModal}
            sx={{ backgroundColor: 'error.main', color: 'error.contrastText' }}
          >
            Cancel
          </Button>
          <Button
            variant="contained"
            onClick={handleAddAsset}
            sx={{ backgroundColor: 'success.main', color: 'success.contrastText' }}
          >
            Add
          </Button>
        </Box>
      </Box>
    </Modal>
  );
}

export default AddAssetModal;
