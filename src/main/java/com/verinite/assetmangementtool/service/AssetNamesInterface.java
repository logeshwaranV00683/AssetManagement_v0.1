package com.verinite.assetmangementtool.service;

import com.verinite.assetmangementtool.dto.AssetNameDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AssetNamesInterface {
	
	public List<AssetNameDTO> getAll();
	AssetNameDTO save(AssetNameDTO assetNamesDto);
}
