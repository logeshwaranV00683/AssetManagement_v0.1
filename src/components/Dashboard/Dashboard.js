import React, { useState, useEffect } from "react";
import {
  Container,
  Typography,
  Box,
  TextField,
  Button,
} from "@mui/material";
import Interactive3DChart from '../Utils/Interactive3DChart'
import { DataGrid } from "@mui/x-data-grid";
import { getAssetTypes, getLocations, getcountsByLocation } from "../Services/DashboardService";
import "../Style/font.css";

function Dashboard() {
  const [locationOptions, setLocationOptions] = useState([]);
  const [selectedLocation, setSelectedLocation] = useState("chennai");
  const [selectedDevice, setSelectedDevice] = useState("Laptop");
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

        const [locations, types] = await Promise.all([
          getLocations(),
          getAssetTypes(),
        ]);
        setLocationOptions(locations);
        setDeviceOptions(types);
      } catch (err) {
        console.error("Error fetching dashboard data:", err);
      }
    };

    loadDashboardData();
  }, [selectedLocation, selectedDevice]);

  return (
    <div style={{ fontFamily: "'Racing Sans One', sans-serif" }}>
      <Container maxWidth="lg" sx={{ mt: 4 }}>
        <Typography variant="h4" align="center" sx={{ color: "#00f0ff" }}>
          Asset Overview - {selectedDevice} @ {selectedLocation.toUpperCase()}
        </Typography>

        <Box
          sx={{
            mt: 4,
            display: "flex",
            justifyContent: "space-between",
            flexDirection: { xs: "column", sm: "row" },
            gap: 4,
          }}
        >
          <Box>
            <Typography sx={{ mb: 1, color: "#00f0ff" }}>Select Location</Typography>
            {locationOptions.map((loc) => (
              <Button
                key={loc}
                onClick={() => setSelectedLocation(loc)}
                variant={selectedLocation === loc ? "contained" : "outlined"}
                sx={{
                  display: "block",
                  mb: 1,
                  backgroundColor: selectedLocation === loc ? "#00e0ff" : "#ffffff",
                  color: selectedLocation === loc ? "#fff" : "#083A40",
                  boxShadow: selectedLocation === loc ? "0 0 10px #00f0ff" : "none",
                }}
              >
                {loc.toUpperCase()}
              </Button>
            ))}
          </Box>

          <Box>
            <Typography sx={{ mb: 1, color: "#00f0ff" }}>Select Device</Typography>
            {deviceOptions.map((type) => (
              <Button
                key={type}
                onClick={() => setSelectedDevice(type)}
                variant={selectedDevice === type ? "contained" : "outlined"}
                sx={{
                  display: "block",
                  mb: 1,
                  backgroundColor: selectedDevice === type ? "#00e0ff" : "#ffffff",
                  color: selectedDevice === type ? "#fff" : "#083A40",
                  boxShadow: selectedDevice === type ? "0 0 10px #00f0ff" : "none",
                }}
              >
                {type}
              </Button>
            ))}
          </Box>
        </Box>

        {/* 3D Chart Placeholder - Will be replaced by Interactive3DChart component */}
        <Box sx={{ mt: 6, height: 400 }}>
          <Interactive3DChart data={chartData} />
        </Box>


        <Box sx={{ mt: 8, mb: 6 }}>
          <TextField
            label="Search"
            fullWidth
            value={filterValue}
            onChange={(e) => setFilterValue(e.target.value)}
            sx={{
              background: "#ffffff",
              borderRadius: "12px",
              boxShadow: "0 0 8px rgba(0, 240, 255, 0.4)",
              input: {
                fontFamily: "'Racing Sans One', sans-serif",
                color: "#083A40",
              },
            }}
          />
        </Box>

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
            background: "#F0FBFF",
            color: "#083A40",
            fontFamily: "'Racing Sans One', sans-serif",
            "& .MuiDataGrid-columnHeaders": {
              background: "linear-gradient(45deg, #6DE0FF, #2BC4F3)",
              color: "#083A40",
              fontWeight: "bold",
              fontSize: "16px",
            },
            "& .MuiDataGrid-row:hover": {
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