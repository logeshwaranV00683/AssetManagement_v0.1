import React, { useState } from "react";
import { makeStyles } from "@material-ui/core/styles";
import { Container, TextField, Box, Button } from "@mui/material";
import { DataGrid } from "@mui/x-data-grid";

const useStyles = makeStyles(() => ({
  content: {
    flexGrow: 1,
  },
  layoutContainer: {
    display: "flex",
    justifyContent: "space-evenly",
    alignItems: "center",
    marginTop: "30px",
    marginBottom: "16px",
  },
}));

// Dummy employee data
const employees = [
  {
    id: "EMP001",
    name: "Suresh Kumar",
    profilePic: "https://randomuser.me/api/portraits/men/45.jpg",
    assets: [
      { id: 1, productName: "Laptop", serialName: "LTP-SK-001" },
      { id: 2, productName: "Mouse", serialName: "MSE-SK-001" },
      { id: 3, productName: "Mouse", serialName: "MSE-SK-001" },
      { id: 4, productName: "Mouse", serialName: "MSE-SK-001" },
      { id: 5, productName: "Mouse", serialName: "MSE-SK-001" },
    ],
  },
  {
    id: "EMP002",
    name: "Divya M",
    profilePic: "https://randomuser.me/api/portraits/women/55.jpg",
    assets: [{ id: 1, productName: "Monitor", serialName: "MON-DM-001" }],
  },
  {
    id: "EMP003",
    name: "Kumar",
    profilePic: "https://randomuser.me/api/portraits/men/45.jpg",
    assets: [
      { id: 1, productName: "Laptop", serialName: "LTP-SK-001" },
      { id: 2, productName: "Mouse", serialName: "MSE-SK-001" },
      { id: 3, productName: "Mouse", serialName: "MSE-SK-001" },
      { id: 4, productName: "Mouse", serialName: "MSE-SK-001" },
      { id: 5, productName: "Mouse", serialName: "MSE-SK-001" },
    ],
  },
];

function AssetsToEmployee() {
  const classes = useStyles();
  const [searchText, setSearchText] = useState("");
  const [selectedEmployee, setSelectedEmployee] = useState(null);

  const columns = [
    { field: "productName", headerName: "PRODUCT", width: 150 },
    { field: "serialName", headerName: "SERIAL NUMBER", width: 200 },
  ];

  const handleSearch = () => {
    const employee = employees.find(
      (e) => e.id.toLowerCase() === searchText.toLowerCase()
    );
    setSelectedEmployee(employee || null);
  };

  return (
    <div style={{ width: "185vh" }}>
      <main className={classes.content}>
        <Container maxWidth="lg">
          {/* 🔍 Search Bar */}
          <Box
            display="flex"
            justifyContent="center"
            alignItems="center"
            mt={8}
            gap={2}
          >
            <TextField
              label="Enter Employee ID"
              variant="outlined"
              value={searchText}
              onChange={(e) => setSearchText(e.target.value)}
              sx={{
                width: { xs: "100%", md: "85vw" },
                maxWidth: "800px",
                "& .MuiOutlinedInput-root": {
                  background: "#ffffff",
                  borderRadius: "15px",
                  color: "#083A40",
                  fontWeight: 500,
                  boxShadow:
                    "0 0 6px rgba(255, 255, 255, 0.8), 0 0 12px rgba(109, 224, 255, 0.6)",
                  "& fieldset": { border: "0.5px solid transparent" },
                  "&:hover fieldset": { border: "0.5px solid #1FCBEA" },
                  "&.Mui-focused fieldset": {
                    boxShadow:
                      "0 0 6px rgba(255, 255, 255, 0.8), 0 0 12px rgba(109, 224, 255, 0.6)",
                    fontSize: "20px",
                  },
                  "& input": {
                    background: "transparent",
                    color: "#083A40",
                    fontFamily: "'Racing Sans One', sans-serif",
                  },
                },
                "& .MuiInputLabel-root": {
                  color: "#083A40",
                  fontFamily: "'Racing Sans One', sans-serif",
                  letterSpacing: "1.0px",
                },
                "& .Mui-focused .MuiInputLabel-root": {
                  color: "#083A40",
                },
                "& .MuiInputLabel-shrink": {
                  transform: "translate(18px, -30px) scale(1.0)",
                  background: "transparent",
                  color: "#fff",
                  padding: "0 6px",
                },
              }}
            />
            <Button
              variant="contained"
              onClick={handleSearch}
              sx={{
                background: "linear-gradient(45deg, #6DE0FF, #2BC4F3)",
                color: "#083A40",
                fontFamily: "'Racing Sans One', sans-serif",
                fontWeight: "bold",
                padding: "8px 20px",
              }}
            >
              Search
            </Button>
          </Box>

          <Box display="flex" justifyContent="center" mt={8} width="120%">
            {/* 👤 Employee Info */}
            {selectedEmployee && (
              <Box
                minHeight="50vh"
                display="flex"
                flexDirection="row"
                justifyContent="center"
                alignItems="flex-start"
                gap={4}
                padding={4}
                bgcolor="transparent"
                borderRadius="15px"
                boxShadow="0 0 12px rgba(0,0,0,0.1)"
                width="90%"
                maxWidth="950px"
              >
                {/* 📸 Left - Profile Pic */}
                <Box>
                  <img
                    src={
                      selectedEmployee.profilePic ||
                      "https://via.placeholder.com/150?text=No+Image"
                    }
                    alt="Profile"
                    style={{
                      width: "300px",
                      height: "300px",
                      borderRadius: "50%",
                      border: "4px solid #2BC4F3",
                      objectFit: "cover",
                    }}
                  />
                </Box>

                {/* 📋 Right - Name & Assets */}
                <Box flex={1}>
                  <h2
                    style={{
                      marginBottom: "32px",
                      color: "#083A40",
                      fontFamily: "'Racing Sans One', sans-serif",
                    }}
                  >
                    {selectedEmployee.name} ({selectedEmployee.id})
                  </h2>

                  <div style={{ height: 300, width: "60%" }}>
                    <DataGrid
                      rows={selectedEmployee.assets}
                      columns={columns}
                      pageSize={5}
                      rowsPerPageOptions={[5, 10, 20]}
                      sx={{
                        borderRadius: "16px",
                        overflow: "hidden",
                        border: "2px solid #1FCBEA",
                        boxShadow: "0 0 3px #6DE0FF, 0 0 4px #2BC4F3",
                        fontFamily: "'Racing Sans One', sans-serif",
                        color: "#083A40",
                        "& .MuiDataGrid-columnHeaders": {
                          background:
                            "linear-gradient(45deg, #6DE0FF, #2BC4F3)",
                          color: "#083A40",
                          fontSize: "16px",
                          fontWeight: 700,
                        },
                        "& .MuiDataGrid-cell": {
                          background: "#F0FBFF",
                          color: "#083A40",
                          fontSize: "15px",
                          borderBottom: "1px solid #D0F0FF",
                        },
                        "& .MuiDataGrid-footerContainer": {
                          background:
                            "linear-gradient(45deg, #6DE0FF, #2BC4F3)",
                          color: "#083A40",
                          fontWeight: 600,
                        },
                        "& .MuiDataGrid-row:hover": {
                          backgroundColor: "#E0F9FF",
                        },
                        "& .MuiDataGrid-selectedRowCount": {
                          color: "#083A40",
                        },
                        "& .MuiCheckbox-root": {
                          color: "#083A40",
                        },
                      }}
                    />
                  </div>
                </Box>
              </Box>
            )}
          </Box>
        </Container>
      </main>
    </div>
  );
}

export default AssetsToEmployee;
