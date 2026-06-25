package com.verinite.assetmanagementtool.controller;

import com.verinite.assetmanagementtool.entity.AssetsHistoryEntity;
import com.verinite.assetmanagementtool.service.AssetsHistoryServices;
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

    @GetMapping("Assets/history/get/all")
    public List<AssetsHistoryEntity> getAll() {
        return assetsHistoryServices.getAll();
    }

    @GetMapping("Assets/history/specificAssetHistory/{serialNumber}")
    public List<AssetsHistoryEntity> getAssetsHistoryBySerialNumber(@PathVariable String serialNumber) {
        return assetsHistoryServices.getAssetsHistoryBySerialNumberSorted(serialNumber);
    }
}
