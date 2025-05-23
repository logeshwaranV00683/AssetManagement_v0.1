import React, { useState } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { Container, Box, Button, ButtonGroup } from '@mui/material';
import { DataGrid } from '@mui/x-data-grid';
import TextField from '@mui/material/TextField';
import "../Style/Assets.css"
import AddAssetModal from './AddAssetModal';
import ExportButton from './ExportButton';
import FileDownloadIcon from '@mui/icons-material/FileDownload';
import SidebarAssets  from './SideBarAssets';
import ImportButton from './ImportButton';
import UploadFileIcon from '@mui/icons-material/UploadFile'; // Add this at the top with other imports
import {IconButton, Tooltip } from '@mui/material';
import VisibilityIcon from '@mui/icons-material/Visibility';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';



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
    const [openModal, setOpenModal] = useState(false);
    const [exportType, setExportType] = useState('all');

    const handleOpenModal = () => {
        setOpenModal(true);
    };
    const rows = [
        { id: 1, name: 'Laptop', serialNumber: 'SN001', location: 'Room A', assignedTo: 'User A' },
        { id: 2, name: 'Desktop', serialNumber: 'SN002', location: 'Room B', assignedTo: 'User B' },
        { id: 3, name: 'Laptop', serialNumber: 'SN003', location: 'Room C', assignedTo: 'User B' },
        { id: 4, name: 'Laptop', serialNumber: 'SN001', location: 'Room A', assignedTo: 'User A' },
        { id: 5, name: 'Desktop', serialNumber: 'SN002', location: 'Room B', assignedTo: 'User B' },
        { id: 6, name: 'Laptop', serialNumber: 'SN003', location: 'Room C', assignedTo: 'User B' },
        { id: 7, name: 'Laptop', serialNumber: 'SN001', location: 'Room A', assignedTo: 'User A' },


        // Add more rows as needed
    ];

    const columns = [
        { field: 'name',headerName: 'Name', width: 200, sortable: true},
        { field: 'serialNumber', headerName: 'Serial Number', width: 200, sortable: true},
        { field: 'assignedTo', headerName: 'Assigned To', width: 200, sortable: true},
        { field: 'location', headerName: 'Location', width: 200, sortable: true},
        {
            field: 'actions',
            headerName: 'Actions',
            width: 150,
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
                        >
                            <VisibilityIcon />
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
                        >
                            <EditIcon />
                        </IconButton>
                    </Tooltip>
                    <Tooltip title="Delete">
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
                        >
                            <DeleteIcon />
                        </IconButton>
                    </Tooltip>
                </Box>
            ),
        },
    ];

    const [filterValue, setFilterValue] = useState('');
    const [filteredRows, setFilteredRows] = useState(rows);

    const handleSearch = (event) => {
        const { value } = event.target;
        const lowercaseValue = value.toLowerCase();
        setFilterValue(lowercaseValue);

        const filteredRows = rows.filter(row =>
            row.name.toLowerCase().includes(lowercaseValue) ||
            row.serialNumber.toLowerCase().includes(lowercaseValue) ||
            row.location.toLowerCase().includes(lowercaseValue) ||
            row.assignedTo.toLowerCase().includes(lowercaseValue)
        );

        setFilteredRows(filteredRows);
    };

    const resetFilters = () => {
        setFilterValue('');
        setFilteredRows(rows);
        setExportType('all');
    };
    const handleClose = () => {
        setOpenModal(false)
    }

    const filterAssigned = () => {
        const assignedRows = rows.filter(row => row.assignedTo !== 'Unassigned');
        setFilteredRows(assignedRows);
        setExportType('assigned');
    };

    const filterScrap = () => {// this is not added in the old one Logeshwaran added for export, still it doesn't fetch data from scrap table and it has to.
        setFilteredRows(rows.filter(row => row.assignedTo === 'Scrap'));
        setExportType('scrap');
      };

    const filterUnassigned = () => {
        const unassignedRows = rows.filter(row => row.assignedTo === 'Unassigned');
        setFilteredRows(unassignedRows);
        setExportType('unassigned');
    };

    return (
        <div style={{width:'185vh'}}>
            <main className={classes.content}>
                <Container maxWidth="lg">
                    <div className={classes.filterContainer}>
                        {/* <Box display="flex" alignItems="center"> */}
                        <div style={{ display: 'flex', position: 'relative' }}>
                            {/* Main content area */}
                            <main className={classes.content} style={{ flexGrow: 1, paddingRight: '80px' }}>
                                <Container maxWidth="lg">
                                {/* ...your existing content... */}
                                </Container>
                            </main>

                            {/* Sidebar only shown on /assets */}
                            <SidebarAssets
                                onAddAsset={handleOpenModal}
                                onFilterAssigned={filterAssigned}
                                onFilterUnassigned={filterUnassigned}
                                onFilterScrap={filterScrap}
                                onResetFilters={resetFilters}
                            />

                        </div>
                        
                        <TextField
                            label="Search"
                            variant="outlined"
                            onChange={handleSearch}
                            value={filterValue}
                            sx={{
                                width: '85vw',
                                borderRadius: '60px',
                                '& .MuiOutlinedInput-root': {
                                background: '#ffffff', // White background
                                borderRadius: '15px',
                                color: '#083A40', // Text color
                                fontWeight: 500,
                                boxShadow: '0 0 6px rgba(255, 255, 255, 0.8), 0 0 12px rgba(109, 224, 255, 0.6)', // Glowing white + blue shadow
                                '& fieldset': {
                                    border: '2px solid transparent',
                                },
                                '&:hover fieldset': {
                                    border: '2px solid #1FCBEA',
                                },
                                '&.Mui-focused fieldset': {
                                    border: '2px solid #000',
                                },
                                '& input': {
                                    background: 'transparent',
                                    color: '#083A40',
                                    fontFamily: "'Racing Sans One', sans-serif",
                                    marginLeft: '200px',
                                },
                                },
                                '& .MuiInputLabel-root': {
                                color: '#083A40',
                                fontFamily: "'Racing Sans One', sans-serif",
                                marginLeft: '20px',
                                letterSpacing: '1.5px',
                                },
                                '& .Mui-focused .MuiInputLabel-root': {
                                color: '#083A40',
                                },
                            }}
                        />


                        
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
                            <div className="export-button">
                                <ExportButton
                                type="assets"
                                status={exportType}
                                filter={filterValue}
                                filePrefix="Verinite"
                                buttonLabel={
                                    <span>
                                    <FileDownloadIcon style={{ fontSize: 20 }} />
                                    {`Export ${exportType} Assets`}
                                    </span>
                                }
                                />
                            </div>

                            <div className="import-button">
                                <ImportButton
                                type="assets"
                                status={exportType}
                                filter={filterValue}
                                filePrefix="Verinite"
                                buttonLabel={
                                    <span>
                                    <UploadFileIcon style={{ fontSize: 20 }} />
                                    Import Assets
                                    </span>
                                }
                                />
                            </div>
                        </div>

                    </div>
                    
                </Container>
                
                <AddAssetModal open={openModal} handleClose={handleClose} />
                
            </main>
        </div>
        
    );
}

export default Assets;
