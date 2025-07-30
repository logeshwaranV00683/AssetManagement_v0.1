import React, { useEffect, useState } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { Container, Box } from '@mui/material';
import { DataGrid } from '@mui/x-data-grid';
import TextField from '@mui/material/TextField';
import '../Style/Assets.css';
import AddAssetModal from './AddAssetModal';
import ExportButton from '../Utils/ExportButton';
import FileDownloadIcon from '@mui/icons-material/FileDownload';
import SidebarAssets from './SideBarAssets';
import UploadFileIcon from '@mui/icons-material/UploadFile';
import { IconButton, Tooltip } from '@mui/material';
import VisibilityIcon from '@mui/icons-material/Visibility';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import HistoryIcon from '@mui/icons-material/History';
import PersonAddIcon from '@mui/icons-material/PersonAdd';
import { getAssetList, scrapAsset, unassignAsset} from '../Services/AssetService';
import EditAssetModal from './EditAssetModal';
import { toast } from 'react-hot-toast';
import { showConfirmAlert } from '../Utils/alerts';
import ImportExcel from '../Utils/ImportExcel';
import AssetHistoryPopup from './AssetHistoryPop';
import { fetchAssetHistory } from '../Services/HistoryServices';
import AssignAsset from './AssignAsset';
import PersonRemoveIcon from '@mui/icons-material/PersonRemove';

const useStyles = makeStyles((theme) => ({
  content: {
    flexGrow: 1,
  },
  filterContainer: {
    display: 'flex',
    justifyContent: 'space-evenly',
    alignItems: 'center',
    marginTop: '30px',
    marginBottom: theme.spacing(2),
  },
}));

