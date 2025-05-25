package com.verinite.assetmangementtool.controller;

import com.verinite.assetmangementtool.dto.AssetExportDto;
import com.verinite.assetmangementtool.dto.AssetsDto;
import com.verinite.assetmangementtool.response.SaveAssetResponse;
import com.verinite.assetmangementtool.service.AssetNameServiceImpl;
import com.verinite.assetmangementtool.service.AssetService;
import com.verinite.assetmangementtool.service.AssetServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
//@CrossOrigin(origins = "https://asset-tool.netlify.app", allowedHeaders = "*", allowCredentials = "true")
@RequestMapping("/assetManager/v1/")
public class AssetsController implements ApplicationRunner {

    AssetNameServiceImpl assetNameServiceImpl;

    @Autowired
    AssetService assetService2;
    @Autowired
    AssetServiceImpl assetService;

    public AssetsController(AssetNameServiceImpl assetNameServiceImpl) {
        super();
        this.assetNameServiceImpl = assetNameServiceImpl;
    }

    //	@GetMapping("/listOfAssets")
//	public ResponseEntity<List<AssetsEntity>> getAllAssets() {
//		List<AssetsEntity> assets = assetService.listOfAllAsset();
//		return ResponseEntity.ok(assets); // Respond with the list of assets and HTTP 200 OK
//	}
    @GetMapping("asset/listOfAssets")
    public ResponseEntity<List<AssetsDto>> getAllAssets() {
        List<AssetsDto> assetsList = assetService.listOfAllAsset();
        if (assetsList.isEmpty()) {
            return ResponseEntity.noContent().build(); // Return 204 No Content if the list is empty
        }
        return ResponseEntity.ok(assetsList); // Return 200 OK with the asset list
    }

    @PostMapping("asset/saveAsset")
    public ResponseEntity<AssetsDto> saveAsset(@RequestBody AssetsDto assetDto) {
        AssetsDto savedAsset = assetService.saveAsset(assetDto);
        return new ResponseEntity<>(savedAsset, HttpStatus.CREATED);
    }

    @GetMapping("asset/id/{serialNumber}")
    public Object getById(@PathVariable String serialNumber) {
        // LOGGER.debug("Hited getById endpoint");
        return assetService.getAssetBySerialNumber(serialNumber);
    }

    @PutMapping("asset/updateAsset/{serialNumber}")
    public ResponseEntity<SaveAssetResponse> updateEmployee(@PathVariable String serialNumber,
                                                            @RequestBody SaveAssetResponse saveAssetResponse) {

        saveAssetResponse.setSerialNumber(serialNumber);

        SaveAssetResponse updateAsset = assetService.updateAsset(saveAssetResponse);

        return new ResponseEntity<>(updateAsset, HttpStatus.OK);
    }

    @DeleteMapping("asset/delete/{id}")
    public String delete(@PathVariable int id) {
        assetService.deleteAsset(id);
        return "Asset delete successfully. ";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
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


//	@GetMapping("/getAllAssetType/{names}")
//	public List<AssetsEntity> getByAssetType(@PathVariable String names) {
//
//		List<AssetsEntity> byAssetNames = assetService.getByAssetNames(names);
//		return byAssetNames;
//
//	}

    // working
//	@GetMapping("asset/get/by/status/{status}")
//	public List<AssetsEntity> getThroughStatus(@PathVariable String status) {
//		// LOGGER.debug("Hited getThroughState endPoint");
//		return assetService.getThroughStatus(status);
//	}

//	@PostMapping("asset/save/bulk")
//	public Object BulkSave(@RequestBody List<AssetsEntity> assetList) {
//		assetList.forEach(asset -> assetService.saveAsset(asset));
//		return "Saved Successfully";
//	}

//	@GetMapping("asset/assigned")
//	public int getAssignedCount() {
//		return assetService.getCountOfAssigned();
//	}

//	@GetMapping("asset/unassigned")
//	public int getUnAssignedCount() {
//		return assetService.getCountOfUnassigned();
//	}
//
//	@GetMapping("asset/laptopcount/{id}")
//	public int getLaptopCount(@PathVariable String id) {
//		return assetService.getLaptopCountByLocation(id);
//	}
//
//	@GetMapping("asset/total/laptop/count")
//	public int getTptalLaptopCount() {
//		return assetService.totalLaptops();
//	}

//	@GetMapping("asset/unassigned/{id}")
//	public int getUnAssignedByLocation(@PathVariable String id) {
//		return assetService.getCountOfUnassignedByLocation(id);
//	}

//	@GetMapping("asset/get/total/unassigned")
//	public List<AssetCounterDto> getUnassignedAndLaptops() {
//		return assetService.getUnassignedAndTotalLaptops();
//	}

//	@GetMapping("asset/get/under/warenty")
//	public List<AssetsEntity> getLaptopsUnderWarenty() {
//		assetService.getLaptopsUnderWarenty();
//		return assetService.getLaptopsUnderWarenty();
//	}

//	@GetMapping("asset/get/over/warenty")
//	public List<AssetsEntity> getLaptopsOverWarenty() {
//		assetService.getLaptopsOverWarenty();
//		return assetService.getLaptopsOverWarenty();
//	}

//	@GetMapping("asset/return/{serialNo}/{empId}")
//	public String returned(@PathVariable String serialNo, @PathVariable String empId) {
//		assetService.saveHistory(serialNo, empId);
//		return "stored Successfully";
//	}

//	@PostMapping("assetname/save")
//	public AssetNameDTO saveAssetName(@RequestBody AssetNameDTO assetNameDTO) {
//		return assetNameServiceImpl.save(assetNameDTO);
//	}

//	@GetMapping("assetname/getall")
//	public List<AssetNameDTO> getAllAssetNames() {
//		return assetNameServiceImpl.getAll();
//	}

//	@GetMapping("asset/getUnassigned")
//	public List<AssetsEntity> getUnAssigned() {
//		return assetService.getUnAssigned();
//	}

//	@GetMapping("/v2/asset/{assetId}")
//	public ResponseEntity<AssetsDto> getAssetsDetails(@PathVariable Integer assetId) {
//		try {
//			AssetsDto assetsDto = assetService.getAssetsDetails(assetId);
//			return ResponseEntity.ok(assetsDto);
//		} catch (RuntimeException e) {
//			return ResponseEntity.status(404).body(null); // You might want to handle this more gracefully
//		}
//	}

}
