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
  const [status, setStatus] = useState("");
  const [assignedDate, setAssignedDate] = useState("");
  const [addedBy] = useState(user.empId);
  const [assignedBy, setAssignedBy] = useState("");
  const [empId, setEmpId] = useState("");
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
  const blinkClass = (condition) => (condition ? "error-blink" : "");
  const errorStyle = (show) => ({
    color: show ? "red" : "transparent",
    fontSize: "0.8rem",
    minHeight: "16px",
    marginBottom: "2px",
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
      setAssignedDate(asset.assignedDate || "");
      setType(asset.type || "");
      setLocation(asset.location || "");
      setAssetSourcedBy(asset.assetSourcedBy || "");
      setAssignedBy(asset.assignedBy || "");
      setEmpId(asset.empId || "");
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
  const [shakeForm, setShakeForm] = useState(false);

  const handleUpdateAsset = async () => {
    const requiredFields = {
      assetName,
      serialNumber,
      location,
      type: type || customType,
      purchaseDate,
      warrantyDate,
      assetSourcedBy,
    };

    const newTouched = { ...touched };
    let hasError = false;
    let firstInvalidElement = null;

    Object.entries(requiredFields).forEach(([key, value]) => {
      if (!value) {
        newTouched[key] = true;
        hasError = true;

        if (!firstInvalidElement) {
          firstInvalidElement = document.querySelector(`[name="${key}"]`);
        }
      }
    });
    setTouched(newTouched);

    if (hasError) {
      setShakeForm(true);
      setTimeout(() => setShakeForm(false), 500);

      if (firstInvalidElement) {
        firstInvalidElement.scrollIntoView({
          behavior: "smooth",
          block: "center",
        });
      }

      return;
    }

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
      assignedBy,
      empId,
      assignedDate,
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
      await updateAsset(changedFields, asset.serialNumber);
      refreshAssetList();
      toast.success(`${serialNumber} Asset Updated Successfully`);
      handleClose();
    } catch (error) {
      console.error("Error updating Asset:", error);

      if (error.status === 400 && typeof error.data === "object") {
        Object.entries(error.data).forEach(([field, message]) =>
          toast.error(`${field}: ${message}`)
        );
      } else if (error.status === 406) {
        toast.error(error.data || `Asset ${serialNumber} already exists`);
      } else {
        toast.error(" Unexpected error occurred while updating.");
      }
    } finally {
      setIsUpdating(false);
    }
  };

  const isAssigned = status === "Assigned";
  const isScrap = status === "Scrap";

  return (
    <Modal open={open} onClose={handleClose}>
      <Box
        sx={{
          backgroundColor: "background.paper",
          boxShadow: 24,
          p: 4,
          width: "70%",
          maxWidth: 800,
          maxHeight: "80%",
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

        <Box
          component="form"
          noValidate
          autoComplete="off"
          className={shakeForm ? "form-shake" : ""}
          sx={{
            display: "grid",
            gridTemplateColumns: { xs: "1fr", sm: "1fr 1fr" },
            gap: 2,
            alignItems: "center",
          }}
        >
          {/* Asset Name */}
          <Box sx={{ display: "flex", flexDirection: "column" }}>
            <span style={errorStyle(touched.assetName && !assetName)}>
              This field is required *
            </span>
            <TextField
              name="assetName"
              label="Asset Name"
              value={assetName}
              onChange={(e) => setAssetName(e.target.value)}
              onBlur={() =>
                setTouched((prev) => ({ ...prev, assetName: true }))
              }
              fullWidth
              disabled={viewOnly}
              className={blinkClass(!assetName && touched.assetName)}
            />
          </Box>

          {/* Serial Number */}
          <Box sx={{ display: "flex", flexDirection: "column" }}>
            <span style={errorStyle(false)}> </span>
            <TextField
              name="serialNumber"
              label="Serial Number"
              value={serialNumber}
              fullWidth
              disabled
            />
          </Box>

          {/* Location */}
          <Box sx={{ display: "flex", flexDirection: "column" }}>
            <span style={errorStyle(touched.location && !location)}>
              This field is required *
            </span>
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
                  name="location"
                  label="Location"
                  fullWidth
                  className={blinkClass(!location && touched.location)}
                />
              )}
              disabled={viewOnly}
            />
          </Box>

          {/* Status */}
          <Box sx={{ display: "flex", flexDirection: "column" }}>
            <span style={errorStyle(false)}> </span>
            <FormControl fullWidth disabled={viewOnly || status === "Assigned"}>
              <InputLabel id="status-label">Status</InputLabel>
              <Select
                labelId="status-label"
                value={status}
                onChange={(e) => {
                  const newStatus = e.target.value;
                  setStatus(newStatus);
                  if (newStatus === "Scrap") {
                    if (!assignedDate && !assignedBy) {
                      setAssignedDate(new Date().toISOString().split("T")[0]);
                      setAssignedBy(user.empId);
                    }
                  }
                }}
                label="Status"
              >
                {status === "Assigned" && (
                  <MenuItem value="Assigned">Assigned</MenuItem>
                )}
                <MenuItem value="UnAssigned">UnAssigned</MenuItem>
                <MenuItem value="Scrap">Scrap</MenuItem>
              </Select>
            </FormControl>
          </Box>

          {/* Assigned By */}
          {(isAssigned || isScrap) && (
            <Box sx={{ display: "flex", flexDirection: "column" }}>
              <span style={errorStyle(false)}> </span>
              <TextField
                label={isScrap ? "Scraped By" : "Assigned By"}
                value={assignedBy}
                fullWidth
                disabled
              />
            </Box>
          )}

          {/* Assigned To */}
          {isAssigned && (
            <Box sx={{ display: "flex", flexDirection: "column" }}>
              <span style={errorStyle(false)}> </span>
              <TextField
                label="Assigned To (Emp ID)"
                value={empId}
                fullWidth
                disabled
              />
            </Box>
          )}

          {/* Asset Type */}
          <Box sx={{ display: "flex", flexDirection: "column" }}>
            <span style={errorStyle(touched.type && !type && !customType)}>
              This field is required *
            </span>
            <Autocomplete
              disabled={viewOnly}
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
                  name="type"
                  label="Asset Type"
                  fullWidth
                  className={blinkClass(!type && touched.type)}
                />
              )}
            />
          </Box>

          {/* Assigned Date */}
          {(isAssigned || isScrap) && (
            <Box sx={{ display: "flex", flexDirection: "column" }}>
              <span style={errorStyle(false)}> </span>
              <TextField
                label={isScrap ? "Scraped Date" : "Assigned Date"}
                type="date"
                value={assignedDate}
                onChange={(e) => setAssignedDate(e.target.value)}
                InputLabelProps={{ shrink: true }}
                fullWidth
                disabled={true}
              />
            </Box>
          )}
          {/* Operating System */}
          <Box sx={{ display: "flex", flexDirection: "column" }}>
            <span style={errorStyle(false)}> </span>
            <TextField
              label="Operating System"
              value={operatingSystem}
              onChange={(e) => setOperatingSystem(e.target.value)}
              fullWidth
              disabled={viewOnly}
            />
          </Box>

          {/* Model Name */}
          <Box sx={{ display: "flex", flexDirection: "column" }}>
            <span style={errorStyle(false)}> </span>
            <TextField
              label="Model Name"
              value={modelName}
              onChange={(e) => setModelName(e.target.value)}
              onBlur={() =>
                setTouched((prev) => ({ ...prev, modelName: true }))
              }
              fullWidth
              disabled={viewOnly}
            />
          </Box>

          {/* Added By */}
          <Box sx={{ display: "flex", flexDirection: "column" }}>
            <span style={errorStyle(false)}> </span>
            <TextField label="Added By" value={addedBy} fullWidth disabled />
          </Box>

          {/* Purchase Date */}
          <Box sx={{ display: "flex", flexDirection: "column" }}>
            <span style={errorStyle(touched.purchaseDate && !purchaseDate)}>
              This field is required *
            </span>
            <TextField
              name="purchaseDate"
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
              className={blinkClass(!purchaseDate && touched.purchaseDate)}
            />
          </Box>

          {/* Warranty Date */}
          <Box sx={{ display: "flex", flexDirection: "column" }}>
            <span style={errorStyle(touched.warrantyDate && !warrantyDate)}>
              This field is required *
            </span>
            <TextField
              name="warrantyDate"
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
              className={blinkClass(!warrantyDate && touched.warrantyDate)}
            />
          </Box>

          {/* Asset Sourced By */}
          <Box sx={{ display: "flex", flexDirection: "column" }}>
            <span style={errorStyle(touched.assetSourcedBy && !assetSourcedBy)}>
              This field is required *
            </span>
            <Autocomplete
              freeSolo
              disabled={viewOnly}
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
                  name="assetSourcedBy"
                  label="Asset Sourced By"
                  fullWidth
                  className={blinkClass(
                    !assetSourcedBy && touched.assetSourcedBy
                  )}
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
