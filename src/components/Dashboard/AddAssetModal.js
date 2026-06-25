import React, { useState, useEffect, useRef } from "react";
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

  const [touched, setTouched] = useState({
    assetName: false,
    serialNumber: false,
    location: false,
    type: false,
    purchaseDate: false,
    warrantyDate: false,
    assetSourcedBy: false,
  });

  const fieldRefs = {
    assetName: useRef(null),
    serialNumber: useRef(null),
    location: useRef(null),
    type: useRef(null),
    purchaseDate: useRef(null),
    warrantyDate: useRef(null),
    assetSourcedBy: useRef(null),
  };

  const errorStyle = (visible) => ({
    color: "red",
    fontSize: "0.8rem",
    minHeight: "16px",
    marginBottom: "2px",
    visibility: visible ? "visible" : "hidden",
  });
  const blinkClass = (condition) => (condition ? "error-blink" : "");

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
  const [shakeForm, setShakeForm] = useState(false);

  const handleAddAsset = async () => {
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
    let firstInvalidKey = null;

    Object.entries(requiredFields).forEach(([key, value]) => {
      if (!value) {
        newTouched[key] = true;
        if (!firstInvalidKey) firstInvalidKey = key;
        hasError = true;
      }
    });
    setTouched(newTouched);

    if (hasError) {
      setShakeForm(true);
      setTimeout(() => setShakeForm(false), 500);
      toast.error("Please fill all required fields!");

      const firstInvalidRef = fieldRefs[firstInvalidKey]?.current;
      if (firstInvalidRef) {
        firstInvalidRef.scrollIntoView({ behavior: "smooth", block: "center" });
      }
      return;
    }

    const formatType = (value) => {
  if (!value) return value; 
  return value.charAt(0).toUpperCase() + value.slice(1).toLowerCase();
};

    const finalType = type === "__custom__" ? customType : type;

    const newAsset = {
      assetName:formatType(assetName),
      serialNumber:formatType(serialNumber),
      location:formatType(location),
      status,
      type: formatType(finalType),
      operatingSystem: operatingSystem.trim() || "Nill",
      modelName: modelName.trim() || "Nill",
      purchaseDate,
      warrantyDate,
      addedBy,
      assetSourcedBy:formatType(assetSourcedBy),
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
    setTouched({
      assetName: false,
      serialNumber: false,
      location: false,
      type: false,
      purchaseDate: false,
      warrantyDate: false,
      assetSourcedBy: false,
    });
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

          scrollbarWidth: "none",
          "&::-webkit-scrollbar": {
            display: "none",
          },
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
          className={shakeForm ? "form-shake" : ""}
          sx={{
            display: "grid",
            gridTemplateColumns: { xs: "1fr", sm: "1fr 1fr" },
            gap: 2,
          }}
        >
          {/* Asset Name */}
          <Box
            sx={{ display: "flex", flexDirection: "column" }}
            ref={fieldRefs.assetName}
          >
            <span style={errorStyle(!assetName && touched.assetName)}>
              This field is required *
            </span>
            <TextField
              label="Asset Name"
              value={assetName}
              onChange={(e) => setAssetName(e.target.value)}
              onBlur={() =>
                setTouched((prev) => ({ ...prev, assetName: true }))
              }
              fullWidth
              required
              className={blinkClass(!assetName && touched.assetName)}
            />
          </Box>

          {/* Serial Number */}
          <Box
            sx={{ display: "flex", flexDirection: "column" }}
            ref={fieldRefs.serialNumber}
          >
            <span style={errorStyle(!serialNumber && touched.serialNumber)}>
              This field is required *
            </span>
            <TextField
              label="Serial Number"
              value={serialNumber}
              onChange={(e) => setSerialNumber(e.target.value)}
              onBlur={() =>
                setTouched((prev) => ({ ...prev, serialNumber: true }))
              }
              fullWidth
              required
              className={blinkClass(!serialNumber && touched.serialNumber)}
            />
          </Box>

          {/* Location */}
          <Box
            sx={{ display: "flex", flexDirection: "column" }}
            ref={fieldRefs.location}
          >
            <span style={errorStyle(!location && touched.location)}>
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
              onInputChange={(event, newInputValue) => {
                setLocation(newInputValue || "");
              }}
              onBlur={() => {
                setTouched((prev) => ({ ...prev, location: true }));
                commitNewOption(location, locationOptions, setLocationOptions);
              }}
              renderInput={(params) => (
                <TextField
                  {...params}
                  label="Location"
                  fullWidth
                  required
                  placeholder="Enter The Location If not in Below List"
                  className={blinkClass(!location && touched.location)}
                />
              )}
            />
          </Box>

          {/* Asset Type */}
          <Box
            sx={{ display: "flex", flexDirection: "column" }}
            ref={fieldRefs.type}
          >
            <span style={errorStyle(!type && !customType && touched.type)}>
              This field is required *
            </span>
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
                  required
                  placeholder="Enter The Type If not in Below List"
                  className={blinkClass(!type && touched.type)}
                />
              )}
            />
          </Box>

          {/* Operating System - Optional */}
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
          <Box
            sx={{ display: "flex", flexDirection: "column" }}
            ref={fieldRefs.purchaseDate}
          >
            <span style={errorStyle(!purchaseDate && touched.purchaseDate)}>
              This field is required *
            </span>
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
              required
              className={blinkClass(!purchaseDate && touched.purchaseDate)}
            />
          </Box>

          {/* Warranty Date */}
          <Box
            sx={{ display: "flex", flexDirection: "column" }}
            ref={fieldRefs.warrantyDate}
          >
            <span style={errorStyle(!warrantyDate && touched.warrantyDate)}>
              This field is required *
            </span>
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
              required
              className={blinkClass(!warrantyDate && touched.warrantyDate)}
            />
          </Box>

          {/* Asset Sourced By */}
          <Box
            sx={{ display: "flex", flexDirection: "column" }}
            ref={fieldRefs.assetSourcedBy}
          >
            <span style={errorStyle(!assetSourcedBy && touched.assetSourcedBy)}>
              This field is required *
            </span>
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
              onBlur={() => {
                setTouched((prev) => ({ ...prev, assetSourcedBy: true }));
                commitNewOption(
                  assetSourcedBy,
                  assetSourceOptions,
                  setAssetSourceOptions
                );
              }}
              renderInput={(params) => (
                <TextField
                  {...params}
                  label="Asset Sourced By"
                  fullWidth
                  required
                  placeholder="Enter The SourcedBy If not in Below List"
                  className={blinkClass(
                    !assetSourcedBy && touched.assetSourcedBy
                  )}
                />
              )}
            />
          </Box>
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
