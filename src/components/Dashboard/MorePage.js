import React, { useEffect, useState } from "react";
import { DataGrid } from "@mui/x-data-grid";
import { motion } from "framer-motion";
import { Box, Tooltip, IconButton } from "@mui/material";
import HistoryIcon from "@mui/icons-material/History";
import { toast } from "react-hot-toast";
import { fetchAssetHistory } from "../Services/HistoryServices";
import {
  RecentAssignedAssetDetails,
  getDeletedAssets,
} from "../Services/AssetService";
import AssetHistoryPopup from "./AssetHistoryPop";

const ToggleButton = ({ showDeleted, setShowDeleted }) => (
  <motion.div
    whileHover={{ scale: 1.1 }}
    whileTap={{ scale: 0.95 }}
    initial={{ opacity: 0, y: -10 }}
    animate={{ opacity: 1, y: 0 }}
    transition={{ type: "spring", stiffness: 200, damping: 12 }}
    onClick={() => setShowDeleted((prev) => !prev)}
    style={toggleButtonStyle}
  >
    {showDeleted ? "Show Assigned Assets" : "Show Deleted Assets"}
  </motion.div>
);

const RecentAssignedAssetPage = () => {
  const [assignedRows, setAssignedRows] = useState([]);
  const [deletedRows, setDeletedRows] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showDeleted, setShowDeleted] = useState(false);

  const [selectedAsset, setSelectedAsset] = useState(null);
  const [historyData, setHistoryData] = useState([]);
  const [openHistoryModal, setOpenHistoryModal] = useState(false);

  const handleClose = () => {
    setOpenHistoryModal(false);
  };

  const handleOpenHistoryModal = async (asset) => {
    try {
      const data = await fetchAssetHistory(asset.serialNo || asset.serialNumber);
      if (data && data.length > 0) {
        setSelectedAsset(asset);
        setHistoryData(data);
        setOpenHistoryModal(true);
      } else {
        toast.error("No history found for this asset.");
      }
    } catch (error) {
      toast.error("Failed to load asset history.");
    }
  };

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [assigned, deleted] = await Promise.all([
          RecentAssignedAssetDetails(),
          getDeletedAssets(),
        ]);

        setAssignedRows(
          (assigned || []).map((item, index) => ({
            id: index + 1,
            empId: item?.empId || "-",
            employeeName: `${item?.firstName || ""} ${item?.lastName || ""}`.trim(),
            serialNumber: item?.serialNumber || "-",
            assignedDate: item?.assignedDate || "-",
          }))
        );

        setDeletedRows(
          (deleted || []).map((item, index) => ({
            id: index + 1,
            serialNo: item?.serialNo || "-",
            assetName: item?.assetName || "-",
            purchaseDate: item?.purchaseDate || "-",
            deletedDate: item?.deletedDate || "-",
            type: item?.type || "-",
            location: item?.location || "-",
            assetSourcedBy: item?.assetSourcedBy || "-",
            deletedBy: item?.deletedBy || "-",
          }))
        );
      } catch (err) {
        setError(err.message || "Failed to fetch data");
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  const assignedColumns = [
    { field: "empId", headerName: "Employee ID", flex: 1 },
    { field: "employeeName", headerName: "Employee Name", flex: 1 },
    { field: "serialNumber", headerName: "Serial Number", flex: 1 },
    { field: "assignedDate", headerName: "Assigned Date", flex: 1 },
  ];

  const deletedColumns = [
    { field: "serialNo", headerName: "Serial Number", flex: 1 },
    { field: "assetName", headerName: "Asset Name", flex: 1 },
    { field: "purchaseDate", headerName: "Purchase Date", flex: 1 },
    { field: "deletedDate", headerName: "Deleted Date", flex: 1 },
    { field: "type", headerName: "Type", flex: 1 },
    { field: "location", headerName: "Location", flex: 1 },
    { field: "assetSourcedBy", headerName: "Sourced By", flex: 1 },
    { field: "deletedBy", headerName: "Deleted By", flex: 1 },
    {
      field: "actions",
      headerName: "Actions",
      minWidth: 150,
      flex: 2,
      sortable: false,
      renderCell: (params) => (
        <Box display="flex" justifyContent="center" alignItems="center" gap={1}>
          <Tooltip title="History">
            <IconButton
              sx={{
                transition: "transform 0.2s",
                "&:hover": {
                  transform: "scale(1.3)",
                  color: "warning.main",
                  filter: "drop-shadow(0 0 4px orange)",
                },
              }}
              onClick={() => handleOpenHistoryModal(params.row)}
            >
              <HistoryIcon />
            </IconButton>
          </Tooltip>
        </Box>
      ),
    },
  ];

  if (loading)
    return <h3 style={messageStyle}>Loading Recent Assigned & Deleted Assets...</h3>;
  if (error) return <h3 style={messageStyle}>Error: {error}</h3>;

  return (
    <div style={containerStyle}>
      <div style={{ width: "80%" }}>
        <div style={{ display: "flex", justifyContent: "flex-end", marginBottom: 12 }}>
          <ToggleButton showDeleted={showDeleted} setShowDeleted={setShowDeleted} />
        </div>

        <h2 style={titleStyle}>
          {showDeleted ? "Permanently Deleted Assets" : "Today Assigned Assets"}
        </h2>

        <div style={scrollWrapperStyle}>
          <style>{`div::-webkit-scrollbar { display: none; }`}</style>
          <DataGrid
            rows={showDeleted ? deletedRows : assignedRows}
            columns={showDeleted ? deletedColumns : assignedColumns}
            pageSize={5}
            rowsPerPageOptions={[5]}
            disableSelectionOnClick
            sx={dataGridStyles}
          />
          <AssetHistoryPopup
            open={openHistoryModal}
            onClose={handleClose}
            asset={selectedAsset}
            history={historyData}
          />
        </div>
      </div>
    </div>
  );
};

