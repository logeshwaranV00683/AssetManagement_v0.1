package com.verinite.assetmanagementtool.repository;

import com.verinite.assetmanagementtool.entity.CountOfAssetsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssetCountRepository extends JpaRepository<CountOfAssetsEntity, Integer> {
    Optional<CountOfAssetsEntity> findByLocationIgnoreCaseAndTypeIgnoreCase(String location, String type);

    List<CountOfAssetsEntity> findByLocationIgnoreCase(String location);

    @Query(value = "SELECT SUM(assigned) FROM tbl_count", nativeQuery = true)
    Integer getTotalAssigned();

    @Query(value = "SELECT SUM(unassigned) FROM tbl_count", nativeQuery = true)
    Integer getTotalUnassigned();

    @Query(value = "SELECT DISTINCT type FROM tbl_count ", nativeQuery = true)
    List<String> findDistinctAssetTypes();
    @Query(value = "SELECT DISTINCT location FROM tbl_count", nativeQuery = true)
    List<String> findDistinctLocation();
}
