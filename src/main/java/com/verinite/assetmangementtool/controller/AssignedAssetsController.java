package com.verinite.assetmangementtool.controller;

import com.verinite.assetmangementtool.dto.AssignableAssetDto;
import com.verinite.assetmangementtool.dto.AssignedAssetDtoList;
import com.verinite.assetmangementtool.entity.AssignedAssetsEntity;
import com.verinite.assetmangementtool.service.AssetServiceImpl;
import com.verinite.assetmangementtool.service.AssignedAssetsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assetManager/v1/admin/")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AssignedAssetsController {

    @Autowired
    private AssignedAssetsServiceImpl assignedAssetsService;

    @Autowired
    private AssetServiceImpl assetService;


    @PostMapping("Asset/assign")
    public ResponseEntity<?> assignMultipleAssets(@RequestBody List<AssignableAssetDto> assignableAssetDtos) {
        if (assignableAssetDtos == null || assignableAssetDtos.isEmpty()) {
            return ResponseEntity.badRequest().body("Asset list is empty");
        }

        return assignedAssetsService.save(assignableAssetDtos);
    }

    @PutMapping("asset/un-assign")
    public ResponseEntity<?> unassignAssetApi(@RequestParam String serialNumber) {

        String assignedAssets = assignedAssetsService.unAssignAsset(serialNumber);
        return ResponseEntity.ok(assignedAssets);
    }

    @GetMapping("get-assigned-assetss/{assignedId}")
    public AssignedAssetsEntity getAssignedAssetsById(@PathVariable("assignedId") int assignedId) {
        return assignedAssetsService.getAssignedAssetsById(assignedId);
    }

    @GetMapping("get-assigned-assets/{assetId}")
    public AssignedAssetsEntity getAssignedAssetsByAssetId(@PathVariable("assetId") int assetId) {
        return assignedAssetsService.getAssignedAssetsByAssetsId(assetId);
    }

    @GetMapping("getall/assigend/assets")
    public ResponseEntity<List<AssignedAssetDtoList>> getAllAssignedAssets() {
        List<AssignedAssetDtoList> assignedAssets = assignedAssetsService.getAllassignedAssets();
        return ResponseEntity.ok(assignedAssets); // Returns HTTP 200 with the list of assigned assets
    }

    @PutMapping("update-assigned-assets/{assignedId}")
    public AssignedAssetsEntity updateAssignedAssets(@PathVariable("assignedId") Integer assignedId,
                                                     @RequestBody AssignedAssetsEntity assignedAssetsEntity) {
        return assignedAssetsService.updateAssignedAssets(assignedId, assignedAssetsEntity);
    }

    @DeleteMapping("update-assigned-assets/{assignedId}")
    public String deleteAssignedAssets(@PathVariable("assignedId") int assignedId) {
        return assignedAssetsService.deleteAssignedAssets(assignedId);
    }

    @GetMapping("get-recent-assigned")
    public ResponseEntity<?> getRecentAssigned() {
        return assignedAssetsService.getRecentAssigned();
    }

    @GetMapping("get/all/assigned/assets/by/{empId}")
    public List<AssignedAssetsEntity> getAllByAssignedAssetsById(@PathVariable String empId) {
        return assignedAssetsService.getAllAssetsAssignedToParticularEmployee(empId);
    }

}
