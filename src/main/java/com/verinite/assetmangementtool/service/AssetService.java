package com.verinite.assetmangementtool.service;

import com.verinite.assetmangementtool.dto.AssetCounterDto;
import com.verinite.assetmangementtool.dto.AssetExportDto;
import com.verinite.assetmangementtool.dto.AssetsDto;
import com.verinite.assetmangementtool.entity.AssetsEntity;
import com.verinite.assetmangementtool.response.SaveAssetResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface AssetService {

    public Object getAssetBySerialNumber(String id);

    // public Object updateAsset(AssetsEntity asset);
    public SaveAssetResponse updateAsset(SaveAssetResponse asset);

    public void deleteAsset(int id);

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

    void exportAssetsToExcel(List<AssetExportDto> allAssets, OutputStream outputStream) throws IOException;

    ResponseEntity<?> importAssetsFromExcel(InputStream inputStream) throws IOException;
}
