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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
    private AssignedAssetsRepository assignedAssetsRepository;
    @Autowired
    private AckMailer ackMailer;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public AssignedAssetsEntity saveAssignedAssets(AssignedAssetsEntity assignedAssetsEntity) {
        return assignedAssetsRepository.save(assignedAssetsEntity);
    }

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
            // Fetch employee entity by empId
            EmployeeEntity employee = employeeRepository.findByEmpId(asset.getEmpId());

            AssignedAssetDtoList dto = new AssignedAssetDtoList();

            // Mapping asset entity fields to DTO
            dto.setAssignedAssetsId(asset.getAssignedAssetsId());
            dto.setAssetId(asset.getAssetId());
            dto.setAssetName(asset.getAssetName() != null ? asset.getAssetName() : "");
            dto.setSerialNumber(asset.getSerialNumber() != null ? asset.getSerialNumber() : "");
            dto.setEmpId(asset.getEmpId() != null ? asset.getEmpId() : "");
            dto.setStatus(asset.getStatus() != null ? asset.getStatus() : "");
            dto.setType(asset.getType() != null ? asset.getType() : "");
            dto.setPurchaseDate(asset.getPurchaseDate() != null ? asset.getPurchaseDate() : "");
            dto.setWarrantyDate(asset.getWarrantyDate() != null ? asset.getWarrantyDate() : "");
            dto.setLocation(asset.getLocation() != null ? asset.getLocation() : "");
            dto.setLocCode(asset.getLocCode());
            dto.setModelName(asset.getModelName() != null ? asset.getModelName() : "");
            dto.setOperatingSystem(asset.getOperatingSystem() != null ? asset.getOperatingSystem() : "");
            dto.setReturnDate(asset.getReturnDate());
            dto.setAddedBy(asset.getAddedBy() != null ? asset.getAddedBy() : "");
            dto.setAssignedDate(asset.getAssignedDate());
            dto.setAssignedBy(asset.getAssignedBy() != null ? asset.getAssignedBy() : "");
            dto.setAssertSourcedBy(asset.getAssertSourcedBy() != null ? asset.getAssertSourcedBy() : "");

            // Null check for employee
            if (employee != null) {
                dto.setEmpFirstName(employee.getFirstName() != null ? employee.getFirstName() : "Unknown");
                dto.setEmpLastName(employee.getLastName() != null ? employee.getLastName() : "");
            } else {
                // Handle the case where the employee is not found
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

        assignedAssetsEntitys.setAssetId(assignedAssetsEntity.getAssetId());

        return assignedAssetsRepository.save(assignedAssetsEntitys);
    }

    public AssignedAssetsEntity unAssignAsset(Integer assignedAssetId) {
        AssignedAssetsEntity assignedAssetsEntitys = new AssignedAssetsEntity();
        try {
            int assignedAssetsEntity = assignedAssetsRepository.updateUnassignStatus("UnAssigned", assignedAssetId);
            if (assignedAssetsEntity == 1) {
                assignedAssetsEntitys = assignedAssetsRepository.findByAssignedAssetsId(assignedAssetId);
                assetsRepo.updateUnassignStatus("UnAssigned", assignedAssetsEntitys.getSerialNumber());
            }
        } catch (Exception e) {
            System.out.println("Given id not found");
        }

        return assignedAssetsEntitys;
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

                    if (!asset.getStatus().equalsIgnoreCase("scrap")) {
                        AssignedAssetsEntity assignedAssetsEntity = new AssignedAssetsEntity();
                        assignedAssetsEntity.setAssetId(asset.getAssetId());
                        assignedAssetsEntity.setAssetName(asset.getAssetName());
                        assignedAssetsEntity.setEmpId(empId);
                        assignedAssetsEntity.setLocation(asset.getLocation());
                        assignedAssetsEntity.setModelName(asset.getModelName());
                        assignedAssetsEntity.setOperatingSystem(asset.getOperatingSystem());
                        assignedAssetsEntity.setPurchaseDate(asset.getPurchaseDate());
                        assignedAssetsEntity.setWarrantyDate(asset.getWarrantyDate());
                        assignedAssetsEntity.setAssignedBy(assignableAssetDto.getAssignedBy());
                        assignedAssetsEntity.setAssignedDate(assignableAssetDto.getAssignedDate());
                        assignedAssetsEntity.setStatus("Assigned");
                        assignedAssetsEntity.setType(asset.getType());
                        assignedAssetsEntity.setSerialNumber(asset.getSerialNumber());
                        asset.setStatus("Assigned");
                        asset.setAssignedDate(assignableAssetDto.getAssignedDate());
                        asset.setAssignedBy(assignableAssetDto.getAssignedBy());
                        asset.setEmpId(empId);
                        assignedAssetsEntity.setAssertSourcedBy(asset.getAssertSourcedBy());

                        for (CountOfAssets i : countOfAssets) {
                            if (asset.getLocation().equalsIgnoreCase(i.getLocation())) {
                                updateUnassignedCount(asset, i);
                                assetCountRepository.save(i);
                            }
                        }

                        assignedAssetsRepository.save(assignedAssetsEntity);
                        assetsRepo.save(asset); // Update asset status
                        List<AssetsEntity> assetsEntityList= assignableAssetDtos.stream().map((data)->{
                            AssetsEntity assetsEntity= new AssetsEntity();
                            assetsEntity.setEmpId(data.getEmpId());
                            assetsEntity.setAssetName(data.getAssetName());
                            assetsEntity.setSerialNumber(data.getSerialNumber());
                            assetsEntity.setAssignedBy(data.getAssignedBy());
                            assetsEntity.setAssignedDate(data.getAssignedDate());
                            return assetsEntity;
                        }) .collect(Collectors.toList());
                        ackMailer.sendAckMail(empId,assetsEntityList);
                    } else {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Asset was in Scrap");
                    }
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

    private void updateUnassignedCount(AssetsEntity asset, CountOfAssets i) {
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

    public List<AssignedAssetsEntity> getAllAssetsAssignedToParticularEmployee(String empId) {
        return assignedAssetsRepository.findByEmpId(empId);
    }

    @Override
    public Object save(AssignableAssetDto assignableAssetDto) {
        // TODO Auto-generated method stub
        return null;
    }
}