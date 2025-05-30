import React, { useState } from "react";
import axios from "axios";
import "../Style/ImportExcel.css";
import { showSuccessAlert, showErrorAlert } from "./alerts";

function ImportExcel({ importType, onClose }) {
  const [file, setFile] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleFileChange = (e) => setFile(e.target.files[0]);

  const handleUpload = async () => {
    if (!file) {
      showErrorAlert("No file selected", "Please select an Excel file to upload.");
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

      showSuccessAlert("Import Successful", response.data);
      setFile(null);
      onClose(); // Close after successful upload
    } catch (error) {
      let message = "Something went wrong.";
      if (error.response?.data) {
        message =
          typeof error.response.data === "string"
            ? error.response.data
            : error.response.data.message ||
              error.response.data.error ||
              JSON.stringify(error.response.data);
      } else if (error.message) {
        message = error.message;
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