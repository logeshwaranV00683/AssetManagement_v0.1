package com.verinite.assetmanagementtool.controller;

import com.verinite.assetmanagementtool.dto.AssetExportDto;
import com.verinite.assetmanagementtool.dto.AssetsDto;
import com.verinite.assetmanagementtool.response.SaveAssetResponse;
import com.verinite.assetmanagementtool.service.AssetService;
import com.verinite.assetmanagementtool.service.serviceImpl.AssetServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
//@CrossOrigin(origins = "https://asset-tool.netlify.app", allowedHeaders = "*", allowCredentials = "true")
@RequestMapping("/assetManager/v1/")
public class AssetsController implements ApplicationRunner {


    @Autowired
    AssetService assetService2;
    @Autowired
    AssetServiceImpl assetService;


    @GetMapping("asset/listOfAssets")
    public ResponseEntity<List<AssetsDto>> getAllAssets() {
        List<AssetsDto> assetsList = assetService.listOfAllAsset();
        if (assetsList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(assetsList);
    }

    @PostMapping("asset/saveAsset")
    public ResponseEntity<AssetsDto> saveAsset(@RequestBody @Valid AssetsDto assetDto) {
        return assetService.saveAsset(assetDto);

    }

    @GetMapping("asset/id/{serialNumber}")
    public Object getById(@PathVariable String serialNumber) {
        return assetService.getAssetBySerialNumber(serialNumber);
    }

    @PutMapping("asset/updateAsset/{serialNumber}")
    public ResponseEntity<?> updateAsset(@PathVariable String serialNumber,
                                         @RequestBody @Valid SaveAssetResponse saveAssetResponse) {

        saveAssetResponse.setSerialNumber(serialNumber);

        SaveAssetResponse updateAsset = assetService.updateAsset(saveAssetResponse);

        return new ResponseEntity<>(updateAsset == null ? "Cannot Update the given details" : updateAsset, updateAsset == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

    }

    @PostMapping("asset/export")
    public ResponseEntity<byte[]> exportData(@RequestBody List<AssetExportDto> filteredRows) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            assetService.exportAssetsToExcel(filteredRows, out);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "exported_assets.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(out.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(("Error generating Excel file: " + e.getMessage()).getBytes());
        }
    }

    @Operation(summary = "Import Excel Data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful import")
    })
    @PostMapping(value = "asset/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importExcel(
            @Parameter(description = "Excel file", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            @RequestParam("file") MultipartFile file) {
        try {
            return assetService.importAssetsFromExcel(file.getInputStream());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("asset/getUniqueAssetSourcedBy")
    public ResponseEntity<List<String>> getUniqueAssetSourcedBy() {
        List<String> assetSourcedBy = assetService.getUniqueAssetSourcedBy();
        return ResponseEntity.ok(assetSourcedBy);
    }

}
