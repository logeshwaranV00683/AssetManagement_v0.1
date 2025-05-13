package com.verinite.assetmangementtool.service;

import com.verinite.assetmangementtool.entity.AssetsHistoryEntity;

import java.util.List;

public interface AssetsHistoryServices {

	public AssetsHistoryEntity  saveHistory(AssetsHistoryEntity history);
	public List<AssetsHistoryEntity> getAll();
	//public Object getByMail(String mail);
	//public Object returnDetails(String mail);
	//public Object updateHistory(String mail,AssetsHistoryEntity history);
	public Object getByHistoryId(long id);

}
