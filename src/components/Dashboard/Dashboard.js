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
        <Typography variant="h4" align="center" sx={{ color: "#00f0ff", mb: 2 }}>
          Asset Overview - {selectedDevice.toUpperCase()} At {selectedLocation.toUpperCase()}
        </Typography>

        {/* Section Grid */}
        <Box
          sx={{
            mt: 2,
            display: "flex",
            flexDirection: { xs: "column", sm: "row" },
            gap: 3,
            backdropFilter: "blur(8px)",
          }}
        >
          {/* Left: Locations */}
          <Box
            sx={{
              flex: 1.5,
              maxHeight: 400,
              overflowY: "auto",
              p: 2,
              borderRadius: 2,
              border: "1px solid rgba(0, 240, 255, 0.3)",
              background: "rgba(255, 255, 255, 0.05)",
            }}
          >
            <Typography sx={{ mb: 1, color: "#00f0ff", fontWeight: "bold" }}>Select Location</Typography>
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
                  boxShadow: selectedLocation === loc ? "0 0 10px #00f0ff" : "none",
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
              flex: 3,
              minHeight: 400,
              p: 2,
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              borderRadius: 2,
              background: "rgba(255, 255, 255, 0.05)",
              border: "1px solid rgba(0, 240, 255, 0.3)",
            }}
          >
            <Interactive3DChart data={chartData} />
          </Box>

          {/* Right: Devices */}
          <Box
            sx={{
              flex: 1.5,
              maxHeight: 400,
              overflowY: "auto",
              p: 2,
              borderRadius: 2,
              background: "rgba(255, 255, 255, 0.05)",
              border: "1px solid rgba(0, 240, 255, 0.3)",
            }}
          >
            <Typography sx={{ mb: 1, color: "#00f0ff", fontWeight: "bold" }}>Select Device</Typography>
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
                  boxShadow: selectedDevice === type ? "0 0 10px #00f0ff" : "none",
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
        <Box sx={{ mt: 3, mb: 4 }}>
          <TextField
            label="Search"
            fullWidth
            value={filterValue}
            onChange={(e) => setFilterValue(e.target.value)}
            sx={{
              backgroundColor: "#E0F9FF",
              borderRadius: "12px",
              boxShadow: "0 0 6px rgba(0, 240, 255, 0.3)",
              input: {
                fontFamily: "'Racing Sans One', sans-serif",
                color: "#083A40",
              },
            }}
          />
        </Box>

        {/* Data Table */}
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
      </Container>
    </div>
  );
}

export default Dashboard;
