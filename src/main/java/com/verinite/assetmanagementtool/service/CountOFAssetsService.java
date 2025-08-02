package com.verinite.assetmanagementtool.service;

import com.verinite.assetmanagementtool.dto.CountOfAssetsDTO;
import com.verinite.assetmanagementtool.entity.CountOfAssetsEntity;

import java.util.List;
import java.util.Map;

public interface CountOFAssetsService {

    public CountOfAssetsEntity postAssetCount(CountOfAssetsEntity countOfAssetsEntity);

    public Object updateAssetCount(String location, CountOfAssetsEntity countOfAssetsEntity);

    public List<CountOfAssetsEntity> getAll();

    public List<CountOfAssetsDTO> getByLoc(String location);

    public Map<String, Integer> getUnassignedAssets(String location);

    public Map<String, Integer> getAssignedAssets(String location);

    List<String> getUniqueAssetTypes();

    List<String> getUniqueLocation();
}
