package com.verinite.assetmangementtool.repository;

import com.verinite.assetmangementtool.entity.CountOfAssets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetCountRepository extends JpaRepository<CountOfAssets, String> {

	@Query(value = "SELECT laptop_count FROM tbl_count WHERE location = ?1", nativeQuery = true)
	int getLaptopCount(String location);

	CountOfAssets findByLocation(String str);

}
