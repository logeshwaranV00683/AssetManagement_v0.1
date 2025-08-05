package com.verinite.assetmanagementtool.service;

import com.verinite.assetmanagementtool.entity.AssetsHistoryEntity;
import com.verinite.assetmanagementtool.entity.AssignedAssetsEntity;

import java.util.List;

public interface AssetsHistoryServices {

    public AssetsHistoryEntity saveHistory(AssetsHistoryEntity assetsHistoryEntity);

    public List<AssetsHistoryEntity> getAll();

    public Object getByHistoryId(String serialNumber);

    public void saveHistory(AssignedAssetsEntity assignedAssetsEntity);

    List<AssetsHistoryEntity> getAssetsHistoryBySerialNumberSorted(String serialNumber);

    void updateHistory(AssignedAssetsEntity assignedAssets, String serialNumber);
}
