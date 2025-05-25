package com.verinite.assetmangementtool.repository;

import com.verinite.assetmangementtool.entity.AssetsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssetsRepository extends JpaRepository<AssetsEntity, Number> {

    List<AssetsEntity> findByStatus(String str);

    List<AssetsEntity> findAllByOrderByAssetIdDesc();

    @Query(value = "select laptop_count from tbl_Count where id=?1", nativeQuery = true)
    int getTotal(int id);

    AssetsEntity findBySerialNumber(String id);

    Optional<AssetsEntity> findByAssetId(int assetId);

    Optional<AssetsEntity> findByEmpId(String empId);

    @Query(value = "SELECT LOWER(asset_name) AS assetName, LOWER(location) AS location, "
            + "SUM(CASE WHEN LOWER(status) = 'unassigned' THEN 1 ELSE 0 END) AS unassignedCount, "
            + "SUM(CASE WHEN LOWER(status) = 'assigned' THEN 1 ELSE 0 END) AS assignedCount, "
            + "SUM(CASE WHEN LOWER(status) IN ('unassigned', 'assigned') THEN 1 ELSE 0 END) AS totalCount "
            + "FROM tbl_assets " + "WHERE LOWER(location) IN :locations "
            + "GROUP BY LOWER(asset_name), LOWER(location)", nativeQuery = true)
    List<Object[]> findAggregatedAssetCountsByLocation(@Param("locations") List<String> locations);

    @Query(value = "SELECT LOWER(location) AS location, " + "LOWER(asset_name) AS assetName, "
            + "SUM(CASE WHEN LOWER(status) = 'unassigned' THEN 1 ELSE 0 END) AS unassignedCount, "
            + "SUM(CASE WHEN LOWER(status) = 'assigned' THEN 1 ELSE 0 END) AS assignedCount, "
            + "SUM(CASE WHEN LOWER(status) IN ('unassigned', 'assigned') THEN 1 ELSE 0 END) AS totalCount "
            + "FROM tbl_assets " + "WHERE LOWER(location) IN :locations "
            + "GROUP BY LOWER(location), LOWER(asset_name)", nativeQuery = true)
    List<Object[]> findAggregatedAssetCountsByLocations(@Param("locations") List<String> locations);

    //@Query("SELECT DISTINCT LOWER(a.location) FROM tbl_assets a")
    //List<String> findDistinctLocations();
    @Transactional
    @Modifying
    @Query(value = "update tbl_assets set status =:status where serial_number =:serialNumber", nativeQuery = true)
    int updateUnassignStatus(@Param("status") String status, @Param("serialNumber") String serialNumber);

    @Query(value = "SELECT LOWER(location) AS location," +
            "SUM(CASE WHEN LOWER(status) = 'unassigned' THEN 1 ELSE 0 END) AS unassignedCount, " +
            "SUM(CASE WHEN LOWER(status) = 'assigned' THEN 1 ELSE 0 END) AS assignedCount, " +
            "SUM(CASE WHEN LOWER(status) IN ('unassigned', 'assigned') THEN 1 ELSE 0 END) AS totalCount " +
            "FROM tbl_assets " +
            "WHERE LOWER(asset_name) = LOWER(:assetName) " +
            "GROUP BY LOWER(location), LOWER(asset_name)",
            nativeQuery = true)
    List<Object[]> findAggregatedAssetCountsByAssetNameAndLocation(@Param("assetName") String assetName);

    @Query(value = "SELECT DISTINCT LOWER(a.asset_name) FROM tbl_assets a", nativeQuery = true)
    List<String> findDistinctAssetTypes();
}
