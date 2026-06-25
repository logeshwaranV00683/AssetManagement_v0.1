package com.verinite.assetmanagementtool.controller;

import com.verinite.assetmanagementtool.dto.DeletedAssetDto;
import com.verinite.assetmanagementtool.service.DeletedAssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
//@CrossOrigin(origins = "https://asset-tool.netlify.app", allowedHeaders = "*", allowCredentials = "true")
@RequestMapping("/assetManager/v1/")
public class DeleteAssetController {

    @Autowired
    DeletedAssetService deletedAssetService;

    @GetMapping("deletedAsset/getAll")
    public ResponseEntity<List<DeletedAssetDto>> getAll() {
        return ResponseEntity.ok(deletedAssetService.getAllDeleted());
    }

    @DeleteMapping("deletedAsset/permananteDelete/{adminId}/{serialNo}")
    public ResponseEntity<?> permananteDelete(@PathVariable("serialNo") String serialNo, @PathVariable("adminId") String adminId) {
        deletedAssetService.permananteDelete(serialNo, adminId);
        return new ResponseEntity<>("Asset Scrapped successfully. ", HttpStatus.OK);
    }

}
