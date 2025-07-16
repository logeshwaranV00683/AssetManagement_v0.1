package com.verinite.assetmangementtool.service.serviceImpl;

import com.verinite.assetmangementtool.entity.AssetsEntity;
import com.verinite.assetmangementtool.repository.AssetsRepository;
import com.verinite.assetmangementtool.repository.LocationRepository;
import com.verinite.assetmangementtool.service.AssetService;
import com.verinite.assetmangementtool.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private AssetsRepository assetRepo;

    @Autowired
    private LocationRepository locationRepo;

    @Autowired
    private AssetService assetService;

    @Autowired
    private DashboardService dashboardService;

    public List<AssetsEntity> getAssetCountsWithLocation() {
        List<AssetsEntity> all = assetRepo.findAll();
        return all;
    }

    public Map<String, Map<String, Object>> getFormattedAssetCounts(List<String> locations) {
        List<Object[]> queryResult = assetRepo.findAggregatedAssetCountsByLocations(locations);
        return transformAssetCounts(queryResult);
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

            // Check if location is already in the map
            if (!locationDataMap.containsKey(location)) {
                locationDataMap.put(location, new HashMap<>());
            }

            // Add or update asset data for the location
            locationDataMap.get(location).put(assetName, assetData);
        }

        return locationDataMap;
    }

    public List<String> getUniqueAssetTypes() {
        return assetRepo.findDistinctAssetTypes();
    }

    public Map<String, Map<String, Object>> getAssetsCountWithLocationByAssetName(String assetName) {
        Map<String, Map<String, Object>> locationDataMap = new HashMap<>();

        try {
            List<Object[]> queryResult = assetRepo.findAggregatedAssetCountsByAssetNameAndLocation(assetName);

            for (Object[] row : queryResult) {
                String location = (String) row[0];
                Long unassignedCount = ((Number) row[1]).longValue();
                Long assignedCount = ((Number) row[2]).longValue();
                Long totalCount = ((Number) row[3]).longValue();

                Map<String, Object> assetData = new HashMap<>();
                assetData.put("unassignedCount", unassignedCount);
                assetData.put("assignedCount", assignedCount);
                assetData.put("totalCount", totalCount);

                // Check if location is already in the map
                if (!locationDataMap.containsKey(location)) {
                    locationDataMap.put(location, assetData);
                } else {
                    // Update the existing data if needed (optional, depending on your logic)
                    // For example, you can add counts together if multiple records exist for the
                    // same location
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
            // Optionally, rethrow the exception or return an empty map/partial data
            throw new RuntimeException("Failed to fetch asset counts", e);
        }

        return locationDataMap;
    }

}
