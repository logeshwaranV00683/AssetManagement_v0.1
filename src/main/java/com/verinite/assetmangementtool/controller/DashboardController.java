package com.verinite.assetmangementtool.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.verinite.assetmangementtool.entity.AssetsEntity;
import com.verinite.assetmangementtool.service.AssetService;
import com.verinite.assetmangementtool.service.DashboardServiceImpl;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
//@CrossOrigin(origins = "https://asset-tool.netlify.app", allowedHeaders = "*", allowCredentials = "true")
@RequestMapping("/dashboard")
public class DashboardController {

	@Autowired
	private AssetService assetService;

	@Autowired
	private DashboardServiceImpl dashboardServiceImpl;

	@GetMapping("/getAllAssets")
	public List<AssetsEntity> getAssetCountsWithLocation() {

		List<AssetsEntity> countsWithLocation = dashboardServiceImpl.getAssetCountsWithLocation();
		return countsWithLocation;
	}

	@GetMapping("/assets/countsByLocation")
	public Map<String, Map<String, Object>> getAssetCounts(@RequestParam List<String> locations) {
		return dashboardServiceImpl.getFormattedAssetCounts(locations);
	}

	@GetMapping("/unique/assets")
	public ResponseEntity<List<String>> getUniqueAssetTypes() {
		List<String> assetTypes = dashboardServiceImpl.getUniqueAssetTypes();
		return ResponseEntity.ok(assetTypes);
	}

	@GetMapping("/assets/count/{assetName}")
	public ResponseEntity<Map<String, Map<String, Object>>> getAssetsCountWithLocationByAssetName(
			@PathVariable String assetName) {

		Map<String, Map<String, Object>> locationByAssetName = dashboardServiceImpl
				.getAssetsCountWithLocationByAssetName(assetName);
		return new ResponseEntity<>(locationByAssetName, HttpStatus.OK);
	}
}
