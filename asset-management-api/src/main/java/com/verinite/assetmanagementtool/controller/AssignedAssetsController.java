package com.verinite.assetmanagementtool.controller;

import com.verinite.assetmanagementtool.dto.AssignableAssetDto;
import com.verinite.assetmanagementtool.dto.AssignedAssetDtoList;
import com.verinite.assetmanagementtool.entity.AssignedAssetsEntity;
import com.verinite.assetmanagementtool.service.AssignedAssetsService;
import com.verinite.assetmanagementtool.service.serviceImpl.AssetServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/assetManager/v1/admin/")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AssignedAssetsController {

    @Autowired
    private AssignedAssetsService assignedAssetsService;

    @Autowired
    private AssetServiceImpl assetService;


    @PostMapping("Asset/assign")
    public ResponseEntity<?> assignMultipleAssets(@RequestBody List<@Valid AssignableAssetDto> assignableAssetDtos) {
        if (assignableAssetDtos == null || assignableAssetDtos.isEmpty()) {
            return ResponseEntity.badRequest().body("Asset list is empty");
        }
        return assignedAssetsService.save(assignableAssetDtos);
    }

    @PutMapping("asset/un-assign")
    public ResponseEntity<?> unassignAssetApi(@RequestBody List<String> serialNumber) {

        String assignedAssets = assignedAssetsService.unAssignAsset(serialNumber);
        return ResponseEntity.ok(assignedAssets);
    }

    @GetMapping("getall/assigned/assets")
    public ResponseEntity<List<AssignedAssetDtoList>> getAllAssignedAssets() {
        List<AssignedAssetDtoList> assignedAssets = assignedAssetsService.getAllAssignedAssets();
        return ResponseEntity.ok(assignedAssets);
    }

    @PutMapping("update-assigned-assets/{assignedId}/{empId}")
    public AssignedAssetsEntity updateAssignedAssets(@PathVariable("assignedId") Integer assignedId,
                                                     @PathVariable("empId") String empId) {
        return assignedAssetsService.updateAssignedAssets(assignedId, empId);
    }

    @DeleteMapping("update-assigned-assets/{assignedId}")
    public String deleteAssignedAssets(@PathVariable("assignedId") int assignedId) {
        return assignedAssetsService.deleteAssignedAssets(assignedId);
    }

    @GetMapping("get-recent-assigned")
    public List<Map<String, Object>> getRecentAssigned() {
        return assignedAssetsService.getRecentAssignedAssets();
    }

    @GetMapping("get/all/assigned/assets/by/{empId}")
    public ResponseEntity<?> getAllByAssignedAssetsById(@PathVariable String empId) {
        return assignedAssetsService.getAllAssetsAssignedToParticularEmployee(empId);
    }

}
