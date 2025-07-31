package com.verinite.assetmanagementtool.service.serviceImpl;

import com.verinite.assetmanagementtool.entity.CountOfAssetsEntity;
import com.verinite.assetmanagementtool.repository.AssetCountRepository;
import com.verinite.assetmanagementtool.service.CountOFAssetsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CountOfAssetsImpl implements CountOFAssetsService {
    @Autowired
    AssetCountRepository assetCountRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public CountOfAssetsEntity postAssetCount(CountOfAssetsEntity countOfAssetsEntity) {
        return assetCountRepository.save(countOfAssetsEntity);
    }

    @Override
    public Object updateAssetCount(String location, CountOfAssetsEntity countOfAssetsEntity) {
        try {
            Optional<CountOfAssetsEntity> optionalEntity = assetCountRepository
                    .findByLocationIgnoreCaseAndTypeIgnoreCase(location, countOfAssetsEntity.getType());

            if (optionalEntity.isPresent()) {
                CountOfAssetsEntity existing = optionalEntity.get();

                if (countOfAssetsEntity.getTotal() != null)
                    existing.setTotal(countOfAssetsEntity.getTotal());

                if (countOfAssetsEntity.getAssigned() != null)
                    existing.setAssigned(countOfAssetsEntity.getAssigned());

                if (countOfAssetsEntity.getUnassigned() != null)
                    existing.setUnassigned(countOfAssetsEntity.getUnassigned());

                if (countOfAssetsEntity.getScrapped() != null)
                    existing.setScrapped(countOfAssetsEntity.getScrapped());

                assetCountRepository.save(existing);
                return "Updated Successfully";
            } else {
                return "No data found for location: " + location + " and type: " + countOfAssetsEntity.getType();
            }
        } catch (Exception e) {
            return "Error occurred while updating: " + e.getMessage();
        }
    }


    @Override
    public List<CountOfAssetsEntity> getAll() {
        return assetCountRepository.findAll();
    }


    public Map<String, Integer> getByLoc(String location) {
        List<CountOfAssetsEntity> resultList = assetCountRepository.findByLocationIgnoreCase(location);

        if (resultList == null || resultList.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, Integer> countMap = new LinkedHashMap<>();

        for (CountOfAssetsEntity entity : resultList) {
            countMap.put(entity.getType().toLowerCase(), entity.getTotal());
        }

        return countMap;
    }


    public Map<String, Integer> getUnassignedAssets(String location) {
        List<CountOfAssetsEntity> entities = assetCountRepository.findByLocationIgnoreCase(location);

        if (entities == null || entities.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, Integer> countMap = new LinkedHashMap<>();

        for (CountOfAssetsEntity entity : entities) {
            countMap.put(
                    entity.getType().toLowerCase(),
                    entity.getUnassigned() != null ? entity.getUnassigned() : 0
            );
        }

        return countMap;
    }


    public Map<String, Integer> getAssignedAssets(String location) {
        List<CountOfAssetsEntity> entities = assetCountRepository.findByLocationIgnoreCase(location);

        if (entities == null || entities.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, Integer> countMap = new LinkedHashMap<>();

        for (CountOfAssetsEntity entity : entities) {
            countMap.put(
                    entity.getType().toLowerCase(),
                    entity.getAssigned() != null ? entity.getAssigned() : 0
            );
        }

        return countMap;
    }


}
