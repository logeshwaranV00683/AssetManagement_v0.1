import React, { useState } from "react";
import AssetHistoryPopup from "./AssetHistoryPopup";
import { fetchAssetHistory } from "./fetchAssetHistory";

const AssetList = ({ token }) => {
  const [showPopup, setShowPopup] = useState(false);
  const [historyData, setHistoryData] = useState([]);
  const [currentSerial, setCurrentSerial] = useState("");

  const handleHistoryClick = async (serialNumber) => {
    const data = await fetchAssetHistory(serialNumber, token);
    if (data.length > 0) {
      setHistoryData(data);
      setCurrentSerial(serialNumber);
      setShowPopup(true);
    }
  };

  return (
    <div>
      <button onClick={() => handleHistoryClick("SN12345")}>
        Show History
      </button>

      {showPopup && (
        <AssetHistoryPopup
          history={historyData}
          serialNumber={currentSerial}
          onClose={() => setShowPopup(false)}
        />
      )}
    </div>
  );
};

export default AssetList;
