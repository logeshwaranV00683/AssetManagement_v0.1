package com.verinite.assetmanagementtool.service;

import com.verinite.assetmanagementtool.dto.AssetExportDto;
import com.verinite.assetmanagementtool.dto.AssetsDto;
import com.verinite.assetmanagementtool.entity.AssetsEntity;
import com.verinite.assetmanagementtool.response.SaveAssetResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface AssetService {

    public Object getAssetBySerialNumber(String id);

    public SaveAssetResponse updateAsset(SaveAssetResponse asset);

    public void scrapAsset(int id);

    public List<AssetsEntity> getThroughStatus(String str);

    public int getCountOfAssigned();

    public int getCountOfUnassigned();

    List<AssetsEntity> getUnAssigned();

    public List<AssetsEntity> getAssigned();

    public AssetsDto getAssetsDetails(Integer assetId);

    List<AssetsDto> listOfAllAsset();

    void exportAssetsToExcel(List<AssetExportDto> allAssets, OutputStream outputStream) throws IOException;

    ResponseEntity<?> importAssetsFromExcel(InputStream inputStream) throws IOException;

    List<String> getUniqueAssetSourcedBy();
}
