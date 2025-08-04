import React, { useState, useEffect } from "react";
import {
  getAssetTypes,
  saveAsset,
  getassetsourcedby,
} from "../Services/AssetService";
import { getLocations } from "../Services/DashboardService";
import Modal from "@mui/material/Modal";
import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import IconButton from "@mui/material/IconButton";
import CloseIcon from "@mui/icons-material/Close";
import Autocomplete from "@mui/material/Autocomplete";
import { toast } from "react-hot-toast";

function AddAssetModal({ open, handleClose, refreshAssetList }) {
  const user = JSON.parse(localStorage.getItem("user"));
  const [isAdding, setIsAdding] = useState(false);

  const [assetName, setAssetName] = useState("");
  const [serialNumber, setSerialNumber] = useState("");
  const [operatingSystem, setOperatingSystem] = useState("");
  const [modelName, setModelName] = useState("");
  const [purchaseDate, setPurchaseDate] = useState("");
  const [warrantyDate, setWarrantyDate] = useState("");
  const [addedBy] = useState(user.empId);
  const [status] = useState("Unassigned");

  const [type, setType] = useState("");
  const [customType, setCustomType] = useState("");
  const [typeOptions, setTypeOptions] = useState([]);

  const [location, setLocation] = useState("");
  const [locationOptions, setLocationOptions] = useState([]);

  const [assetSourcedBy, setAssetSourcedBy] = useState("");
  const [assetSourceOptions, setAssetSourceOptions] = useState([]);

  useEffect(() => {
    if (open) {
      fetchAssetTypes();
      fetchLocations();
      fetchAssetSourcedBy();
    }
  }, [open]);

  const fetchAssetTypes = async () => {
    try {
      const types = await getAssetTypes();
      setTypeOptions(types || []);
    } catch (err) {
      console.error("Failed to fetch asset types:", err);
    }
  };

  const fetchLocations = async () => {
    try {
      const locations = await getLocations();
      setLocationOptions(locations || []);
    } catch (err) {
      console.error("Failed to fetch locations:", err);
    }
  };

  const fetchAssetSourcedBy = async () => {
    try {
      const sourcedBy = await getassetsourcedby();
      setAssetSourceOptions(sourcedBy || []);
    } catch (err) {
      console.error("Failed to fetch Asset Sourced By:", err);
    }
  };

  const commitNewOption = (value, options, setOptions) => {
    if (value && !options.includes(value)) {
      setOptions((prev) => [...prev, value]);
    }
  };

  const handleAddAsset = async () => {
    const finalType = type === "__custom__" ? customType : type;

    const newAsset = {
      assetName,
      serialNumber,
      location,
      status,
      type: finalType,
      operatingSystem,
      modelName,
      purchaseDate,
      warrantyDate,
      addedBy,
      assetSourcedBy,
    };

    console.log("Asset added:", newAsset);
    setIsAdding(true);

    try {
      await saveAsset(newAsset);
      refreshAssetList();
      toast.success(`${newAsset.serialNumber} Asset Added Successfully`);
      handleCloseModal();
    } catch (error) {
      console.error("Error adding Asset:", error);

      if (error.status === 400 && typeof error.data === "object") {
        Object.entries(error.data).forEach(([field, message]) => {
          toast.error(`${field}: ${message}`);
        });
      } else if (error.status === 409) {
        toast.error(
          error.data || `Asset ${newAsset.serialNumber} already exists`
        );
      } else {
        toast.error(
          `Adding ${newAsset.serialNumber} failed: Warranty Date must not be before Purchase Date or Serial Number already exists`
        );
      }
    } finally {
      setIsAdding(false);
    }
  };

  const resetForm = () => {
    setAssetName("");
    setSerialNumber("");
    setOperatingSystem("");
    setModelName("");
    setType("");
    setCustomType("");
    setLocation("");
    setAssetSourcedBy("");
    setPurchaseDate("");
    setWarrantyDate("");
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
          backgroundColor: "background.paper",
          boxShadow: 24,
          p: 4,
          width: "60%",
          maxWidth: 700,
          maxHeight: "75%",
          overflowY: "auto",
          borderRadius: 4,
          position: "absolute",
          top: "50%",
          left: "50%",
          transform: "translate(-50%, -50%)",
        }}
      >
        <IconButton
          aria-label="close"
          onClick={handleCloseModal}
          sx={{ position: "absolute", top: 8, right: 8 }}
        >
          <CloseIcon />
        </IconButton>

        <h2
          id="add-asset-modal-title"
          style={{ textAlign: "center", color: "#083A40" }}
        >
          Add Asset
        </h2>

        <Box
          component="form"
          noValidate
          autoComplete="off"
          sx={{
            display: "grid",
            gridTemplateColumns: { xs: "1fr", sm: "1fr 1fr" },
            gap: 2,
          }}
        >
          {/* Asset Name */}
          <TextField
            label="Asset Name"
            value={assetName}
            onChange={(e) => setAssetName(e.target.value)}
            fullWidth
          />

          {/* Serial Number */}
          <TextField
            label="Serial Number"
            value={serialNumber}
            onChange={(e) => setSerialNumber(e.target.value)}
            fullWidth
          />

          {/* Location Autocomplete */}
          <Autocomplete
            freeSolo
            options={locationOptions}
            value={location}
            onChange={(event, newValue) => {
              setLocation(newValue || "");
              commitNewOption(newValue, locationOptions, setLocationOptions);
            }}
            onInputChange={(event, newInputValue) => {
              setLocation(newInputValue || "");
            }}
            onBlur={() =>
              commitNewOption(location, locationOptions, setLocationOptions)
            }
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

          {/* Asset Type Autocomplete */}
          <Autocomplete
            freeSolo
            options={typeOptions}
            value={customType || type}
            onChange={(event, newValue) => {
              if (newValue && !typeOptions.includes(newValue)) {
                setTypeOptions((prev) => [...prev, newValue]);
                setCustomType(newValue);
                setType("__custom__");
              } else {
                setType(newValue || "");
                setCustomType("");
              }
            }}
            onInputChange={(event, newInputValue) => {
              if (newInputValue && !typeOptions.includes(newInputValue)) {
                setCustomType(newInputValue);
                setType("__custom__");
              } else {
                setType(newInputValue);
                setCustomType("");
              }
            }}
            onBlur={() => {
              if (customType && !typeOptions.includes(customType)) {
                setTypeOptions((prev) => [...prev, customType]);
              }
            }}
            renderInput={(params) => (
              <TextField
                {...params}
                label="Asset Type"
                placeholder="Enter Asset Type (New if not in list)"
                variant="outlined"
                fullWidth
              />
            )}
          />

          {/* Operating System */}
          <TextField
            label="Operating System"
            value={operatingSystem}
            onChange={(e) => setOperatingSystem(e.target.value)}
            fullWidth
          />

          {/* Model Name */}
          <TextField
            label="Model Name"
            value={modelName}
            onChange={(e) => setModelName(e.target.value)}
            fullWidth
          />

          {/* Purchase Date */}
          <TextField
            label="Purchase Date"
            type="date"
            value={purchaseDate}
            onChange={(e) => setPurchaseDate(e.target.value)}
            InputLabelProps={{ shrink: true }}
            fullWidth
          />

          {/* Warranty Date */}
          <TextField
            label="Warranty Date"
            type="date"
            value={warrantyDate}
            onChange={(e) => setWarrantyDate(e.target.value)}
            InputLabelProps={{ shrink: true }}
            fullWidth
          />

          {/* Asset Sourced By Autocomplete */}
          <Autocomplete
            freeSolo
            options={assetSourceOptions}
            value={assetSourcedBy}
            onChange={(event, newValue) => {
              setAssetSourcedBy(newValue || "");
              commitNewOption(
                newValue,
                assetSourceOptions,
                setAssetSourceOptions
              );
            }}
            onInputChange={(event, newInputValue) => {
              setAssetSourcedBy(newInputValue || "");
            }}
            onBlur={() =>
              commitNewOption(
                assetSourcedBy,
                assetSourceOptions,
                setAssetSourceOptions
              )
            }
            renderInput={(params) => (
              <TextField
                {...params}
                label="Asset Sourced By"
                placeholder="Enter Client Company Name (New if not in list)"
                variant="outlined"
                fullWidth
              />
            )}
          />
        </Box>

        <Box
          sx={{
            display: "flex",
            justifyContent: "space-between",
            mt: 3,
          }}
        >
          <Button
            variant="contained"
            onClick={handleCloseModal}
            sx={{ backgroundColor: "error.main", color: "error.contrastText" }}
          >
            Cancel
          </Button>
          <Button
            variant="contained"
            onClick={handleAddAsset}
            disabled={isAdding}
            sx={{
              backgroundColor: "success.main",
              color: "success.contrastText",
            }}
          >
            {isAdding ? "Adding..." : "Add"}
          </Button>
        </Box>
      </Box>
    </Modal>
  );
}

export default AddAssetModal;
