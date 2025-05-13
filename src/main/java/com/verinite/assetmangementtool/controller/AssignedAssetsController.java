package com.verinite.assetmangementtool.controller;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.verinite.assetmangementtool.dto.AssignableAssetDto;
import com.verinite.assetmangementtool.dto.AssignedAssetDtoList;
import com.verinite.assetmangementtool.entity.AssignedAssetsEntity;
import com.verinite.assetmangementtool.service.AssetServiceImpl;
import com.verinite.assetmangementtool.service.AssignedAssetsServiceImpl;

@RestController
@RequestMapping("/assetManager/v1/admin/")
@CrossOrigin(origins = "*", allowedHeaders = "*")
//@CrossOrigin(origins = "https://asset-tool.netlify.app", allowedHeaders = "*", allowCredentials = "true")
public class AssignedAssetsController {

	@Autowired
	private AssignedAssetsServiceImpl assignedAssetsService;

	@Autowired
	private AssetServiceImpl assetService;

//	@PostMapping("assignable/save")
//	public ResponseEntity<?> assignAsset(@RequestBody AssignableAssetDto assignableAssetDto) {
//		try {
//			return assignedAssetsService.save(assignableAssetDto);
//		} catch (Exception e) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//					.body("An error occurred while assigning the asset.");
//		}
//	}

	@PostMapping("assignable/save")
	public ResponseEntity<?> assignMultipleAssets(@RequestBody List<AssignableAssetDto> assignableAssetDtos) {
		if (assignableAssetDtos == null || assignableAssetDtos.isEmpty()) {
			return ResponseEntity.badRequest().body("Asset list is empty");
		}

		// Call the service method to save/assign assets
		return assignedAssetsService.save(assignableAssetDtos);
	}
	@PutMapping("asset/un-assign")
	public ResponseEntity<?> unassignAssetApi(@RequestParam  Integer assignedAssetId) {

		AssignedAssetsEntity assignedAssets =  assignedAssetsService.unAssignAsset(assignedAssetId);
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

	@GetMapping("/export/assigned-assets")
	public ResponseEntity<byte[]> exportAssignedAssets() {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			// Generate Excel file content in memory
			assignedAssetsService.exportAssignedAssetsToExcel(out);

			// Set response headers for file download
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDispositionFormData("attachment", "assigned_assets.xlsx");

			return ResponseEntity.ok()
					.headers(headers)
					.body(out.toByteArray());
		} catch (Exception e) {
			return ResponseEntity.internalServerError()
					.body(("Error generating Excel file: " + e.getMessage()).getBytes());
		}
	}
}
