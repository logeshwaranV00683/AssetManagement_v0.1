package com.verinite.assetmanagementtool.repository;

import com.verinite.assetmanagementtool.entity.AssignedAssetsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface AssignedAssetsRepository extends JpaRepository<AssignedAssetsEntity, Integer> {


    List<AssignedAssetsEntity> findAllByOrderByAssignedAssetsIdDesc();

    AssignedAssetsEntity findByAssignedAssetsId(int assignedAssetsId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE tbl_assigned_assets SET status = :status WHERE assigned_assets_id IN :assetId", nativeQuery = true)
    int updateUnassignStatus(@Param("status") String status, @Param("assetId") int assetId);

    @Query(value = "select from tbl_assigned_assets where assets_id =?", nativeQuery = true)
    AssignedAssetsEntity getAssignedAssetsByAssetsId(int assetId);

    @Query(value = "select * from tbl_assigned_assets where serial_number IN (?1)", nativeQuery = true)
    AssignedAssetsEntity findBySerialNumber(String serialNumber);

    AssignedAssetsEntity findBySerialNumberAndEmpId(String serialNo, String empId);

    List<AssignedAssetsEntity> findByEmpId(String empId);

    List<AssignedAssetsEntity> findByEmpIdNotNull();

}
