import React, { useEffect, useState, useMemo, useCallback } from 'react';
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
} from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';
import { updateAsset } from '../Services/AssetService';
import { toast } from 'react-hot-toast';


function EditAssetModal({ open, handleClose, refreshAssetList, asset, viewOnly }) {
  const [fields, setFields] = useState({
    assetName: '',
    serialNumber: '',
    location: '',
    locCode: '',
    operatingSystem: '',
    modelName: '',
    purchaseDate: '',
    warrantyDate: '',
    addedBy: '',
    assignedBy: '',
    assignedDate: '',
    returnDate: '',
    type: '',
    assertSourcedBy: '',
    empId: '',
    status: '',
  });

  useEffect(() => {
    if (asset) {
      setFields({
        assetName: asset.assetName || '',
        serialNumber: asset.serialNumber || '',
        location: asset.location || '',
        locCode: asset.locCode || '',
        operatingSystem: asset.operatingSystem || '',
        modelName: asset.modelName || '',
        purchaseDate: asset.purchaseDate || '',
        warrantyDate: asset.warrantyDate || '',
        addedBy: asset.addedBy || '',
        assignedBy: asset.assignedBy || '',
        assignedDate: asset.assignedDate || '',
        returnDate: asset.returnDate || '',
        type: asset.type || '',
        assertSourcedBy: asset.assertSourcedBy || '',
        empId: asset.empId || '',
        status: asset.status || '',
      });
    }
  }, [asset]);

  const isUnassigned = useMemo(() => fields.status === 'UnAssigned', [fields.status]);
  const isAssigned = useMemo(() => fields.status === 'Assigned', [fields.status]);
  const isScrap = useMemo(() => fields.status === 'Scrap', [fields.status]);

  const handleChange = useCallback(
    (field) => (e) => {
      const value = e.target.value;
      setFields((prev) => ({ ...prev, [field]: value }));
    },
    []
  );

  const handleAssertSourcedByChange = useCallback(
    (e) => {
      const val = e.target.value;
      setFields((prev) => ({
        ...prev,
        assertSourcedBy: val === 'Verinite' ? 'Verinite' : '',
      }));
    },
    []
  );

  const handleClientCompanyNameChange = useCallback(
    (e) => {
      const val = e.target.value;
      setFields((prev) => ({
        ...prev,
        assertSourcedBy: val,
      }));
    },
    []
  );

  const handleUpdateAsset = async () => {
    if (!asset) return;

    const updatedFields = {};
    for (const key in fields) {
      const oldVal = asset[key] ?? '';
      const newVal = fields[key] ?? '';
      if (newVal !== oldVal) {
        updatedFields[key] = newVal;
      }
    }

    if (Object.keys(updatedFields).length === 0) {
      toast('No changes detected');
      return;
    }

    try {
      await updateAsset(updatedFields, fields.serialNumber);
      toast.success(`${fields.serialNumber} Updated Successfully`);
      await refreshAssetList();
      handleClose();
    } catch (error) {
      console.error('Error updating Asset:', error);
      toast.error(`${fields.serialNumber} Update Failed`);
    }
  };

  return (
    <Modal
      open={open}
      onClose={handleClose}
      aria-labelledby="edit-asset-modal-title"
      aria-describedby="edit-asset-modal-description"
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
          onClick={handleClose}
          sx={{ position: 'absolute', top: 8, right: 8 }}
        >
          <CloseIcon />
        </IconButton>

        <h2 id="edit-asset-modal-title" style={{ textAlign: 'center', color: '#083A40' }}>
          {viewOnly ? 'View Asset' : 'Edit Asset'}
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
          <TextField
            label="Asset Name"
            value={fields.assetName}
            onChange={handleChange('assetName')}
            fullWidth
            disabled={viewOnly}
          />
          <TextField
            label="Serial Number"
            value={fields.serialNumber}
            onChange={handleChange('serialNumber')}
            fullWidth
            disabled={viewOnly}
          />
          <TextField
            label="Location"
            value={fields.location}
            onChange={handleChange('location')}
            fullWidth
            disabled={viewOnly}
          />
          <TextField
            label="Location Code"
            value={fields.locCode}
            onChange={handleChange('locCode')}
            fullWidth
            disabled={viewOnly}
          />
          <TextField
            label="Operating System"
            value={fields.operatingSystem}
            onChange={handleChange('operatingSystem')}
            fullWidth
            disabled={viewOnly}
          />
          <TextField
            label="Variant"
            value={fields.modelName}
            onChange={handleChange('modelName')}
            fullWidth
            disabled={viewOnly}
          />

          <FormControl fullWidth disabled={viewOnly}>
            <InputLabel id="type-label">Type</InputLabel>
            <Select
              labelId="type-label"
              value={fields.type}
              label="Type"
              onChange={handleChange('type')}
            >
              <MenuItem value="LAPTOP">LAPTOP</MenuItem>
              <MenuItem value="MOUSE">MOUSE</MenuItem>
              <MenuItem value="BAG">BAG</MenuItem>
              <MenuItem value="TSHIRT">TSHIRT</MenuItem>
              <MenuItem value="BOTTLE">BOTTLE</MenuItem>
              <MenuItem value="HEADFONE">HEADFONE</MenuItem>
              <MenuItem value="Note">Note</MenuItem>
            </Select>
          </FormControl>

          <FormControl fullWidth disabled={viewOnly}>
            <InputLabel id="status-label">Status</InputLabel>
            <Select
              labelId="status-label"
              value={fields.status}
              label="Status"
              onChange={handleChange('status')}
            >
              <MenuItem value="Assigned">Assigned</MenuItem>
              <MenuItem value="UnAssigned">UnAssigned</MenuItem>
              <MenuItem value="Scrap">Scrap</MenuItem>
            </Select>
          </FormControl>

          {isAssigned && (
            <TextField
              label="Assigned To (Emp ID)"
              value={fields.empId}
              onChange={handleChange('empId')}
              fullWidth
              disabled={viewOnly}
            />
          )}

          <TextField
            label="Purchase Date"
            type="date"
            value={fields.purchaseDate}
            onChange={handleChange('purchaseDate')}
            InputLabelProps={{ shrink: true }}
            fullWidth
            disabled={viewOnly}
          />
          <TextField
            label="Warranty Date"
            type="date"
            value={fields.warrantyDate}
            onChange={handleChange('warrantyDate')}
            InputLabelProps={{ shrink: true }}
            fullWidth
            disabled={viewOnly}
          />

          <TextField
            label="Added By"
            value={fields.addedBy}
            onChange={handleChange('addedBy')}
            fullWidth
            disabled={viewOnly}
          />

          {(isAssigned || isScrap) && (
            <TextField
              label={isScrap ? 'Scraped By' : 'Assigned By'}
              value={fields.assignedBy}
              onChange={handleChange('assignedBy')}
              fullWidth
              disabled={viewOnly}
            />
          )}

          {(isAssigned || isScrap) && (
            <TextField
              label={isScrap ? 'Scraped Date' : 'Assigned Date'}
              type="date"
              value={fields.assignedDate}
              onChange={handleChange('assignedDate')}
              InputLabelProps={{ shrink: true }}
              fullWidth
              disabled={viewOnly}
            />
          )}

          {isAssigned && (
            <TextField
              label="Return Date"
              type="date"
              value={fields.returnDate}
              onChange={handleChange('returnDate')}
              InputLabelProps={{ shrink: true }}
              fullWidth
              disabled={viewOnly}
            />
          )}

          <FormControl fullWidth disabled={viewOnly}>
            <InputLabel id="assert-sourced-by-label">Asset Sourced By</InputLabel>
            <Select
              labelId="assert-sourced-by-label"
              value={fields.assertSourcedBy === 'Verinite' ? 'Verinite' : 'Client Company'}
              label="Asset Sourced By"
              onChange={handleAssertSourcedByChange}
            >
              <MenuItem value="Verinite">Verinite</MenuItem>
              <MenuItem value="Client Company">Client Company</MenuItem>
            </Select>
          </FormControl>

          {fields.assertSourcedBy !== 'Verinite' && (
            <TextField
              label="Enter Client Company Name"
              value={fields.assertSourcedBy}
              onChange={handleClientCompanyNameChange}
              fullWidth
              disabled={viewOnly}
            />
          )}
        </Box>

        {!viewOnly && (
          <Box sx={{ display: 'flex', justifyContent: 'flex-end', mt: 3 }}>
            <Button
              variant="contained"
              onClick={handleClose}
              sx={{ backgroundColor: 'error.main', color: 'error.contrastText', mr: 2 }}
            >
              Cancel
            </Button>
            <Button
              variant="contained"
              onClick={handleUpdateAsset}
              sx={{ backgroundColor: 'success.main', color: 'success.contrastText' }}
            >
              Update
            </Button>
          </Box>
        )}
      </Box>
    </Modal>
  );
}

export default EditAssetModal;
