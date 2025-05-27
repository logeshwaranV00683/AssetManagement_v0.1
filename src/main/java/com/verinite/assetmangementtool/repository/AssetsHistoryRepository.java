package com.verinite.assetmangementtool.repository;

import com.verinite.assetmangementtool.entity.AssetsHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AssetsHistoryRepository extends JpaRepository<AssetsHistoryEntity, Number> {


    AssetsHistoryEntity findBySerialNumberAndAssignedDate(String serialNo, Date assignedDate);

    List<AssetsHistoryEntity> findBySerialNumberOrderByAssignedDateAsc(String serialNumber);

}


