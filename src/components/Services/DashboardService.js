import axios from 'axios';
const token = localStorage.getItem('authToken');
const apiUrl = process.env.REACT_APP_API_URL;


export const getAssetsByLocation = async (location) => {
  const url = `${apiUrl}/assetManager/v1/AssetCount/location?location=${encodeURIComponent(location)}`;

  try {
    const response = await fetch(url, {
      method: 'GET',
      headers: {
       'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
    });

    const data = await response.json();

    if (!data || typeof data !== 'object') {
      console.error('Invalid response structure:', data);
      return {};
    }

    return data;

  } catch (error) {
    console.error('Error fetching assets:', error);
    return {};
  }
};

export const getUnassignedAssetsByLocation = async (location) => {
  const url = `${apiUrl}/assetManager/v1/AssetCount/unAssigned?location=${encodeURIComponent(location)}`;

  try {
    const response = await fetch(url, {
      method: 'GET',
      headers: {
       'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
    });

    const data = await response.json();

    if (!data || typeof data !== 'object') {
      console.error('Invalid response structure:', data);
      return {};
    }

    return data;

  } catch (error) {
    console.error('Error fetching assets:', error);
    return {};
  }
};

export const getAssignedAssetsByLocation = async (location) => {
  const url = `${apiUrl}/assetManager/v1/AssetCount/assigned?location=${encodeURIComponent(location)}`;

  try {
    const response = await fetch(url, {
      method: 'GET',
      headers: {
       'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
    });

    const data = await response.json();

    if (!data || typeof data !== 'object') {
      console.error('Invalid response structure:', data);
      return {};
    }

    return data;

  } catch (error) {
    console.error('Error fetching assets:', error);
    return {};
  }
};




export const getAssetNames = async () => {
  const url = `${apiUrl}/assetManager/v1/AssetCount/GetAssetName`;

  try {
    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
    });

    const data = await response.json();
    console.log("Asset name API response:", data);

    if (!data || typeof data !== 'object' || !Array.isArray(data.assetNames)) {
      console.error('Invalid response structure:', data);
      return [];
    }

    return data.assetNames;  

  } catch (error) {
    console.error('Error fetching assets:', error);
    return [];
  }
};

