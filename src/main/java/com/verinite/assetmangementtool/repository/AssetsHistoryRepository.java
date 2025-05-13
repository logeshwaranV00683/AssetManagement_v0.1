package com.verinite.assetmangementtool.repository;

import com.verinite.assetmangementtool.entity.AssetsHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetsHistoryRepository extends JpaRepository<AssetsHistoryEntity, Number>{

	//AssetsHistoryEntity getAssetsHistoryByAssetsId(int assetId);

	//AssetsHistoryEntity findByMail(String mail);


}


