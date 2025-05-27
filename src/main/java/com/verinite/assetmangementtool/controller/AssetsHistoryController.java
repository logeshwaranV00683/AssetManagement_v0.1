package com.verinite.assetmangementtool.controller;

import com.verinite.assetmangementtool.entity.AssetsHistoryEntity;
import com.verinite.assetmangementtool.service.AssetsHistoryServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
//@CrossOrigin(origins = "https://asset-tool.netlify.app", allowedHeaders = "*", allowCredentials = "true")
@RequestMapping("/assetManager/v1/")
public class AssetsHistoryController {

    @Autowired
    private AssetsHistoryServices assetsHistoryServices;

    @PostMapping("Assets/history/save")
    public AssetsHistoryEntity save(@RequestBody AssetsHistoryEntity history) {
        return assetsHistoryServices.saveHistory(history);
    }

    @GetMapping("Assets/history/get/all")
    public List<AssetsHistoryEntity> getAll() {
        return assetsHistoryServices.getAll();
    }


    @GetMapping("Assets/history/get/by/id/{id}")
    public Object getByHistoryId(@RequestParam String serialNumber) {
        return assetsHistoryServices.getByHistoryId(serialNumber);
    }

    @GetMapping("Assets/history/specificAssetHistory/{serialNumber}")
    public List<AssetsHistoryEntity> getAssetsHistoryBySerialNumber(@PathVariable String serialNumber) {
        return assetsHistoryServices.getAssetsHistoryBySerialNumberSorted(serialNumber);
    }
}
