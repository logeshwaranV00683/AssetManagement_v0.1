import React, { useEffect, useState } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { Container, Box} from '@mui/material';
import { DataGrid } from '@mui/x-data-grid';
import TextField from '@mui/material/TextField';
import "../Style/Assets.css"
import AddAssetModal from './AddAssetModal';
import ExportButton from './ExportButton';
import FileDownloadIcon from '@mui/icons-material/FileDownload';
import SidebarAssets  from './SideBarAssets';
import UploadFileIcon from '@mui/icons-material/UploadFile';  
import {IconButton, Tooltip } from '@mui/material';        
import VisibilityIcon from '@mui/icons-material/Visibility';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete'; 
import HistoryIcon from '@mui/icons-material/History';
import { getAssetList, scrapAsset } from '../Services/AssetService';
import  EditAssetModal  from './EditAssetModal';
import { toast } from 'react-hot-toast';
import { showConfirmAlert } from '../Utils/alerts';
import ImportExcel from  "../Utils/ImportExcel";
import AssetHistoryPopup from './AssetHistoryPop.js'; 
import { fetchAssetHistory } from '../Services/HistoryServices.js'; 
import AssignAsset from './AssignAsset';
import PersonAddIcon from '@mui/icons-material/PersonAdd';
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
    const [filteredRows, setFilteredRows] = useState(assets);
    const [showImportModal, setShowImportModal] = useState(false);


//sakthi
const [openHistoryModal, setOpenHistoryModule] = useState(false);
const [historyData, setHistoryData] = useState([]);
//sakthi
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

        //sakthi
const handleOpenHistoryModal = async (asset) => {
  try {
    const data = await fetchAssetHistory(asset.serialNumber);
    console.log("Fetched History data:", data);

    if (data && data.length > 0) {
      setSelectedAsset(asset);
      setHistoryData(data);
      setOpenHistoryModule(true);
    } else {
      toast.error("No history found for this asset.");
    }
  } catch (error) {
    console.error("Failed to fetch history", error);
    toast.error("Failed to load asset history.");
  }
};

//sakthi

