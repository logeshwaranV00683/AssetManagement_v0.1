import React, { useState } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { Container, TextField, Box, Button } from '@mui/material';
import { DataGrid } from '@mui/x-data-grid';

const useStyles = makeStyles(() => ({
  content: {
    flexGrow: 1,
  },
  layoutContainer: {
    display: 'flex',
    justifyContent: 'space-evenly',
    alignItems: 'center',
    marginTop: '30px',
    marginBottom: '16px',
  },
}));

// Dummy employee data
const employees = [
  {
    id: 'EMP001',
    name: 'Suresh Kumar',
    profilePic: 'https://randomuser.me/api/portraits/men/45.jpg',
    assets: [
      { id: 1, productName: 'Laptop', serialName: 'LTP-SK-001' },
      { id: 2, productName: 'Mouse', serialName: 'MSE-SK-001' },
      { id: 3, productName: 'Mouse', serialName: 'MSE-SK-001' },
      { id: 4, productName: 'Mouse', serialName: 'MSE-SK-001' },
      { id: 5, productName: 'Mouse', serialName: 'MSE-SK-001' },

    ],
  },
  {
    id: 'EMP002',
    name: 'Divya M',
    profilePic: 'https://randomuser.me/api/portraits/women/55.jpg',
    assets: [
      { id: 1, productName: 'Monitor', serialName: 'MON-DM-001' },
    ],
  },
];

function AssetsToEmployee() {
  const classes = useStyles();
  const [searchText, setSearchText] = useState('');
  const [selectedEmployee, setSelectedEmployee] = useState(null);

  const columns = [
    { field: 'productName', headerName: 'PRODUCT', width: 150 },
    { field: 'serialName', headerName: 'SERIAL NUMBER', width: 200 },
  ];

  const handleSearch = () => {
    const employee = employees.find(e => e.id.toLowerCase() === searchText.toLowerCase());
    setSelectedEmployee(employee || null);
  };

  return (
    <div style={{ width: '185vh' }}>
      <main className={classes.content}>
        <Container maxWidth="lg">

          {/* 🔍 Centered Search Bar */}
          <Box display="flex" justifyContent="center" alignItems="center" mt={8} gap={2}>
            <TextField
              label="Enter Employee ID"
              variant="outlined"
              value={searchText}
              onChange={(e) => setSearchText(e.target.value)}
              sx={{
                  width: { xs: '100%', md: '85vw' },
                  maxWidth: '1000px',
                  '& .MuiOutlinedInput-root': {
                    background: '#ffffff',
                    borderRadius: '15px',
                    color: '#083A40',
                    fontWeight: 500,
                    boxShadow: '0 0 6px rgba(255, 255, 255, 0.8), 0 0 12px rgba(109, 224, 255, 0.6)',
                    '& fieldset': { border: '0.5px solid transparent' },
                    '&:hover fieldset': { border: '0.5px solid #1FCBEA' },
                    '&.Mui-focused fieldset': {
                      boxShadow: '0 0 6px rgba(255, 255, 255, 0.8), 0 0 12px rgba(109, 224, 255, 0.6)',
                      fontSize: '20px',
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
                    letterSpacing: '1.0px',
                    
                  },
                  '& .Mui-focused .MuiInputLabel-root': {
                    color: '#083A40',
                  },
                  '& .MuiInputLabel-shrink': {
                      transform: 'translate(18px, -30px) scale(1.0)',
                      background: 'transparent',
                      color: '#fff',
                      padding: '0 6px',
                    },
    
                }}
            />
            <Button
              variant="contained"
              onClick={handleSearch}
              sx={{
                background: 'linear-gradient(45deg, #6DE0FF, #2BC4F3)',
                color: '#083A40',
                fontFamily: "'Racing Sans One', sans-serif",
                fontWeight: 'bold',
                padding: '8px 20px',
              }}
            >
              Search
            </Button>
          </Box>

          {/* 📋 Table + 🖼️ Profile Side by Side */}
          {selectedEmployee && (
            <Box display="flex" alignItems="flex-start" gap={20} mt={6} ml={30}>
              {/* Data Table */}
              <div style={{ height: 300, width: 400 }}>
                <DataGrid
                  rows={selectedEmployee.assets}
                  columns={columns}
                  hideFooter
                  disableColumnMenu
                  disableRowSelectionOnClick
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
                    '& .MuiDataGrid-row:hover': {
                      backgroundColor: '#E0F9FF',
                    },
                  }}
                />
              </div>

              {/* Profile Picture */}
              <div style={{
                height: 300,
                width: 300,
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                borderRadius: '50%',
                overflow: 'hidden',
                border: '2px solid #1FCBEA',
                boxShadow: '0 0 6px #6DE0FF, 0 0 8px #2BC4F3',
                backgroundColor: '#ffffff',
              }}>
                <img
                  src={selectedEmployee.profilePic}
                  alt={selectedEmployee.name}
                  style={{
                    width: '100%',
                    height: '100%',
                    objectFit: 'cover',
                    borderRadius: '12px',
                  }}
                />
              </div>
            </Box>
          )}

        </Container>
      </main>
    </div>
  );
}

export default AssetsToEmployee;
