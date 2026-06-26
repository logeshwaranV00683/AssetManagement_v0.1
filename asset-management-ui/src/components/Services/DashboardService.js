const apiUrl = process.env.REACT_APP_API_URL;

const getAuthHeaders = () => ({
  "Content-Type": "application/json",
  Authorization: `Bearer ${localStorage.getItem("authToken")}`,
});

export const getcountsByLocation = async (location) => {
  const url = `${apiUrl}/assetManager/v1/AssetCount/location?location=${encodeURIComponent(
    location
  )}`;

  try {
    const response = await fetch(url, {
      method: "GET",
      headers: getAuthHeaders(),
    });

    const data = await response.json();

    if (!data || typeof data !== "object") {
      console.error("Invalid response structure:", data);
      return {};
    }

    return data;
  } catch (error) {
    console.error("Error fetching assets:", error);
    return {};
  }
};

export const getLocations = async () => {
  const url = `${apiUrl}/dashboard/unique/location`;

  try {
    const response = await fetch(url, {
      method: "GET",
      headers: getAuthHeaders(),
    });

    const data = await response.json();
    console.log("Asset Location API response:", data);

    if (!Array.isArray(data)) {
      console.error("Expected an array, but got:", data);
      return [];
    }

    return data;
  } catch (error) {
    console.error("Error fetching Locations:", error);
    return [];
  }
};

export const getAssetTypes = async (location) => {
  const url = `${apiUrl}/dashboard/unique/assetTypes`;
  try {
    const response = await fetch(url, {
      method: "GET",
      headers: getAuthHeaders(),
    });

    const data = await response.json();
    console.log("Asset Location API response:", data);

    if (!Array.isArray(data)) {
      console.error("Expected an array, but got:", data);
      return [];
    }

    return data;
  } catch (error) {
    console.error("Error fetching Locations:", error);
    return [];
  }
};