function Assets() {
  const classes = useStyles();
  const [openAddModal, setOpenAddModal] = useState(false);
  const [openEditModal, setOpenEditModal] = useState(false);
  const [viewOnly, setViewOnly] = useState(false);
  const [selectedAsset, setSelectedAsset] = useState(null);
  const [exportType, setExportType] = useState('all');
  const [filterValue, setFilterValue] = useState('');
  const [assets, setAssets] = useState([]);
  const [filteredRows, setFilteredRows] = useState([]);
  const [showImportModal, setShowImportModal] = useState(false);
  const [openHistoryModal, setOpenHistoryModule] = useState(false);
  const [historyData, setHistoryData] = useState([]);
  const [openAssignModal, setOpenAssignModal] = useState(false);

  const fetchAssets = async () => {
    try {
      const data = await getAssetList();
      setAssets(data);
      setFilteredRows(data);
    } catch (error) {
      console.error('Error fetching assets:', error);
    }
  };

  const handleOpenHistoryModal = async (asset) => {
    try {
      const data = await fetchAssetHistory(asset.serialNumber);
      if (data && data.length > 0) {
        setSelectedAsset(asset);
        setHistoryData(data);
        setOpenHistoryModule(true);
      } else {
        toast.error('No history found for this asset.');
      }
    } catch (error) {
      toast.error('Failed to load asset history.');
    }
  };

  const handleDelete = async (asset) => {
    const confirmDelete = await showConfirmAlert('Are you sure?', 'Do you want to Scrap this asset?');
    if (!confirmDelete) return;
    try {
      const success = await scrapAsset(asset.assetId);
      if (success) {
        toast.success(`Scrapped the Asset with Serial Number: ${asset.serialNumber}`);
        fetchAssets();
      }
    } catch (error) {
      toast.error(`Cannot Scrap the Asset with Serial Number: ${asset.serialNumber}`);
    }
  };

  useEffect(() => {
    fetchAssets();
  }, []);

  const handleOpenModal = () => setOpenAddModal(true);

  const handleSearch = (event) => {
    const value = event.target.value.toLowerCase();
    setFilterValue(value);
    const filtered = assets.filter((asset) =>
      Object.values(asset).some((val) => String(val).toLowerCase().includes(value))
    );
    setFilteredRows(filtered);
  };

  const resetFilters = () => {
    setFilterValue('');
    setFilteredRows(assets);
    setExportType('all');
  };

  const handleClose = () => {
    setOpenAddModal(false);
    setOpenEditModal(false);
    setSelectedAsset(null);
    setViewOnly(false);
    setOpenHistoryModule(false);
    setOpenAssignModal(false);
  };

  const handleOpenEditModal = (asset, isViewOnly = false) => {
    setSelectedAsset(asset);
    setViewOnly(isViewOnly);
    setOpenEditModal(true);
  };

  const filterByAssetStatus = (status) => {
    setExportType(status);
    setFilteredRows(assets.filter((a) => a.status?.toLowerCase() === status.toLowerCase()));
  };

  const handleAssign = (asset) => {
    if (asset.empId) {
      showConfirmAlert('Unassign Asset?', `Unassign asset ${asset.serialNumber} from employee ${asset.empId}?`)  
        .then(async (confirmed) => {
          if (confirmed) {
            try {
              await unassignAsset([asset.serialNumber]);
              toast.success(`Asset ${asset.serialNumber} unassigned successfully.`);
              fetchAssets();
            } catch (err) {
              toast.error('Unassign failed.');
            }
          }
        });
    } else {
      setSelectedAsset(asset);
      setOpenAssignModal(true);
    }
  };

  const columns = [
  { field: 'serialNumber', headerName: 'Serial Number', minWidth: 100, flex: 1 },
  { field: 'assetName', headerName: 'Name', minWidth: 80, flex: 0.75 },
  { field: 'type', headerName: 'Type', minWidth: 100, flex: 0.8 },
  { field: 'status', headerName: 'Status', minWidth: 60, flex: 0.9 },
  { field: 'empId', headerName: 'Assigned To', minWidth: 120, flex: 0.65 },
  { field: 'location', headerName: 'Location', minWidth: 100, flex: 0.6 },
  { field: 'assetSourcedBy', headerName: 'Sourced By', minWidth: 110, flex: 0.65 },
  {
    field: 'actions',
    headerName: 'Actions',
    minWidth: 150,
    flex: 2,
    sortable: false,
    renderCell: (params) => (
      <Box display="flex" justifyContent="center" alignItems="center" gap={0.5}>
        <Tooltip title="View">
          <IconButton
            sx={{
              transition: 'transform 0.2s',
              '&:hover': {
                transform: 'scale(1.2)',
                color: 'info.main',
                filter: 'drop-shadow(0 0 30px #2196f3)',
              },
            }}
            onClick={() => handleOpenEditModal(params.row, true)}
          >
            <VisibilityIcon />
          </IconButton>
        </Tooltip>

        <Tooltip title={params.row.empId ? 'Unassign' : 'Assign'}>
          <IconButton
            sx={{
              transition: 'transform 0.2s',
              '&:hover': {
                transform: 'scale(1.2)',
                color: params.row.empId ? 'error.main' : 'success.main',
                filter: params.row.empId
                  ? 'drop-shadow(0 0 4px rgb(246, 102, 59))'
                  : 'drop-shadow(0 0 4px rgb(30, 237, 68))',
              },
            }}
            onClick={() => handleAssign(params.row)}
          >
            {params.row.empId ? <PersonRemoveIcon /> : <PersonAddIcon />}
          </IconButton>
        </Tooltip>

        <Tooltip title="History">
          <IconButton
            sx={{
              transition: 'transform 0.2s',
              '&:hover': {
                transform: 'scale(1.2)',
                color: 'warning.main',
                filter: 'drop-shadow(0 0 4px orange)',
              },
            }}
            onClick={() => handleOpenHistoryModal(params.row)}
          >
            <HistoryIcon />
          </IconButton>
        </Tooltip>

        <Tooltip title="Edit">
          <IconButton
            sx={{
              transition: 'transform 0.2s',
              '&:hover': {
                transform: 'scale(1.2)',
                color: 'primary.main',
                filter: 'drop-shadow(0 0 4px #1976d2)',
              },
            }}
            onClick={() => handleOpenEditModal(params.row)}
          >
            <EditIcon />
          </IconButton>
        </Tooltip>

        <Tooltip title="Scrap">
          <IconButton
            sx={{
              transition: 'transform 0.2s',
              '&:hover': {
                transform: 'scale(1.2)',
                color: 'error.main',
                filter: 'drop-shadow(0 0 4px red)',
              },
            }}
            onClick={() => handleDelete(params.row)}
          >
            <DeleteIcon />
          </IconButton>
        </Tooltip>
      </Box>
    ),
  },
];


  return (
    <div style={{ width: '100%', display: 'flex', justifyContent: 'center' }}>
      <main className={classes.content}>
        <Container maxWidth={false} style={{ width: '100%', padding: 0 }}>

          <SidebarAssets onAddAsset={handleOpenModal} onFilter={filterByAssetStatus} onResetFilters={resetFilters} />

          <div className={classes.filterContainer}>
            <div
              style={{
                display: 'flex',
                justifyContent: 'center',
                width: '100%',
              }}
            >
              <div
                style={{
                  display: 'flex',
                  flexWrap: 'wrap',
                  justifyContent: 'space-between',
                  alignItems: 'center',
                  gap: '1rem',
                  width: '80%'
                }}
              >
              {/* Import Button */}
              <div style={{ width: '20%', minWidth: '150px', textAlign: 'center' }}>
                <div
                  className="import-button"
                  style={{
                    padding: '0.4rem 0.8rem',
                    textAlign: 'center',
                    borderRadius: '0.6rem',
                    fontWeight: 600,
                    fontSize: '0.8rem',
                  }}
                  onClick={() => setShowImportModal(true)}
                >
                  <span style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '0.3rem' }}>
                    <UploadFileIcon fontSize="small" /> IMPORT
                  </span>
                </div>
              </div>

              {/* Import Modal */}
              {showImportModal && (
                <ImportExcel
                  importType="asset"
                  onClose={() => setShowImportModal(false)}
                  refreshList={fetchAssets}
                />
              )}

              {/* Search */}
              <div style={{ width: '55%', minWidth: '200px' }}>
                <TextField
                  label="Search"
                  variant="outlined"
                  onChange={handleSearch}
                  value={filterValue}
                  fullWidth
                  sx={{
                    '& .MuiOutlinedInput-root': {
                      background: '#ffffff',
                      borderRadius: '0.6rem',
                      fontWeight: 500,
                      boxShadow: '0 0 0.3rem rgba(109, 224, 255, 0.4)',
                      '& fieldset': { border: '1px solid #ccc' },
                      '&:hover fieldset': { borderColor: '#1FCBEA' },
                      '&.Mui-focused fieldset': { borderColor: '#1FCBEA' },
                      '& input': {
                        fontFamily: "'Racing Sans One', sans-serif",
                        fontSize: '0.9rem',
                        color: '#083A40',
                      },
                    },
                    '& .MuiInputLabel-root': {
                      fontFamily: "'Racing Sans One', sans-serif",
                      fontSize: '0.8rem',
                      color: '#083A40',
                    },
                  }}
                />
              </div>

              {/* Export Button */}
              <div style={{ width: '20%', minWidth: '150px', textAlign: 'center' }}>
                <div
                  className="export-button"
                  style={{
                    padding: '0.4rem 0.8rem',
                    textAlign: 'center',
                    borderRadius: '0.6rem',
                    fontWeight: 600,
                    fontSize: '0.8rem',
                  }}
                >
                  <ExportButton
                    type="asset"
                    status={exportType}
                    filter={filterValue}
                    filePrefix="Verinite"
                    buttonLabel={
                      <span style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '0.3rem' }}>
                        <FileDownloadIcon fontSize="small" /> EXPORT
                      </span>
                    }
                    filteredRows={filteredRows}
                  />
                </div>
              </div>
            </div>
          </div>
        </div>






          <div
            style={{
              width: '80%',
              height: '65vh',
              margin: '0 auto',
              display: 'flex',
              flexDirection: 'column',
              justifyContent: 'center',
              alignItems: 'center',
            }}
          >
            <div
              style={{
                width: '100%',
                maxWidth: '100%',
                height: '100%',
              }}
            >

              <DataGrid
                rows={filteredRows}
                columns={columns}
                sx={{
                  borderRadius: '16px',
                  overflowX: 'auto',
                  border: '2px solid #0a1113ff',
                  fontFamily: "'Racing Sans One', sans-serif",
                  color: '#083A40',
                  '& .MuiDataGrid-columnHeaders': {
                    background: 'linear-gradient(45deg, #6DE0FF, #2BC4F3)',
                    color: '#083A40',
                    fontSize: '16px',
                    fontWeight: 700,
                  },
                  '& .MuiDataGrid-cell': {
                    background: '#F0FBFF',
                    color: '#083A40',
                    fontSize: '15px',
                    borderBottom: '1px solid #D0F0FF',
                  },
                  '& .MuiDataGrid-footerContainer': {
                    background: 'linear-gradient(45deg, #6DE0FF, #2BC4F3)',
                    color: '#083A40',
                    fontWeight: 600,
                  },
                  '& .MuiDataGrid-row:hover': {
                    backgroundColor: '#E0F9FF',
                  },
                }}
              />
            </div>
          </div>

          <AddAssetModal open={openAddModal} handleClose={handleClose} refreshAssetList={fetchAssets} />

          <EditAssetModal
            open={openEditModal}
            handleClose={handleClose}
            asset={selectedAsset}
            refreshAssetList={fetchAssets}
            viewOnly={viewOnly}
          />

          <AssetHistoryPopup
            open={openHistoryModal}
            onClose={handleClose}
            asset={selectedAsset}
            history={historyData}
          />

          <AssignAsset open={openAssignModal} handleClose={handleClose} asset={selectedAsset} fetchAssets={fetchAssets} />
        </Container>
      </main>
    </div>
  );
}

export default Assets;
