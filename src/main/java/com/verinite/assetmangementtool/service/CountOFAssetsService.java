package com.verinite.assetmangementtool.service;

import com.verinite.assetmangementtool.entity.CountOfAssets;

import java.util.List;
import java.util.Map;

public interface CountOFAssetsService {
    public int getLaptopCount(String id);

    //	public int getMouseCount();
//	public int getBagCount();
//	public int getChargerCount();
//	public int getHeadPhonesCount();
    public CountOfAssets postAssetCount(CountOfAssets countOfAssets);

    public Object updateAssetCount(String location, CountOfAssets countOfAssets);

    public List<CountOfAssets> getAll();

    public Map<String, Integer> getByLoc(String location);

    public Map<String, Integer> getUnassignedAssets(String location);

    public Map<String, Integer> getAssignedAssets(String location);

        public int totalLaptops();


}
