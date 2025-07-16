package com.verinite.assetmangementtool.service.serviceImpl;

import com.verinite.assetmangementtool.dto.AssignableAssetDto;
import com.verinite.assetmangementtool.dto.AssignedAssetDtoList;
import com.verinite.assetmangementtool.dto.CountOfAssetsDTO;
import com.verinite.assetmangementtool.dto.RecentAssignedEmp;
import com.verinite.assetmangementtool.entity.AssetsEntity;
import com.verinite.assetmangementtool.entity.AssignedAssetsEntity;
import com.verinite.assetmangementtool.entity.CountOfAssetsEntity;
import com.verinite.assetmangementtool.entity.EmployeeEntity;
import com.verinite.assetmangementtool.repository.*;
import com.verinite.assetmangementtool.service.AssetsHistoryServices;
import com.verinite.assetmangementtool.service.AssignedAssetsService;
import com.verinite.assetmangementtool.service.mailservice.AckMailer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class AssignedAssetsServiceImpl implements AssignedAssetsService {

    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    AssetsRepository assetsRepo;
    @Autowired
    AssetCountRepository assetCountRepository;
    @Autowired
    AssetsHistoryServices assetsHistoryServices;
    @Autowired
    AssetsHistoryServiceImpl assetsHistoryService;
    @Autowired
    private AssignedAssetsRepository assignedAssetsRepository;
    @Autowired
    private AckMailer ackMailer;

    @Autowired
    CountOfAssetsRepository countOfAssetsRepository;

    @Autowired
    ModelMapper modelMapper;


    @Override
    public AssignedAssetsEntity getAssignedAssetsById(int assignedId) {
        return assignedAssetsRepository.findByAssignedAssetsId(assignedId);
    }

    @Override
    public AssignedAssetsEntity getAssignedAssetsByAssetsId(int assetId) {
        return assignedAssetsRepository.findByAssignedAssetsId(assetId);
    }

    public List<AssignedAssetDtoList> getAllassignedAssets() {
        return assignedAssetsRepository.findAllByOrderByAssignedAssetsIdDesc().stream().filter(asset -> asset.getEmpId() != null).map(asset -> {
            EmployeeEntity employee = employeeRepository.findByEmpId(asset.getEmpId());

            AssignedAssetDtoList dto = new AssignedAssetDtoList();

            dto.setAssignedAssetsId(asset.getAssignedAssetsId());
            dto.setAssetName(asset.getAssetName() != null ? asset.getAssetName() : "");
            dto.setSerialNumber(asset.getSerialNumber() != null ? asset.getSerialNumber() : "");
            dto.setEmpId(asset.getEmpId() != null ? asset.getEmpId() : "");
            dto.setStatus(asset.getStatus() != null ? asset.getStatus() : "");
            dto.setAssignedDate(asset.getAssignedDate());
            dto.setAssignedBy(asset.getAssignedBy() != null ? asset.getAssignedBy() : "");

            if (employee != null) {
                dto.setEmpFirstName(employee.getFirstName() != null ? employee.getFirstName() : "Unknown");
                dto.setEmpLastName(employee.getLastName() != null ? employee.getLastName() : "");
            } else {
                dto.setEmpFirstName("Unknown");
                dto.setEmpLastName("");
            }

            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public AssignedAssetsEntity updateAssignedAssets(int assignedId, AssignedAssetsEntity assignedAssetsEntity) {
        AssignedAssetsEntity assignedAssetsEntitys = new AssignedAssetsEntity();
        String assetName = assignedAssetsEntitys.getAssetName();
        try {
            assignedAssetsEntitys = assignedAssetsRepository.findByAssignedAssetsId(assignedId);
        } catch (Exception e) {
            System.out.println("Given id not found");
        }
        assignedCount(assetName);
        assignedAssetsEntitys.setEmpId(assignedAssetsEntity.getEmpId());

        return assignedAssetsRepository.save(assignedAssetsEntitys);
    }


    @Override
    public String deleteAssignedAssets(int assignedId) {
        String deletedMessage = "Record deleted successfully";
        String notFoundMessage = "Given id not found";

        AssignedAssetsEntity assignedAssetsEntity = assignedAssetsRepository.findByAssignedAssetsId(assignedId);

        if (assignedAssetsEntity == null) {
            return notFoundMessage;
        }

        assignedAssetsRepository.delete(assignedAssetsEntity);
        return deletedMessage;
    }

    public ResponseEntity<String> save(List<AssignableAssetDto> assignableAssetDtos) {

        String empId = assignableAssetDtos.get(0).getEmpId();
        EmployeeEntity employeeEntity = employeeRepository.findByEmpId(empId);
        List<AssetsEntity> assetsEntityList = List.of();
        if (employeeEntity != null ) {
            try {
                List<CountOfAssetsEntity> countOfAssetEntities = assetCountRepository.findAll();
                for (AssignableAssetDto assignableAssetDto : assignableAssetDtos) {
                    AssetsEntity asset = assetsRepo.findBySerialNumber(assignableAssetDto.getSerialNumber());
                    if (asset == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Asset not found");
                    }

                    if (asset.getStatus().equalsIgnoreCase("UnAssigned") && employeeEntity.getStatus().equalsIgnoreCase("Active")) {
                        asset.setEmpId(empId);
                        asset.setStatus("Assigned");

                        AssignedAssetsEntity assignedAssetsEntity = getAssignedAssetsEntity(assignableAssetDto, asset);
                        asset.setAssignedDate(assignableAssetDto.getAssignedDate());
                        asset.setAssignedBy(assignableAssetDto.getAssignedBy());


                        for (CountOfAssetsEntity i : countOfAssetEntities) {
                            if (asset.getLocation().equalsIgnoreCase(i.getLocation())) {
                                updateUnassignedCount(asset, i);
                                assetCountRepository.save(i);
                            }
                        }

                        assignedAssetsRepository.save(assignedAssetsEntity);
                        assetsRepo.save(asset);
                        assetsEntityList= assignableAssetDtos.stream().map((data)->{
                            AssetsEntity assetsEntity= new AssetsEntity();
                            assetsEntity.setEmpId(data.getEmpId());
                            assetsEntity.setAssetName(data.getAssetName());
                            assetsEntity.setSerialNumber(data.getSerialNumber());
                            assetsEntity.setAssignedBy(data.getAssignedBy());
                            assetsEntity.setAssignedDate(data.getAssignedDate());
                            return assetsEntity;
                        }).collect(Collectors.toList());
                        assetsHistoryServices.saveHistory(assignedAssetsEntity);
                    } else if (asset.getStatus().equalsIgnoreCase("Scrap")) {

                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Asset was in Scrap");
                    } else if (employeeEntity.getStatus().equalsIgnoreCase("InActive"))
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee Was In InActive");

                     else
                        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Asset Already In Assigned");
                }
                List<AssetsEntity> finalAssetsEntityList = assetsEntityList;
                new Thread(()->{
                    try {
                        ackMailer.sendAckMail(empId, finalAssetsEntityList);
                    } catch (MessagingException | UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
                return ResponseEntity.ok("Asset Assigned To " + employeeEntity.getEmpId());
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("asset");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("emp");
        }
    }

    AssignedAssetsEntity getAssignedAssetsEntity(AssignableAssetDto assignableAssetDto, AssetsEntity asset) {
        AssignedAssetsEntity assignedAssetsEntity = new AssignedAssetsEntity();
        assignedAssetsEntity.setAssetName(asset.getAssetName());
        assignedAssetsEntity.setEmpId(asset.getEmpId());
        assignedAssetsEntity.setAssignedBy(assignableAssetDto.getAssignedBy());
        assignedAssetsEntity.setAssignedDate(assignableAssetDto.getAssignedDate());
        assignedAssetsEntity.setStatus("Assigned");
        assignedAssetsEntity.setSerialNumber(asset.getSerialNumber());
        return assignedAssetsEntity;
    }

    void updateUnassignedCount(AssetsEntity asset, CountOfAssetsEntity i) {
        String assetName = asset.getAssetName().toLowerCase();
        switch (assetName) {
            case "laptop":
                i.setUnAssignedLaptopCount(i.getUnAssignedLaptopCount() - 1);
                break;
            case "mouse":
                i.setUnAssignedMouseCount(i.getUnAssignedMouseCount() - 1);
                break;
            case "laptopcharger":
                i.setUnAssignedLaptopChargerCount(i.getUnAssignedLaptopChargerCount() - 1);
                break;
            case "headphone":
                i.setUnAssignedHeadphonesCount(i.getUnAssignedHeadphonesCount() - 1);
                break;
            case "bag":
                i.setUnAssignedBagCount(i.getUnAssignedBagCount() - 1);
                break;
            case "datacard":
                i.setUnAssignedDataCardCount(i.getUnAssignedDataCardCount() - 1);
                break;
            case "mobile":
                i.setUnAssignedMobileCount(i.getUnAssignedMobileCount() - 1);
                break;
            case "camera":
                i.setUnAssignedCameraCount(i.getUnAssignedCameraCount() - 1);
                break;
            case "projector":
                i.setUnAssignedProjectorCount(i.getUnAssignedProjectorCount() - 1);
                break;
            case "firewall":
                i.setUnAssignedFireWallCount(i.getUnAssignedFireWallCount() - 1);
                break;
            case "switch":
                i.setUnAssignedSwitchCount(i.getUnAssignedSwitchCount() - 1);
                break;
            case "dvr":
                i.setUnAssignedDvrCount(i.getUnAssignedDvrCount() - 1);
                break;
            case "speaker":
                i.setUnAssignedSpeakerCount(i.getUnAssignedSpeakerCount() - 1);
                break;
            default:
                break;
        }
    }

    @Override
    @Async
    public ResponseEntity<?> getRecentAssigned() {
        RecentAssignedEmp recent = new RecentAssignedEmp();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        List<EmployeeEntity> emps = new ArrayList<>();
        List<RecentAssignedEmp> recentAssignedEmpSet = new ArrayList<>();
        List<AssignedAssetsEntity> set = assignedAssetsRepository.findByEmpIdNotNull().stream().filter(x -> {
            try {
                return (DAYS.between(sf.parse(x.getAssignedDate().toString()).toInstant(),
                        new Date().toInstant())) < 15;
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        set.forEach(x -> emps.add(employeeRepository.findByEmpId(x.getEmpId())));
        List<EmployeeEntity> uniqEmp = new ArrayList<>(new HashSet<>(emps));
        return ResponseEntity.ok().body(uniqEmp);
    }

    @Override
    public String unAssignAsset(List<String> serialNumber){
        AssignedAssetsEntity assignedAssets=null;
        for(String serialNo :serialNumber) {
            assignedAssets = assignedAssetsRepository.findBySerialNumber(serialNo);
            if (assignedAssets == null) return "assert Not found";
            deleteAssignedAssets(assignedAssets.getAssignedAssetsId());
            int i = assetsRepo.updateUnassignStatus("UnAssigned", null, null, null, serialNumber);

            Optional<AssetsEntity> assets = assetsRepo.findByAssetId(i);
            if (assets.isPresent()) {
                assetsRepo.save(assets.get());
                return " asset Unassigned";
            }
            assetsHistoryService.updateHistory(assignedAssets, serialNo);
        }
        return " Asset is UnAssigned for " + assignedAssets.getEmpId();
    }

    public List<AssignedAssetsEntity> getAllAssetsAssignedToParticularEmployee(String empId) {
        return assignedAssetsRepository.findByEmpId(empId);
    }


    public void assignedCount(String assetName){
        CountOfAssetsEntity countOfAssetsEntity = new CountOfAssetsEntity();

        switch (assetName.toLowerCase()) {
            case "laptop":
                countOfAssetsEntity.setUnAssignedLaptopCount(countOfAssetsEntity.getUnAssignedLaptopCount() - 1);
                countOfAssetsEntity.setAssignedLaptopCount(countOfAssetsEntity.getAssignedLaptopCount() + 1);
                break;
            case "mouse":
                countOfAssetsEntity.setUnAssignedMouseCount(countOfAssetsEntity.getUnAssignedMouseCount() - 1);
                countOfAssetsEntity.setAssignedMouseCount(countOfAssetsEntity.getAssignedMouseCount() + 1);
                break;
            case "laptopcharger":
                countOfAssetsEntity.setUnAssignedLaptopChargerCount(countOfAssetsEntity.getUnAssignedLaptopChargerCount() - 1);
                countOfAssetsEntity.setAssignedLaptopChargerCount(countOfAssetsEntity.getAssignedLaptopChargerCount() + 1);
                break;
            case "headphone":
                countOfAssetsEntity.setUnAssignedHeadphonesCount(countOfAssetsEntity.getUnAssignedHeadphonesCount() - 1);
                countOfAssetsEntity.setAssignedHeadphonesCount(countOfAssetsEntity.getAssignedHeadphonesCount() + 1);
                break;
            case "bag":
                countOfAssetsEntity.setUnAssignedBagCount(countOfAssetsEntity.getUnAssignedBagCount() - 1);
                countOfAssetsEntity.setAssignedBagCount(countOfAssetsEntity.getAssignedBagCount() + 1);
                break;
            case "datacard":
                countOfAssetsEntity.setUnAssignedDataCardCount(countOfAssetsEntity.getUnAssignedDataCardCount() - 1);
                countOfAssetsEntity.setAssignedDataCardCount(countOfAssetsEntity.getAssignedDataCardCount() + 1);
                break;
            case "mobile":
                countOfAssetsEntity.setUnAssignedMobileCount(countOfAssetsEntity.getUnAssignedMobileCount() - 1);
                countOfAssetsEntity.setAssignedMobileCount(countOfAssetsEntity.getAssignedMobileCount() + 1);
                break;
            case "camera":
                countOfAssetsEntity.setUnAssignedCameraCount(countOfAssetsEntity.getUnAssignedCameraCount() - 1);
                countOfAssetsEntity.setAssignedCameraCount(countOfAssetsEntity.getAssignedCameraCount() + 1);
                break;
            case "projector":
                countOfAssetsEntity.setUnAssignedProjectorCount(countOfAssetsEntity.getUnAssignedProjectorCount() - 1);
                countOfAssetsEntity.setAssignedProjectorCount(countOfAssetsEntity.getAssignedProjectorCount() + 1);
                break;
            case "firewall":
                countOfAssetsEntity.setUnAssignedFireWallCount(countOfAssetsEntity.getUnAssignedFireWallCount() - 1);
                countOfAssetsEntity.setAssignedFireWallCount(countOfAssetsEntity.getAssignedFireWallCount() + 1);
                break;
            case "switch":
                countOfAssetsEntity.setUnAssignedSwitchCount(countOfAssetsEntity.getUnAssignedSwitchCount() - 1);
                countOfAssetsEntity.setAssignedSwitchCount(countOfAssetsEntity.getAssignedSwitchCount() + 1);
                break;
            case "dvr":
                countOfAssetsEntity.setUnAssignedDvrCount(countOfAssetsEntity.getUnAssignedDvrCount() - 1);
                countOfAssetsEntity.setAssignedDvrCount(countOfAssetsEntity.getAssignedDvrCount() + 1);
                break;
            case "speaker":
                countOfAssetsEntity.setUnAssignedSpeakerCount(countOfAssetsEntity.getUnAssignedSpeakerCount() - 1);
                countOfAssetsEntity.setAssignedSpeakerCount(countOfAssetsEntity.getAssignedSpeakerCount() + 1);
                break;
            default:
                // Handle unknown asset types
                break;
        }
    }

    public void totalCountImport(String assetName, String location) {
        Optional<CountOfAssetsEntity> optionalEntity = assetCountRepository.findById(location);

        if (!optionalEntity.isPresent()) {
            // log.warn("Location not found: {}", location);
            return;
        }

        CountOfAssetsEntity entity = optionalEntity.get();

        switch (assetName.toLowerCase()) {
            case "laptop":
                entity.setLaptopCount(entity.getLaptopCount() + 1);
                break;
            case "mouse":
                entity.setMouseCount(entity.getMouseCount() + 1);
                break;
            case "laptop charger":
                entity.setLaptopChargerCount(entity.getLaptopChargerCount() + 1);
                break;
            case "head phone":
                entity.setHeadPhonesCount(entity.getHeadPhonesCount() + 1);
                break;
            case "bag":
                entity.setBagCount(entity.getBagCount() + 1);
                break;
            case "data card":
                entity.setDataCardCount(entity.getDataCardCount() + 1);
                break;
            case "mobile":
                entity.setMobileCount(entity.getMobileCount() + 1);
                break;
            case "camera":
                entity.setCameraCount(entity.getCameraCount() + 1);
                break;
            case "projector":
                entity.setProjectorCount(entity.getProjectorCount() + 1);
                break;
            case "fire wall":
                entity.setFireWallCount(entity.getFireWallCount() + 1);
                break;
            case "switch":
                entity.setSwitchCount(entity.getSwitchCount() + 1);
                break;
            case "dvr":
                entity.setDvrCount(entity.getDvrCount() + 1);
                break;
            case "speaker":
                entity.setSpeakerCount(entity.getSpeakerCount() + 1);
                break;
            default:
                // log.warn("Unknown asset type: {}", assetName);
                return;
        }
        assetCountRepository.save(entity);
    }


    public void unAssignedCountImport(String assetName, String location) {
        Optional<CountOfAssetsEntity> optionalEntity = assetCountRepository.findById(location);

        if (!optionalEntity.isPresent()) {
            // log.warn("Location not found: {}", location);
            return;
        }

        CountOfAssetsEntity entity = optionalEntity.get();

        switch (assetName.toLowerCase()) {
            case "laptop":
                entity.setUnAssignedLaptopCount(entity.getUnAssignedLaptopCount() + 1);
                break;
            case "mouse":
                entity.setUnAssignedMouseCount(entity.getUnAssignedMouseCount() + 1);
                break;
            case "laptop charger":
                entity.setUnAssignedLaptopChargerCount(entity.getUnAssignedLaptopChargerCount() + 1);
                break;
            case "head phone":
                entity.setUnAssignedHeadphonesCount(entity.getUnAssignedHeadphonesCount() + 1);
                break;
            case "bag":
                entity.setUnAssignedBagCount(entity.getUnAssignedBagCount() + 1);
                break;
            case "data card":
                entity.setUnAssignedDataCardCount(entity.getUnAssignedDataCardCount() + 1);
                break;
            case "mobile":
                entity.setUnAssignedMobileCount(entity.getUnAssignedMobileCount() + 1);
                break;
            case "camera":
                entity.setUnAssignedCameraCount(entity.getUnAssignedCameraCount() + 1);
                break;
            case "projector":
                entity.setUnAssignedProjectorCount(entity.getUnAssignedProjectorCount() + 1);
                break;
            case "firewall":
                entity.setUnAssignedFireWallCount(entity.getUnAssignedFireWallCount() + 1);
                break;
            case "switch":
                entity.setUnAssignedSwitchCount(entity.getUnAssignedSwitchCount() + 1);
                break;
            case "dvr":
                entity.setUnAssignedDvrCount(entity.getUnAssignedDvrCount() + 1);
                break;
            case "speaker":
                entity.setUnAssignedSpeakerCount(entity.getUnAssignedSpeakerCount() + 1);
                break;
            default:
                // log.warn("Unknown asset type: {}", assetName);
                return;
        }
        assetCountRepository.save(entity);
    }


    public void assignedCountImport(String assetName, String location) {
        Optional<CountOfAssetsEntity> optionalEntity = assetCountRepository.findById(location);

        if (optionalEntity.isEmpty()) {
            // log.warn("Location not found: {}", location);
            return;
        }

        CountOfAssetsEntity entity = optionalEntity.get();

        switch (assetName.toLowerCase()) {
            case "laptop":
                entity.setAssignedLaptopCount(entity.getAssignedLaptopCount() + 1);
                break;
            case "mouse":
                entity.setAssignedMouseCount(entity.getAssignedMouseCount() + 1);
                break;
            case "laptop charger":
                entity.setAssignedLaptopChargerCount(entity.getAssignedLaptopChargerCount() + 1);
                break;
            case "head phone":
                entity.setAssignedHeadphonesCount(entity.getAssignedHeadphonesCount() + 1);
                break;
            case "bag":
                entity.setAssignedBagCount(entity.getAssignedBagCount() + 1);
                break;
            case "data card":
                entity.setAssignedDataCardCount(entity.getAssignedDataCardCount() + 1);
                break;
            case "mobile":
                entity.setAssignedMobileCount(entity.getAssignedMobileCount() + 1);
                break;
            case "camera":
                entity.setAssignedCameraCount(entity.getAssignedCameraCount() + 1);
                break;
            case "projector":
                entity.setAssignedProjectorCount(entity.getAssignedProjectorCount() + 1);
                break;
            case "fire wall":
                entity.setAssignedFireWallCount(entity.getAssignedFireWallCount() + 1);
                break;
            case "switch":
                entity.setAssignedSwitchCount(entity.getAssignedSwitchCount() + 1);
                break;
            case "dvr":
                entity.setAssignedDvrCount(entity.getAssignedDvrCount() + 1);
                break;
            case "speaker":
                entity.setAssignedSpeakerCount(entity.getAssignedSpeakerCount() + 1);
                break;
            default:
                // log.warn("Unknown asset type: {}", assetName);
                return;
        }
        assetCountRepository.save(entity);
    }




    public CountOfAssetsDTO convertToDTO(CountOfAssetsEntity entity) {
        return modelMapper.map(entity, CountOfAssetsDTO.class);
    }

    public CountOfAssetsEntity convertToEntity(CountOfAssetsDTO dto) {
        return modelMapper.map(dto, CountOfAssetsEntity.class);
    }

    public List<CountOfAssetsDTO> convertEntityListToDTOList(List<CountOfAssetsEntity> entityList) {
        return entityList.stream()
                .map(entity -> modelMapper.map(entity, CountOfAssetsDTO.class))
                .collect(Collectors.toList());
    }

}