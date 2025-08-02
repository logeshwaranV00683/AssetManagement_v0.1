import React, { useState, useEffect } from "react";
import { makeStyles } from "@material-ui/core/styles";
import {
  PieChart,
  Pie,
  Cell,
  ResponsiveContainer,
  Tooltip,
  Legend,
} from "recharts";
import {
  Container,
  Card,
  CardContent,
  Typography,
  Box,
  TextField,
  MenuItem,
  Select,
  FormControl,
  FormLabel,
} from "@mui/material";
import { DataGrid } from "@mui/x-data-grid";
import "../Style/font.css";
import {
  getAssetTypes,
  getLocations,
  getcountsByLocation,
  getUnassignedAssetsByLocation,
  getAssignedAssetsByLocation,
} from "../Services/DashboardService";

const useStyles = makeStyles((theme) => ({
  content: {
    flexGrow: 1,
    padding: theme.spacing(3),
    fontFamily: "'Racing Sans One', sans-serif",
  },
  pieContainer: {
    display: "flex",
    flexDirection: "column",
    justifyContent: "space-around",
    alignItems: "center",
    marginTop: theme.spacing(3),
    marginLeft: "10px",
    gap: "30px",
    [theme.breakpoints.up("md")]: {
      flexDirection: "row",
      justifyContent: "space-around",
    },
  },
  pieCard: {
    width: "45%",
    fontFamily: "'Racing Sans One', sans-serif",
  },
  label: {
    textAlign: "center",
    color: "grey",
    marginTop: theme.spacing(2),
    fontFamily: "'Racing Sans One', sans-serif",
  },
}));

