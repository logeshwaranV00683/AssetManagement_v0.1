package com.verinite.assetmangementtool.service;

import com.verinite.assetmangementtool.entity.CountOfAssets;

import java.util.List;

public interface CountOFAssetsService {
	public int getLaptopCount(String id);
//	public int getMouseCount();
//	public int getBagCount();
//	public int getChargerCount();
//	public int getHeadPhonesCount();
	public CountOfAssets postAssestCount(CountOfAssets countOfAssets);
	public Object updateAssetCount(String location,CountOfAssets countOfAssets);
	public List<CountOfAssets> getAll();
	public int totalLaptops();


	
}
