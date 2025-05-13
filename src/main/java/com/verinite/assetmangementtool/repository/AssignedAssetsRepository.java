package com.verinite.assetmangementtool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.verinite.assetmangementtool.entity.AssignedAssetsEntity;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface AssignedAssetsRepository extends JpaRepository<AssignedAssetsEntity, Integer> {

    //	@Query(value = "select from tbl_assined_assets where assigned_assets_id =?", nativeQuery = true)
//	AssignedAssetsEntity getAssignedAssetsById(int assignedId);
    List<AssignedAssetsEntity> findAllByOrderByAssignedAssetsIdDesc();

    AssignedAssetsEntity findByAssignedAssetsId(int assignedAssetsId);
    @Transactional
    @Modifying
    @Query(value = "UPDATE tbl_assined_assets SET status = :status WHERE assigned_assets_id = :assetId", nativeQuery = true)
    int updateUnassignStatus(@Param("status") String status, @Param("assetId") int assetId);
    @Query(value = "select from tbl_assined_assets where assets_id =?", nativeQuery = true)
    AssignedAssetsEntity getAssignedAssetsByAssetsId(int assetId);

    AssignedAssetsEntity findBySerialNumber(String serialNo);

    AssignedAssetsEntity findBySerialNumberAndEmpId(String serialNo, String empId);

    List<AssignedAssetsEntity> findByEmpId(String empId);

    List<AssignedAssetsEntity> findByEmpIdNotNull();
}
