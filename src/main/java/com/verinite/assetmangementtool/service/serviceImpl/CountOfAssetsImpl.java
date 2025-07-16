package com.verinite.assetmangementtool.service.serviceImpl;

import com.verinite.assetmangementtool.entity.CountOfAssetsEntity;
import com.verinite.assetmangementtool.repository.AssetCountRepository;
import com.verinite.assetmangementtool.service.CountOFAssetsService;
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
    public int getLaptopCount(String id) {
        try {
            return assetCountRepository.getLaptopCount(id);
        } catch (Exception e) {
            return 0;
        }

    }


    @Override
    public CountOfAssetsEntity postAssetCount(CountOfAssetsEntity countOfAssetsEntity) {
        return assetCountRepository.save(countOfAssetsEntity);
    }

    @Override
    public Object updateAssetCount(String location, CountOfAssetsEntity countOfAssetsEntity) {
        try {
            CountOfAssetsEntity countOfAssetsEntity2 = assetCountRepository.findByLocation(location);
            if (countOfAssetsEntity.getLaptopCount() != 0)
                countOfAssetsEntity2.setLaptopCount(countOfAssetsEntity.getLaptopCount());
            if (countOfAssetsEntity.getBagCount() != 0)
                countOfAssetsEntity2.setBagCount(countOfAssetsEntity.getBagCount());
            if (countOfAssetsEntity.getLaptopChargerCount() != 0)
                countOfAssetsEntity2.setLaptopChargerCount(countOfAssetsEntity.getLaptopChargerCount());
            if (countOfAssetsEntity.getHeadPhonesCount() != 0)
                countOfAssetsEntity2.setHeadPhonesCount(countOfAssetsEntity.getHeadPhonesCount());
            if (countOfAssetsEntity.getMouseCount() != 0)
                countOfAssetsEntity2.setMouseCount(countOfAssetsEntity.getMouseCount());
            assetCountRepository.save(countOfAssetsEntity2);
            return "Updated Successfully";
        } catch (Exception e) {
            return "No Location is found";
        }
    }

    @Override
    public List<CountOfAssetsEntity> getAll() {
        return assetCountRepository.findAll();
    }



    public Map<String, Integer> getByLoc(String location) {
        List<Object[]> resultList = assetCountRepository.findByLoc(location);

        if (resultList == null || resultList.isEmpty()) {
            return Collections.emptyMap();
        }

        Object[] row = resultList.get(0); // first row

        String[] keys = {
                "bag", "camera", "data_card", "dvr", "fire_wall",
                "head_phones", "laptop_charger", "laptop", "mobile",
                "mouse", "projector", "speaker", "switch"
        };

        Map<String, Integer> countMap = new LinkedHashMap<>();
        for (int i = 0; i < keys.length; i++) {
            countMap.put(keys[i], ((Number) row[i]).intValue());
        }

        return countMap;
    }


    public Map<String, Integer> getUnassignedAssets(String location) {
        List<Object[]> resultList = assetCountRepository.findUnassignedAssets(location);

        if (resultList == null || resultList.isEmpty()) {
            return Collections.emptyMap();
        }

        Object[] row = resultList.get(0); // first row

        String[] keys = {
                "bag", "camera", "data_card", "dvr", "fire_wall",
                "head_phones", "laptop_charger", "laptop", "mobile",
                "mouse", "projector", "speaker", "switch"
        };

        Map<String, Integer> countMap = new LinkedHashMap<>();
        for (int i = 0; i < keys.length; i++) {
            countMap.put(keys[i], ((Number) row[i]).intValue());
        }

        return countMap;
    }

    public Map<String, Integer> getAssignedAssets(String location) {
        List<Object[]> resultList = assetCountRepository.findAssignedAssets(location);

        if (resultList == null || resultList.isEmpty()) {
            return Collections.emptyMap();
        }

        Object[] row = resultList.get(0); // first row

        String[] keys = {
                "bag", "camera", "data_card", "dvr", "fire_wall",
                "head_phones", "laptop_charger", "laptop", "mobile",
                "mouse", "projector", "speaker", "switch"
        };

        Map<String, Integer> countMap = new LinkedHashMap<>();
        for (int i = 0; i < keys.length; i++) {
            countMap.put(keys[i], ((Number) row[i]).intValue());
        }

        return countMap;
    }


    @Override
    public int totalLaptops() {
        List<CountOfAssetsEntity> countOfAssetEntities = assetCountRepository.findAll();
        int total = 0;
        for (CountOfAssetsEntity i : countOfAssetEntities) {
            total += i.getLaptopCount();
        }
        return total;
    }


}
