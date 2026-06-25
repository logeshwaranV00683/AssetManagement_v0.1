import React from "react";
import { Modal, Box, Typography, IconButton, Tooltip } from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";

function AssetHistoryPopup({ open, onClose, history = [], asset }) {
  const serialNumber = asset?.serialNumber || "N/A";

  const boxWidth = 240;
  const boxHeight = 90;
  const verticalGap = 100;

  const centerX = 400;

  return (
    <Modal open={open} onClose={onClose}>
      <Box
        sx={{
          position: "absolute",
          top: "50%",
          left: "50%",
          transform: "translate(-50%, -50%)",
          width: "90%",
          maxWidth: 900,
          backgroundColor: "#3FCCF6",
          borderRadius: 3,
          border: "4px solid #000",
          p: 4,
          fontFamily: "Racing Sans One",
          maxHeight: "90vh",
          overflowY: "auto",
        }}
      >
        <IconButton
          onClick={onClose}
          sx={{ position: "absolute", top: 8, right: 8 }}
          aria-label="Close"
        >
          <CloseIcon />
        </IconButton>

        <Typography
          variant="h4"
          sx={{
            mb: 4,
            textAlign: "center",
            color: "#000",
            fontFamily: "Racing Sans One",
          }}
        >
          📜 History for Serial Number: {serialNumber}
        </Typography>

        {history.length === 0 ? (
          <Typography sx={{ textAlign: "center", mt: 4, color: "#5c4326" }}>
            No history available.
          </Typography>
        ) : (
          <Box
            sx={{
              position: "relative",
              minHeight: history.length * (boxHeight + verticalGap) + 100,
            }}
          >
            {/* Boxes */}
            {history.map((entry, index) => {
              const isLeft = index % 2 === 0;
              const x = centerX + (isLeft ? -boxWidth - 40 : 40);
              const y = index * (boxHeight + verticalGap);

              return (
                <Box
                  key={index}
                  sx={{
                    position: "absolute",
                    top: y,
                    left: x,
                    width: boxWidth,
                    height: boxHeight,
                    p: 2,
                    backgroundColor: "#fff",
                    borderRadius: 2,
                    border: "2px solid #000",
                    boxShadow: "2px 2px 6px rgba(0,0,0,0.1)",
                    color: "#2c2c2c",
                  }}
                >
                  <Typography>
                    <strong>👤 Employee ID:</strong> {entry.empId || "N/A"}
                  </Typography>
                  <Typography>
                    <strong>📅 Assigned:</strong>{" "}
                    {entry.assignedDate
                      ? new Date(entry.assignedDate).toLocaleDateString("en-GB")
                      : "N/A"}
                  </Typography>
                  <Typography>
                    <strong>📅 Return Date:</strong>{" "}
                    {entry.returnDate
                      ? new Date(entry.returnDate).toLocaleDateString("en-GB")
                      : "Asset is not yet returned"}
                  </Typography>
                </Box>
              );
            })}

            {/* SVG Connectors */}
            <svg
              width="100%"
              height={history.length * (boxHeight + verticalGap) + 100}
              style={{ position: "absolute", top: 0, left: 0 }}
            >
              <defs>
                <marker
                  id="arrow"
                  viewBox="0 0 10 10"
                  refX="6"
                  refY="5"
                  markerWidth="6"
                  markerHeight="6"
                  orient="auto-start-reverse"
                >
                  <path d="M 0 0 L 10 5 L 0 10 z" fill="black" />
                </marker>
              </defs>

              {history.map((entry, index) => {
                if (index === 0) {
                  const isLeft = index % 2 === 0;
                  const x =
                    centerX + (isLeft ? -boxWidth - 40 : 40) + boxWidth / 2;
                  const y = index * (boxHeight + verticalGap);
                  return (
                    <line
                      key="start"
                      x1={x}
                      y1={y - 40}
                      x2={x}
                      y2={y}
                      stroke="black"
                      strokeWidth={2}
                      markerEnd="url(#arrow)"
                      style={{
                        strokeDasharray: 1000,
                        strokeDashoffset: 1000,
                        animation: "dash 1s ease forwards",
                      }}
                    />
                  );
                }

                const prevIsLeft = (index - 1) % 2 === 0;
                const currIsLeft = index % 2 === 0;

                const x1 =
                  centerX + (prevIsLeft ? -boxWidth - 40 : 40) + boxWidth / 2;
                const y1 = (index - 1) * (boxHeight + verticalGap) + boxHeight;

                const x2 =
                  centerX + (currIsLeft ? -boxWidth - 40 : 40) + boxWidth / 2;
                const y2 = index * (boxHeight + verticalGap);

                const controlX = x1 + (x2 - x1) / 2;
                const controlY1 = y1 + 20;
                const controlY2 = y2 - 20;

                const path = `M${x1},${y1} C${controlX},${controlY1} ${controlX},${controlY2} ${x2},${y2}`;

                return (
                  <Tooltip
                    key={index}
                    title={`From ${history[index - 1].empId || "N/A"} → ${entry.empId || "N/A"
                      }`}
                    arrow
                    placement="top"
                  >
                    <path
                      d={path}
                      stroke="black"
                      strokeWidth={2}
                      fill="none"
                      markerEnd="url(#arrow)"
                      style={{
                        strokeDasharray: 1000,
                        strokeDashoffset: 1000,
                        animation: `dash 1s ease ${index * 0.3}s forwards`,
                      }}
                    />
                  </Tooltip>
                );
              })}
            </svg>

            <style>
              {`
                @keyframes dash {
                  to {
                    stroke-dashoffset: 0;
                  }
                }
              `}
            </style>
          </Box>
        )}
      </Box>
    </Modal>
  );
}

export default AssetHistoryPopup;
