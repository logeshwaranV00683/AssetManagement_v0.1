import React, { useState } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { Container, Box, Button, ButtonGroup } from '@mui/material';
import { DataGrid } from '@mui/x-data-grid';
import TextField from '@mui/material/TextField';
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline';
import "./Assets.css"
import AddAssetModal from './AddAssetModal';
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

    const handleOpenModal = () => {
        setOpenModal(true);
    };
    const rows = [
        { id: 1, name: 'Laptop', serialNumber: 'SN001', location: 'Room A', assignedTo: 'User A' },
        { id: 2, name: 'Desktop', serialNumber: 'SN002', location: 'Room B', assignedTo: 'User B' },
        // Add more rows as needed
    ];

    const columns = [
        { field: 'name', headerName: 'Name', width: 150, sortable: true },
        { field: 'serialNumber', headerName: 'Serial Number', width: 200, sortable: true },
        { field: 'assignedTo', headerName: 'Assigned To', width: 200, sortable: true },
        { field: 'location', headerName: 'Location', width: 150, sortable: true },
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
    };
    const handleClose = () => {
        setOpenModal(false)
    }

    const filterAssigned = () => {
        const assignedRows = rows.filter(row => row.assignedTo !== 'Unassigned');
        setFilteredRows(assignedRows);
    };

    const filterUnassigned = () => {
        const unassignedRows = rows.filter(row => row.assignedTo === 'Unassigned');
        setFilteredRows(unassignedRows);
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
                        <TextField
                            label="Search"
                            variant="standard"
                            onChange={handleSearch}
                            style={{ width: 400 }}
                        />
                        {/* </Box> */}
                        {/* <Box display="flex" alignItems="center" justifyContent={'centr'}> */}
                        <ButtonGroup variant="contained" style={{ boxShadow: 'none' }}>
                            <Button className="btn-border" onClick={filterUnassigned}>Unassigned</Button>
                            <Button className="btn-border" onClick={filterAssigned}>Assigned</Button>
                            <Button className="btn-border" onClick={filterUnassigned}>Scrap</Button>
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