function Dashboard() {
  const classes = useStyles();
  const COLORS = ["#00e0ff", "#f72585"];
  const [locationOptions, setLocationOptions] = useState([]);
  const [selectedLocation, setSelectedLocation] = useState("chennai");
  const [selectedDevice, setSelectedDevice] = useState("Laptop");
  const [filterValue, setFilterValue] = useState("");
  const [assetCountData,setAssetCountData]= useState([]);
  const [deviceOptions, setDeviceOptions] = useState([]);

 useEffect(() => {
  const loadDashboardData = async () => {
    try {
      const countsData = await getcountsByLocation(selectedLocation);
      const formattedAssetData = Array.isArray(countsData)
        ? countsData.map((item, idx) => ({
            id: item.id ?? idx + 1,
            type: item.type,
            total: item.total ?? 0,
            assigned: item.assigned ?? 0,
            unassigned: item.unassigned ?? 0,
            scrapped: item.scrapped ?? 0,
          }))
        : [];

      if (!Array.isArray(countsData)) {
        console.warn("Asset count response was not an array:", countsData);
      }

      setAssetCountData(formattedAssetData);

      const [locations, assetTypes] = await Promise.all([
        getLocations(),
        getAssetTypes(),
      ]);

      setLocationOptions(locations);
      setDeviceOptions(assetTypes);
    } catch (err) {
      console.error("Failed to load dashboard data:", err);
    }
  };

  loadDashboardData();
}, [selectedLocation]);


  const [pieDataChennai, setPieDataChennai] = useState([]);
  const [pieDataPune, setPieDataPune] = useState([]);

  useEffect(() => {
    const fetchPieDataForLocation = async (location, setDataFn) => {
      try {
        const assignedData = await getAssignedAssetsByLocation(location);
        const unassignedData = await getUnassignedAssetsByLocation(location);

        const key = selectedDevice.toLowerCase().replace(/\s/g, "_");

        const assignedCount = Number(assignedData?.[key] ?? 0);
        const unassignedCount = Number(unassignedData?.[key] ?? 0);

        setDataFn([
          { name: "Assigned", value: assignedCount },
          { name: "Unassigned", value: unassignedCount },
        ]);
      } catch (err) {
        console.error(`Error fetching pie data for ${location}:`, err);
        setDataFn([]);
      }
    };
    fetchPieDataForLocation("chennai", setPieDataChennai);
    fetchPieDataForLocation("pune", setPieDataPune);
  }, [selectedDevice]);

  return (
    <div>
      <main className={classes.content}>
        <Container
          maxWidth="lg"
          style={{ fontFamily: "'Racing Sans One', sans-serif" }}
        >
          <div className={classes.pieContainer}>
            <div
              style={{
                display: "flex",
                justifyContent: "space-between",
                alignItems: "center",
                flexWrap: "wrap",
                gap: "30px",
                width: "80%",
              }}
            >
              {/* Pie Chart - Chennai */}
              <Card
                className={classes.pieCard}
                style={{
                  flex: 1,
                  width: "100%",
                  background: "transparent",
                  boxShadow: "none",
                }}
              >
                <CardContent>
                  <div className={classes.label} style={{ margin: "20px" }}>
                    <Typography
                      variant="h6"
                      style={{ color: "#00f0ff", letterSpacing: "1.5px" }}
                    >
                      {selectedDevice}: {pieDataChennai?.[0]?.value ?? 0}/
                      {pieDataChennai?.[1]?.value ?? 0}
                    </Typography>
                    <Typography
                      variant="h6"
                      style={{ color: "#00f0ff", letterSpacing: "1.5px" }}
                    >
                      CHENNAI
                    </Typography>
                  </div>
                  <div style={{ height: 250, width: "100%" }}>
                    <ResponsiveContainer width="100%" height={260}>
                      <PieChart>
                        <Pie
                          dataKey="value"
                          data={pieDataChennai}
                          cx="50%"
                          cy="50%"
                          outerRadius={90}
                          label
                        >
                          {pieDataChennai.map((entry, index) => (
                            <Cell
                              key={`cell-${index}`}
                              fill={COLORS[index % COLORS.length]}
                            />
                          ))}
                        </Pie>
                        <Tooltip
                          contentStyle={{
                            backgroundColor: "#0d1b2a",
                            border: "1px solid #00f0ff",
                            color: "#ffffff",
                          }}
                          itemStyle={{ color: "#00f0ff" }}
                        />
                        <Legend wrapperStyle={{ color: "#00f0ff" }} />
                      </PieChart>
                    </ResponsiveContainer>
                  </div>
                </CardContent>
              </Card>

              {/* Dropdown */}
              <Box
                sx={{
                  width: 150,
                  display: "flex",
                  justifyContent: "center",
                  alignItems: "center",
                  my: { xs: 2, md: 6 },
                }}
              >
                <FormControl variant="outlined" style={{ width: 150 }}>
                  <FormLabel
                    sx={{
                      fontFamily: "'Racing Sans One', sans-serif",
                      color: "#00f0ff",
                      marginBottom: "8px",
                    }}
                  >
                    Device
                  </FormLabel>
                  <Select
                    value={selectedDevice}
                    onChange={(e) => setSelectedDevice(e.target.value)}
                    label="Device"
                    sx={{
                      fontFamily: "'Racing Sans One', sans-serif",
                      background: "#ffffff",
                      borderRadius: "12px",
                      boxShadow: "0 0 8px rgba(0, 240, 255, 0.4)",
                      color: "#083A40",
                    }}
                  >
                    {deviceOptions.map((device) => (
                      <MenuItem key={device} value={device}>
                        {device}
                      </MenuItem>
                    ))}
                  </Select>
                </FormControl>
              </Box>
              {/* Pie Chart - Pune */}
              <Card
                className={classes.pieCard}
                style={{
                  flex: 1,
                  minWidth: "280px",
                  background: "transparent",
                  boxShadow: "none",
                }}
              >
                <CardContent>
                  <div className={classes.label} style={{ margin: "20px" }}>
                    <Typography
                      variant="h6"
                      style={{ color: "#00f0ff", letterSpacing: "1.5px" }}
                    >
                      {selectedDevice}: {pieDataPune?.[0]?.value ?? 0}/
                      {pieDataPune?.[1]?.value ?? 0}
                    </Typography>
                    <Typography
                      variant="h6"
                      style={{ color: "#00f0ff", letterSpacing: "1.5px" }}
                    >
                      PUNE
                    </Typography>
                  </div>
                  <div style={{ height: 250, width: "100%" }}>
                    <ResponsiveContainer width="100%" height={260}>
                      <PieChart>
                        <Pie
                          dataKey="value"
                          data={pieDataPune}
                          cx="50%"
                          cy="50%"
                          outerRadius={90}
                          label
                        >
                          {pieDataPune.map((entry, index) => (
                            <Cell
                              key={`cell-${index}`}
                              fill={COLORS[index % COLORS.length]}
                            />
                          ))}
                        </Pie>

                        <Tooltip
                          contentStyle={{
                            backgroundColor: "#0d1b2a",
                            border: "1px solid #00f0ff",
                            color: "#ffffff",
                          }}
                          itemStyle={{ color: "#00f0ff" }}
                        />
                        <Legend wrapperStyle={{ color: "#00f0ff" }} />
                      </PieChart>
                    </ResponsiveContainer>
                  </div>
                </CardContent>
              </Card>
            </div>
          </div>

          {/* Search & DataGrid */}
          <div
            style={{
              height: "60vh",
              width: "90%",
              display: "flex",
              margin: "auto",
              marginTop: "10vh",
              flexDirection: "column",
              alignItems: "center",
              marginBottom: "5vh",
            }}
          >
            <Box
              sx={{
                width: "100%",
                display: "flex",
                justifyContent: "center",
                mb: 2,
              }}
            >
              <TextField
                label="Search"
                variant="outlined"
                value={filterValue}
                onChange={(e) => setFilterValue(e.target.value)}
                sx={{
                  width: { xs: "90%", sm: "70%", md: "60%" },
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
                    letterSpacing: "3.0px",
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
            </Box>

            {/* Location Switch */}
           <div>
  <Box
    sx={{
      display: "flex",
      justifyContent: "center",
      alignItems: "center",
      margin: "20px",
    }}
  >
    <Box
      sx={{
        display: "flex",
        backgroundColor: "#fff",
        borderRadius: "10px 20px",
        padding: "2px 0px",
        boxShadow: "0 0 5px rgba(0,0,0,0.2)",
        maxWidth: "50vw", // Restrict width to half of screen
        overflowX: "auto", // Enable horizontal scroll
        scrollbarWidth: "thin", // Firefox
        "&::-webkit-scrollbar": {
          height: "6px",
        },
        "&::-webkit-scrollbar-thumb": {
          backgroundColor: "#bbb",
          borderRadius: "10px",
        },
      }}
    >
      {locationOptions.map((loc) => (
        <Box
          key={loc}
          onClick={() => setSelectedLocation(loc)}
          sx={{
            flexShrink: 0,
            minWidth: 100,
            padding: "8px 18px",
            margin: "0 3px",
            borderRadius: "10px 20px",
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            fontFamily: "'Racing Sans One', sans-serif",
            fontWeight: 600,
            letterSpacing: "1.2px",
            color: selectedLocation === loc ? "#fff" : "#083A40",
            backgroundColor:
              selectedLocation === loc ? "#2196f3" : "#e0f7fa",
            cursor: "pointer",
            transition: "all 0.3s ease",
            "&:hover": {
              backgroundColor:
                selectedLocation === loc ? "#1976d2" : "#b2ebf2",
            },
          }}
        >
          {loc.toUpperCase()}
        </Box>
      ))}
    </Box>
  </Box>
</div>


              {/* Asset Count DataGrid */}
              <Box
                sx={{
                  width: "100%",
                  display: "flex",
                  justifyContent: "center",
                  alignItems: "center",
                  mt: 4,
                }}
              >
                <Box
                  sx={{
                    width: { xs: "100%", sm: "90%", md: "85%", lg: "80%" },
                    borderRadius: "20px",
                    overflow: "hidden",
                    boxShadow: "0 10px 20px rgba(0, 0, 0, 0.1)",
                    border: "2px solid #060a0bff",
                  }}
                >
                  <DataGrid
                    rows={assetCountData.filter((row) =>
                      row.type.toLowerCase().includes(filterValue.toLowerCase())
                    )}
                    columns={[
                      {
                        field: "type",
                        headerName: "Asset Type",
                        flex: 1,
                        headerAlign: "center",
                        align: "center",
                      },
                      {
                        field: "total",
                        headerName: "Total Stock",
                        type: "number",
                        flex: 1,
                        headerAlign: "center",
                        align: "center",
                      },
                      {
                        field: "assigned",
                        headerName: "Assigned Stock",
                        type: "number",
                        flex: 1,
                        headerAlign: "center",
                        align: "center",
                      },
                      {
                        field: "unassigned",
                        headerName: "Unassigned Stock",
                        type: "number",
                        flex: 1,
                        headerAlign: "center",
                        align: "center",
                      },
                      {
                        field: "scrapped",
                        headerName: "Scrapped Stock",
                        type: "number",
                        flex: 1,
                        headerAlign: "center",
                        align: "center",
                      },
                    ]}
                    getRowId={(row) => row.id}
                    autoHeight
                    disableSelectionOnClick
                    sx={{
                      width: "100%",
                      fontFamily: "'Racing Sans One', sans-serif",
                      color: "#083A40",
                      "& .MuiDataGrid-virtualScroller": {
                        overflowX: "hidden",
                      },
                      "& .MuiDataGrid-columnHeaders": {
                        background: "linear-gradient(45deg, #6DE0FF, #2BC4F3)",
                        color: "#083A40",
                        fontSize: "16px",
                        fontWeight: 700,
                        textAlign: "center",
                      },
                      "& .MuiDataGrid-cell": {
                        background: "#F0FBFF",
                        color: "#083A40",
                        fontSize: "15px",
                        textAlign: "center",
                        borderBottom: "1px solid #D0F0FF",
                      },
                      "& .MuiDataGrid-footerContainer": {
                        background: "linear-gradient(45deg, #6DE0FF, #2BC4F3)",
                        color: "#083A40",
                        fontWeight: 600,
                      },
                      "& .MuiDataGrid-row:hover": {
                        backgroundColor: "#E0F9FF",
                      },
                    }}
                  />
                </Box>
              </Box>

        </div>
      </Container>
    </main>
  </div>
);

}

export default Dashboard;
