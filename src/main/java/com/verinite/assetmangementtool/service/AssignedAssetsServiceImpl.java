package com.verinite.assetmangementtool.service;

import com.verinite.assetmangementtool.dto.AssignableAssetDto;
import com.verinite.assetmangementtool.dto.AssignedAssetDtoList;
import com.verinite.assetmangementtool.dto.RecentAssignedEmp;
import com.verinite.assetmangementtool.entity.AssetsEntity;
import com.verinite.assetmangementtool.entity.AssignedAssetsEntity;
import com.verinite.assetmangementtool.entity.CountOfAssets;
import com.verinite.assetmangementtool.entity.EmployeeEntity;
import com.verinite.assetmangementtool.repository.AssetCountRepository;
import com.verinite.assetmangementtool.repository.AssetsRepository;
import com.verinite.assetmangementtool.repository.AssignedAssetsRepository;
import com.verinite.assetmangementtool.repository.EmployeeRepository;
import com.verinite.assetmangementtool.service.mailservice.AckMailer;
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
        try {
            assignedAssetsEntitys = assignedAssetsRepository.findByAssignedAssetsId(assignedId);
        } catch (Exception e) {
            System.out.println("Given id not found");
        }

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
        String empId = assignableAssetDtos.get(0).getEmpId(); // Assuming all assets belong to the same employee
        EmployeeEntity employeeEntity = employeeRepository.findByEmpId(empId);
        if (employeeEntity != null) {
            try {
                List<CountOfAssets> countOfAssets = assetCountRepository.findAll();
                for (AssignableAssetDto assignableAssetDto : assignableAssetDtos) {
                    AssetsEntity asset = assetsRepo.findBySerialNumber(assignableAssetDto.getSerialNumber());
                    if (asset == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Asset not found");
                    }

                    if (asset.getStatus().equalsIgnoreCase("UnAssigned")) {
                        asset.setEmpId(empId);
                        asset.setStatus("Assigned");
                        AssignedAssetsEntity assignedAssetsEntity = getAssignedAssetsEntity(assignableAssetDto, asset);
                        asset.setAssignedDate(assignableAssetDto.getAssignedDate());
                        asset.setAssignedBy(assignableAssetDto.getAssignedBy());


                        for (CountOfAssets i : countOfAssets) {
                            if (asset.getLocation().equalsIgnoreCase(i.getLocation())) {
                                updateUnassignedCount(asset, i);
                                assetCountRepository.save(i);
                            }
                        }

                        assignedAssetsRepository.save(assignedAssetsEntity);
                        assetsRepo.save(asset);
                        new Thread(() -> {
                            List<AssetsEntity> assetsEntityList = assignableAssetDtos.stream().map((data) -> {
                                AssetsEntity assetsEntity = new AssetsEntity();
                                assetsEntity.setEmpId(data.getEmpId());
                                assetsEntity.setAssetName(data.getAssetName());
                                assetsEntity.setSerialNumber(data.getSerialNumber());
                                assetsEntity.setAssignedBy(data.getAssignedBy());
                                assetsEntity.setAssignedDate(data.getAssignedDate());
                                return assetsEntity;
                            }).collect(Collectors.toList());

                            assetsHistoryServices.saveHistory(assignedAssetsEntity);
                            try {
                                ackMailer.sendAckMail(empId, assetsEntityList);
                            } catch (MessagingException | UnsupportedEncodingException e) {
                                throw new RuntimeException(e);
                            }
                        }).start();
                    } else if (asset.getStatus().equalsIgnoreCase("Scrap")) {

                        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Asset was in Scrap");
                    } else
                        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Asset Already In Assigned");
                }
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

    void updateUnassignedCount(AssetsEntity asset, CountOfAssets i) {
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
    public String unAssignAsset(List<String> serialNumber) {
        AssignedAssetsEntity assignedAssets = null;
        for (String serialNo : serialNumber) {
            assignedAssets = assignedAssetsRepository.findBySerialNumber(serialNo);
            if (assignedAssets == null) return "assert Not found";
            assetsHistoryService.updateHistory(assignedAssets, serialNo);

            deleteAssignedAssets(assignedAssets.getAssignedAssetsId());
            int i = assetsRepo.updateUnassignStatus("unAssigned", null, null, null, serialNumber);

            Optional<AssetsEntity> assets = assetsRepo.findByAssetId(i);
            if (assets.isPresent()) {
                assetsRepo.save(assets.get());
                return " asset Unassigned";
            }
        }
        return " Asset is UnAssigned for " + assignedAssets.getEmpId();
    }

    public List<AssignedAssetsEntity> getAllAssetsAssignedToParticularEmployee(String empId) {
        return assignedAssetsRepository.findByEmpId(empId);
    }


}