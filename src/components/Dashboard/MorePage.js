import React, { useEffect, useState } from "react";
import { DataGrid } from "@mui/x-data-grid";
import { motion } from "framer-motion";
import { Box, Tooltip, IconButton } from "@mui/material";
import HistoryIcon from "@mui/icons-material/History";
import PersonIcon from "@mui/icons-material/Person";
import LaptopChromebookIcon from "@mui/icons-material/LaptopChromebook";
import { toast } from "react-hot-toast";

import {
  RecentAssignedAssetDetails,
  getDeletedAssets
} from "../Services/AssetService";
import {
  fetchAssetHistory
} from "../Services/HistoryServices";
import AssetHistoryPopup from "./AssetHistoryPop";
import EditAssetModal from "./EditAssetModal";
import EditEmployeeModal from "./EditEmployeeModal";
import { blueGrey } from "@material-ui/core/colors";

const ToggleButton = ({ showDeleted, setShowDeleted }) => (
  <motion.div
    whileHover={{ scale: 1.1 }}
    whileTap={{ scale: 0.95 }}
    initial={{ opacity: 0, y: -10 }}
    animate={{ opacity: 1, y: 0 }}
    transition={{ type: "spring", stiffness: 200, damping: 12 }}
    onClick={() => setShowDeleted((prev) => !prev)}
  >
  <div
    style={{
    backgroundColor: showDeleted ? "red" : "green",
    color: "white",
    padding: "8px 16px",
    borderRadius: "8px",
    display: "inline-block",
    fontWeight: "bold",
    cursor: "pointer"
  }}
>
  {showDeleted ? "Show Deleted Assets" : "Show Assigned Assets"}
</div>
  </motion.div>
);

