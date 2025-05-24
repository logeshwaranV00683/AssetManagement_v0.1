import axios from 'axios';

const token = localStorage.getItem('authToken');
const apiUrl = process.env.REACT_APP_API_URL;

export const getAssetList = async() => {
    const url = `${apiUrl}/assetManager/v1/asset/listOfAssets`;
    try {
      const response = await fetch(url, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      if (!response.ok) {
        throw new Error('Network response was not ok ' + response.statusText);
      }
      let data = await response.json();
      let filteredData = data.map(asset => ({
        ...asset,
        id: asset.assetId,
      }));
      console.log('dt', filteredData);
      return filteredData;
    } catch (error) {
      console.error('Error fetching employee list:', error);
      throw error;
    }
  };

  export const saveAsset = async (assetData) => {
      try {
        const response = await fetch(`${apiUrl}/assetManager/v1/asset/saveAsset`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          },
          body: JSON.stringify(assetData),
        });
        
        if (!response.ok) {
          throw new Error('Network response was not ok ' + response.statusText);
        }
        return await response.json();
      } catch (error) {
        console.error('Error saving asset:', error);
        throw error;
      }
    };

    export const updateAsset = async (asset,serialNumber) => {
      console.log('json',  JSON.stringify(asset),serialNumber);
      try {
        const response = await fetch(`${apiUrl}/assetManager/v1/asset/updateAsset/${serialNumber}`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          },
          body: JSON.stringify(asset),
        });
        
        if (!response.ok) {
          throw new Error('Network response was not ok ' + response.statusText);
        }
        return await response.json();
      } catch (error) {
        console.error('Error updating asset:', error);
        throw error;
      }
    };

export const scrapAsset = async (assetId) => {
  console.log(`Scrapping Asset with ID: ${assetId}`);
  try {
    const response = await fetch(`${apiUrl}/assetManager/v1/asset/delete/${assetId}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
    });

    if (!response.ok) {
      throw new Error('Network response was not ok ' + response.statusText);
    }
    return true;
  } catch (error) {
    console.error('Error updating asset:', error);
    throw error;
  }
};
