package com.verinite.assetmangementtool.service;

import com.verinite.assetmangementtool.entity.AssetsHistoryEntity;
import com.verinite.assetmangementtool.repository.AssetsHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetsHistoryServiceImpl implements AssetsHistoryServices{

	@Autowired
	AssetsHistoryRepository assetsHistoryRepository;

	@Override
	public AssetsHistoryEntity saveHistory(AssetsHistoryEntity history) {

		return assetsHistoryRepository.save(history);

	}

	@Override
	public List<AssetsHistoryEntity> getAll() {
		return assetsHistoryRepository.findAll();
	}

	@Override
	public Object getByHistoryId(long id) {
		return assetsHistoryRepository.findById(id).get();
	}

}