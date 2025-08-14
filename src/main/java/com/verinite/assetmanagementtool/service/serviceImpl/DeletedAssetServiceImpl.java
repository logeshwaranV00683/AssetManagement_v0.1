package com.verinite.assetmanagementtool.service.serviceImpl;


import com.verinite.assetmanagementtool.dto.DeletedAssetDto;
import com.verinite.assetmanagementtool.entity.AssetsEntity;
import com.verinite.assetmanagementtool.entity.DeletedAssetEntity;
import com.verinite.assetmanagementtool.repository.AdminRegistrationRepository;
import com.verinite.assetmanagementtool.repository.AssetsRepository;
import com.verinite.assetmanagementtool.repository.DeletedAssetRepository;
import com.verinite.assetmanagementtool.service.DeletedAssetService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class DeletedAssetServiceImpl implements DeletedAssetService {

    @Autowired
    DeletedAssetRepository deletedAssetRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    AssetsRepository assetRepo;
    @Autowired
    AdminRegistrationRepository adminRegistrationRepository;

    @Override
    public List<DeletedAssetDto> getAllDeleted() {
        return deletedAssetRepository.findAll().stream().map(data -> modelMapper.map(data, DeletedAssetDto.class)).toList();
    }


    @Override
    @Transactional
    public void permananteDelete(String serialNumber, String adminId) {

        AssetsEntity assetsEntity = assetRepo.findBySerialNumber(serialNumber);
        if (!adminRegistrationRepository.existsByEmpId(adminId)) {
            throw new IllegalArgumentException("Admin id is not valid " + adminId);
        }
        if (assetsEntity == null) {
            throw new IllegalArgumentException("Asset not found for SerialNumber: " + serialNumber);
        }
        if (assetsEntity.getStatus().equalsIgnoreCase("Assigned")) {
            throw new IllegalArgumentException("Asset in Assigned Status");
        }
        deletedAssetRepository.save(new DeletedAssetEntity(assetsEntity.getSerialNumber(),
                assetsEntity.getAssetName(), assetsEntity.getPurchaseDate(),
                LocalDate.now(), assetsEntity.getType(), assetsEntity.getLocation(),
                assetsEntity.getAssetSourcedBy(), adminId));
        assetRepo.deleteBySerialNumber(serialNumber);
    }
}
