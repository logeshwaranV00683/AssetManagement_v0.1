package com.verinite.assetmangementtool.service;

import java.util.List;

import com.verinite.assetmangementtool.dto.AssetCounterDto;
import com.verinite.assetmangementtool.dto.AssetsDto;
import com.verinite.assetmangementtool.entity.AssetTypes;
import com.verinite.assetmangementtool.entity.AssetsEntity;
import com.verinite.assetmangementtool.response.SaveAssetResponse;

public interface AssetService {

	public Object getAssetBySerialNumber(String id);

	// public Object updateAsset(AssetsEntity asset);
	public SaveAssetResponse updateAsset(SaveAssetResponse asset);

	public Object deleteAsset(int id);

	public List<AssetsEntity> getThroughStatus(String str);

	public int getCountOfAssigned();

	public int getCountOfUnassigned();

	public int getLaptopCountByLocation(String id);

//    public int getLaptopCountByAssertSourced(String assertSourcedBy);

    public int totalLaptops();

	public int getCountOfUnassignedByLocation(String id);

	public List<AssetCounterDto> getUnassignedAndTotalLaptops();

	public List<AssetsEntity> getLaptopsUnderWarenty();

	public List<AssetsEntity> getLaptopsOverWarenty();

	public Object saveHistory(String serialNo, String empId);

	List<AssetsEntity> getUnAssigned();

	public List<AssetsEntity> getAssigned();

	public AssetsDto getAssetsDetails(Integer assetId);

	String deleteAsset(Integer assetId);

	List<AssetsDto> listOfAllAsset();

}
