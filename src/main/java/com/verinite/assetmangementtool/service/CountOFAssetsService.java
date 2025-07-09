package com.verinite.assetmangementtool.service;

import com.verinite.assetmangementtool.entity.CountOfAssetsEntity;

import java.util.List;
import java.util.Map;

public interface CountOFAssetsService {
    public int getLaptopCount(String id);

    //	public int getMouseCount();
//	public int getBagCount();
//	public int getChargerCount();
//	public int getHeadPhonesCount();
    public CountOfAssetsEntity postAssetCount(CountOfAssetsEntity countOfAssetsEntity);

    public Object updateAssetCount(String location, CountOfAssetsEntity countOfAssetsEntity);

    public List<CountOfAssetsEntity> getAll();

    public Map<String, Integer> getByLoc(String location);

    public Map<String, Integer> getUnassignedAssets(String location);

    public Map<String, Integer> getAssignedAssets(String location);

        public int totalLaptops();


}
