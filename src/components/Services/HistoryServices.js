const token = localStorage.getItem("authToken");
const apiUrl = process.env.REACT_APP_API_URL;

export const fetchAssetHistory = async (serialNumber) => {
  try {
    const response = await fetch(
      `${apiUrl}/assetManager/v1/Assets/history/specificAssetHistory/${serialNumber}`,
      {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      }
    );

    if (response.status === 404) {
      alert(`No history found for serial number: ${serialNumber}`);
      return [];
    }

    if (!response.ok) {
      throw new Error("Failed to fetch history");
    }

    return await response.json();
  } catch (error) {
    console.error("Error:", error.message);
    alert("An error occurred while fetching history");
    return [];
  }
};
