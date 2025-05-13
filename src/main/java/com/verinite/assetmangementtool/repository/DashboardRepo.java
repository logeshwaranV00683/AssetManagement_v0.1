package com.verinite.assetmangementtool.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.verinite.assetmangementtool.entity.AssetTypes;

public interface DashboardRepo extends JpaRepository<AssetTypes, Integer>{

}
