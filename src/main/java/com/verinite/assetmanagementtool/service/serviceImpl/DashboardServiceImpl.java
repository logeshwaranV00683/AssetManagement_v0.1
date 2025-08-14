package com.verinite.assetmanagementtool.service.serviceImpl;

import com.verinite.assetmanagementtool.entity.AssetsEntity;
import com.verinite.assetmanagementtool.repository.AssetsRepository;
import com.verinite.assetmanagementtool.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private AssetsRepository assetRepo;

    public List<AssetsEntity> getAssetCountsWithLocation() {
        return assetRepo.findAll();
    }

    public ResponseEntity<?> getFormattedAssetCounts(List<String> locations) {
        List<Object[]> queryResult = assetRepo.findAggregatedAssetCountsByLocations(locations);
        if (queryResult.isEmpty()) {
            String message = "No assets found for location: " + locations;
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(transformAssetCounts(queryResult));
    }

    private Map<String, Map<String, Object>> transformAssetCounts(List<Object[]> queryResult) {
        Map<String, Map<String, Object>> locationDataMap = new HashMap<>();

        for (Object[] row : queryResult) {
            String location = (String) row[0];
            String assetName = (String) row[1];
            Long unassignedCount = ((Number) row[2]).longValue();
            Long assignedCount = ((Number) row[3]).longValue();
            Long totalCount = ((Number) row[4]).longValue();

            Map<String, Object> assetData = new HashMap<>();
            assetData.put("assetName", assetName);
            assetData.put("unassignedCount", unassignedCount);
            assetData.put("assignedCount", assignedCount);
            assetData.put("totalCount", totalCount);

            if (!locationDataMap.containsKey(location)) {
                locationDataMap.put(location, new HashMap<>());
            }

            locationDataMap.get(location).put(assetName, assetData);
        }

        return locationDataMap;
    }

    public Map<String, Map<String, Object>> getAssetsCountWithLocationByAssetType(String assetType) {
        Map<String, Map<String, Object>> locationDataMap = new HashMap<>();

        try {
            List<Object[]> queryResult = assetRepo.findAggregatedAssetCountsByAssetTypeAndLocation(assetType);

            for (Object[] row : queryResult) {
                String location = (String) row[0];
                Long unassignedCount = ((Number) row[1]).longValue();
                Long assignedCount = ((Number) row[2]).longValue();
                Long totalCount = ((Number) row[3]).longValue();

                Map<String, Object> assetData = new HashMap<>();
                assetData.put("unassignedCount", unassignedCount);
                assetData.put("assignedCount", assignedCount);
                assetData.put("totalCount", totalCount);

                if (!locationDataMap.containsKey(location)) {
                    locationDataMap.put(location, assetData);
                } else {
                    Map<String, Object> existingAssetData = locationDataMap.get(location);
                    existingAssetData.put("unassignedCount",
                            (Long) existingAssetData.get("unassignedCount") + unassignedCount);
                    existingAssetData.put("assignedCount",
                            (Long) existingAssetData.get("assignedCount") + assignedCount);
                    existingAssetData.put("totalCount", (Long) existingAssetData.get("totalCount") + totalCount);
                }
            }
        } catch (Exception e) {

            System.err.println("An error occurred while fetching asset counts: " + e.getMessage());
            throw new RuntimeException("Failed to fetch asset counts", e);
        }

        return locationDataMap;
    }

}
