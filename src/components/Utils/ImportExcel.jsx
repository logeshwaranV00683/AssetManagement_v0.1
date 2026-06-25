import React, { useState } from "react";
import axios from "axios";
import "../Style/ImportExcel.css";
import { showSuccessAlert, showErrorAlert, showDataPreviewAlert } from "./alerts";
import { exportToExcel } from "./exportToExcel";

function ImportExcel({ importType, onClose, refreshList }) {
  const [file, setFile] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleFileChange = (e) => setFile(e.target.files[0]);
  const transformFailedRecords = (records, importType) => {
    const keyLabel = importType === "employee" ? "Employee ID" : "Serial Number";

    return Object.entries(records).map(([code, details]) => ({
      [keyLabel]: code,
      Reason:
        typeof details === "string"
          ? details
          : details?.reason || "Unknown",
    }));
  };

  const handleUpload = async () => {
    if (!file) {
      showErrorAlert("No File Selected", "Please select an Excel file to upload.");
      return;
    }

    const formData = new FormData();
    formData.append("file", file);
    setLoading(true);

    try {
      const url =
        importType === "employee"
          ? `${process.env.REACT_APP_API_URL}/assetManager/v1/employee/import`
          : importType === "asset"
            ? `${process.env.REACT_APP_API_URL}/assetManager/v1/asset/import`
            : "";

      if (!url) {
        showErrorAlert("Invalid Import Type", "Please provide a valid import type.");
        setLoading(false);
        return;
      }

      const token = localStorage.getItem("authToken");
      const response = await axios.post(url, formData, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "multipart/form-data",
        },
      });

      const failedRecords = response.data;

      if (failedRecords && Object.keys(failedRecords).length > 0) {
        const transformed = transformFailedRecords(failedRecords, importType);
        const confirmExport = await showDataPreviewAlert(transformed, importType, "Ignored Data!");

        if (confirmExport) {
          exportToExcel(transformed, "Not_Imported_Data.xlsx");
        }
      } else {
        showSuccessAlert("Import Successful", "All records were successfully imported.");
      }

      refreshList();
      setFile(null);
      onClose();
    } catch (error) {
      let message = "Something went wrong.";
      const responseData = error.response?.data;

      if (responseData && Object.keys(responseData).length > 0) {
        const transformed = transformFailedRecords(responseData, importType);
        exportToExcel(transformed, "not_imported_data.xlsx");
        message = "Some records failed. Failed data exported to Excel.";
      } else {
        message =
          typeof responseData === "string"
            ? responseData
            : responseData?.message || responseData?.error || error.message || "Unexpected error.";
      }

      showErrorAlert("Import Failed", message);
    } finally {
      setLoading(false);
    }
  };


  return (
    <div className="import-3d-overlay">
      <div className="import-3d-modal">
        <button className="import-3d-close" onClick={onClose}>✕</button>
        <h2 className="import-3d-title">Import {importType === "employee" ? "Employees" : "Assets"}</h2>

        <div className="import-3d-file-wrapper">
          <label htmlFor="fileInput" className="import-3d-file-button">Choose File</label>
          <input
            id="fileInput"
            type="file"
            accept=".xls,.xlsx"
            onChange={handleFileChange}
            disabled={loading}
            className="import-3d-file"
          />
          <div className={`import-3d-filename ${file ? "has-file" : ""}`}>
            {file ? file.name : "No file chosen"}
          </div>
        </div>

        <button
          className={`import-3d-button ${loading ? "loading" : ""}`}
          onClick={handleUpload}
          disabled={loading}
        >
          {loading ? <div className="spinner"></div> : <span>Upload</span>}
        </button>
      </div>
    </div>
  );
}

export default ImportExcel;
