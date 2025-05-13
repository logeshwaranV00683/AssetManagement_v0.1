package com.verinite.assetmangementtool.repository;

import com.verinite.assetmangementtool.dto.AssetNameDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface AssetNamesRepo extends JpaRepository<AssetNameDTO, Long>{

    AssetNameDTO findByAssetName(String assetName);
}
