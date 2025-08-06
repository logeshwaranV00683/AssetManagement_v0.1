import React, { useState, useEffect } from "react";
import {
  getAssetTypes,
  updateAsset,
  getassetsourcedby,
} from "../Services/AssetService";
import { getLocations } from "../Services/DashboardService";
import {
  Modal,
  Box,
  TextField,
  Button,
  IconButton,
  Autocomplete,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
} from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import { toast } from "react-hot-toast";

function EditAssetModal({
  open,
  handleClose,
  asset,
  refreshAssetList,
  viewOnly,
}) {
  const user = JSON.parse(localStorage.getItem("user"));

  const [isUpdating, setIsUpdating] = useState(false);

  const [assetName, setAssetName] = useState("");
  const [serialNumber, setSerialNumber] = useState("");
  const [operatingSystem, setOperatingSystem] = useState("");
  const [modelName, setModelName] = useState("");
  const [purchaseDate, setPurchaseDate] = useState("");
  const [warrantyDate, setWarrantyDate] = useState("");
  const [status, setStatus] = useState("UnAssigned");
  const [addedBy] = useState(user.empId);

  const [type, setType] = useState("");
  const [customType, setCustomType] = useState("");
  const [typeOptions, setTypeOptions] = useState([]);

  const [location, setLocation] = useState("");
  const [locationOptions, setLocationOptions] = useState([]);

  const [assetSourcedBy, setAssetSourcedBy] = useState("");
  const [assetSourceOptions, setAssetSourceOptions] = useState([]);

  const [touched, setTouched] = useState({
    assetName: false,
    location: false,
    type: false,
    modelName: false,
    purchaseDate: false,
    warrantyDate: false,
    assetSourcedBy: false,
  });

  useEffect(() => {
    if (!open) return;

    const fetchData = async () => {
      try {
        const [types, locations, sourcedBy] = await Promise.all([
          getAssetTypes(),
          getLocations(),
          getassetsourcedby(),
        ]);
        setTypeOptions(types || []);
        setLocationOptions(locations || []);
        setAssetSourceOptions(sourcedBy || []);
      } catch (err) {
        console.error("Failed to fetch dropdown data:", err);
      }
    };

    fetchData();

    if (asset) {
      setAssetName(asset.assetName || "");
      setSerialNumber(asset.serialNumber || "");
      setOperatingSystem(asset.operatingSystem || "");
      setModelName(asset.modelName || "");
      setPurchaseDate(asset.purchaseDate || "");
      setWarrantyDate(asset.warrantyDate || "");
      setStatus(asset.status || "UnAssigned");
      setType(asset.type || "");
      setLocation(asset.location || "");
      setAssetSourcedBy(asset.assetSourcedBy || "");
    }

    setTouched({
      assetName: false,
      location: false,
      type: false,
      modelName: false,
      purchaseDate: false,
      warrantyDate: false,
      assetSourcedBy: false,
    });
  }, [open, asset]);

  const commitNewOption = (value, options, setOptions) => {
    if (value && !options.includes(value)) {
      setOptions((prev) => [...prev, value]);
    }
  };

  const handleUpdateAsset = async () => {
    if (!asset) return;

    const finalType = type === "__custom__" ? customType : type;

    const updatedFields = {
      assetName,
      serialNumber,
      operatingSystem,
      modelName,
      purchaseDate,
      warrantyDate,
      status,
      type: finalType,
      location,
      assetSourcedBy,
      addedBy,
    };

    const changedFields = {};
    Object.entries(updatedFields).forEach(([key, value]) => {
      if (value !== (asset[key] || "")) changedFields[key] = value;
    });

    if (Object.keys(changedFields).length === 0) {
      toast.error("No changes detected.");
      return;
    }

    setIsUpdating(true);
    try {
      await updateAsset(asset.serialNumber, changedFields);
      refreshAssetList();
      toast.success(`${serialNumber} Asset Updated Successfully`);
      handleClose();
    } catch (error) {
      console.error("Error updating Asset:", error);

      if (error.status === 400 && typeof error.data === "object") {
        Object.entries(error.data).forEach(([field, message]) =>
          toast.error(`${field}: ${message}`)
        );
      } else if (error.status === 409) {
        toast.error(error.data || `Asset ${serialNumber} already exists`);
      } else {
        toast.error(
          `Updating ${serialNumber} failed: Warranty Date must not be before Purchase Date or Serial Number already exists`
        );
      }
    } finally {
      setIsUpdating(false);
    }
  };

  return (
    <Modal open={open} onClose={handleClose}>
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
          scrollbarWidth: "none",
          "&::-webkit-scrollbar": { display: "none" },
        }}
      >
        {/* Close Button */}
        <IconButton
          aria-label="close"
          onClick={handleClose}
          sx={{ position: "absolute", top: 8, right: 8 }}
        >
          <CloseIcon />
        </IconButton>

        <h2 style={{ textAlign: "center", color: "#083A40" }}>
          {viewOnly ? "View Asset" : "Edit Asset"}
        </h2>

        {/* Form */}
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
          <Box sx={{ display: "flex", flexDirection: "column" }}>
            {touched.assetName && !assetName && (
              <span style={{ color: "red", fontSize: "0.8rem" }}>
                This field is required *
              </span>
            )}
            <TextField
              label="Asset Name"
              value={assetName}
              onChange={(e) => setAssetName(e.target.value)}
              onBlur={() =>
                setTouched((prev) => ({ ...prev, assetName: true }))
              }
              fullWidth
              disabled={viewOnly}
            />
          </Box>

          {/* Serial Number (Read-only) */}
          <TextField
            label="Serial Number"
            value={serialNumber}
            fullWidth
            disabled
          />

          {/* Location */}
          <Box sx={{ display: "flex", flexDirection: "column" }}>
            {touched.location && !location && (
              <span style={{ color: "red", fontSize: "0.8rem" }}>
                This field is required *
              </span>
            )}
            <Autocomplete
              freeSolo
              options={locationOptions}
              value={location}
              onChange={(event, newValue) => {
                setLocation(newValue || "");
                commitNewOption(newValue, locationOptions, setLocationOptions);
              }}
              onInputChange={(event, newInputValue) =>
                setLocation(newInputValue || "")
              }
              onBlur={() => setTouched((prev) => ({ ...prev, location: true }))}
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
          <FormControl fullWidth disabled={viewOnly || status === "Assigned"}>
            <InputLabel id="status-label">Status</InputLabel>
            <Select
              labelId="status-label"
              value={status}
              onChange={(e) => setStatus(e.target.value)}
              label="Status"
            >
              {status === "Assigned" && (
                <MenuItem value="Assigned">Assigned</MenuItem>
              )}
                  <MenuItem value="UnAssigned">UnAssigned</MenuItem>
                  <MenuItem value="Scrap">Scrap</MenuItem>
            </Select>
          </FormControl>

          {/* Asset Type */}
          <Box sx={{ display: "flex", flexDirection: "column" }}>
            {touched.type && !type && !customType && (
              <span style={{ color: "red", fontSize: "0.8rem" }}>
                This field is required *
              </span>
            )}
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
              onBlur={() => setTouched((prev) => ({ ...prev, type: true }))}
              renderInput={(params) => (
                <TextField
                  {...params}
                  label="Asset Type"
                  fullWidth
                  disabled={viewOnly}
                />
              )}
            />
          </Box>

          {/* Operating System */}
          <TextField
            label="Operating System"
            value={operatingSystem}
            onChange={(e) => setOperatingSystem(e.target.value)}
            fullWidth
            disabled={viewOnly}
          />

          {/* Model Name */}
          <TextField
            label="Model Name"
            value={modelName}
            onChange={(e) => setModelName(e.target.value)}
            onBlur={() => setTouched((prev) => ({ ...prev, modelName: true }))}
            fullWidth
            disabled={viewOnly}
          />

          {/* Added By */}
          <TextField label="Added By" value={addedBy} fullWidth disabled />

          {/* Purchase Date */}
          <Box sx={{ display: "flex", flexDirection: "column" }}>
            {touched.purchaseDate && !purchaseDate && (
              <span style={{ color: "red", fontSize: "0.8rem" }}>
                This field is required *
              </span>
            )}
            <TextField
              label="Purchase Date"
              type="date"
              value={purchaseDate}
              onChange={(e) => setPurchaseDate(e.target.value)}
              onBlur={() =>
                setTouched((prev) => ({ ...prev, purchaseDate: true }))
              }
              InputLabelProps={{ shrink: true }}
              fullWidth
              disabled={viewOnly}
            />
          </Box>

          {/* Warranty Date */}
          <Box sx={{ display: "flex", flexDirection: "column" }}>
            {touched.warrantyDate && !warrantyDate && (
              <span style={{ color: "red", fontSize: "0.8rem" }}>
                This field is required *
              </span>
            )}
            <TextField
              label="Warranty Date"
              type="date"
              value={warrantyDate}
              onChange={(e) => setWarrantyDate(e.target.value)}
              onBlur={() =>
                setTouched((prev) => ({ ...prev, warrantyDate: true }))
              }
              InputLabelProps={{ shrink: true }}
              fullWidth
              disabled={viewOnly}
            />
          </Box>

          {/* Asset Sourced By */}
          <Box sx={{ display: "flex", flexDirection: "column" }}>
            {touched.assetSourcedBy && !assetSourcedBy && (
              <span style={{ color: "red", fontSize: "0.8rem" }}>
                This field is required *
              </span>
            )}
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
              onInputChange={(event, newInputValue) =>
                setAssetSourcedBy(newInputValue || "")
              }
              onBlur={() =>
                setTouched((prev) => ({ ...prev, assetSourcedBy: true }))
              }
              renderInput={(params) => (
                <TextField
                  {...params}
                  label="Asset Sourced By"
                  fullWidth
                  disabled={viewOnly}
                />
              )}
            />
          </Box>
        </Box>

        {/* Action Buttons */}
        {!viewOnly && (
          <Box sx={{ display: "flex", justifyContent: "space-between", mt: 3 }}>
            <Button
              variant="contained"
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
              onClick={handleUpdateAsset}
              disabled={isUpdating}
              sx={{
                backgroundColor: "success.main",
                color: "success.contrastText",
              }}
            >
              {isUpdating ? "Updating..." : "Update"}
            </Button>
          </Box>
        )}
      </Box>
    </Modal>
  );
}

export default EditAssetModal;
