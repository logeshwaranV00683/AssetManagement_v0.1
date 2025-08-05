package com.verinite.assetmanagementtool.repository;

import com.verinite.assetmanagementtool.entity.DeletedAssetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeletedAssetRepository extends JpaRepository<DeletedAssetEntity, Integer>
{}
