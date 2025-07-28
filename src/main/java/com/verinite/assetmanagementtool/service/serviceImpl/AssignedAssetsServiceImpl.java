package com.verinite.assetmanagementtool.service.serviceImpl;

import com.verinite.assetmanagementtool.dto.AssignableAssetDto;
import com.verinite.assetmanagementtool.dto.AssignedAssetDtoList;
import com.verinite.assetmanagementtool.dto.CountOfAssetsDTO;
import com.verinite.assetmanagementtool.dto.RecentAssignedEmp;
import com.verinite.assetmanagementtool.entity.AssetsEntity;
import com.verinite.assetmanagementtool.entity.AssignedAssetsEntity;
import com.verinite.assetmanagementtool.entity.CountOfAssetsEntity;
import com.verinite.assetmanagementtool.entity.EmployeeEntity;
import com.verinite.assetmanagementtool.repository.*;
import com.verinite.assetmanagementtool.service.AssetsHistoryServices;
import com.verinite.assetmanagementtool.service.AssignedAssetsService;
import com.verinite.assetmanagementtool.service.mailservice.AckMailer;
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
    CountOfAssetsRepository countOfAssetsRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private AssignedAssetsRepository assignedAssetsRepository;
    @Autowired
    private AckMailer ackMailer;
    @Autowired
    private AdminRegistrationRepository adminRepo;

    @Override
    public AssignedAssetsEntity getAssignedAssetsById(int assignedId) {
        return assignedAssetsRepository.findByAssignedAssetsId(assignedId);
    }

    @Override
    public AssignedAssetsEntity getAssignedAssetsByAssetsId(int assetId) {
        return assignedAssetsRepository.findByAssignedAssetsId(assetId);
    }

    public List<AssignedAssetDtoList> getAllAssignedAssets() {
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
    public AssignedAssetsEntity updateAssignedAssets(int assignedId, String empId) {
        AssignedAssetsEntity assignedAssetsEntitys = new AssignedAssetsEntity();
        String assetName = assignedAssetsEntitys.getAssetName();
        try {
            assignedAssetsEntitys = assignedAssetsRepository.findByAssignedAssetsId(assignedId);
        } catch (Exception e) {
            System.out.println("Given id not found");
        }
        unassignedToAssignedCount(assetName);
        assignedAssetsEntitys.setEmpId(empId);

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

    public ResponseEntity<?> save(List<AssignableAssetDto> assignableAssetDtos) {

        List<AssetsEntity> assetsEntityList = new LinkedList<>();
        Map<String, String> ignoredAsset = new HashMap<>();
        List<CountOfAssetsEntity> countOfAssetEntities = assetCountRepository.findAll();
        for (AssignableAssetDto assignableAssetDto : assignableAssetDtos) {
            String empId = assignableAssetDto.getEmpId();
            EmployeeEntity employeeEntity = employeeRepository.findByEmpId(empId);
            if(employeeEntity==null)
            {
                ignoredAsset.put(assignableAssetDto.getSerialNumber(),"Employee Not found: "+assignableAssetDto.getEmpId());
                continue;
            }
            AssetsEntity asset = assetsRepo.findBySerialNumber(assignableAssetDto.getSerialNumber());
            if (asset == null) {
                ignoredAsset.put(assignableAssetDto.getSerialNumber(),"Asset Not found");
                continue;
            }
            if(asset.getPurchaseDate().isAfter(assignableAssetDto.getAssignedDate()))
            {
                ignoredAsset.put(assignableAssetDto.getSerialNumber(),"Asset Assigned Date is not valid");
                continue;
            }
            if(!adminRepo.existsByEmpId(assignableAssetDto.getAssignedBy()))
            {
                ignoredAsset.put(assignableAssetDto.getSerialNumber(),"Admin Id is not valid");
                continue;
            }
            if (asset.getStatus().equalsIgnoreCase("UnAssigned") && employeeEntity.getStatus().equalsIgnoreCase("Active")) {
                asset.setEmpId(empId);
                asset.setStatus("Assigned");

                AssignedAssetsEntity assignedAssetsEntity = getAssignedAssetsEntity(assignableAssetDto, asset);
                asset.setAssignedDate(assignableAssetDto.getAssignedDate());
                asset.setAssignedBy(assignableAssetDto.getAssignedBy());


                for (CountOfAssetsEntity i : countOfAssetEntities) {
                    if (asset.getLocation().equalsIgnoreCase(i.getLocation())) {

                        updateAssignedCount(asset, i);
                        //updateTotalCount(asset, i);
                        assetCountRepository.save(i);
                    }
                }

                assignedAssetsRepository.save(assignedAssetsEntity);
                assetsRepo.save(asset);
                assetsEntityList.add(asset);
                assetsHistoryServices.saveHistory(assignedAssetsEntity);
            } else if (asset.getStatus().equalsIgnoreCase("Scrap"))
                ignoredAsset.put(assignableAssetDto.getSerialNumber(),"Asset was in Scrap");
             else if (employeeEntity.getStatus().equalsIgnoreCase("InActive"))
                ignoredAsset.put(assignableAssetDto.getSerialNumber(),"Employee Was In InActive");
            else
                ignoredAsset.put(assignableAssetDto.getSerialNumber(),"Asset Already In Assigned");
        }
        sendAcknowledgementMails(assetsEntityList);
        if(ignoredAsset.isEmpty())
        {
            return ResponseEntity.ok("All data's are Assigned");
        }
        return ResponseEntity.ok(ignoredAsset);
    }

    public void sendAcknowledgementMails(List<AssetsEntity> assetsEntityList) {

        Map<String, List<AssetsEntity>> groupedByEmpId = assetsEntityList.stream()
                .filter(asset -> asset.getEmpId() != null)
                .collect(Collectors.groupingBy(AssetsEntity::getEmpId));

        for (Map.Entry<String, List<AssetsEntity>> entry : groupedByEmpId.entrySet()) {
            new Thread(() -> {
                try {
                    ackMailer.sendAckMail(entry.getKey(), entry.getValue());
                } catch (MessagingException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }).start();
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
    }).toList();
    set.forEach(x -> emps.add(employeeRepository.findByEmpId(x.getEmpId())));
    List<EmployeeEntity> uniqEmp = new ArrayList<>(new HashSet<>(emps));
    return ResponseEntity.ok().body(uniqEmp);
}

@Override
public String unAssignAsset(List<String> serialNumber) {
    AssignedAssetsEntity assignedAssets = null;
    List<CountOfAssetsEntity> countOfAssetsEntities = countOfAssetsRepository.findAll();
    for (String serialNo : serialNumber) {
        assignedAssets = assignedAssetsRepository.findBySerialNumber(serialNo);
        if (assignedAssets == null) return "assert Not found";
        deleteAssignedAssets(assignedAssets.getAssignedAssetsId());
        int i = assetsRepo.updateUnassignStatus("UnAssigned", null, null, null, serialNumber);


        Optional<AssetsEntity> assets = assetsRepo.findByAssetId(i);
        if (assets.isPresent()) {
            AssetsEntity assetsEntity = assetsRepo.findBySerialNumber(serialNo);
            for (CountOfAssetsEntity entity : countOfAssetsEntities) {
                updateUnassignedCount(assetsEntity, entity);
                countOfAssetsRepository.save(entity);
            }

            assetsRepo.save(assets.get());
            assetsHistoryService.updateHistory(assignedAssets, serialNo);
            return "Asset is UnAssigned for " + assignedAssets.getEmpId();
        }
    }
    return " Asset is Not UnAssigned for " + assignedAssets.getEmpId();
}

public ResponseEntity<?> getAllAssetsAssignedToParticularEmployee(String empId) {
    List<AssignedAssetsEntity> assignedAssetsEntities = assignedAssetsRepository.findByEmpId(empId);
    if(assignedAssetsEntities.isEmpty()){
        String message = "No assigned assets found for employee ID : "+empId;
        return ResponseEntity.ok(message);
    }
    return ResponseEntity.ok(assignedAssetsEntities);
}



public void assignedCount(String assetName) {

}

void updateTotalCount(AssetsEntity asset, CountOfAssetsEntity i) {
    String assetType = asset.getType().toLowerCase();
    switch (assetType) {
        case "laptop":
            i.setLaptopCount(i.getLaptopCount() + 1);
            break;
        case "mouse":
            i.setMouseCount(i.getMouseCount() + 1);
            break;
        case "laptopcharger":
            i.setLaptopChargerCount(i.getLaptopChargerCount() + 1);
            break;
        case "headphone":
            i.setHeadPhonesCount(i.getHeadPhonesCount() + 1);
            break;
        case "bag":
            i.setBagCount(i.getBagCount() + 1);
            break;
        case "datacard":
            i.setDataCardCount(i.getDataCardCount() + 1);
            break;
        case "mobile":
            i.setMobileCount(i.getMobileCount() + 1);
            break;
        case "camera":
            i.setCameraCount(i.getCameraCount() + 1);
            break;
        case "projector":
            i.setProjectorCount(i.getProjectorCount() + 1);
            break;
        case "firewall":
            i.setFireWallCount(i.getFireWallCount() + 1);
            break;
        case "switch":
            i.setSwitchCount(i.getSwitchCount() + 1);
            break;
        case "dvr":
            i.setDvrCount(i.getDvrCount() + 1);
            break;
        case "speaker":
            i.setSpeakerCount(i.getSpeakerCount() + 1);
            break;
        default:
            break;
    }
}


void deleteTotalCount(AssetsEntity asset, CountOfAssetsEntity i) {
    String assetType = asset.getType().toLowerCase();
    switch (assetType) {
        case "laptop":
            i.setLaptopCount(i.getLaptopCount() - 1);
            break;
        case "mouse":
            i.setMouseCount(i.getMouseCount() - 1);
            break;
        case "laptopcharger":
            i.setLaptopChargerCount(i.getLaptopChargerCount() - 1);
            break;
        case "headphone":
            i.setHeadPhonesCount(i.getHeadPhonesCount() - 1);
            break;
        case "bag":
            i.setBagCount(i.getBagCount() - 1);
            break;
        case "datacard":
            i.setDataCardCount(i.getDataCardCount() - 1);
            break;
        case "mobile":
            i.setMobileCount(i.getMobileCount() - 1);
            break;
        case "camera":
            i.setCameraCount(i.getCameraCount() - 1);
            break;
        case "projector":
            i.setProjectorCount(i.getProjectorCount() - 1);
            break;
        case "firewall":
            i.setFireWallCount(i.getFireWallCount() - 1);
            break;
        case "switch":
            i.setSwitchCount(i.getSwitchCount() - 1);
            break;
        case "dvr":
            i.setDvrCount(i.getDvrCount() - 1);
            break;
        case "speaker":
            i.setSpeakerCount(i.getSpeakerCount() - 1);
            break;
        default:
            break;
    }
}


void updateImportUnassignedCount(AssetsEntity asset, CountOfAssetsEntity i) {
    String assetType = asset.getType().toLowerCase();
    switch (assetType) {
        case "laptop":
            i.setUnAssignedLaptopCount(i.getUnAssignedLaptopCount() + 1);
            break;
        case "mouse":
            i.setUnAssignedMouseCount(i.getUnAssignedMouseCount() + 1);
            break;
        case "laptopcharger":
            i.setUnAssignedLaptopChargerCount(i.getUnAssignedLaptopChargerCount() + 1);
            break;
        case "headphone":
            i.setUnAssignedHeadphonesCount(i.getUnAssignedHeadphonesCount() + 1);
            break;
        case "bag":
            i.setUnAssignedBagCount(i.getUnAssignedBagCount() + 1);
            break;
        case "datacard":
            i.setUnAssignedDataCardCount(i.getUnAssignedDataCardCount() + 1);
            break;
        case "mobile":
            i.setUnAssignedMobileCount(i.getUnAssignedMobileCount() + 1);
            break;
        case "camera":
            i.setUnAssignedCameraCount(i.getUnAssignedCameraCount() + 1);
            break;
        case "projector":
            i.setUnAssignedProjectorCount(i.getUnAssignedProjectorCount() + 1);
            break;
        case "firewall":
            i.setUnAssignedFireWallCount(i.getUnAssignedFireWallCount() + 1);
            break;
        case "switch":
            i.setUnAssignedSwitchCount(i.getUnAssignedSwitchCount() + 1);
            break;
        case "dvr":
            i.setUnAssignedDvrCount(i.getUnAssignedDvrCount() + 1);
            break;
        case "speaker":
            i.setUnAssignedSpeakerCount(i.getUnAssignedSpeakerCount() + 1);
            break;
        default:
            break;
    }
}


void updateAssignedCount(AssetsEntity asset, CountOfAssetsEntity i) {
    String assetType = asset.getType().toLowerCase();
    switch (assetType) {
        case "laptop":
            i.setUnAssignedLaptopCount(i.getUnAssignedLaptopCount() - 1);
            i.setAssignedLaptopCount(i.getAssignedLaptopCount() + 1);
            break;
        case "mouse":
            i.setUnAssignedMouseCount(i.getUnAssignedMouseCount() - 1);
            i.setAssignedMouseCount(i.getAssignedMouseCount() + 1);
            break;
        case "laptopcharger":
            i.setUnAssignedLaptopChargerCount(i.getUnAssignedLaptopChargerCount() - 1);
            i.setAssignedLaptopChargerCount(i.getAssignedLaptopChargerCount() + 1);
            break;
        case "headphone":
            i.setUnAssignedHeadphonesCount(i.getUnAssignedHeadphonesCount() - 1);
            i.setAssignedHeadphonesCount(i.getAssignedHeadphonesCount() + 1);
            break;
        case "bag":
            i.setUnAssignedBagCount(i.getUnAssignedBagCount() - 1);
            i.setAssignedBagCount(i.getAssignedBagCount() + 1);
            break;
        case "datacard":
            i.setUnAssignedDataCardCount(i.getUnAssignedDataCardCount() - 1);
            i.setAssignedDataCardCount(i.getAssignedDataCardCount() + 1);
            break;
        case "mobile":
            i.setUnAssignedMobileCount(i.getUnAssignedMobileCount() - 1);
            i.setAssignedMobileCount(i.getAssignedMobileCount() + 1);
            break;
        case "camera":
            i.setUnAssignedCameraCount(i.getUnAssignedCameraCount() - 1);
            i.setAssignedCameraCount(i.getAssignedCameraCount() - 1);
            break;
        case "projector":
            i.setUnAssignedProjectorCount(i.getUnAssignedProjectorCount() - 1);
            i.setAssignedProjectorCount(i.getAssignedProjectorCount() + 1);
            break;
        case "firewall":
            i.setUnAssignedFireWallCount(i.getUnAssignedFireWallCount() - 1);
            i.setAssignedFireWallCount(i.getAssignedFireWallCount() + 1);
            break;
        case "switch":
            i.setUnAssignedSwitchCount(i.getUnAssignedSwitchCount() - 1);
            i.setAssignedSwitchCount(i.getAssignedSwitchCount() + 1);
            break;
        case "dvr":
            i.setUnAssignedDvrCount(i.getUnAssignedDvrCount() - 1);
            i.setAssignedDvrCount(i.getAssignedDvrCount() + 1);
            break;
        case "speaker":
            i.setUnAssignedSpeakerCount(i.getUnAssignedSpeakerCount() - 1);
            i.setAssignedSpeakerCount(i.getAssignedSpeakerCount() + 1);
            break;
        default:
            break;
    }
}


void updateUnassignedCount(AssetsEntity asset, CountOfAssetsEntity i) {
    String assetType = asset.getType().toLowerCase();
    switch (assetType) {
        case "laptop":
            i.setUnAssignedLaptopCount(i.getUnAssignedLaptopCount() + 1);
            i.setAssignedLaptopCount(i.getAssignedLaptopCount() - 1);
            break;
        case "mouse":
            i.setUnAssignedMouseCount(i.getUnAssignedMouseCount() + 1);
            i.setAssignedMouseCount(i.getAssignedMouseCount() - 1);
            break;
        case "laptopcharger":
            i.setUnAssignedLaptopChargerCount(i.getUnAssignedLaptopChargerCount() + 1);
            i.setAssignedLaptopChargerCount(i.getAssignedLaptopChargerCount() - 1);
            break;
        case "headphone":
            i.setUnAssignedHeadphonesCount(i.getUnAssignedHeadphonesCount() + 1);
            i.setAssignedHeadphonesCount(i.getAssignedHeadphonesCount() - 1);
            break;
        case "bag":
            i.setUnAssignedBagCount(i.getUnAssignedBagCount() + 1);
            i.setAssignedBagCount(i.getAssignedBagCount() - 1);
            break;
        case "datacard":
            i.setUnAssignedDataCardCount(i.getUnAssignedDataCardCount() + 1);
            i.setAssignedDataCardCount(i.getAssignedDataCardCount() - 1);
            break;
        case "mobile":
            i.setUnAssignedMobileCount(i.getUnAssignedMobileCount() + 1);
            i.setAssignedMobileCount(i.getAssignedMobileCount() - 1);
            break;
        case "camera":
            i.setUnAssignedCameraCount(i.getUnAssignedCameraCount() + 1);
            i.setAssignedCameraCount(i.getAssignedCameraCount() - 1);
            break;
        case "projector":
            i.setUnAssignedProjectorCount(i.getUnAssignedProjectorCount() + 1);
            i.setAssignedProjectorCount(i.getAssignedProjectorCount() - 1);
            break;
        case "firewall":
            i.setUnAssignedFireWallCount(i.getUnAssignedFireWallCount() + 1);
            i.setAssignedFireWallCount(i.getAssignedFireWallCount() - 1);
            break;
        case "switch":
            i.setUnAssignedSwitchCount(i.getUnAssignedSwitchCount() + 1);
            i.setAssignedSwitchCount(i.getAssignedSwitchCount() - 1);
            break;
        case "dvr":
            i.setUnAssignedDvrCount(i.getUnAssignedDvrCount() + 1);
            i.setAssignedDvrCount(i.getAssignedDvrCount() - 1);
            break;
        case "speaker":
            i.setUnAssignedSpeakerCount(i.getUnAssignedSpeakerCount() + 1);
            i.setAssignedSpeakerCount(i.getAssignedSpeakerCount() - 1);
            break;
        default:
            break;
    }
}


public void unassignedToAssignedCount(String assetType) {
    CountOfAssetsEntity countOfAssetsEntity = new CountOfAssetsEntity();

    switch (assetType.toLowerCase()) {
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
    assetCountRepository.save(countOfAssetsEntity);

}


//    public void assignedToUnassignedCount(String assetType){
//        CountOfAssetsEntity countOfAssetsEntity = new CountOfAssetsEntity();
//
//        switch (assetType.toLowerCase()) {
//            case "laptop":
//                countOfAssetsEntity.setUnAssignedLaptopCount(countOfAssetsEntity.getUnAssignedLaptopCount() + 1);
//                countOfAssetsEntity.setAssignedLaptopCount(countOfAssetsEntity.getAssignedLaptopCount() - 1);
//                break;
//            case "mouse":
//                countOfAssetsEntity.setUnAssignedMouseCount(countOfAssetsEntity.getUnAssignedMouseCount() + 1);
//                countOfAssetsEntity.setAssignedMouseCount(countOfAssetsEntity.getAssignedMouseCount() - 1);
//                break;
//            case "laptopcharger":
//                countOfAssetsEntity.setUnAssignedLaptopChargerCount(countOfAssetsEntity.getUnAssignedLaptopChargerCount() + 1);
//                countOfAssetsEntity.setAssignedLaptopChargerCount(countOfAssetsEntity.getAssignedLaptopChargerCount() - 1);
//                break;
//            case "headphone":
//                countOfAssetsEntity.setUnAssignedHeadphonesCount(countOfAssetsEntity.getUnAssignedHeadphonesCount() + 1);
//                countOfAssetsEntity.setAssignedHeadphonesCount(countOfAssetsEntity.getAssignedHeadphonesCount() - 1);
//                break;
//            case "bag":
//                countOfAssetsEntity.setUnAssignedBagCount(countOfAssetsEntity.getUnAssignedBagCount() + 1);
//                countOfAssetsEntity.setAssignedBagCount(countOfAssetsEntity.getAssignedBagCount() - 1);
//                break;
//            case "datacard":
//                countOfAssetsEntity.setUnAssignedDataCardCount(countOfAssetsEntity.getUnAssignedDataCardCount() + 1);
//                countOfAssetsEntity.setAssignedDataCardCount(countOfAssetsEntity.getAssignedDataCardCount() - 1);
//                break;
//            case "mobile":
//                countOfAssetsEntity.setUnAssignedMobileCount(countOfAssetsEntity.getUnAssignedMobileCount() + 1);
//                countOfAssetsEntity.setAssignedMobileCount(countOfAssetsEntity.getAssignedMobileCount() - 1);
//                break;
//            case "camera":
//                countOfAssetsEntity.setUnAssignedCameraCount(countOfAssetsEntity.getUnAssignedCameraCount() + 1);
//                countOfAssetsEntity.setAssignedCameraCount(countOfAssetsEntity.getAssignedCameraCount() - 1);
//                break;
//            case "projector":
//                countOfAssetsEntity.setUnAssignedProjectorCount(countOfAssetsEntity.getUnAssignedProjectorCount() + 1);
//                countOfAssetsEntity.setAssignedProjectorCount(countOfAssetsEntity.getAssignedProjectorCount() - 1);
//                break;
//            case "firewall":
//                countOfAssetsEntity.setUnAssignedFireWallCount(countOfAssetsEntity.getUnAssignedFireWallCount() + 1);
//                countOfAssetsEntity.setAssignedFireWallCount(countOfAssetsEntity.getAssignedFireWallCount() - 1);
//                break;
//            case "switch":
//                countOfAssetsEntity.setUnAssignedSwitchCount(countOfAssetsEntity.getUnAssignedSwitchCount() + 1);
//                countOfAssetsEntity.setAssignedSwitchCount(countOfAssetsEntity.getAssignedSwitchCount() - 1);
//                break;
//            case "dvr":
//                countOfAssetsEntity.setUnAssignedDvrCount(countOfAssetsEntity.getUnAssignedDvrCount() + 1);
//                countOfAssetsEntity.setAssignedDvrCount(countOfAssetsEntity.getAssignedDvrCount() - 1);
//                break;
//            case "speaker":
//                countOfAssetsEntity.setUnAssignedSpeakerCount(countOfAssetsEntity.getUnAssignedSpeakerCount() + 1);
//                countOfAssetsEntity.setAssignedSpeakerCount(countOfAssetsEntity.getAssignedSpeakerCount() - 1);
//                break;
//            default:
//                // Handle unknown asset types
//                break;
//        }
//        assetCountRepository.save(countOfAssetsEntity);
//    }


public void totalCountImport(String assetType, String location) {
    Optional<CountOfAssetsEntity> optionalEntity = assetCountRepository.findById(location);

    if (!optionalEntity.isPresent()) {
        // log.warn("Location not found: {}", location);
        return;
    }

    CountOfAssetsEntity entity = optionalEntity.get();

    switch (assetType.toLowerCase()) {
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
            // log.warn("Unknown asset type: {}", assetType);
            return;
    }
    assetCountRepository.save(entity);
}


public void unAssignedCountImport(String assetType, String location) {
    Optional<CountOfAssetsEntity> optionalEntity = assetCountRepository.findById(location);

    if (!optionalEntity.isPresent()) {
        // log.warn("Location not found: {}", location);
        return;
    }

    CountOfAssetsEntity entity = optionalEntity.get();

    switch (assetType.toLowerCase()) {
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
            // log.warn("Unknown asset type: {}", assetType);
            return;
    }
    assetCountRepository.save(entity);
}


public void assignedCountImport(String assetType, String location) {
    Optional<CountOfAssetsEntity> optionalEntity = assetCountRepository.findById(location);

    if (optionalEntity.isEmpty()) {
        // log.warn("Location not found: {}", location);
        return;
    }

    CountOfAssetsEntity entity = optionalEntity.get();

    switch (assetType.toLowerCase()) {
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
            // log.warn("Unknown asset type: {}", assetType);
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