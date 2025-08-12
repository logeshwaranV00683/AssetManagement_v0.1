import React, { useState } from 'react';
import axios from 'axios';
import { Button } from '@mui/material';
import { showDataPreviewAlert, showErrorAlert } from '../Utils/alerts';
import '../Style/ExportButton.css';

const ExportButton = ({
  type,
  filteredRows = [],
  state = '',
  buttonLabel = 'Export'

}) => {
  const [loading, setLoading] = useState(false);
  const exportFilteredData = async () => {
    if (!Array.isArray(filteredRows) || filteredRows.length === 0) {
      showErrorAlert("Export Failed", 'No data available to export.');
      return;
    }
    const cleanData = filteredRows.map(row => {
      if (type === 'asset') {
        const newRow = {};
        Object.entries(row).forEach(([key, value]) => {
          if (key === 'id' || key === 'assetId') return;
          if (key === 'empId') {
            newRow['assignedTo'] = value;
          } else {
            newRow[key] = value;
          }
        });
        return newRow;
      } else {
        const newRow = { ...row };
        delete newRow.name;
        delete newRow.id;
        return newRow;
      }
    });

    filteredRows = cleanData;
    const confirmed = await showDataPreviewAlert(filteredRows, type);
    if (!confirmed) return;

    try {
      setLoading(true);
      const token = localStorage.getItem('authToken');

      const response = await axios.post(
        `${process.env.REACT_APP_API_URL}/assetManager/v1/${type}/export`,
        filteredRows,
        {
          responseType: 'blob',
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
        }
      );

      const blob = new Blob([response.data], {
        type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
      });

      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `${type}_${state || 'data'}_export.xlsx`;
      a.click();
      console.log(filteredRows);
      window.URL.revokeObjectURL(url);
    } catch (error) {
      showErrorAlert('Export Failed', 'Something went wrong. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Button
      onClick={exportFilteredData}
      disabled={loading}
      className="export-button"

    >
      {loading ? 'Exporting...' : buttonLabel}
    </Button>
  );
};

export default ExportButton;
