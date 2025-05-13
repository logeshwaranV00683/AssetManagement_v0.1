package com.verinite.assetmangementtool.repository;

import com.verinite.assetmangementtool.entity.DesignationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DesignationRepository extends JpaRepository<DesignationEntity,Long> {
    DesignationEntity findByTitle(String name);

    DesignationEntity findByDescId(long code);

    DesignationEntity findByPosition(String position);
}
