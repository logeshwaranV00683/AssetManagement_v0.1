import React from 'react';
import {
  Modal,
  Box,
  Typography,
  IconButton,
} from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';
import { keyframes } from '@emotion/react';

const scrollUnroll = keyframes`
  0% {
    transform: perspective(1000px) rotateX(-90deg) scaleY(0.3);
    opacity: 0;
  }
  60% {
    transform: perspective(1000px) rotateX(20deg) scaleY(1.05);
    opacity: 1;
  }
  100% {
    transform: perspective(1000px) rotateX(0deg) scaleY(1);
  }
`;

function AssetHistoryPopup({ open, onClose, history = [], asset }) {
  const serialNumber = asset?.serialNumber || 'N/A';

  return (
    <Modal
      open={open}
      onClose={onClose}
      aria-labelledby="asset-history-modal-title"
      aria-describedby="asset-history-modal-description"
      closeAfterTransition
      slotProps={{
        backdrop: {
          timeout: 500,
        },
      }}
    >
      <Box
        sx={{
          position: 'absolute',
          top: '50%',
          left: '50%',
          transform: 'translate(-50%, -50%)',
          backgroundColor: '#fefae0',
          boxShadow: '0 20px 50px rgba(0,0,0,0.2)',
          p: 4,
          width: '80%',
          maxWidth: 700,
          borderRadius: '12px',
          maxHeight: '80vh',
          overflowY: 'auto',
          animation: `${scrollUnroll} 0.8s ease-out`,
          border: '4px solid #c9a86b',
          backgroundImage: 'linear-gradient(to bottom, #fefae0 0%, #f0e6c8 100%)',
          fontFamily: `'Garamond', serif`,
        }}
      >
        <IconButton
          onClick={onClose}
          sx={{ position: 'absolute', top: 8, right: 8 }}
        >
          <CloseIcon />
        </IconButton>

        <Typography variant="h6" sx={{ mb: 2, textAlign: 'center', color: '#5c4326' }}>
          📜 History for Serial Number: {serialNumber}
        </Typography>

        {Array.isArray(history) && history.length > 0 ? (
          history.map((entry, index) => (
            <Box
              key={index}
              sx={{
                mb: 2,
                p: 2,
                border: '1px dashed #b58953',
                borderRadius: 2,
                backgroundColor: '#fffef5',
                color: '#3a2d1b',
              }}
            >
              <Typography><strong>👤 Employee ID:</strong> {entry.empId || 'N/A'}</Typography>
              <Typography><strong>📅 Assigned Date:</strong> {entry.assignedDate ? new Date(entry.assignedDate).toLocaleDateString() : 'N/A'}</Typography>
              <Typography><strong>📦 Return Date:</strong> {entry.returnDate ? new Date(entry.returnDate).toLocaleDateString() : 'N/A'}</Typography>
              <Typography><strong>🖊️ Assigned By:</strong> {entry.assignedBy || 'N/A'}</Typography>
            </Box>
          ))
        ) : (
          <Typography sx={{ textAlign: 'center', color: '#5c4326' }}>
            No history available for this asset.
          </Typography>
        )}
      </Box>
    </Modal>
  );
}

export default AssetHistoryPopup;
