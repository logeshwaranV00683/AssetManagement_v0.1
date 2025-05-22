package com.verinite.assetmangementtool.service;

import com.verinite.assetmangementtool.dto.AssetCounterDto;
import com.verinite.assetmangementtool.dto.AssetsDto;
import com.verinite.assetmangementtool.entity.*;
import com.verinite.assetmangementtool.repository.*;
import com.verinite.assetmangementtool.response.SaveAssetResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AssetServiceImpl implements AssetService, ApplicationRunner {
    // private static final Logger logger =
    // LogManager.getLogger(AssetmanagementtoolApplication.class);

    @Autowired
    AssetsRepository assetRepo;
    @Autowired
    AssetCountRepository assetCountRepository;
    @Autowired
    ScarpRepository scrapRepository;
    @Autowired
    AssetsHistoryRepository assetsHistoryRepository;
    @Autowired
    AssignedAssetsRepository assignedAssetsRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ScarpRepository scarpRepository;

    @Autowired
    private DashboardRepo dashboardRepo;

    public AssetsDto saveAsset(AssetsDto assetDto) {
        ModelMapper modelMapper = new ModelMapper();
        AssetsEntity assets = modelMapper.map(assetDto, AssetsEntity.class);
        assets.setStatus("UnAssigned");
        // assetDto.setStatus("UnAssigned");

        int count = 0;

        List<CountOfAssets> countOfAssets = assetCountRepository.findAll();
        CountOfAssets countOfAssets2 = new CountOfAssets();
        CountOfAssets countOfAssets3 = new CountOfAssets();
        if (countOfAssets.isEmpty()) {
            countOfAssets3.setLocation(assetDto.getLocation());
            assetCountRepository.save(countOfAssets3);
            countOfAssets.add(countOfAssets3);
        }
        for (CountOfAssets i : countOfAssets) {
            if (assetDto.getLocation().equalsIgnoreCase(i.getLocation())) {
                count += 1;
            }
        }
        if (count == 0) {
            countOfAssets2.setLocation(assetDto.getLocation());
            assetCountRepository.save(countOfAssets2);
            countOfAssets.add(countOfAssets2);
            count += 1;
            System.out.println(countOfAssets.size());
        }
        try {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(assetDto.getPurchaseDate());
            for (CountOfAssets i : countOfAssets) {
                if (i.getLocation().equalsIgnoreCase(assetDto.getLocation())) {
                    if (assetDto.getAssetName().equalsIgnoreCase("Laptop")) {
                        i.setLaptopCount(i.getLaptopCount() + 1);
                        i.setUnAssignedLaptopCount(i.getUnAssignedLaptopCount() + 1);
                    }
                    if (assetDto.getAssetName().equalsIgnoreCase("Mouse")) {
                        i.setMouseCount(i.getMouseCount() + 1);
                        i.setUnAssignedMouseCount(i.getUnAssignedMouseCount() + 1);
                    }
                    if (assetDto.getAssetName().equalsIgnoreCase("LaptopCharger")) {
                        i.setLaptopChargerCount(i.getLaptopChargerCount() + 1);
                        i.setUnAssignedLaptopChargerCount(i.getUnAssignedLaptopChargerCount() + 1);
                    }
                    if (assetDto.getAssetName().equalsIgnoreCase("HaedPhone")) {
                        i.setHeadPhonesCount(i.getHeadPhonesCount() + 1);
                        i.setUnAssignedHeadphonesCount(i.getUnAssignedHeadphonesCount() + 1);
                    }
                    if (assetDto.getAssetName().equalsIgnoreCase("Bag")) {
                        i.setBagCount(i.getBagCount() + 1);
                        i.setUnAssignedBagCount(i.getUnAssignedBagCount() + 1);
                    }
                    if (assetDto.getAssetName().equalsIgnoreCase("DataCard")) {
                        i.setDataCardCount(i.getDataCardCount() + 1);
                        i.setUnAssignedDataCardCount(i.getUnAssignedDataCardCount() + 1);
                    }
                    if (assetDto.getAssetName().equalsIgnoreCase("Mobile")) {
                        i.setMobileCount(i.getMobileCount() + 1);
                        i.setUnAssignedMobileCount(i.getUnAssignedMobileCount() + 1);
                    }
                    if (assetDto.getAssetName().equalsIgnoreCase("Camera")) {
                        i.setCameraCount(i.getCameraCount() + 1);
                        i.setUnAssignedCameraCount(i.getUnAssignedCameraCount() + 1);
                    }
                    if (assetDto.getAssetName().equalsIgnoreCase("Projector")) {
                        i.setProjectorCount(i.getProjectorCount() + 1);
                        i.setUnAssignedProjectorCount(i.getUnAssignedProjectorCount() + 1);
                    }
                    if (assetDto.getAssetName().equalsIgnoreCase("Firewall")) {
                        i.setFireWallCount(i.getFireWallCount() + 1);
                        i.setUnAssignedFireWallCount(i.getUnAssignedFireWallCount() + 1);
                    }
                    if (assetDto.getAssetName().equalsIgnoreCase("Switch")) {
                        i.setSwitchCount(i.getSwitchCount() + 1);
                        i.setUnAssignedSwitchCount(i.getUnAssignedSwitchCount() + 1);
                    }
                    if (assetDto.getAssetName().equalsIgnoreCase("DVR")) {
                        i.setDvrCount(i.getDvrCount() + 1);
                        i.setUnAssignedDvrCount(i.getUnAssignedDvrCount() + 1);
                    }
                    if (assetDto.getAssetName().equalsIgnoreCase("Speaker")) {
                        i.setSpeakerCount(i.getSpeakerCount() + 1);
                        i.setUnAssignedSpeakerCount(i.getUnAssignedSpeakerCount() + 1);
                    }

                    assetCountRepository.save(i);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assetRepo.save(assets);

        // Map the saved entity back to AssetDto
        AssetsDto assetsDto = modelMapper.map(assets, AssetsDto.class);
        return assetsDto;
    }

    private void updateCountOfAssets(CountOfAssets countOfAssets, String assetName) {
        switch (assetName.toLowerCase()) {
            case "laptop":
                countOfAssets.setLaptopCount(countOfAssets.getLaptopCount() + 1);
                countOfAssets.setUnAssignedLaptopCount(countOfAssets.getUnAssignedLaptopCount() + 1);
                break;
            case "mouse":
                countOfAssets.setMouseCount(countOfAssets.getMouseCount() + 1);
                countOfAssets.setUnAssignedMouseCount(countOfAssets.getUnAssignedMouseCount() + 1);
                break;
            case "laptopcharger":
                countOfAssets.setLaptopChargerCount(countOfAssets.getLaptopChargerCount() + 1);
                countOfAssets.setUnAssignedLaptopChargerCount(countOfAssets.getUnAssignedLaptopChargerCount() + 1);
                break;
            // Add cases for other asset types as needed
            default:
                // Handle unknown asset types
                break;
        }
    }

    private void updateAssetCount(CountOfAssets countOfAssets, String assetName) {
        switch (assetName.toLowerCase()) {
            case "laptop":
                countOfAssets.setLaptopCount(countOfAssets.getLaptopCount() + 1);
                countOfAssets.setUnAssignedLaptopCount(countOfAssets.getUnAssignedLaptopCount() + 1);
                break;
            case "mouse":
                countOfAssets.setMouseCount(countOfAssets.getMouseCount() + 1);
                countOfAssets.setUnAssignedMouseCount(countOfAssets.getUnAssignedMouseCount() + 1);
                break;
            case "laptopcharger":
                countOfAssets.setLaptopChargerCount(countOfAssets.getLaptopChargerCount() + 1);
                countOfAssets.setUnAssignedLaptopChargerCount(countOfAssets.getUnAssignedLaptopChargerCount() + 1);
                break;
            case "headphone":
                countOfAssets.setHeadPhonesCount(countOfAssets.getHeadPhonesCount() + 1);
                countOfAssets.setUnAssignedHeadphonesCount(countOfAssets.getUnAssignedHeadphonesCount() + 1);
                break;
            case "bag":
                countOfAssets.setBagCount(countOfAssets.getBagCount() + 1);
                countOfAssets.setUnAssignedBagCount(countOfAssets.getUnAssignedBagCount() + 1);
                break;
            case "datacard":
                countOfAssets.setDataCardCount(countOfAssets.getDataCardCount() + 1);
                countOfAssets.setUnAssignedDataCardCount(countOfAssets.getUnAssignedDataCardCount() + 1);
                break;
            case "mobile":
                countOfAssets.setMobileCount(countOfAssets.getMobileCount() + 1);
                countOfAssets.setUnAssignedMobileCount(countOfAssets.getUnAssignedMobileCount() + 1);
                break;
            case "camera":
                countOfAssets.setCameraCount(countOfAssets.getCameraCount() + 1);
                countOfAssets.setUnAssignedCameraCount(countOfAssets.getUnAssignedCameraCount() + 1);
                break;
            case "projector":
                countOfAssets.setProjectorCount(countOfAssets.getProjectorCount() + 1);
                countOfAssets.setUnAssignedProjectorCount(countOfAssets.getUnAssignedProjectorCount() + 1);
                break;
            case "firewall":
                countOfAssets.setFireWallCount(countOfAssets.getFireWallCount() + 1);
                countOfAssets.setUnAssignedFireWallCount(countOfAssets.getUnAssignedFireWallCount() + 1);
                break;
            case "switch":
                countOfAssets.setSwitchCount(countOfAssets.getSwitchCount() + 1);
                countOfAssets.setUnAssignedSwitchCount(countOfAssets.getUnAssignedSwitchCount() + 1);
                break;
            case "dvr":
                countOfAssets.setDvrCount(countOfAssets.getDvrCount() + 1);
                countOfAssets.setUnAssignedDvrCount(countOfAssets.getUnAssignedDvrCount() + 1);
                break;
            case "speaker":
                countOfAssets.setSpeakerCount(countOfAssets.getSpeakerCount() + 1);
                countOfAssets.setUnAssignedSpeakerCount(countOfAssets.getUnAssignedSpeakerCount() + 1);
                break;
            default:
                // Handle unexpected asset names
                throw new IllegalArgumentException("Unknown asset name: " + assetName);
        }
    }

    @Override
    public Object getAssetBySerialNumber(String id) {
        try {
            AssetsEntity asset = assetRepo.findBySerialNumber(id);
            if (!asset.getStatus().equalsIgnoreCase("scrap"))
                return asset;
            else {
                return "In Scrap";
            }
        } catch (NoSuchElementException e) {
            return "id Not Found";
        }
    }

    @Override
    public SaveAssetResponse updateAsset(SaveAssetResponse asset) {
        modelMapper.map(asset, AssetsEntity.class);

        AssetsEntity existingAsset = assetRepo.findBySerialNumber(asset.getSerialNumber());
        if (existingAsset != null) {
            // Map only non-null fields from the input asset to the existing asset
            if (asset.getAssetName() != null)
                existingAsset.setAssetName(asset.getAssetName());
            if (asset.getPurchaseDate() != null)
                existingAsset.setPurchaseDate(asset.getPurchaseDate());
            if (asset.getWarrantyDate() != null)
                existingAsset.setWarrantyDate(asset.getWarrantyDate());
            if (asset.getSerialNumber() != null)
                existingAsset.setSerialNumber(asset.getSerialNumber());
            if (asset.getStatus() != null)
                existingAsset.setStatus(asset.getStatus());
            if (asset.getType() != null)
                existingAsset.setType(asset.getType());
            if (asset.getAddedBy() != null)
                existingAsset.setAddedBy(asset.getAddedBy());
            if (asset.getOperatingSystem() != null)
                existingAsset.setOperatingSystem(asset.getOperatingSystem());
            if (asset.getModelName() != null)
                existingAsset.setModelName(asset.getModelName());
            // Save the updated asset to the repository
            AssetsEntity updatedAsset = assetRepo.save(existingAsset);
        } else {
            assetRepo.save(existingAsset);
        }
        return modelMapper.map(asset, SaveAssetResponse.class);
    }

    // below code i write
    public ResponseEntity<Object> updateAssets(AssetsEntity asset) {
        // Log input serial number for debugging
        System.out.println("Updating asset with serial number: " + asset.getSerialNumber());

        try {
            // Find the existing asset by serial number
            AssetsEntity existingAsset = assetRepo.findBySerialNumber(asset.getSerialNumber());

            // Check if the asset status is not 'Scrap'
            if (!"Scrap".equalsIgnoreCase(existingAsset.getStatus())) {
                // Update fields only if they are provided
                if (asset.getAssetName() != null) {
                    existingAsset.setAssetName(asset.getAssetName());
                }
                if (asset.getEmpId() != null) {
                    existingAsset.setEmpId(asset.getEmpId());
                }
                if (asset.getPurchaseDate() != null) {
                    existingAsset.setPurchaseDate(asset.getPurchaseDate());
                }
                if (asset.getWarrantyDate() != null) {
                    existingAsset.setWarrantyDate(asset.getWarrantyDate());
                }
                if (asset.getSerialNumber() != null) {
                    existingAsset.setSerialNumber(asset.getSerialNumber());
                }
                if (asset.getStatus() != null) {
                    existingAsset.setStatus(asset.getStatus());
                }
                if (asset.getType() != null) {
                    existingAsset.setType(asset.getType());
                }
                if (asset.getAddedBy() != null) {
                    existingAsset.setAddedBy(asset.getAddedBy());
                }
                if (asset.getOperatingSystem() != null) {
                    existingAsset.setOperatingSystem(asset.getOperatingSystem());
                }
                if (asset.getModelName() != null) {
                    existingAsset.setModelName(asset.getModelName());
                }

                // Save the updated asset
                AssetsEntity updatedAsset = assetRepo.save(existingAsset);
                return ResponseEntity.ok(updatedAsset);

            } else {
                // Asset is marked as 'Scrap', cannot be updated
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("The asset is marked as Scrap and cannot be updated.");
            }

        } catch (NoSuchElementException e) {
            // Asset with the given serial number does not exist
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Asset with the provided serial number not found.");
        } catch (Exception e) {
            // Handle any other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating the asset.");
        }
    }

    @Override
    public Object deleteAsset(int id) {
        try {
            AssetsEntity asset = assetRepo.findById(id).get();
            System.out.println(asset.getAssetId());
            if (!asset.getStatus().equalsIgnoreCase("Scrap")) {
                asset.setStatus("Scrap");

                return assetRepo.save(asset);
                // return scarpRepository.save(asset);
            } else
                return "Already in Scrap";
        } catch (NoSuchElementException e) {
            return "Id not Found";
        }

    }

    @Override
    public List<AssetsEntity> getThroughStatus(String str) {
        List<AssetsEntity> assets = get();
        List<AssetsEntity> assets2 = new ArrayList<AssetsEntity>();
        for (AssetsEntity i : assets) {
            if (i.getStatus().equalsIgnoreCase(str))
                assets2.add(i);
        }
        return assets2;
    }

    public List<AssetsEntity> get() {
        return assetRepo.findAll();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

    }

    @Override
    public int getCountOfAssigned() {
        int assigned = 0;
        List<AssetsEntity> assetsEntities = assetRepo.findAll();
        for (AssetsEntity i : assetsEntities) {
            if (i.getStatus().equalsIgnoreCase("Assigned"))
                assigned += 1;
        }
        return assigned;

    }

    @Override
    public int getCountOfUnassigned() {
        int unassigned = 0;
        List<AssetsEntity> assetsEntities = assetRepo.findAll();
        for (AssetsEntity i : assetsEntities) {
            if (i.getStatus().equalsIgnoreCase("UnAssigned"))
                unassigned += 1;
        }
        return unassigned;
    }

    @Override
    public int getLaptopCountByLocation(String id) {
        CountOfAssets countOfAsset = assetCountRepository.findByLocation(id);
        return countOfAsset.getLaptopCount();
    }


    /// /////////////////////////////////////////////////
//	@Override
//	public int getLaptopCountByAssertSourced(String assertSourcedBy) {
//		CountOfAssets countOfAsset = assetCountRepository.findByAssertSourcedBy(assertSourcedBy);
//		return countOfAsset.getLaptopCount();
//	}

    /// //////////////////////////////
    ///
    ///
    @Override
    public int totalLaptops() {
        List<CountOfAssets> countOfAssets = assetCountRepository.findAll();
        int total = 0;
        for (CountOfAssets i : countOfAssets) {
            total += i.getLaptopCount();
        }
        return total;
    }

    @Override
    public int getCountOfUnassignedByLocation(String id) {
        CountOfAssets countOfAsset = assetCountRepository.findByLocation(id);
        return countOfAsset.getUnAssignedLaptopCount();
    }

    @Override
    public List<AssetCounterDto> getUnassignedAndTotalLaptops() {
        List<AssetCounterDto> assetCounterDtos = new ArrayList<AssetCounterDto>();
        AssetCounterDto temp1 = new AssetCounterDto();
        List<CountOfAssets> countOfAssets = assetCountRepository.findAll();
        countOfAssets.forEach(x -> {
            temp1.setLocation(x.getLocation());
            temp1.setTotal(x.getLaptopCount());
            temp1.setUnAssigned(x.getUnAssignedLaptopCount());
            assetCounterDtos.add(temp1);
        });
        for (AssetCounterDto i : assetCounterDtos)
            System.out.println(i.getLocation());

        return assetCounterDtos;
    }

    @Override
    public List<AssetsEntity> getLaptopsUnderWarenty() {
        List<AssetsEntity> all = assetRepo.findAll();
        List<AssetsEntity> assetsEntities = new ArrayList<AssetsEntity>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        // System.out.println(dtf.format(now));
        String today = dtf.format(now);

        for (AssetsEntity i : all) {
            try {
                Date date = new SimpleDateFormat("dd/MM/yyyy").parse(i.getWarrantyDate());
                Date todatDate = new SimpleDateFormat("dd/MM/yyyy").parse(today);
                if (date.compareTo(todatDate) > 0) {
                    assetsEntities.add(i);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        return assetsEntities;
    }

    @Override
    public List<AssetsEntity> getLaptopsOverWarenty() {
        List<AssetsEntity> all = assetRepo.findAll();
        List<AssetsEntity> assetsEntities = new ArrayList<AssetsEntity>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        // System.out.println(dtf.format(now));
        String today = dtf.format(now);

        for (AssetsEntity i : all) {
            try {
                Date date = new SimpleDateFormat("dd/MM/yyyy").parse(i.getWarrantyDate());
                Date todatDate = new SimpleDateFormat("dd/MM/yyyy").parse(today);
                if (date.compareTo(todatDate) < 0) {
                    assetsEntities.add(i);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        return assetsEntities;
    }

    @Override
    public Object saveHistory(String serialNo, String empId) {
        try {
            AssignedAssetsEntity asset = assignedAssetsRepository.findBySerialNumberAndEmpId(serialNo, empId);
            AssetsHistoryEntity assetsHistoryEntity = new AssetsHistoryEntity();
            List<AssetsHistoryEntity> assetsHistoryEntities = assetsHistoryRepository.findAll();
            List<CountOfAssets> countOfAssets = assetCountRepository.findAll();
            if (!asset.getStatus().equalsIgnoreCase("scrap")) {

                assetsHistoryEntity.setEmpId(asset.getEmpId());
                assetsHistoryEntity.setReturnDate(new Date());
                assetsHistoryEntity.setEmpId(asset.getEmpId());
                assetsHistoryEntity.setSerialNumber(asset.getSerialNumber());
                assetsHistoryEntity.setAssignedBy(asset.getAssignedBy());
                assetsHistoryEntity.setAssignedDate(asset.getAssignedDate());
                asset.setEmpId(null);
                asset.setAssignedBy(null);
                asset.setAssignedDate(null);
                asset.setStatus("Unassigned");
                asset.setReturnDate(null);
                for (CountOfAssets i : countOfAssets) {
                    if (i.getLocation().equalsIgnoreCase(asset.getLocation())) {
                        if (asset.getAssetName().equalsIgnoreCase("Laptop")) {
                            i.setUnAssignedLaptopCount(i.getUnAssignedLaptopCount() + 1);
                            assetCountRepository.save(i);
                        }
                        if (asset.getAssetName().equalsIgnoreCase("Mouse")) {
                            i.setUnAssignedMouseCount(i.getUnAssignedMouseCount() + 1);
                            assetCountRepository.save(i);
                        }
                        if (asset.getAssetName().equalsIgnoreCase("LaptopCharger")) {
                            i.setUnAssignedLaptopChargerCount(i.getUnAssignedLaptopChargerCount());
                            assetCountRepository.save(i);
                        }
                        if (asset.getAssetName().equalsIgnoreCase("HaedPhone")) {
                            i.setUnAssignedHeadphonesCount(i.getUnAssignedHeadphonesCount() + 1);
                            assetCountRepository.save(i);
                        }
                        if (asset.getAssetName().equalsIgnoreCase("Bag")) {
                            i.setUnAssignedBagCount(i.getUnAssignedBagCount() + 1);
                            assetCountRepository.save(i);
                        }

                        if (asset.getAssetName().equalsIgnoreCase("DataCard")) {
                            i.setUnAssignedDataCardCount(i.getUnAssignedDataCardCount() + 1);
                            assetCountRepository.save(i);
                        }

                        if (asset.getAssetName().equalsIgnoreCase("Mobile")) {
                            i.setUnAssignedMobileCount(i.getUnAssignedMobileCount() + 1);
                            assetCountRepository.save(i);
                        }
                        if (asset.getAssetName().equalsIgnoreCase("Camera")) {
                            i.setUnAssignedCameraCount(i.getUnAssignedCameraCount() + 1);
                            assetCountRepository.save(i);
                        }
                        if (asset.getAssetName().equalsIgnoreCase("Projector")) {
                            i.setUnAssignedProjectorCount(i.getUnAssignedProjectorCount() + 1);
                            assetCountRepository.save(i);
                        }
                        if (asset.getAssetName().equalsIgnoreCase("FireWall")) {
                            i.setUnAssignedFireWallCount(i.getUnAssignedFireWallCount() + 1);
                            assetCountRepository.save(i);
                        }
                        if (asset.getAssetName().equalsIgnoreCase("Switch")) {
                            i.setUnAssignedSwitchCount(i.getUnAssignedSwitchCount() + 1);
                            assetCountRepository.save(i);
                        }
                        if (asset.getAssetName().equalsIgnoreCase("DVR")) {
                            i.setUnAssignedDvrCount(i.getUnAssignedDvrCount() + 1);
                            assetCountRepository.save(i);
                        }
                        if (asset.getAssetName().equalsIgnoreCase("Speaker")) {
                            i.setUnAssignedSpeakerCount(i.getUnAssignedSpeakerCount() + 1);
                            assetCountRepository.save(i);
                        }
                        assetCountRepository.save(i);
                    }
                }
                AssetsEntity assetCopy = assetRepo.findBySerialNumber(serialNo);
                assetCopy.setStatus("unassigned");
                assetCopy.setEmpId(null);
                assetRepo.save(assetCopy);
                assignedAssetsRepository.save(asset);
                assetsHistoryEntities.add(assetsHistoryEntity);
                assetsHistoryRepository.saveAll(assetsHistoryEntities);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "Serial Number not found or The Item belongs to this Serial Number is in Scrap";
        }
        System.out.println("Updated Successfully");
        return "Updated Successfully";
    }

    @Override
    public List<AssetsEntity> getUnAssigned() {
        return assetRepo.findAll().stream().filter(x -> x.getStatus().equalsIgnoreCase("unassigned"))
                .collect(Collectors.toList());
    }

    @Override
    public List<AssetsEntity> getAssigned() {
        return assetRepo.findAll().stream().filter(x -> x.getStatus().equalsIgnoreCase("assigned"))
                .collect(Collectors.toList());
    }

    @Override
    public AssetsDto getAssetsDetails(Integer assetId) {
        if (assetId == null) {
            throw new RuntimeException("Asset id is empty: " + assetId);
        }

        Optional<AssetsEntity> optionalAssetsEntity = assetRepo.findByAssetId(assetId);

        if (optionalAssetsEntity.isEmpty()) {
            throw new RuntimeException("No asset found with id: " + assetId);
        }

        AssetsEntity assetsEntity = optionalAssetsEntity.get();
        return modelMapper.map(assetsEntity, AssetsDto.class);
    }

    @Override
    public String deleteAsset(Integer assetId) {
        Optional<AssetsEntity> optionalAssetsEntity = assetRepo.findByAssetId(assetId);

        if (optionalAssetsEntity.isEmpty()) {
            throw new RuntimeException("Asset not found for ID: " + assetId);
        }

        assetRepo.deleteById(assetId);

        return "Asset with ID " + assetId + " has been successfully deleted.";
    }

    @Override
    public List<AssetsDto> listOfAllAsset() {
        List<AssetsEntity> assetsEntities = assetRepo.findAllByOrderByAssetIdDesc(); // Fetch all asset entities
        return assetsEntities.stream().map(asset -> modelMapper.map(asset, AssetsDto.class)) // Convert each entity to
                // DTO
                .collect(Collectors.toList()); // Collect the results as a list of DTOs
    }

    public void exportAssetsToExcel(OutputStream outputStream, String exportType, String filter) throws Exception {

        List<?> assets;

        if ("Scrap".equalsIgnoreCase(exportType)) {
            assets = filter == null || filter.isEmpty()
                    ? scrapRepository.findAll()
                    : scrapRepository.findByFilter("%" + filter.toLowerCase() + "%");
        } else if (exportType.isBlank() || exportType.equalsIgnoreCase("all")) {
            assets = filter == null || filter.isEmpty()
                    ? assetRepo.findAll()
                    : assetRepo.findByFilter("%" + filter.toLowerCase() + "%");
        } else {
            assets = filter == null || filter.isEmpty()
                    ? assetRepo.findByStatus(exportType)
                    : assetRepo.findByStatusAndFilter(exportType, "%" + filter.toLowerCase() + "%");
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(exportType + " Assets");

        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);

        String[] headers = getHeaders(exportType);
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowNum = 1;
        for (Object asset : assets) {
            Row row = sheet.createRow(rowNum++);
            populateDataRow(row, asset, dataStyle, exportType);
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        workbook.write(outputStream);
        workbook.close();
    }

    private void createDataCell(Row row, int colIdx, String value, CellStyle style) {
        Cell cell = row.createCell(colIdx);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setColor(IndexedColors.BLACK.getIndex());
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private void populateDataRow(Row row, Object asset, CellStyle style, String exportType) {
        int col = 0;
        if (asset instanceof ScrapEntity) {
            ScrapEntity scrap = (ScrapEntity) asset;
            createDataCell(row, col++, String.valueOf(scrap.getScrapId()), style);
            createDataCell(row, col++, scrap.getAssetname(), style);
            createDataCell(row, col++, scrap.getSerialNo(), style);
            createDataCell(row, col++, String.valueOf(scrap.getPurchaseDate()), style);
            createDataCell(row, col++, String.valueOf(scrap.getWarrantyDate()), style);
            createDataCell(row, col++, scrap.getUsers(), style);
            createDataCell(row, col++, scrap.getStatus(), style);
            createDataCell(row, col++, scrap.getType(), style);
            createDataCell(row, col++, String.valueOf(scrap.getAssetId()), style);
        } else if (asset instanceof AssetsEntity) {
            AssetsEntity a = (AssetsEntity) asset;
            createDataCell(row, col++, String.valueOf(a.getAssetId()), style);
            createDataCell(row, col++, a.getAssetName(), style);
            createDataCell(row, col++, a.getSerialNumber(), style);
            createDataCell(row, col++, String.valueOf(a.getEmpId()), style);
            createDataCell(row, col++, a.getStatus(), style);
            createDataCell(row, col++, a.getType(), style);
            createDataCell(row, col++, String.valueOf(a.getPurchaseDate()), style);
            createDataCell(row, col++, String.valueOf(a.getWarrantyDate()), style);
            createDataCell(row, col++, a.getLocation(), style);
            createDataCell(row, col++, a.getLocCode() != null ? a.getLocCode().toString() : "0", style);
            createDataCell(row, col++, a.getModelName(), style);
            createDataCell(row, col++, a.getOperatingSystem(), style);
            createDataCell(row, col++, String.valueOf(a.getReturnDate()), style);
            createDataCell(row, col++, a.getAddedBy(), style);
            createDataCell(row, col++, String.valueOf(a.getAssignedDate()), style);
            createDataCell(row, col++, a.getAssignedBy(), style);
            createDataCell(row, col++, a.getAssertSourcedBy(), style);
        }
    }

    private String[] getHeaders(String exportType) {
        if ("Scrap".equalsIgnoreCase(exportType)) {
            return new String[]{"Scrap ID", "Asset Name", "Serial No", "Purchase Date", "Warranty Date",
                    "Users", "Status", "Type", "Asset ID"};
        }
        return new String[]{"Asset ID", "Asset Name", "Serial Number", "Emp ID", "Status", "Type", "Purchase Date",
                "Warranty Date", "Location", "Loc Code", "Model Name", "Operating System", "Return Date",
                "Added By", "Assigned Date", "Assigned By", "Sourced By"};
    }
}