import React, { useState } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { Container, Box, Button, ButtonGroup } from '@mui/material';
import { DataGrid } from '@mui/x-data-grid';
import TextField from '@mui/material/TextField';
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline';
import "../Style/Assethistory.css";
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

function Assethistory() {
  const classes = useStyles();
  const [openModal, setOpenModal] = useState(false);

  const handleOpenModal = () => {
    setOpenModal(true);
  };

  const handleClose = () => {
    setOpenModal(false);
  };

  const handleHistory = (row) => {
    console.log("History clicked for:", row);
    // You can open a modal or navigate to a detailed page here
  };

  const rows = [
    {
      id: 1,
      historyId: 1001,
      assetId: 101,
      assetName: 'Laptop',
      serialNumber: 'SN001',
      location: 'Room A',
      assignedTo: 'User A',
      returnDate: '2024-05-10',
      status: 'Assigned',
      type: 'Electronics',
      purchaseDate: '2022-01-15',
      warrantyDate: '2025-01-15',
      empId: 'EMP001',
      locCode: 101,
      modelName: 'Dell XPS 13',
      operatingSystem: 'Windows 11',
      assignedDate: '2024-04-01',
      assignedBy: 'Admin',
      assertSourcedBy: 'Vendor A'
    },
    {
      id: 2,
      historyId: 1002,
      assetId: 102,
      assetName: 'Desktop',
      serialNumber: 'SN002',
      location: 'Room B',
      assignedTo: 'User B',
      returnDate: '2024-06-10',
      status: 'Unassigned',
      type: 'Electronics',
      purchaseDate: '2021-07-10',
      warrantyDate: '2024-07-10',
      empId: 'EMP002',
      locCode: 102,
      modelName: 'HP EliteDesk',
      operatingSystem: 'Windows 10',
      assignedDate: '2023-12-01',
      assignedBy: 'Manager',
      assertSourcedBy: 'Vendor B'
    }
  ];

  const columns = [
    { field: 'assetName', headerName: 'Asset Name', width: 150 },
    { field: 'serialNumber', headerName: 'Serial Number', width: 200 },
    { field: 'assignedTo', headerName: 'Assigned To', width: 200 },
    { field: 'location', headerName: 'Location', width: 150 },
    {
      field: 'actions',
      headerName: 'Actions',
      width: 400,
      sortable: false,
      renderCell: (params) => (
        <Box>
          <Button variant="contained" color="primary" style={{ marginRight: 8 }}>
            View
          </Button>
          <Button variant="contained" color="secondary" style={{ marginRight: 8 }}>
            Edit
          </Button>
          <Button variant="contained" color="error" style={{ marginRight: 8 }}>
            Delete
          </Button>
          <Button
            variant="outlined"
            color="info"
            onClick={() => handleHistory(params.row)}
          >
            History
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

    const filtered = rows.filter(row =>
      row.assetName.toLowerCase().includes(lowercaseValue) ||
      row.serialNumber.toLowerCase().includes(lowercaseValue) ||
      row.location.toLowerCase().includes(lowercaseValue) ||
      row.assignedTo.toLowerCase().includes(lowercaseValue)
    );

    setFilteredRows(filtered);
  };

  const resetFilters = () => {
    setFilterValue('');
    setFilteredRows(rows);
  };

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
            <Button
              variant="contained"
              color="primary"
              style={{ marginRight: 10 }}
              startIcon={<AddCircleOutlineIcon />}
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
            <ButtonGroup variant="contained" style={{ boxShadow: 'none' }}>
              <Button className="btn-border" onClick={filterUnassigned}>Unassigned</Button>
              <Button className="btn-border" onClick={filterAssigned}>Assigned</Button>
              <Button className="btn-border" onClick={filterUnassigned}>Scrap</Button>
              <Button className="btn-border" variant="contained" onClick={resetFilters}>
                Reset
              </Button>
            </ButtonGroup>
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

export default Assethistory;
