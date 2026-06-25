const token = localStorage.getItem("authToken");
const userData = JSON.parse(localStorage.getItem("user"));
const apiUrl = process.env.REACT_APP_API_URL;

export const getAssetList = async () => {
  const url = `${apiUrl}/assetManager/v1/asset/listOfAssets`;
  try {
    const response = await fetch(url, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    if (!response.ok) {
      throw new Error("Network response was not ok " + response.statusText);
    }
    let data = await response.json();
    let filteredData = data.map((asset) => ({
      ...asset,
      id: asset.assetId,
    }));
    console.log("dt", filteredData);
    return filteredData;
  } catch (error) {
    console.error("Error fetching employee list:", error);
    throw error;
  }
};

export const saveAsset = async (assetData) => {
  try {
    const response = await fetch(`${apiUrl}/assetManager/v1/asset/saveAsset`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(assetData),
    });

    if (!response.ok) {
      let errorData;
      const contentType = response.headers.get("content-type");

      if (contentType && contentType.includes("application/json")) {
        errorData = await response.json();
      } else {
        errorData = await response.text();
      }

      const error = new Error("Request failed");
      error.status = response.status;
      error.data = errorData;
      throw error;
    }

    return await response.json();
  } catch (error) {
    throw error;
  }
};

export const updateAsset = async (asset, serialNumber) => {
  console.log("updating json: ", JSON.stringify(asset), serialNumber);
  try {
    const response = await fetch(
      `${apiUrl}/assetManager/v1/asset/updateAsset/${serialNumber}`,
      {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(asset),
      }
    );

    if (!response.ok) {
      let errorData;
      const contentType = response.headers.get("content-type");

      if (contentType && contentType.includes("application/json")) {
        errorData = await response.json();
      } else {
        errorData = await response.text();
      }

      const error = new Error("Request failed");
      error.status = response.status;
      error.data = errorData;
      throw error;
    }

    const text = await response.text();
    try {
      return JSON.parse(text);
    } catch {
      return { message: text };
    }
  } catch (error) {
    console.error("Error updating employee:", error);
    throw error;
  }
};

export const deleteAsset = async (serialNo) => {
  try {
    const empId = userData.empId;
    const response = await fetch(
      `${apiUrl}/assetManager/v1/deletedAsset/permananteDelete/${empId}/${serialNo}`,
      {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      }
    );

    if (!response.ok) {
      const errorText = await response.text();
      const error = new Error(errorText || "Failed to delete asset");
      error.status = response.status;
      throw error;
    }

    console.log("Asset deleted successfully.");
    return true;
  } catch (error) {
    console.error("Error deleting asset:", error);
    throw error;
  }
};

export const assignAsset = async (assetData) => {
  console.log("Assign Asset API Request Body:", assetData);

  try {
    const response = await fetch(
      `${apiUrl}/assetManager/v1/admin/Asset/assign`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(assetData),
      }
    );

    const contentType = response.headers.get("Content-Type");

    let result;
    if (contentType && contentType.includes("application/json")) {
      result = await response.json();
    } else {
      result = await response.text();
    }

    if (!response.ok) {
      const error = new Error(result || "Failed to assign asset");
      error.status = response.status;
      error.message = result;
      throw error;
    }

    return result;
  } catch (error) {
    console.error("Assign Asset Exception:", error);
    throw error;
  }
};

export const unassignAsset = async (serialNumbers) => {
  try {
    const response = await fetch(
      `${apiUrl}/assetManager/v1/admin/asset/un-assign`,
      {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(serialNumbers),
      }
    );

    if (!response.ok) {
      throw new Error(`Unassign failed: ${response.statusText}`);
    }
    if (response.status === 200) {
      return { message: "Asset(s) unassigned successfully" };
    }

    return await response.json();
  } catch (error) {
    console.error("Error unassigning asset:", error);
    throw error;
  }
};

export const getAssignedAssetsByEmployee = async (empId) => {
  const response = await fetch(
    `${apiUrl}/assetManager/v1/admin/get/all/assigned/assets/by/${empId}`,
    {
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    }
  );
  if (!response.ok) throw new Error("Failed to fetch assigned assets");
  return await response.json();
};

export const getAssetTypes = async () => {
  const url = `${apiUrl}/assetManager/v1/asset/listOfAssets`;

  try {
    const response = await fetch(url, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    });

    const data = await response.json();
    console.log("Asset type API response:", data);

    if (!Array.isArray(data)) {
      console.error("Expected an array of assets but got:", data);
      return [];
    }

    const types = [...new Set(data.map((item) => item.type))];
    return types;
  } catch (error) {
    console.error("Error fetching asset types:", error);
    return [];
  }
};

export const RecentAssignedAssetDetails = async () => {
  try {
    const response = await fetch(
      `${apiUrl}/assetManager/v1/admin/get-recent-assigned`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      }
    );
    if (!response.ok) throw new Error("Failed to fetch More page data");
    return await response.json();
  } catch (error) {
    console.error("Error fetching More page data:", error);
    throw error;
  }
};

export const getassetsourcedby = async () => {
  const url = `${apiUrl}/assetManager/v1/asset/getUniqueAssetSourcedBy`;

  try {
    const response = await fetch(url, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    });

    const data = await response.json();
    console.log("AssetSourcedBy API response:", data);

    if (!Array.isArray(data)) {
      console.error("Expected an array, but got:", data);
      return [];
    }

    return data;
  } catch (error) {
    console.error("Error fetching assetSourcedBy:", error);
    return [];
  }
};
export const getDeletedAssets = async () => {
  const url = `${apiUrl}/assetManager/v1/deletedAsset/getAll`;
  try {
    const response = await fetch(url, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      throw new Error("Network response was not ok: " + response.statusText);
    }

    let data = await response.json();

    let filteredData = data.map((asset, index) => ({
      id: index + 1,
      ...asset,
    }));

    console.log("Deleted Assets:", filteredData);
    return filteredData;
  } catch (error) {
    console.error("Error fetching deleted assets:", error);
    throw error;
  }
};
export const getAssetBySerialNumber = async (serialNumber) => {
  const url = `${apiUrl}/assetManager/v1/asset/id/${serialNumber}`;

  try {
    const response = await fetch(url, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      const errorText = await response.text();
      const error = new Error(errorText || "Failed to fetch asset");
      error.status = response.status;
      throw error;
    }

    const asset = await response.json();

    return {
      ...asset,
      id: asset.assetId,
    };
  } catch (error) {
    console.error("Error fetching asset by serial number:", error);
    throw error;
  }
};
