import React, { useState } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { Container, Box, Button, ButtonGroup } from '@mui/material';
import { DataGrid } from '@mui/x-data-grid';
import TextField from '@mui/material/TextField';
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline';
import "./Assets.css"
import AddAssetModal from './AddAssetModal';
import ExportButton from './ExportButton';
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

        // Add more rows as needed
    ];

    const columns = [
        { field: 'name',headerName: 'Name', width: 200, sortable: true,
            renderHeader: () => (
                <Box display="flex" flexDirection="column" alignItems="center" padding={0}>
                <div>Name</div>
                <TextField
                    label="Search"
                    variant="outlined"
                    size="small"
                    onChange={handleSearch}
                    InputLabelProps={{ style: { fontSize: 11 } }}
                    style={{ marginTop: 2, width: 140, height: 30 }}
                    inputProps={{ style: { fontSize: 10, padding: '6px 8px' } }}
                >Search</TextField>
                </Box>
            ),},
    
        { field: 'serialNumber', headerName: 'Serial Number', width: 200, sortable: true, renderHeader: () => (
                <Box display="flex" flexDirection="column" alignItems="center" padding={0}>
                <div>Serial Number</div>
                <TextField
                    label="Search"
                    variant="outlined"
                    size="small"
                    onChange={handleSearch}
                    InputLabelProps={{ style: { fontSize: 11 } }}
                    style={{ marginTop: 2, width: 140, height: 30 }}
                    inputProps={{ style: { fontSize: 10, padding: '6px 8px' } }}
                >Search</TextField>
                </Box>
            ),},


        { field: 'assignedTo', headerName: 'Assigned To', width: 200, sortable: true, renderHeader: () => (
                <Box display="flex" flexDirection="column" alignItems="center" padding={0}>
                <div>Assigned To</div>
                <TextField
                    label="Search"
                    variant="outlined"
                    size="small"
                    onChange={handleSearch}
                    InputLabelProps={{ style: { fontSize: 11 } }}
                    style={{ marginTop: 2, width: 140, height: 30 }}
                    inputProps={{ style: { fontSize: 10, padding: '6px 8px' } }}
                >Search</TextField>
                </Box>
            ),},
        { field: 'location', headerName: 'Location', width: 200, sortable: true, renderHeader: () => (
                <Box display="flex" flexDirection="column" alignItems="center" padding={0}>
                <div>Location</div>
                <TextField
                    label="Search"
                    variant="outlined"
                    size="small"
                    onChange={handleSearch}
                    InputLabelProps={{ style: { fontSize: 11 } }}
                    style={{ marginTop: 2, width: 140, height: 30 }}
                    inputProps={{ style: { fontSize: 10, padding: '6px 8px' } }}
                >Search</TextField>
                </Box>
            ),},
        {
            field: 'actions',
            headerName: 'Actions',
            width: 300,
            sortable: false,
            renderCell: (params) => (
                <Box>
                    <Button variant="contained" color="primary" style={{ marginRight: 8 }}>
                        View
                    </Button>
                    
                    <Button variant="contained" color="secondary" style={{ marginRight: 8 }}>
                        Edit
                    </Button>
                    <Button variant="contained" color="error">
                        Delete
                    </Button>
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
        <div>
            <main className={classes.content}>
                <Container maxWidth="lg">
                    <div className={classes.filterContainer}>
                        {/* <Box display="flex" alignItems="center"> */}
                        <Button
                            variant="contained"
                            color="primary"
                            style={{ marginRight: 10 }}
                            startIcon={<AddCircleOutlineIcon />} // Add the icon as startIcon
                            onClick={handleOpenModal}
                        >
                            Add Asset
                        </Button>
                        <ExportButton
                             type="assets"
                             status={exportType}
                             filter={filterValue}
                             buttonLabel={`Export ${exportType} Assets`}
                             filePrefix="Verinite"
                        />
                        <TextField
                            label="Search"
                            variant="standard"
                            onChange={handleSearch}
                            style={{ width: 400 }}
                        />
                        {/* </Box> */}
                        {/* <Box display="flex" alignItems="center" justifyContent={'centr'}> */}
                        <ButtonGroup variant="contained" style={{ boxShadow: 'none', gap:'3px' }}>
                            <Button className="btn-border" onClick={filterUnassigned}>Unassigned</Button>
                            <Button className="btn-border" onClick={filterAssigned}>Assigned</Button>
                            <Button className="btn-border" onClick={filterScrap}>Scrap</Button>
                            <Button className="btn-border" variant="contained" onClick={resetFilters}>
                                Reset
                            </Button>
                            {/* Add buttons for other actions */}
                        </ButtonGroup>
                        {/* </Box> */}
                    </div>
                    <div style={{ height: 400, width: '100%', display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                        <div style={{ height: 350, marginLeft: '2%', width: '95%', flexGrow: 1 }}>
                            <DataGrid
                                rows={filteredRows}
                                columns={columns}
                                pageSize={5}
                                rowsPerPageOptions={[5, 10, 20]}
                            />
                        </div>
                    </div>
                </Container>
                
                <AddAssetModal open={openModal} handleClose={handleClose} />

            </main>
        </div>
    );
}

export default Assets;
