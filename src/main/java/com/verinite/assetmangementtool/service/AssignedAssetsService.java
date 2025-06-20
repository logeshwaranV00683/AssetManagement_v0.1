package com.verinite.assetmangementtool.service;

import com.verinite.assetmangementtool.dto.AssignableAssetDto;
import com.verinite.assetmangementtool.dto.AssignedAssetDtoList;
import com.verinite.assetmangementtool.entity.AssignedAssetsEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AssignedAssetsService {


    AssignedAssetsEntity getAssignedAssetsById(int assignedId);

    AssignedAssetsEntity getAssignedAssetsByAssetsId(int assetId);

    List<AssignedAssetDtoList> getAllassignedAssets();

    AssignedAssetsEntity updateAssignedAssets(int assignedId, AssignedAssetsEntity assignedAssetsEntity);

    String deleteAssignedAssets(int assignedId);


    ResponseEntity<?> getRecentAssigned();

    ResponseEntity<String> save(List<AssignableAssetDto> assignableAssetDtos);

    String unAssignAsset(List<String> serialNumber);

     List<AssignedAssetsEntity> getAllAssetsAssignedToParticularEmployee(String empId);


}
