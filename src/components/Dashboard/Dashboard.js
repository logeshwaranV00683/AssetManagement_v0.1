import React, { useState, useEffect } from "react";
import {
  Container,
  Typography,
  Box,
  TextField,
  Button,
} from "@mui/material";
import Interactive3DChart from '../Utils/Interactive3DChart';
import { DataGrid } from "@mui/x-data-grid";
import { getAssetTypes, getLocations, getcountsByLocation } from "../Services/DashboardService";
import "../Style/font.css";


function Dashboard() {
  const [locationOptions, setLocationOptions] = useState([]);
  const [selectedLocation, setSelectedLocation] = useState("chennai");
  const [selectedDevice, setSelectedDevice] = useState("");
  const [filterValue, setFilterValue] = useState("");
  const [assetCountData, setAssetCountData] = useState([]);
  const [deviceOptions, setDeviceOptions] = useState([]);
  const [chartData, setChartData] = useState([]);

  const handleSearch = (event) => {
    event.target.value.toLowerCase();
    setFilterValue(event.target.value);
  };

  useEffect(() => {
    const loadDashboardData = async () => {
      try {
        const countsData = await getcountsByLocation(selectedLocation);
        const formattedData = Array.isArray(countsData)
          ? countsData.map((item, idx) => ({
            id: item.id ?? idx + 1,
            type: item.type,
            total: item.total ?? 0,
            assigned: item.assigned ?? 0,
            unassigned: item.unassigned ?? 0,
            scrapped: item.scrapped ?? 0,
          }))
          : [];

        setAssetCountData(formattedData);

        const [locations, types] = await Promise.all([
          getLocations(),
          getAssetTypes(),
        ]);

        setLocationOptions(locations);
        setDeviceOptions(types);

        if (types.length > 0 && !types.includes(selectedDevice)) {
          setSelectedDevice(types[0]);
        }

        const pieData = formattedData.find(
          (d) => d.type.toLowerCase() === selectedDevice.toLowerCase()
        );

        if (pieData) {
          setChartData([
            { name: "Assigned", value: pieData.assigned },
            { name: "Unassigned", value: pieData.unassigned },
            { name: "Scrapped", value: pieData.scrapped },
          ]);
        } else {
          setChartData([]);
        }
      } catch (err) {
        console.error("Error fetching dashboard data:", err);
      }
    };

    loadDashboardData();
  }, [selectedLocation, selectedDevice]);

  return (
    <div style={{ fontFamily: "'Racing Sans One', sans-serif" }}>
      <Container maxWidth="lg" sx={{ mt: 4 }}>
        <Typography variant="h4" align="center" sx={{ color: "#00f0ff", mb: 2, fontFamily: "'Racing Sans One', sans-serif", marginBottom: "2rem" }}>
          Asset Overview - {selectedDevice.toUpperCase()} at {selectedLocation.toUpperCase()}
        </Typography>

        {/* 1 Section Grid */}
        <Box
          sx={{
            width: "90%",
            maxWidth: "100%",
            mx: "auto",
            px: 2,
            mt: 2,
            display: "flex",
            flexWrap: "wrap",
            justifyContent: "space-between",
            gap: 5,
          }}
        >

          {/* Left: Locations */}
          <Box
            sx={{
              width: { xs: "100%", sm: "20%" },
              flex: 1.5,
              maxHeight: 400,
              overflowY: "auto",
              p: 2,
              borderRadius: 2,
              border: "1px solid rgba(0, 240, 255, 0.3)",
              background: "rgba(255, 255, 255, 0.05)",
              "&::-webkit-scrollbar": {
                width: 0,
              },
              "&::-webkit-scrollbar-thumb": {
                backgroundColor: "transparent",
              },

            }}
          >
            <Typography sx={{
              mb: 1, color: "#00f0ff", fontWeight: "bold", fontFamily: "'Racing Sans One', sans-serif",
              letterSpacing: "1px", marginBottom: "1rem"
            }}>Select Location</Typography>
            {locationOptions.map((loc) => (
              <Button
                key={loc}
                onClick={() => setSelectedLocation(loc)}
                variant={selectedLocation === loc ? "contained" : "outlined"}
                sx={{
                  display: "block",
                  mb: 1,
                  backgroundColor: selectedLocation === loc ? "#00e0ff" : "transparent",
                  color: selectedLocation === loc ? "#fff" : "#00f0ff",
                  borderColor: "#00e0ff",
                  fontFamily: "'Racing Sans One', sans-serif",
                  letterSpacing: "1.5px",
                  width: "100%",
                  fontWeight: 600,
                  textTransform: "none",
                }}
              >
                {loc.toUpperCase()}
              </Button>
            ))}
          </Box>

          {/* Center: 3D Chart */}
          <Box
            sx={{
              width: { xs: "100%", sm: "50%" },
              flex: 3,
              minHeight: 400,
              p: 2,
              display: "flex",
              alignItems: "center",
              justifyContent: "center",

            }}
          >
            <Interactive3DChart data={chartData} />
          </Box>

          {/* Right: Devices */}
          <Box
            sx={{
              width: { xs: "100%", sm: "15%" },
              flex: 1.5,
              maxHeight: 400,
              overflowY: "auto",
              p: 2,
              borderRadius: 2,
              background: "rgba(255, 255, 255, 0.05)",
              border: "1px solid rgba(0, 240, 255, 0.3)",
              "&::-webkit-scrollbar": {
                width: 0,
              },
              "&::-webkit-scrollbar-thumb": {
                backgroundColor: "transparent",
              },

            }}
          >
            <Typography sx={{
              mb: 1, color: "#00f0ff", fontWeight: "bold", fontFamily: "'Racing Sans One', sans-serif",
              letterSpacing: "1px", marginBottom: "1rem"
            }}>Select Device</Typography>
            {deviceOptions.map((type) => (
              <Button
                key={type}
                onClick={() => setSelectedDevice(type)}
                variant={selectedDevice === type ? "contained" : "outlined"}
                sx={{
                  display: "block",
                  mb: 1,
                  backgroundColor: selectedDevice === type ? "#00e0ff" : "transparent",
                  color: selectedDevice === type ? "#fff" : "#00f0ff",
                  borderColor: "#00e0ff",
                  fontFamily: "'Racing Sans One', sans-serif",
                  letterSpacing: "1.5px",
                  width: "100%",
                  fontWeight: 600,
                  textTransform: "none",
                }}
              >
                {type.toUpperCase()}
              </Button>
            ))}
          </Box>
        </Box>

        {/* Search Field */}
        <div style={{ display: "flex", justifyContent: "center", width: "100%", marginTop: "3rem", marginBottom: "2rem" }}>
          <div style={{ width: "55%", minWidth: "200px" }}>
            <TextField
              label="Search"
              variant="outlined"
              onChange={handleSearch}
              value={filterValue}
              placeholder="Search"
              fullWidth
              sx={{
                "& .MuiOutlinedInput-root": {
                  background: "#ffffff",
                  borderRadius: "0.6rem",
                  fontWeight: 500,
                  boxShadow: "0 0 0.3rem rgba(109, 224, 255, 0.4)",
                  "& fieldset": { border: "1px solid #ccc" },
                  "&:hover fieldset": { borderColor: "#1FCBEA" },
                  "&.Mui-focused fieldset": { borderColor: "#1FCBEA" },
                  "& input": {
                    fontFamily: "'Racing Sans One', sans-serif",
                    fontSize: "0.9rem",
                    color: "#083A40",
                    paddingTop: "20px",
                  },
                },
                "& .MuiInputLabel-root": {
                  fontFamily: "'Racing Sans One', sans-serif",
                  fontSize: "1rem",
                  transition: "all 0.2s ease",
                },
                "& .MuiInputLabel-root.Mui-focused": {
                  transform: "translate(14px, -25px) scale(0.85)",
                  fontSize: "1.3rem",
                  padding: "0 4px",
                  color: "#fff",
                },
              }}
            />
          </div>
        </div>

        {/* Data Table */}
        <div style={{ width: "90%", maxWidth: "1200px", margin: "2rem auto" }}>
          <DataGrid
            autoHeight
            rows={assetCountData.filter((row) =>
              row.type.toLowerCase().includes(filterValue.toLowerCase())
            )}
            columns={[
              { field: "type", headerName: "Asset Type", flex: 1, align: "center", headerAlign: "center" },
              { field: "total", headerName: "Total", flex: 1, align: "center", headerAlign: "center" },
              { field: "assigned", headerName: "Assigned", flex: 1, align: "center", headerAlign: "center" },
              { field: "unassigned", headerName: "Unassigned", flex: 1, align: "center", headerAlign: "center" },
              { field: "scrapped", headerName: "Scrapped", flex: 1, align: "center", headerAlign: "center" },
            ]}
            getRowId={(row) => row.id}
            sx={{

              borderRadius: 2,
              boxShadow: "0 0 10px rgba(0,0,0,0.1)",
              background: "#E0F9FF",
              color: "#083A40",
              fontFamily: "'Racing Sans One', sans-serif",
              "& .MuiDataGrid-columnHeaders": {
                background: "linear-gradient(45deg, #6DE0FF, #2BC4F3)",
                color: "#083A40",
                fontWeight: "bold",
                fontSize: "16px",
              },
              "& .MuiDataGrid-row": {
                backgroundColor: "#E0F9FF",
              },
              "& .MuiDataGrid-footerContainer": {
                background: "linear-gradient(45deg, #6DE0FF, #2BC4F3)",
              },
            }}
          />
        </div>


      </Container>
    </div>
  );
}

export default Dashboard;
