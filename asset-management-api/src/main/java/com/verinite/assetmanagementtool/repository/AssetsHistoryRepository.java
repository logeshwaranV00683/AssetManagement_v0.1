package com.verinite.assetmanagementtool.repository;

import com.verinite.assetmanagementtool.entity.AssetsHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AssetsHistoryRepository extends JpaRepository<AssetsHistoryEntity, Number> {


    @Query(value = "select * from tbl_assets_history where serial_number IN (:serialNumber) AND assigned_date IN (:assignedDate)", nativeQuery = true)
    List<AssetsHistoryEntity> findBySerialNumberAndAssignedDate(@Param("serialNumber") String serialNo, @Param("assignedDate") LocalDate assignedDate);

    List<AssetsHistoryEntity> findBySerialNumberOrderByAssignedDateAsc(String serialNumber);

}


