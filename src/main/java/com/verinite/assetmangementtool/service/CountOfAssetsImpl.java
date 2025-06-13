package com.verinite.assetmangementtool.service;

import com.verinite.assetmangementtool.entity.CountOfAssets;
import com.verinite.assetmangementtool.repository.AssetCountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CountOfAssetsImpl implements CountOFAssetsService {
    @Autowired
    AssetCountRepository assetCountRepository;

    @Override
    public int getLaptopCount(String id) {
        try {
            return assetCountRepository.getLaptopCount(id);
        } catch (Exception e) {
            return 0;
        }

    }


    @Override
    public CountOfAssets postAssetCount(CountOfAssets countOfAssets) {
        return assetCountRepository.save(countOfAssets);
    }

    @Override
    public Object updateAssetCount(String location, CountOfAssets countOfAssets) {
        try {
            CountOfAssets countOfAssets2 = assetCountRepository.findByLocation(location);
            if (countOfAssets.getLaptopCount() != 0)
                countOfAssets2.setLaptopCount(countOfAssets.getLaptopCount());
            if (countOfAssets.getBagCount() != 0)
                countOfAssets2.setBagCount(countOfAssets.getBagCount());
            if (countOfAssets.getLaptopChargerCount() != 0)
                countOfAssets2.setLaptopChargerCount(countOfAssets.getLaptopChargerCount());
            if (countOfAssets.getHeadPhonesCount() != 0)
                countOfAssets2.setHeadPhonesCount(countOfAssets.getHeadPhonesCount());
            if (countOfAssets.getMouseCount() != 0)
                countOfAssets2.setMouseCount(countOfAssets.getMouseCount());
            assetCountRepository.save(countOfAssets2);
            return "Updated Successfully";
        } catch (Exception e) {
            return "No Location is found";
        }
    }

    @Override
    public List<CountOfAssets> getAll() {
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
        List<CountOfAssets> countOfAssets = assetCountRepository.findAll();
        int total = 0;
        for (CountOfAssets i : countOfAssets) {
            total += i.getLaptopCount();
        }
        return total;
    }
}
