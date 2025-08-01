package com.verinite.assetmanagementtool.controller;

import com.verinite.assetmanagementtool.entity.AssetsEntity;
import com.verinite.assetmanagementtool.service.AssetService;
import com.verinite.assetmanagementtool.service.CountOFAssetsService;
import com.verinite.assetmanagementtool.service.serviceImpl.DashboardServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
//@CrossOrigin(origins = "https://asset-tool.netlify.app", allowedHeaders = "*", allowCredentials = "true")
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private CountOFAssetsService countOfAssetsService;

    @Autowired
    private DashboardServiceImpl dashboardServiceImpl;

    @GetMapping("/getAllAssets")
    public List<AssetsEntity> getAssetCountsWithLocation() {
        return dashboardServiceImpl.getAssetCountsWithLocation();
    }

    @GetMapping("/assets/countsByLocation")
    public ResponseEntity<?> getAssetCounts(@RequestParam List<String> locations) {
        return dashboardServiceImpl.getFormattedAssetCounts(locations);
    }

    @GetMapping("/unique/assets")
    public ResponseEntity<List<String>> getUniqueAssetTypes() {
        List<String> assetTypes = countOfAssetsService.getUniqueAssetTypes();
        return ResponseEntity.ok(assetTypes);
    }

    @GetMapping("/assets/count/{assetType}")
    public ResponseEntity<Map<String, Map<String, Object>>> getAssetsCountWithLocationByAssetType(
            @PathVariable String assetType) {

        Map<String, Map<String, Object>> locationByAssetType = dashboardServiceImpl
                .getAssetsCountWithLocationByAssetType(assetType);
        return new ResponseEntity<>(locationByAssetType, HttpStatus.OK);
    }
}
