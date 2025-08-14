package com.verinite.assetmanagementtool.repository;

import com.verinite.assetmanagementtool.entity.AssetsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssetsRepository extends JpaRepository<AssetsEntity, Number> {

    List<AssetsEntity> findAllByOrderByAssetIdDesc();

    AssetsEntity findBySerialNumber(String id);

    Optional<AssetsEntity> findByAssetId(int assetId);

    Optional<AssetsEntity> findByEmpId(String empId);

    @Query(value = "SELECT LOWER(location) AS location, " + "LOWER(asset_name) AS assetName, "
            + "SUM(CASE WHEN LOWER(status) = 'unassigned' THEN 1 ELSE 0 END) AS unassignedCount, "
            + "SUM(CASE WHEN LOWER(status) = 'assigned' THEN 1 ELSE 0 END) AS assignedCount, "
            + "SUM(CASE WHEN LOWER(status) IN ('unassigned', 'assigned') THEN 1 ELSE 0 END) AS totalCount "
            + "FROM tbl_assets " + "WHERE LOWER(location) IN :locations "
            + "GROUP BY LOWER(location), LOWER(asset_name)", nativeQuery = true)
    List<Object[]> findAggregatedAssetCountsByLocations(@Param("locations") List<String> locations);

    @Transactional
    @Modifying
    @Query(value = "update tbl_assets set status =:status,emp_Id =:empId,assigned_By=:assignedBy,assigned_date = :assignedDate where serial_number IN (:serialNumber)", nativeQuery = true)
    void updateUnassignStatus(@Param("status") String status, @Param("empId") String emp_Id, @Param("assignedBy") String assigned_By, @Param("assignedDate") Date assigned_Date, @Param("serialNumber") List<String> serialNumber);

    @Query(value = "SELECT LOWER(location) AS location," +
            "SUM(CASE WHEN LOWER(status) = 'unassigned' THEN 1 ELSE 0 END) AS unassignedCount, " +
            "SUM(CASE WHEN LOWER(status) = 'assigned' THEN 1 ELSE 0 END) AS assignedCount, " +
            "SUM(CASE WHEN LOWER(status) IN ('unassigned', 'assigned') THEN 1 ELSE 0 END) AS totalCount " +
            "FROM tbl_assets " +
            "WHERE LOWER(type) = LOWER(:assetType) " +
            "GROUP BY LOWER(location), LOWER(type)",
            nativeQuery = true)
    List<Object[]> findAggregatedAssetCountsByAssetTypeAndLocation(@Param("assetType") String assetType);

    boolean existsBySerialNumber(String serialNumber);

    @Query(value = "SELECT DISTINCT asset_sourced_by FROM tbl_assets", nativeQuery = true)
    List<String> getUniqueAssetSourcedBy();

    void deleteBySerialNumber(String serialNumber);
}
