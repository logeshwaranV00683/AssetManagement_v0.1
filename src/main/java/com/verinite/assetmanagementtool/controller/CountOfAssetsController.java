package com.verinite.assetmanagementtool.controller;

import com.verinite.assetmanagementtool.entity.AssetsEntity;
import com.verinite.assetmanagementtool.entity.CountOfAssetsEntity;
import com.verinite.assetmanagementtool.service.AssetService;
import com.verinite.assetmanagementtool.service.CountOFAssetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/assetManager/v1/")
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
//@CrossOrigin(origins = "https://asset-tool.netlify.app", allowedHeaders = "*", allowCredentials = "true")
public class CountOfAssetsController {
    @Autowired
    CountOFAssetsService countOFAssetsService;

    @Autowired
    private AssetService assetService;

    @GetMapping("Assetcount/allLocationCount")
    public List<CountOfAssetsEntity> getAll() {
        return countOFAssetsService.getAll();
    }

    @GetMapping("AssetCount/location")
    public Map<String, Integer> getByLoc(@RequestParam String location) {
        return countOFAssetsService.getByLoc(location);
    }

    @GetMapping("AssetCount/unAssigned")
    public Map<String, Integer> getUnassignedAssets(@RequestParam String location) {
        return countOFAssetsService.getUnassignedAssets(location);
    }

    @GetMapping("AssetCount/assigned")
    public Map<String, Integer> getAssignedAssets(@RequestParam String location) {
        return countOFAssetsService.getAssignedAssets(location);
    }

    @GetMapping("/unassignedCount")
    public ResponseEntity<Integer> getCountOfUnassigned() {
        int unassignedCount = assetService.getCountOfUnassigned();
        return ResponseEntity.ok(unassignedCount);
    }

    @GetMapping("/assignedCount")
    public ResponseEntity<Integer> getCountOfAssigned() {
        int assignedCount = assetService.getCountOfAssigned();
        return ResponseEntity.ok(assignedCount);
    }

    @GetMapping("/unassignedList")
    public ResponseEntity<List<AssetsEntity>> getUnAssignedAssets() {
        List<AssetsEntity> unAssignedAssets = assetService.getUnAssigned();
        return ResponseEntity.ok(unAssignedAssets);
    }

    @GetMapping("/assignedList")
    public ResponseEntity<List<AssetsEntity>> getAssignedAssets() {
        List<AssetsEntity> assignedAssets = assetService.getAssigned();
        return ResponseEntity.ok(assignedAssets);
    }

}