// Styles
const containerStyle = {
  flex: 1,
  padding: 20,
  display: "flex",
  flexDirection: "column",
  alignItems: "center",
};

const titleStyle = {
  marginBottom: 12,
  fontFamily: "Arial, sans-serif",
  color: "#0d9ee6ff",
};

const messageStyle = {
  fontFamily: "Arial, sans-serif",
  color: "#0a7cc3",
  textAlign: "center",
};

const scrollWrapperStyle = {
  height: 480,
  overflowY: "scroll",
  scrollbarWidth: "none",
  msOverflowStyle: "none",
};

const toggleButtonStyle = {
  background: "#0a7cc3",
  color: "#fff",
  padding: "10px 16px",
  borderRadius: 8,
  fontSize: 13,
  fontFamily: "Arial, sans-serif",
  cursor: "pointer",
  textAlign: "center",
  boxShadow: "0px 4px 8px rgba(0,0,0,0.2)",
  userSelect: "none",
  whiteSpace: "nowrap",
  maxWidth: 180,
};

const dataGridStyles = {
  border: 2,
  borderRadius: 3,
  "& .MuiDataGrid-columnHeaders": {
    backgroundColor: "#E6F7FF",
    color: "#083A40",
    fontSize: 12,
    fontFamily: "Arial, sans-serif",
    minHeight: 40,
  },
  "& .MuiDataGrid-columnHeaderTitle": {
    color: "#083A40",
  },
  "& .MuiDataGrid-cell": {
    backgroundColor: "#E6F7FF",
    fontSize: 12,
    textAlign: "center",
    fontFamily: "Arial, sans-serif",
    minHeight: 35,
    lineHeight: "35px",
    color: "#083A40",
  },
  "& .MuiDataGrid-footerContainer": {
    backgroundColor: "#f5f5f5",
    minHeight: 40,
  },
};

export default RecentAssignedAssetPage;
