package com.verinite.assetmangementtool.service;

import com.verinite.assetmangementtool.entity.AssetsHistoryEntity;
import com.verinite.assetmangementtool.entity.AssignedAssetsEntity;
import com.verinite.assetmangementtool.repository.AssetsHistoryRepository;
import com.verinite.assetmangementtool.repository.AssignedAssetsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class AssetsHistoryServiceImpl implements AssetsHistoryServices {

    @Autowired
    AssetsHistoryRepository assetsHistoryRepository;

    @Autowired
    AssetsHistoryServices assetsHistoryServices;

    @Autowired
    AssignedAssetsRepository assignedAssetsRepository;

    @Override
    public AssetsHistoryEntity saveHistory(AssetsHistoryEntity assetsHistoryEntity) {

        return assetsHistoryRepository.save(assetsHistoryEntity);

    }

    @Override
    public List<AssetsHistoryEntity> getAll() {
        return assetsHistoryRepository.findAll();
    }

    @Override
    public Object getByHistoryId(String serialNumber) {

        for (AssetsHistoryEntity assetsHistoryEntity : getAll()) {
            if (serialNumber.equalsIgnoreCase(assetsHistoryEntity.getSerialNumber())) {
                return assetsHistoryRepository.findById(assetsHistoryEntity.getHistoryId()).orElseThrow(() -> new UsernameNotFoundException(" there is no Asset  not found with serial number " + serialNumber));

            }
        }
        return null;
    }

    public void saveHistory(AssignedAssetsEntity assignedAssetsEntity) {

        AssetsHistoryEntity assetHistory = new AssetsHistoryEntity();

        assetHistory.setSerialNumber(assignedAssetsEntity.getSerialNumber());
        assetHistory.setEmpId(assignedAssetsEntity.getEmpId());

        assetHistory.setAssignedDate(assignedAssetsEntity.getAssignedDate());
        assetHistory.setAssignedBy(assignedAssetsEntity.getAssignedBy());
        assetsHistoryRepository.save(assetHistory);
    }


    public void updateHistory(AssignedAssetsEntity assignedAssets, String serialNumber) {
        AssetsHistoryEntity assets = assetsHistoryRepository.findBySerialNumberAndAssignedDate(serialNumber, assignedAssets.getAssignedDate());
        LocalDate localDate = LocalDate.now();
        assets.setReturnDate(localDate);
        assetsHistoryServices.saveHistory(assets);
    }

    @Override
    public List<AssetsHistoryEntity> getAssetsHistoryBySerialNumberSorted(String serialNumber) {
        return assetsHistoryRepository.findBySerialNumberOrderByAssignedDateAsc(serialNumber);
    }
}