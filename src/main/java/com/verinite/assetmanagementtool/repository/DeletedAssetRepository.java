package com.verinite.assetmanagementtool.repository;

import com.verinite.assetmanagementtool.entity.DeletedAssetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeletedAssetRepository extends JpaRepository<DeletedAssetEntity, Integer> {
}
