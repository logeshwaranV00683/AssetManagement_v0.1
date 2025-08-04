import React, { useEffect, useState } from "react";
import { DataGrid } from "@mui/x-data-grid";
import { RecentAssignedAssetDetails } from "../Services/AssetService";

const RecentAssignedAssetPage = () => {
  const [rows, setRows] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const columns = [
    {
      field: "empId",
      headerName: "Employee ID",
      flex: 1,
      headerAlign: "center",
      align: "center",
    },
    {
      field: "firstName",
      headerName: "First Name",
      flex: 1,
      headerAlign: "center",
      align: "center",
    },
    {
      field: "lastName",
      headerName: "Last Name",
      flex: 1,
      headerAlign: "center",
      align: "center",
    },
    {
      field: "serialNumber",
      headerName: "Serial Number",
      flex: 1,
      headerAlign: "center",
      align: "center",
    },
    {
      field: "assignedDate",
      headerName: "Assigned Date",
      flex: 1,
      headerAlign: "center",
      align: "center",
    },
  ];

  useEffect(() => {
    const fetchData = async () => {
      try {
        const result = await RecentAssignedAssetDetails();
        const formattedData = result.map((item, index) => ({
          id: index + 1,
          ...item,
        }));
        setRows(formattedData);
      } catch (err) {
        setError(err.message || "Failed to fetch data");
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  if (loading) return <h3>Loading Recent Assigned Assets...</h3>;
  if (error) return <h3>Error: {error}</h3>;

  return (
    <div
      style={{
        flex: 1,
        padding: "20px",
        display: "flex",
        flexDirection: "column",
        alignItems: "flex",
        marginLeft: "5%",
      }}
    >
      <h2
        style={{
          marginBottom: "12px",
          fontFamily: "'Racing Sans One', sans-serif",
          color: "#0d9ee6ff",
          marginLeft: "15%",
          display: "flex",
        }}
      >
        Today Assigned Assets
      </h2>

      <div
        style={{
          height: 400,
          width: "50%",
          backgroundColor: "#fff",
          borderRadius: "16px",
          boxShadow: "0 4px 12px rgba(0,0,0,0.1)",
        }}
      >
        <DataGrid
          rows={rows}
          columns={columns}
          pageSize={5}
          rowsPerPageOptions={[5]}
          disableSelectionOnClick
          sx={{
            border: 2,
            borderRadius: 3,
            "& .MuiDataGrid-columnHeaders": {
              backgroundColor: "#E6F7FF",
              color: "#083A40",
              fontSize: "12px",
              fontFamily: "'Racing Sans One', sans-serif",
              minHeight: "40px",
            },
            "& .MuiDataGrid-columnHeaderTitle": {
              color: "#083A40",
            },
            "& .MuiDataGrid-cell": {
              backgroundColor: "#E6F7FF",
              fontSize: "12px",
              textAlign: "center",
              fontFamily: "'Racing Sans One', sans-serif",
              minHeight: "35px",
              lineHeight: "35px",
              color: "#083A40",
            },
            "& .MuiDataGrid-footerContainer": {
              backgroundColor: "#f5f5f5",
              minHeight: "40px",
            },
          }}
        />
      </div>
    </div>
  );
};

export default RecentAssignedAssetPage;
