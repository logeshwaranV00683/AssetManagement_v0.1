import React, { useState } from "react";
import Modal from "@mui/material/Modal";
import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import Select from "@mui/material/Select";
import MenuItem from "@mui/material/MenuItem";
import FormControl from "@mui/material/FormControl";
import InputLabel from "@mui/material/InputLabel";
import IconButton from "@mui/material/IconButton";
import CloseIcon from "@mui/icons-material/Close";
import { saveAsset } from "../Services/AssetService";
import { toast } from "react-hot-toast";

function AddAssetModal({ open, handleClose, refreshAssetList }) {
  const user = JSON.parse(localStorage.getItem("user"));
  const [isAdding, setIsAdding] = useState(false);

  const [assetName, setAssetName] = useState("");
  const [serialNumber, setSerialNumber] = useState("");
  const [location, setLocation] = useState("");
  const [locCode, setLocCode] = useState("0");
  const [operatingSystem, setOperatingSystem] = useState("");
  const [modelName, setModelName] = useState("");
  const [purchaseDate, setPurchaseDate] = useState("");
  const [warrantyDate, setWarrantyDate] = useState("");
  const [addedBy, setAddedBy] = useState(user.empId);
  const [status, setStatus] = useState("Unassigned");
  const [type, setType] = useState("");
  const [assetSourcedBy, setAssetSourcedBy] = useState("");

  const handleAddAsset = async () => {
    const newAsset = {
      assetName,
      serialNumber,
      location,
      locCode,
      status,
      type,
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
      console.log("Asset added:", newAsset);
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
        toast.error(`Adding ${newAsset.serialNumber} Asset Failed`);
      }
    } finally {
      setIsAdding(false);
    }
  };

  const resetForm = () => {
    setAssetName("");
    setSerialNumber("");
    setLocation("");
    setStatus("Unassigned");
    setLocCode("");
    setOperatingSystem("");
    setModelName("");
    setType("");
    setPurchaseDate("");
    setWarrantyDate("");
    setAddedBy(user.empId);
    setAssetSourcedBy("");
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
          maxHeight: "80%",
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
          <TextField
            labelId="asset-name-label"
            id="asset-name"
            value={assetName}
            label="Asset Name"
            onChange={(e) => setAssetName(e.target.value)}
            fullWidth
          />

          <TextField
            label="Serial Number"
            value={serialNumber}
            onChange={(e) => setSerialNumber(e.target.value)}
            fullWidth
          />
          <FormControl fullWidth>
            <InputLabel id="location">Location</InputLabel>
            <Select
              labelId="location"
              id="location-name"
              value={location}
              label="Location Code"
              onChange={(e) => setLocation(e.target.value)}
            >
              <MenuItem value="Chennai">Chennai</MenuItem>
              <MenuItem value="Pune">Pune</MenuItem>
            </Select>
          </FormControl>
          <TextField
            label="Operating System"
            value={operatingSystem}
            onChange={(e) => setOperatingSystem(e.target.value)}
            fullWidth
          />
          <TextField
            label="Model Name"
            value={modelName}
            onChange={(e) => setModelName(e.target.value)}
            fullWidth
          />

          <FormControl fullWidth>
            <InputLabel id="Type">Type</InputLabel>
            <Select
              labelId="Type"
              id="Type"
              label="Type"
              value={type}
              onChange={(e) => setType(e.target.value)}
            >
              <MenuItem value="bag">Bag</MenuItem>
              <MenuItem value="camera">Camera</MenuItem>
              <MenuItem value="data_card">Data card</MenuItem>
              <MenuItem value="dvr">DVR</MenuItem>
              <MenuItem value="fire_wall">Fire wall</MenuItem>
              <MenuItem value="head_phones">Head phones</MenuItem>
              <MenuItem value="laptop_charger">Laptop charger</MenuItem>
              <MenuItem value="Laptop">Laptop</MenuItem>
              <MenuItem value="mobile">Mobile</MenuItem>
              <MenuItem value="mouse">Mouse</MenuItem>
              <MenuItem value="projector">Projector</MenuItem>
              <MenuItem value="speaker">Speaker</MenuItem>
              <MenuItem value="switch">Switch</MenuItem>
            </Select>
          </FormControl>

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
            <InputLabel id="asset-sourced-by-label">
              Asset Sourced By
            </InputLabel>
            <Select
              labelId="asset-sourced-by-label"
              id="asset-sourced-by"
              value={
                ["Verinite", "Client Company", ""].includes(assetSourcedBy)
                  ? assetSourcedBy
                  : "Client Company"
              }
              label="Asset Sourced By"
              onChange={(e) => {
                const value = e.target.value;
                if (value === "Verinite") {
                  setAssetSourcedBy("Verinite");
                } else {
                  setAssetSourcedBy("");
                }
              }}
            >
              <MenuItem value="Verinite">Verinite</MenuItem>
              <MenuItem value="Client Company">Client Company</MenuItem>
            </Select>
          </FormControl>

          {assetSourcedBy !== "Verinite" && (
            <TextField
              label="Enter Client Company Name"
              value={assetSourcedBy}
              onChange={(e) => setAssetSourcedBy(e.target.value)}
              fullWidth
            />
          )}
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
