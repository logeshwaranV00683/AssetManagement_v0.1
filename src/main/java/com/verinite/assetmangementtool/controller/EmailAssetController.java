package com.verinite.assetmangementtool.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.verinite.assetmangementtool.dto.AssignableAssetDto;
import com.verinite.assetmangementtool.service.EmailAssetService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
//@CrossOrigin(origins = "https://asset-tool.netlify.app", allowedHeaders = "*", allowCredentials = "true")
@RequestMapping("/api/assets")
public class EmailAssetController {

	@Autowired
	private EmailAssetService emailAssetService;

	@PostMapping("/assign")
	public ResponseEntity<?> assignAsset(@RequestBody List<AssignableAssetDto> assignableAssetDto) {
		return emailAssetService.save(assignableAssetDto);
	}
}
