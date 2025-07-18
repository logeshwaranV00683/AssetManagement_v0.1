package com.verinite.assetmangementtool.service.serviceImpl;

import com.verinite.assetmangementtool.dto.*;
import com.verinite.assetmangementtool.entity.*;
import com.verinite.assetmangementtool.repository.*;
import com.verinite.assetmangementtool.response.SaveAssetResponse;
import com.verinite.assetmangementtool.service.AssetService;
import com.verinite.assetmangementtool.service.AssetsHistoryServices;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AssetServiceImpl implements AssetService, ApplicationRunner {


    @Autowired
    AssetsRepository assetRepo;
    @Autowired
    AssetCountRepository assetCountRepository;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    AssetsHistoryRepository assetsHistoryRepository;
    @Autowired
    AssignedAssetsRepository assignedAssetsRepository;
    @Autowired
    AssetsHistoryServices assetsHistoryServices;
    @Autowired
    AssignedAssetsServiceImpl assignedAssetsServiceImp;
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AssignedAssetsServiceImpl assignedAssetsService;


    public ResponseEntity<AssetsDto> saveAsset(AssetsDto assetDto) {
        ModelMapper modelMapper = new ModelMapper();
        AssetsEntity assets = modelMapper.map(assetDto, AssetsEntity.class);

        if(assetDto.getPurchaseDate().isAfter(assetDto.getWarrantyDate())) {
            throw new IllegalArgumentException("Given warranty Date is lesser than Purchase Date or Equals to it");
        }


        int count = 0;
        assets.setStatus("UnAssigned");
        List<CountOfAssetsEntity> countOfAssetEntities = assetCountRepository.findAll();
        CountOfAssetsEntity countOfAssetsEntity2 = new CountOfAssetsEntity();
        CountOfAssetsEntity countOfAssetsEntity3 = new CountOfAssetsEntity();
        if (countOfAssetEntities.isEmpty()) {
            countOfAssetsEntity3.setLocation(assetDto.getLocation());
            assetCountRepository.save(countOfAssetsEntity3);
            countOfAssetEntities.add(countOfAssetsEntity3);
        }
        for (CountOfAssetsEntity i : countOfAssetEntities) {
            if (assetDto.getLocation().equalsIgnoreCase(i.getLocation())) {
                count += 1;
            }
        }
        if (count == 0) {
            countOfAssetsEntity2.setLocation(assetDto.getLocation());
            assetCountRepository.save(countOfAssetsEntity2);
            countOfAssetEntities.add(countOfAssetsEntity2);
            count += 1;
            System.out.println(countOfAssetEntities.size());
        }

            for (CountOfAssetsEntity i : countOfAssetEntities) {
                if (i.getLocation().equalsIgnoreCase(assetDto.getLocation())) {
                    if (assetDto.getType().equalsIgnoreCase("Laptop")) {
                        i.setLaptopCount(i.getLaptopCount() + 1);
                        i.setUnAssignedLaptopCount(i.getUnAssignedLaptopCount() + 1);
                    }
                    if (assetDto.getType().equalsIgnoreCase("Mouse")) {
                        i.setMouseCount(i.getMouseCount() + 1);
                        i.setUnAssignedMouseCount(i.getUnAssignedMouseCount() + 1);
                    }
                    if (assetDto.getType().equalsIgnoreCase("LaptopCharger")) {
                        i.setLaptopChargerCount(i.getLaptopChargerCount() + 1);
                        i.setUnAssignedLaptopChargerCount(i.getUnAssignedLaptopChargerCount() + 1);
                    }
                    if (assetDto.getType().equalsIgnoreCase("HeadPhone")) {
                        i.setHeadPhonesCount(i.getHeadPhonesCount() + 1);
                        i.setUnAssignedHeadphonesCount(i.getUnAssignedHeadphonesCount() + 1);
                    }
                    if (assetDto.getType().equalsIgnoreCase("Bag")) {
                        i.setBagCount(i.getBagCount() + 1);
                        i.setUnAssignedBagCount(i.getUnAssignedBagCount() + 1);
                    }
                    if (assetDto.getType().equalsIgnoreCase("DataCard")) {
                        i.setDataCardCount(i.getDataCardCount() + 1);
                        i.setUnAssignedDataCardCount(i.getUnAssignedDataCardCount() + 1);
                    }
                    if (assetDto.getType().equalsIgnoreCase("Mobile")) {
                        i.setMobileCount(i.getMobileCount() + 1);
                        i.setUnAssignedMobileCount(i.getUnAssignedMobileCount() + 1);
                    }
                    if (assetDto.getType().equalsIgnoreCase("Camera")) {
                        i.setCameraCount(i.getCameraCount() + 1);
                        i.setUnAssignedCameraCount(i.getUnAssignedCameraCount() + 1);
                    }
                    if (assetDto.getType().equalsIgnoreCase("Projector")) {
                        i.setProjectorCount(i.getProjectorCount() + 1);
                        i.setUnAssignedProjectorCount(i.getUnAssignedProjectorCount() + 1);
                    }
                    if (assetDto.getType().equalsIgnoreCase("Firewall")) {
                        i.setFireWallCount(i.getFireWallCount() + 1);
                        i.setUnAssignedFireWallCount(i.getUnAssignedFireWallCount() + 1);
                    }
                    if (assetDto.getType().equalsIgnoreCase("Switch")) {
                        i.setSwitchCount(i.getSwitchCount() + 1);
                        i.setUnAssignedSwitchCount(i.getUnAssignedSwitchCount() + 1);
                    }
                    if (assetDto.getType().equalsIgnoreCase("DVR")) {
                        i.setDvrCount(i.getDvrCount() + 1);
                        i.setUnAssignedDvrCount(i.getUnAssignedDvrCount() + 1);
                    }
                    if (assetDto.getType().equalsIgnoreCase("Speaker")) {
                        i.setSpeakerCount(i.getSpeakerCount() + 1);
                        i.setUnAssignedSpeakerCount(i.getUnAssignedSpeakerCount() + 1);
                    }

                    assetCountRepository.save(i);
                }
            }

        assetRepo.save(assets);
        return ResponseEntity.ok(modelMapper.map(assets, AssetsDto.class));
    }

    private void updateCountOfAssets(CountOfAssetsEntity countOfAssetsEntity, String assetName) {
        switch (assetName.toLowerCase()) {
            case "laptop":
                countOfAssetsEntity.setLaptopCount(countOfAssetsEntity.getLaptopCount() + 1);
                countOfAssetsEntity.setUnAssignedLaptopCount(countOfAssetsEntity.getUnAssignedLaptopCount() + 1);
                break;
            case "mouse":
                countOfAssetsEntity.setMouseCount(countOfAssetsEntity.getMouseCount() + 1);
                countOfAssetsEntity.setUnAssignedMouseCount(countOfAssetsEntity.getUnAssignedMouseCount() + 1);
                break;
            case "laptopcharger":
                countOfAssetsEntity.setLaptopChargerCount(countOfAssetsEntity.getLaptopChargerCount() + 1);
                countOfAssetsEntity.setUnAssignedLaptopChargerCount(countOfAssetsEntity.getUnAssignedLaptopChargerCount() + 1);
                break;
            // Add cases for other asset types as needed
            default:
                // Handle unknown asset types
                break;
        }
    }

    private void updateAssetCount(CountOfAssetsEntity countOfAssetsEntity, String assetName) {
        switch (assetName.toLowerCase()) {
            case "laptop":
                countOfAssetsEntity.setLaptopCount(countOfAssetsEntity.getLaptopCount() + 1);
                countOfAssetsEntity.setUnAssignedLaptopCount(countOfAssetsEntity.getUnAssignedLaptopCount() + 1);
                break;
            case "mouse":
                countOfAssetsEntity.setMouseCount(countOfAssetsEntity.getMouseCount() + 1);
                countOfAssetsEntity.setUnAssignedMouseCount(countOfAssetsEntity.getUnAssignedMouseCount() + 1);
                break;
            case "laptopcharger":
                countOfAssetsEntity.setLaptopChargerCount(countOfAssetsEntity.getLaptopChargerCount() + 1);
                countOfAssetsEntity.setUnAssignedLaptopChargerCount(countOfAssetsEntity.getUnAssignedLaptopChargerCount() + 1);
                break;
            case "headphone":
                countOfAssetsEntity.setHeadPhonesCount(countOfAssetsEntity.getHeadPhonesCount() + 1);
                countOfAssetsEntity.setUnAssignedHeadphonesCount(countOfAssetsEntity.getUnAssignedHeadphonesCount() + 1);
                break;
            case "bag":
                countOfAssetsEntity.setBagCount(countOfAssetsEntity.getBagCount() + 1);
                countOfAssetsEntity.setUnAssignedBagCount(countOfAssetsEntity.getUnAssignedBagCount() + 1);
                break;
            case "datacard":
                countOfAssetsEntity.setDataCardCount(countOfAssetsEntity.getDataCardCount() + 1);
                countOfAssetsEntity.setUnAssignedDataCardCount(countOfAssetsEntity.getUnAssignedDataCardCount() + 1);
                break;
            case "mobile":
                countOfAssetsEntity.setMobileCount(countOfAssetsEntity.getMobileCount() + 1);
                countOfAssetsEntity.setUnAssignedMobileCount(countOfAssetsEntity.getUnAssignedMobileCount() + 1);
                break;
            case "camera":
                countOfAssetsEntity.setCameraCount(countOfAssetsEntity.getCameraCount() + 1);
                countOfAssetsEntity.setUnAssignedCameraCount(countOfAssetsEntity.getUnAssignedCameraCount() + 1);
                break;
            case "projector":
                countOfAssetsEntity.setProjectorCount(countOfAssetsEntity.getProjectorCount() + 1);
                countOfAssetsEntity.setUnAssignedProjectorCount(countOfAssetsEntity.getUnAssignedProjectorCount() + 1);
                break;
            case "firewall":
                countOfAssetsEntity.setFireWallCount(countOfAssetsEntity.getFireWallCount() + 1);
                countOfAssetsEntity.setUnAssignedFireWallCount(countOfAssetsEntity.getUnAssignedFireWallCount() + 1);
                break;
            case "switch":
                countOfAssetsEntity.setSwitchCount(countOfAssetsEntity.getSwitchCount() + 1);
                countOfAssetsEntity.setUnAssignedSwitchCount(countOfAssetsEntity.getUnAssignedSwitchCount() + 1);
                break;
            case "dvr":
                countOfAssetsEntity.setDvrCount(countOfAssetsEntity.getDvrCount() + 1);
                countOfAssetsEntity.setUnAssignedDvrCount(countOfAssetsEntity.getUnAssignedDvrCount() + 1);
                break;
            case "speaker":
                countOfAssetsEntity.setSpeakerCount(countOfAssetsEntity.getSpeakerCount() + 1);
                countOfAssetsEntity.setUnAssignedSpeakerCount(countOfAssetsEntity.getUnAssignedSpeakerCount() + 1);
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
            if (asset.getAssetName() != null)
                existingAsset.setAssetName(asset.getAssetName());
            if (asset.getPurchaseDate() != null)
                existingAsset.setPurchaseDate(LocalDate.parse(asset.getPurchaseDate()));
            if (asset.getWarrantyDate() != null)
                existingAsset.setWarrantyDate(LocalDate.parse(asset.getWarrantyDate()));
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
            if (asset.getEmpId() != null)
                existingAsset.setEmpId(asset.getEmpId());
            if (asset.getLocation() != null)
                existingAsset.setLocation(asset.getLocation());
            if (asset.getLocCode() != null)
                existingAsset.setLocCode(asset.getLocCode());
            if (asset.getReturnDate() != null)
                existingAsset.setReturnDate(asset.getReturnDate());
            if (asset.getAssignedDate() != null)
                existingAsset.setAssignedDate(asset.getAssignedDate());
            if (asset.getAssignedBy() != null)
                existingAsset.setAssignedBy(asset.getAssignedBy());
            if (asset.getAssertSourcedBy() != null)
                existingAsset.setAssetSourcedBy(asset.getAssertSourcedBy());

            return modelMapper.map(assetRepo.save(existingAsset), SaveAssetResponse.class);
        } else {
            return null;
        }
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
        List<AssetsEntity> assets2 = new ArrayList<>();
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
        CountOfAssetsEntity countOfAsset = assetCountRepository.findByLocation(id);
        return countOfAsset.getLaptopCount();
    }


    /// /////////////////////////////////////////////////
//	@Override
//	public int getLaptopCountByAssertSourced(String assertSourcedBy) {
//		CountOfAssetsRepository countOfAsset = assetCountRepository.findByAssertSourcedBy(assertSourcedBy);
//		return countOfAsset.getLaptopCount();
//	}

    /// //////////////////////////////
    ///
    @Override
    public int totalLaptops() {
        List<CountOfAssetsEntity> countOfAssetEntities = assetCountRepository.findAll();
        int total = 0;
        for (CountOfAssetsEntity i : countOfAssetEntities) {
            total += i.getLaptopCount();
        }
        return total;
    }

    @Override
    public int getCountOfUnassignedByLocation(String id) {
        CountOfAssetsEntity countOfAsset = assetCountRepository.findByLocation(id);
        return countOfAsset.getUnAssignedLaptopCount();
    }

    @Override
    public List<AssetCounterDto> getUnassignedAndTotalLaptops() {
        List<AssetCounterDto> assetCounterDTOs = new ArrayList<>();
        AssetCounterDto temp1 = new AssetCounterDto();
        List<CountOfAssetsEntity> countOfAssetEntities = assetCountRepository.findAll();
        countOfAssetEntities.forEach(x -> {
            temp1.setLocation(x.getLocation());
            temp1.setTotal(x.getLaptopCount());
            temp1.setUnAssigned(x.getUnAssignedLaptopCount());
            assetCounterDTOs.add(temp1);
        });
        for (AssetCounterDto i : assetCounterDTOs)
            System.out.println(i.getLocation());

        return assetCounterDTOs;
    }

    @Override
    public List<AssetsEntity> getLaptopsUnderWarenty() {
        List<AssetsEntity> all = assetRepo.findAll();
        List<AssetsEntity> assetsEntities = new ArrayList<>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        // System.out.println(dtf.format(now));
        String today = dtf.format(now);

        for (AssetsEntity i : all) {
            try {
                Date date = new SimpleDateFormat("dd/MM/yyyy").parse(String.valueOf(i.getWarrantyDate()));
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
        List<AssetsEntity> assetsEntities = new ArrayList<>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        // System.out.println(dtf.format(now));
        String today = dtf.format(now);

        for (AssetsEntity i : all) {
            try {
                Date date = new SimpleDateFormat("dd/MM/yyyy").parse(String.valueOf(i.getWarrantyDate()));
                Date todayDate = new SimpleDateFormat("dd/MM/yyyy").parse(today);
                if (date.compareTo(todayDate) < 0) {
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
            AssetsEntity asset = assetRepo.findBySerialNumber(serialNo);
            AssetsHistoryEntity assetsHistoryEntity = new AssetsHistoryEntity();
            List<AssetsHistoryEntity> assetsHistoryEntities = assetsHistoryRepository.findAll();
            List<CountOfAssetsEntity> countOfAssetEntities = assetCountRepository.findAll();
            if (!asset.getStatus().equalsIgnoreCase("scrap")) {

                assetsHistoryEntity.setEmpId(asset.getEmpId());
                assetsHistoryEntity.setReturnDate(LocalDate.now());
                assetsHistoryEntity.setEmpId(asset.getEmpId());
                assetsHistoryEntity.setSerialNumber(asset.getSerialNumber());
                assetsHistoryEntity.setAssignedBy(asset.getAssignedBy());
                assetsHistoryEntity.setAssignedDate(asset.getAssignedDate());
                asset.setEmpId(null);
                asset.setAssignedBy(null);
                asset.setAssignedDate(null);
                asset.setStatus("Unassigned");
                asset.setReturnDate(null);
                for (CountOfAssetsEntity i : countOfAssetEntities) {
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
                assetRepo.save(asset);
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
            throw new RuntimeException("Asset id is empty: null");
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

    @Override
    public void exportAssetsToExcel(List<AssetExportDto> allAssets, OutputStream outputStream) throws IOException {
        Workbook workbook = new XSSFWorkbook();

        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);

        List<AssetExportDto> assigned = allAssets.stream()
                .filter(a -> "Assigned".equalsIgnoreCase(a.getStatus()))
                .collect(Collectors.toList());
        writeAssignedSheet(workbook, assigned, headerStyle, dataStyle);

        List<AssetExportDto> unassigned = allAssets.stream()
                .filter(a -> "Unassigned".equalsIgnoreCase(a.getStatus()))
                .collect(Collectors.toList());
        writeUnassignedSheet(workbook, unassigned, headerStyle, dataStyle);

        List<AssetExportDto> scrap = allAssets.stream()
                .filter(a -> "Scrap".equalsIgnoreCase(a.getStatus()))
                .collect(Collectors.toList());
        writeScrapSheet(workbook, scrap, headerStyle, dataStyle);

        workbook.write(outputStream);
        workbook.close();
    }

    private void writeAssignedSheet(Workbook workbook, List<AssetExportDto> data, CellStyle headerStyle, CellStyle dataStyle) {
        Sheet sheet = workbook.createSheet("Assigned Assets");
        String[] headers = {
                "Asset Name", "Serial Number", "Assigned To (Emp ID)", "Status", "Type", "Purchase Date",
                "Warranty Date", "Location", "Loc Code", "Model Name", "Operating System",
                /*"Return Date",*/ "Added By", "Assigned Date", "Assigned By", "Sourced By"
        };

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowNum = 1;
        for (AssetExportDto dto : data) {
            Row row = sheet.createRow(rowNum++);
            int col = 0;
            createDataCell(row, col++, dto.getAssetName(), dataStyle);
            createDataCell(row, col++, dto.getSerialNumber(), dataStyle);
            createDataCell(row, col++, dto.getAssignedTo(), dataStyle);
            createDataCell(row, col++, dto.getStatus(), dataStyle);
            createDataCell(row, col++, dto.getType(), dataStyle);
            createDataCell(row, col++, String.valueOf(dto.getPurchaseDate()), dataStyle);
            createDataCell(row, col++, String.valueOf(dto.getWarrantyDate()), dataStyle);
            createDataCell(row, col++, dto.getLocation(), dataStyle);
            createDataCell(row, col++, dto.getLocCode(), dataStyle);
            createDataCell(row, col++, dto.getModelName(), dataStyle);
            createDataCell(row, col++, dto.getOperatingSystem(), dataStyle);
//            createDataCell(row, col++, String.valueOf(dto.getReturnDate()), dataStyle);
            createDataCell(row, col++, dto.getAddedBy(), dataStyle);
            createDataCell(row, col++, String.valueOf(dto.getAssignedDate()), dataStyle);
            createDataCell(row, col++, dto.getAssignedBy(), dataStyle);
            createDataCell(row, col, dto.getAssetSourcedBy(), dataStyle);
        }

        for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);
    }

    private void writeUnassignedSheet(Workbook workbook, List<AssetExportDto> data, CellStyle headerStyle, CellStyle dataStyle) {
        Sheet sheet = workbook.createSheet("Unassigned Assets");
        String[] headers = {
                "Asset Name", "Serial Number", "Status", "Type", "Purchase Date",
                "Warranty Date", "Location", "Loc Code", "Model Name", "Operating System",
                "Added By", "Sourced By"
        };

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowNum = 1;
        for (AssetExportDto dto : data) {
            Row row = sheet.createRow(rowNum++);
            int col = 0;
            createDataCell(row, col++, dto.getAssetName(), dataStyle);
            createDataCell(row, col++, dto.getSerialNumber(), dataStyle);
            createDataCell(row, col++, dto.getStatus(), dataStyle);
            createDataCell(row, col++, dto.getType(), dataStyle);
            createDataCell(row, col++, String.valueOf(dto.getPurchaseDate()), dataStyle);
            createDataCell(row, col++, String.valueOf(dto.getWarrantyDate()), dataStyle);
            createDataCell(row, col++, dto.getLocation(), dataStyle);
            createDataCell(row, col++, dto.getLocCode(), dataStyle);
            createDataCell(row, col++, dto.getModelName(), dataStyle);
            createDataCell(row, col++, dto.getOperatingSystem(), dataStyle);
            createDataCell(row, col++, dto.getAddedBy(), dataStyle);
            createDataCell(row, col, dto.getAssetSourcedBy(), dataStyle);
        }

        for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);
    }

    private void writeScrapSheet(Workbook workbook, List<AssetExportDto> data, CellStyle headerStyle, CellStyle dataStyle) {
        Sheet sheet = workbook.createSheet("Scraped Assets");
        String[] headers = {
                "Asset Name", "Serial Number", "Purchase Date", "Scraped Date", "Scraped By",
                "Operating System"/*, "Users"*/, "Status", "Type", "Location", "Loc Code",
                "Model Name", "Added By", "Sourced By"
        };

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowNum = 1;
        for (AssetExportDto dto : data) {
            Row row = sheet.createRow(rowNum++);
            int col = 0;
            createDataCell(row, col++, dto.getAssetName(), dataStyle);
            createDataCell(row, col++, dto.getSerialNumber(), dataStyle);
            createDataCell(row, col++, String.valueOf(dto.getPurchaseDate()), dataStyle);
            createDataCell(row, col++, String.valueOf(dto.getAssignedDate()), dataStyle);
            createDataCell(row, col++, dto.getAssignedBy(), dataStyle);
            createDataCell(row, col++, dto.getOperatingSystem(), dataStyle);
            //    createDataCell(row, col++, dto.EmpId(), dataStyle); // Users
            createDataCell(row, col++, dto.getStatus(), dataStyle);
            createDataCell(row, col++, dto.getType(), dataStyle);
            createDataCell(row, col++, dto.getLocation(), dataStyle);
            createDataCell(row, col++, dto.getLocCode(), dataStyle);
            createDataCell(row, col++, dto.getModelName(), dataStyle);
            createDataCell(row, col++, dto.getAddedBy(), dataStyle);
            createDataCell(row, col, dto.getAssetSourcedBy(), dataStyle);
        }

        for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);
    }

    private void createDataCell(Row row, int colIdx, String value, CellStyle style) {
        Cell cell = row.createCell(colIdx);
        cell.setCellValue(value != null ? value : "");
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

    @Override
    public void importAssetsFromExcel(InputStream inputStream) throws IOException {
        Workbook workbook = new XSSFWorkbook(inputStream);

        importUnassignedAssets(workbook.getSheet("Unassigned Assets"));
        importAssignedAssets(workbook.getSheet("Assigned Assets"));
        importScrapedAssets(workbook.getSheet("Scraped Assets"));

        workbook.close();
    }


    private void importUnassignedAssets(Sheet sheet) {
        if (sheet == null) {
            log.warn("Sheet 'Unassigned Assets' not found.");
            return;
        }

        Iterator<Row> rows = sheet.iterator();
        if (rows.hasNext()) rows.next();

        while (rows.hasNext()) {
            Row row = rows.next();
            AssetsDto asset = new AssetsDto();
            asset.setAssetName((getCellValue(row, 0)));
            asset.setSerialNumber(getCellValue(row, 1));
            asset.setStatus("Unassigned");
            asset.setType(getCellValue(row, 3));
            asset.setPurchaseDate(parseDateSafe(getCellValue(row, 4)));
            asset.setWarrantyDate(parseDateSafe(getCellValue(row, 5)));
            asset.setLocation(getCellValue(row, 6));
            asset.setLocCode(parseIntSafe(getCellValue(row, 7)));
            asset.setModelName(getCellValue(row, 8));
            asset.setOperatingSystem(getCellValue(row, 9));
            asset.setAddedBy(getCellValue(row, 10));
            asset.setAssetSourcedBy(getCellValue(row, 11));
            if (asset.getSerialNumber() == null || asset.getSerialNumber().isEmpty()) {
                log.warn("Skipping row due to missing serial number.");continue;
            }
            if(assetRepo.existsBySerialNumber(asset.getSerialNumber())){ log.warn("Skipping row due to already exist serial number.");continue;}
            if(asset.getPurchaseDate().isAfter(asset.getWarrantyDate())){log.warn("Warranty Date is lower than purchase date.");continue;}
            saveAsset(asset);
            assignedAssetsService.unAssignedCountImport((Objects.requireNonNull(getCellValue(row, 3))),(Objects.requireNonNull(getCellValue(row, 6))));

            assignedAssetsService.totalCountImport((Objects.requireNonNull(getCellValue(row, 3))),(Objects.requireNonNull(getCellValue(row, 6))));
        }
    }

    private void importAssignedAssets(Sheet sheet) {
        if (sheet == null) {
            log.warn("Sheet 'Assigned Assets' not found.");
            return;
        }


        Iterator<Row> rows = sheet.iterator();
        if (rows.hasNext()) rows.next();

        while (rows.hasNext()) {
            Row row = rows.next();
            AssetsDto asset = new AssetsDto();
            asset.setEmpId(getCellValue(row, 2));

            if (asset.getEmpId()!=null&&employeeRepository.existsById(asset.getEmpId())) {
                System.out.println(asset.getEmpId());
                asset.setAssetName(getCellValue(row, 0));
                asset.setSerialNumber(getCellValue(row, 1));
                asset.setStatus("Assigned");
                asset.setType(getCellValue(row, 4));
                asset.setPurchaseDate(parseDateSafe(getCellValue(row, 5)));
                asset.setWarrantyDate(parseDateSafe(getCellValue(row, 6)));
                asset.setLocation(getCellValue(row, 7));
                asset.setLocCode(parseIntSafe(getCellValue(row, 8)));
                asset.setModelName(getCellValue(row, 9));
                asset.setOperatingSystem(getCellValue(row, 10));
                asset.setAddedBy(getCellValue(row, 11));
                asset.setAssignedBy(getCellValue(row, 13));
                asset.setAssetSourcedBy(getCellValue(row, 14));
                asset.setAssignedDate(parseDateSafe(getCellValue(row, 12)));
           //     asset.setReturnDate(parseDateSafe(getCellValue(row, )));
                assignedAssetsService.assignedCountImport((Objects.requireNonNull(getCellValue(row, 4))),(Objects.requireNonNull(getCellValue(row, 7))));
                assignedAssetsService.totalCountImport((Objects.requireNonNull(getCellValue(row, 4))),(Objects.requireNonNull(getCellValue(row, 7))));

                if (asset.getSerialNumber() == null || asset.getSerialNumber().isEmpty()) {
                    log.warn("Skipping row due to missing serial number while Importing Assigned Asset.");continue;
                }
                if(!assetRepo.existsBySerialNumber(asset.getSerialNumber())&&asset.getPurchaseDate().isAfter(asset.getWarrantyDate()))
                {
                    asset = saveAsset(asset).getBody();
                }
                else{
                    log.warn("Skipping save the row due to already exist serial number while Importing Assigned Asset.");

                    AssetsEntity assetsEntity = assetRepo.findBySerialNumber(asset.getSerialNumber());
                    if(assetsEntity.getAssetName().equalsIgnoreCase(asset.getAssetName())&&assetsEntity.getPurchaseDate().isEqual(asset.getPurchaseDate()))
                    {
                        if(asset.getStatus().equalsIgnoreCase("unassigned")) {
                            assetsEntity.setAssignedBy(asset.getAssignedBy());
                            assetsEntity.setAssignedDate(asset.getAssignedDate());
                            assetsEntity.setEmpId(asset.getEmpId());
                            asset = modelMapper.map(assetsEntity, AssetsDto.class);
                        }
                        else {
                            log.warn("Skipping the Asset Assigning Because Asset Already Present and it status is {} [Serial Number: {}]", asset.getStatus(),asset.getSerialNumber());
                            continue;
                        }
                    }
                    else {
                        log.warn("Skipping Asset Assigning due to already exist serial number while Importing Assigned Asset, Importing asset differ from the DB asset [Serial Number: {}]",asset.getSerialNumber());
                        continue;
                    }
                }

                asset.setStatus("Assigned");
                assetRepo.save(modelMapper.map(asset,AssetsEntity.class));
                AssignableAssetDto assignableAssetDto =new AssignableAssetDto();
                assignableAssetDto.setAssignedBy(asset.getAssignedBy());
                assignableAssetDto.setAssignedDate(asset.getAssignedDate());
                AssignedAssetsEntity assignedAssetsEntity = assignedAssetsServiceImp.getAssignedAssetsEntity(assignableAssetDto
                        , modelMapper.map(asset, AssetsEntity.class));

                for (CountOfAssetsEntity count : assetCountRepository.findAll()) {
                    if (asset.getLocation().equalsIgnoreCase(count.getLocation())) {
                        assignedAssetsServiceImp.updateUnassignedCount(modelMapper.map(asset, AssetsEntity.class), count);
                        assetCountRepository.save(count);
                    }
                }
                assignedAssetsService.assignedCountImport((Objects.requireNonNull(getCellValue(row, 4))),(Objects.requireNonNull(getCellValue(row, 7))));
                assignedAssetsService.totalCountImport((Objects.requireNonNull(getCellValue(row, 4))),(Objects.requireNonNull(getCellValue(row, 7))));

                assignedAssetsRepository.save(assignedAssetsEntity);
                assetsHistoryServices.saveHistory(assignedAssetsEntity);
            } else {
                log.warn("No employee found for EmpId: {}", asset.getEmpId());
            }
        }
    }

    private void importScrapedAssets(Sheet sheet) {
        if (sheet == null) {
            log.warn("Sheet 'Scraped Assets' not found.");
            return;
        }

        Iterator<Row> rows = sheet.iterator();
        if (rows.hasNext()) rows.next();

        while (rows.hasNext()) {
            Row row = rows.next();
            AssetsDto asset = new AssetsDto();

            asset.setAssetName(getCellValue(row, 0));
            asset.setSerialNumber(getCellValue(row, 1));
            asset.setPurchaseDate(parseDateSafe(getCellValue(row, 2)));

            if (asset.getSerialNumber() == null || asset.getSerialNumber().isEmpty()) {
                log.warn("Skipping row due to missing serial number while importing Scrap Asset.");continue;
            }
           AssetsEntity assetsEntity = assetRepo.findBySerialNumber(asset.getSerialNumber());
            if(assetsEntity!=null)
            {
                if(assetsEntity.getAssetName().equalsIgnoreCase(asset.getAssetName())&&assetsEntity.getPurchaseDate().isEqual(asset.getPurchaseDate()))
                 deleteAsset(assetsEntity.getAssetId());
                continue;
            }
            asset.setAssignedDate(parseDateSafe(getCellValue(row, 3)));
            asset.setAssignedBy(getCellValue(row, 4));
            asset.setOperatingSystem(getCellValue(row, 5));
            asset.setType(getCellValue(row, 7));
            asset.setLocation(getCellValue(row, 8));
            asset.setLocCode(parseIntSafe(getCellValue(row, 9)));
            asset.setModelName(getCellValue(row, 10));
            asset.setAddedBy(getCellValue(row, 11));
            asset.setAssetSourcedBy(getCellValue(row, 12));
            if(asset.getPurchaseDate().isAfter(asset.getWarrantyDate())){log.warn("Warranty Date is lower than purchase date @ Importing Scrap asset");continue;}
            deleteAsset(Objects.requireNonNull(saveAsset(asset).getBody()).getAssetId());
        }
    }

    private String getCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return null;

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> null;
        };
    }

    private Integer parseIntSafe(String value) {
        try {
            return (value != null && !value.isBlank()) ? Integer.parseInt(value) : null;
        } catch (NumberFormatException e) {
            log.warn("Failed to parse integer: {}", value);
            return null;
        }
    }

    private LocalDate parseDateSafe(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) return null;

        try {
            Date parsed = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
            return parsed.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } catch (ParseException e) {
            try {
                double excelDate = Double.parseDouble(dateStr);
                Date parsed = DateUtil.getJavaDate(excelDate);
                return parsed.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            } catch (NumberFormatException ne) {
                log.warn("Invalid date: {}", dateStr);
                return null;
            }
        }
    }
}