const RecentAssignedAssetPage = () => {
  const [assignedRows, setAssignedRows] = useState([]);
  const [deletedRows, setDeletedRows] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showDeleted, setShowDeleted] = useState(false);

  const [selectedAsset, setSelectedAsset] = useState(null);
  const [selectedEmployee, setSelectedEmployee] = useState(null);

  const [historyData, setHistoryData] = useState([]);
  const [openHistoryModal, setOpenHistoryModal] = useState(false);
  const [openEditModal, setOpenEditModal] = useState(false);
  const [viewing, setViewing] = useState(""); // 'asset' or 'employee'

  const handleClose = () => {
    setOpenHistoryModal(false);
    setOpenEditModal(false);
    setSelectedAsset(null);
    setSelectedEmployee(null);
    setViewing("");
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
    } catch {
      toast.error("Failed to load asset history.");
    }
  };

  const handleViewAsset = (asset) => {
    setSelectedAsset(asset);
    setViewing("asset");
    setOpenEditModal(true);
  };

  const handleViewEmployee = (employee) => {
    setSelectedEmployee(employee);
    setViewing("employee");
    setOpenEditModal(true);
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
            raw: item,
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
    { field: "empId", headerName: "Employee ID",  headerAlign: "center", align: "center",flex: 1 },
    { field: "employeeName", headerName: "Employee Name", headerAlign: "center", align: "center",flex: 1 },
    { field: "serialNumber", headerName: "Serial Number",  headerAlign: "center", align: "center",flex: 1 },
    { field: "assignedDate", headerName: "Assigned Date",  headerAlign: "center", align: "center",flex: 1 },
    {
      field: "actions",
      headerName: "Actions",
      flex: 1,
      sortable: false,
      headerAlign: "center", 
      align: "center",
      renderCell: (params) => (
        <Box display="flex" justifyContent="center" alignItems="center" gap={1}>
          <Tooltip title="View Employee">
            <IconButton
              onClick={() => handleViewEmployee(params.row.raw)}
              sx={{
                transition: "transform 0.2s",
                "&:hover": {
                  transform: "scale(1.3)",
                  color: "info.main",
                  filter: "drop-shadow(0 0 4px skyblue)",
                },
              }}
            >
              <PersonIcon />
            </IconButton>
          </Tooltip>

          <Tooltip title="View Asset">
            <IconButton
              onClick={() => handleViewAsset(params.row.raw)}
              sx={{
                transition: "transform 0.2s",
                "&:hover": {
                  transform: "scale(1.3)",
                  color: "warning.main",
                  filter: "drop-shadow(0 0 4px orange)",
                },
              }}
            >
              <LaptopChromebookIcon />
            </IconButton>
          </Tooltip>
        </Box>
      ),
    },
  ];

  const deletedColumns = [
    { field: "serialNo", headerName: "Serial Number", headerAlign: "center", align: "center", flex: 1,},
    { field: "assetName", headerName: "Asset Name", headerAlign: "center", align: "center", flex: 1,},
    { field: "purchaseDate", headerName: "Purchase Date", headerAlign: "center", align: "center", flex: 1,},
    { field: "deletedDate", headerName: "Deleted Date", headerAlign: "center", align: "center", flex: 1,},
    { field: "type", headerName: "Type", headerAlign: "center", align: "center", flex: 1,},
    { field: "location", headerName: "Location", headerAlign: "center", align: "center", flex: 1,},
    { field: "assetSourcedBy", headerName: "Sourced By", headerAlign: "center", align: "center", flex: 1,},
    { field: "deletedBy", headerName: "Deleted By", headerAlign: "center", align: "center", flex: 1,},
    {
      field: "history",
      headerName: "History",
      headerAlign: "center", align: "center",
      flex: 1,
      sortable: false,
      renderCell: (params) => (
        <Box display="flex" justifyContent="center" alignItems="center" gap={1}>
          <Tooltip title="Asset History">
            <IconButton
              onClick={() => handleOpenHistoryModal(params.row)}
              sx={{
                transition: "transform 0.2s",
                "&:hover": {
                  transform: "scale(1.3)",
                  color: "warning.main",
                  filter: "drop-shadow(0 0 4px orange)",
                },
              }}
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

        <h2>
  {showDeleted ? "Permanently Deleted Assets" : "Today Assigned Assets"}
</h2>


        <div style={scrollWrapperStyle}>
          <style>{`div::-webkit-scrollbar { display: none; }`}</style>
          <DataGrid
  rows={showDeleted ? deletedRows : assignedRows}
  columns={showDeleted ? deletedColumns : assignedColumns}
  pageSize={5}
  rowsPerPageOptions={[5]}
  autoHeight
  disableSelectionOnClick
  sx={{
    maxHeight: "70vh",  
    overflow: "hidden",
    borderRadius: "16px",
    border: "2px solid #0a1113ff",
    fontFamily: "'Racing Sans One', sans-serif",
    color: "#083A40",

    // Hide scrollbar
    "& .MuiDataGrid-main": {
      scrollbarWidth: "none",
      msOverflowStyle: "none",
    },
    "& .MuiDataGrid-main::-webkit-scrollbar": {
      display: "none",
    },
    "& .MuiDataGrid-virtualScroller": {
      scrollbarWidth: "none",
    },
    "& .MuiDataGrid-virtualScroller::-webkit-scrollbar": {
      display: "none",
    },

    // Styling headers
    "& .MuiDataGrid-columnHeaders": {
      background: "linear-gradient(45deg, #6DE0FF, #2BC4F3)",
      color: "#083A40",
      fontSize: "16px",
      fontWeight: 700,
    },
    // Cell styling
    "& .MuiDataGrid-cell": {
      background: "#F0FBFF",
      color: "#083A40",
      fontSize: "15px",
      borderBottom: "1px solid #D0F0FF",
    },
    // Footer styling
    "& .MuiDataGrid-footerContainer": {
      background: "linear-gradient(45deg, #6DE0FF, #2BC4F3)",
      color: "#083A40",
      fontWeight: 600,
    },
    // Hover row effect
    "& .MuiDataGrid-row:hover": {
      backgroundColor: "#E0F9FF",
    },
  }}
/>
          <AssetHistoryPopup
            open={openHistoryModal}
            onClose={handleClose}
            asset={selectedAsset}
            history={historyData}
          />

          {viewing === "employee" && selectedEmployee && (
            <EditEmployeeModal
              open={openEditModal}
              handleClose={handleClose}
              employee={selectedEmployee}
              refreshEmployeeList={() => {}}
              viewOnly={true}
            />
          )}

          {viewing === "asset" && selectedAsset && (
            <EditAssetModal
              open={openEditModal}
              handleClose={handleClose}
              asset={selectedAsset}
              refreshAssetList={() => {}}
              viewOnly={true}
            />
          )}
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
