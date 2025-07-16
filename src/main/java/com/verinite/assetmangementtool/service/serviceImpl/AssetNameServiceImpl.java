package com.verinite.assetmangementtool.service.serviceImpl;

import com.verinite.assetmangementtool.dto.AssetNameDTO;
import com.verinite.assetmangementtool.repository.AssetNamesRepo;
import com.verinite.assetmangementtool.service.AssetNamesInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetNameServiceImpl implements AssetNamesInterface {
    @Autowired
    AssetNamesRepo assetNamesRepo;

    @Override
    public AssetNameDTO save(AssetNameDTO assetNamesDto) {
        assetNamesDto.setAssetName(assetNamesDto.getAssetName().toLowerCase());
        if (assetNamesRepo.findByAssetName(assetNamesDto.getAssetName()) != null) {
            return assetNamesDto;
        } else {

            return assetNamesRepo.save(assetNamesDto);
        }

    }

    @Override
    public List<AssetNameDTO> getAll() {
        return assetNamesRepo.findAll();
    }

}