const handleDelete = async (asset) => {
  const confirmDelete = await showConfirmAlert('Are you sure?', 'Do you want to Scrap this asset?');
  if (!confirmDelete) return;

  try {
    const success = await scrapAsset(asset.assetId);
    if (success) {
         toast.success(`Scrapped the Asset with ID: ${asset.serialNumber}`); 
         fetchAssets();
    }
  } catch (error) {
    console.error('Error deleting asset:', error);
    toast.error(`Cannot Scrapped the Asset with ID: ${asset.serialNumber}`); 
  }
};


    useEffect(() => {
            fetchAssets();
        }, []);

    const handleOpenModal = () => {
        setOpenAddModal(true);
    };
     const handleSearch = (event) => {
        const value = event.target.value.toLowerCase();
        setFilterValue(value);

        const filtered = assets.filter(asset =>
            Object.values(asset).some(val =>
                String(val).toLowerCase().includes(value)
            )
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
        setOpenAssignModal(false)
    };
    const handleOpenEditModal = (asset, isViewOnly = false) => {
        setSelectedAsset(asset);
        setViewOnly(isViewOnly);
        setOpenEditModal(true);
    };

    const filterByAssetStatus = (status) => {
    setExportType(status);
    setFilteredRows(assets.filter(a => a.status?.toLowerCase() === status.toLowerCase()));
};
const handleAssign = async (asset) => {
 setOpenAssignModal(true);
setSelectedAsset(asset);
}

    const columns = [
        { field: 'serialNumber', headerName: 'Serial Number', width: 150, sortable: true},
        { field: 'assetName',headerName: 'Name', width: 100, sortable: true},
        { field: 'type', headerName: 'Type', width: 100, sortable: true},
        { field: 'status', headerName: 'Status', width: 120, sortable: true},
        { field: 'empId', headerName: 'Assigned To', width: 120, sortable: true},
        { field: 'location', headerName: 'Location', width: 100, sortable: true},
        { field: 'assetSourcedBy', headerName: 'Asset Sourced By', width: 170, sortable: true},
        {
            field: 'actions',
            headerName: 'Actions',
            width: 200,
            sortable: false,
            renderCell: (params) => (
                <Box display="flex" alignItems="center">
                    <Tooltip title="View">
                        <IconButton
                            color="inherit"
                            sx={{
                                transition: 'transform 0.2s',
                                '&:hover': {
                                    transform: 'scale(1.3)',
                                    color: 'primary.main',
                                    filter: 'drop-shadow(0 0 4px rgba(25, 118, 210, 0.8))',
                                },
                                mr: 1,
                            }}
                            onClick={() => handleOpenEditModal(params.row, true)}
                        >
                            <VisibilityIcon />
                        </IconButton>
                    </Tooltip>
                    <Tooltip title="Assign">
                    <IconButton
                        color="inherit"
                        sx={{ transition: 'transform 0.2s',
                                '&:hover': {
                                     transform: 'scale(1.3)',
                                     color: 'error.main',
                                     filter: 'drop-shadow(0 0 4px rgb(10, 227, 50))',
                                },
                            }}
                            onClick={() => handleAssign(params.row)}                                        
                    >
                       <PersonAddIcon />
                    </IconButton>
                </Tooltip>
                    <Tooltip title="History">
                        <IconButton
                            color="inherit"
                            sx={{
                                transition: 'transform 0.2s',
                                '&:hover': {
                                    transform: 'scale(1.3)',
                                    color: 'primary.main',
                                    filter: 'drop-shadow(0 0 4px rgba(0, 224, 240, 0.8))',
                                },
                            }}
                            onClick={()=>handleOpenHistoryModal(params.row)}
                        >
                            <HistoryIcon />
                        </IconButton>
                    </Tooltip>
                    <Tooltip title="Edit">
                        <IconButton
                            color="inherit"
                            sx={{
                                transition: 'transform 0.2s',
                                '&:hover': {
                                    transform: 'scale(1.3)',
                                    color: 'secondary.main',
                                    filter: 'drop-shadow(0 0 4px rgba(156, 39, 176, 0.8))',
                                },
                                mr: 1,
                            }}
                            onClick={()=>handleOpenEditModal(params.row)}
                        >
                            <EditIcon />
                        </IconButton>
                    </Tooltip>
                    <Tooltip title="Scrap">
                        <IconButton
                            color="inherit"
                            sx={{
                                transition: 'transform 0.2s',
                                '&:hover': {
                                    transform: 'scale(1.3)',
                                    color: 'error.main',
                                    filter: 'drop-shadow(0 0 4px rgba(211, 47, 47, 0.8))',
                                },
                            }}
                            onClick={()=>handleDelete(params.row)}
                        >
                            <DeleteIcon />
                        </IconButton>
                    </Tooltip>
                </Box>
            ),
        },
    ];

    return (
        <div style={{width:'185vh'}}>
            <main className={classes.content}>
                <Container maxWidth="lg">
                    <div className={classes.filterContainer}>
                        <div style={{ display: 'flex', position: 'relative' }}>
                            <main className={classes.content} style={{ flexGrow: 1, paddingRight: '80px' }}>
                                <Container maxWidth="lg">
                                </Container>
                            </main>
                            <SidebarAssets
                              onAddAsset={handleOpenModal}
                              onFilter={filterByAssetStatus}
                              onResetFilters={resetFilters}
                            />
                        </div>
                             <div className="import-button" onClick={() => setShowImportModal(true)}>
        <span><UploadFileIcon /> Import Assets</span>
      </div>

      {showImportModal && (
        <ImportExcel
          importType="asset"
          onClose={() => setShowImportModal(false)}
        />
      )}


                        <TextField
                            label="Search"
                            variant="outlined"
                            onChange={handleSearch}
                            value={filterValue}
                            sx={{
                                width: { xs: '100%', md: '85vw' },
                                maxWidth: '1000px',
                                '& .MuiOutlinedInput-root': {
                                    margin: '10px',
                                background: '#ffffff',
                                borderRadius: '15px',
                                color: '#083A40',
                                fontWeight: 500,
                                boxShadow: '0 0 6px rgba(255, 255, 255, 0.8), 0 0 12px rgba(109, 224, 255, 0.6)',
                                '& fieldset': {
                                    border: '0.5px solid transparent',
                                },
                                '&:hover fieldset': {
                                    border: '0.5px solid #1FCBEA',
                                },
                                '&.Mui-focused fieldset': {
                                     boxShadow: '0 0 6px rgba(255, 255, 255, 0.8), 0 0 12px rgba(109, 224, 255, 0.6)',
                                     fontSize: '20px'

                                },
                                '& input': {
                                    background: 'transparent',
                                    color: '#083A40',
                                    fontFamily: "'Racing Sans One', sans-serif",
                                },
                                },
                                '& .MuiInputLabel-root': {
                                color: '#083A40',
                                fontFamily: "'Racing Sans One', sans-serif",
                                letterSpacing: '2.0px',
                                },
                                '& .Mui-focused .MuiInputLabel-root': {
                                color: '#083A40',
                                },

                            }}
                        />
                        <div className="export-button">
                                <ExportButton
                                type="asset"
                                status={exportType}
                                filter={filterValue}
                                filePrefix="Verinite"
                                buttonLabel={
                                    <span>
                                    <FileDownloadIcon style={{ fontSize: 20 }} />
                                    Export Assets
                                    </span>
                                }
                                filteredRows={filteredRows}
                                />
                            </div>
                    </div>
                    <div style={{ height: '65vh', width: '85vw', display: 'flex', flexDirection: 'column', alignItems: 'center', }}>
                        <div style={{ height: 350, marginLeft: '2%', width: '95%', flexGrow: 1 }}>
                            <DataGrid
                                rows={filteredRows}
                                columns={columns}
                                pageSize={5}
                                rowsPerPageOptions={[5, 10, 20]}
                                sx={{
                                    borderRadius: '16px',
                                    overflow: 'hidden',
                                    border: '2px solid #1FCBEA',
                                    boxShadow: '0 0 3px #6DE0FF, 0 0 4px #2BC4F3',
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
                                    '& .MuiDataGrid-selectedRowCount': {
                                    color: '#083A40',
                                    },
                                    '& .MuiCheckbox-root': {
                                    color: '#083A40',
                                    },
                                }}
                            />

                        </div>
                        <div style={{ display: 'flex', justifyContent: 'center', gap: '20px', width: '95%', marginTop: '40px' }}>
                        </div>

                    </div>
                    
                </Container>
                
                <AddAssetModal 
                open={openAddModal} 
                handleClose={handleClose} 
                refreshAssetList={fetchAssets}
                />

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
               <AssignAsset
                open={openAssignModal}
                handleClose={handleClose }
                asset={selectedAsset} 
                fetchAssets={fetchAssets}
                />

            </main>
        </div>
        
    );
}

export default Assets;
