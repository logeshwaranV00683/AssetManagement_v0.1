package com.verinite.assetmanagementtool.service;

import com.verinite.assetmanagementtool.dto.AssignableAssetDto;
import com.verinite.assetmanagementtool.dto.AssignedAssetDtoList;
import com.verinite.assetmanagementtool.entity.AssignedAssetsEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface AssignedAssetsService {


    AssignedAssetsEntity getAssignedAssetsById(int assignedId);

    AssignedAssetsEntity getAssignedAssetsByAssetsId(int assetId);

    List<AssignedAssetDtoList> getAllAssignedAssets();

    AssignedAssetsEntity updateAssignedAssets(int assignedId, String empId);

    String deleteAssignedAssets(int assignedId);

    List<Map<String, Object>> getRecentAssignedAssets();

    ResponseEntity<?> save(List<AssignableAssetDto> assignableAssetDtos);

    String unAssignAsset(List<String> serialNumber);

    ResponseEntity<?> getAllAssetsAssignedToParticularEmployee(String empId);
}
