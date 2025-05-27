package com.verinite.assetmangementtool.service;

import com.verinite.assetmangementtool.entity.AssetsEntity;
import com.verinite.assetmangementtool.entity.AssetsHistoryEntity;
import com.verinite.assetmangementtool.entity.AssignedAssetsEntity;

import java.util.List;

public interface AssetsHistoryServices {

    public AssetsHistoryEntity saveHistory(AssetsHistoryEntity assetsHistoryEntity);

    public List<AssetsHistoryEntity> getAll();


    public Object getByHistoryId(String serialNumber);

    public void saveHistory(AssignedAssetsEntity assignedAssetsEntity);

    List<AssetsHistoryEntity> getAssetsHistoryBySerialNumberSorted(String serialNumber);
}
