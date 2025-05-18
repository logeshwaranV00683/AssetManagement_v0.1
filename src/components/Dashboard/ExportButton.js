import React, { useState } from 'react';
import axios from 'axios';
import { Button } from '@mui/material';

const ExportButton = ({
  type,        // 'employees' or 'assets' or anything your backend supports
  status = '', // 'assigned', 'unassigned', 'scrap', etc. Optional
  filter = '', // optional filter string
  buttonLabel, // button text override
  filePrefix,  // optional prefix for filename
}) => {
  const [loading, setLoading] = useState(false);

  const handleExport = async () => {
    setLoading(true);
    try {
      const token = localStorage.getItem('authToken');
      // Build endpoint dynamically
      const endpoint = status
        ? `/assetManager/v1/export/${type}/${status}`
        : `/assetManager/v1/export/${type}`;

      const response = await axios.get(
        `${process.env.REACT_APP_API_URL}${endpoint}`,
        {
          params: filter ? { filter } : {},
          responseType: 'blob',
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      const blob = new Blob([response.data], {
        type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
      });
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;

      // Build filename dynamically
      const prefix = filePrefix ? `${filePrefix}_` : '';
      const statusPart = status && status !== 'all' ? `${status}_` : '';  // note underscore after status
      const filterPart = filter ? `_${filter}` : '';
      const fileName = `${prefix}${statusPart}${type}${filterPart}.xlsx`;

      a.download = fileName;
      a.click();
      window.URL.revokeObjectURL(url);
    } catch (error) {
      alert('Failed to export data. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Button
      variant="contained"
      color="primary"
      onClick={handleExport}
      disabled={loading}
    >
      {loading ? 'Exporting...' : buttonLabel || `Export ${type}`}
    </Button>
  );
};

export default ExportButton;
