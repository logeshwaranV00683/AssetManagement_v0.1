package com.verinite.assetmanagementtool.service.serviceImpl;

import com.verinite.assetmanagementtool.dto.AssignableAssetDto;
import com.verinite.assetmanagementtool.dto.AssignedAssetDtoList;
import com.verinite.assetmanagementtool.entity.AssetsEntity;
import com.verinite.assetmanagementtool.entity.AssignedAssetsEntity;
import com.verinite.assetmanagementtool.entity.CountOfAssetsEntity;
import com.verinite.assetmanagementtool.entity.EmployeeEntity;
import com.verinite.assetmanagementtool.repository.*;
import com.verinite.assetmanagementtool.service.AssetsHistoryServices;
import com.verinite.assetmanagementtool.service.AssignedAssetsService;
import com.verinite.assetmanagementtool.service.mailservice.AckMailer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
            dto.setType(asset.getType() != null ? asset.getType() : "");

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
        for (AssignableAssetDto assignableAssetDto : assignableAssetDtos) {
            String empId = assignableAssetDto.getEmpId();
            EmployeeEntity employeeEntity = employeeRepository.findByEmpId(empId);
            if (employeeEntity == null) {
                ignoredAsset.put(assignableAssetDto.getSerialNumber(), "Employee Not found: " + assignableAssetDto.getEmpId());
                continue;
            }
            AssetsEntity asset = assetsRepo.findBySerialNumber(assignableAssetDto.getSerialNumber());
            if (asset == null) {
                ignoredAsset.put(assignableAssetDto.getSerialNumber(), "Asset Not found");
                continue;
            }
            if (asset.getPurchaseDate().isAfter(assignableAssetDto.getAssignedDate())) {
                ignoredAsset.put(assignableAssetDto.getSerialNumber(), "Asset Assigned Date is not valid");
                continue;
            }
            if (!adminRepo.existsByEmpId(assignableAssetDto.getAssignedBy())) {
                ignoredAsset.put(assignableAssetDto.getSerialNumber(), "Admin Id is not valid");
                continue;
            }
            if (assignableAssetDto.getAssignedDate().isAfter(LocalDate.now())) {
                ignoredAsset.put(assignableAssetDto.getSerialNumber(), "AssignedDate should not be in the future date");
                continue;
            }
            if (asset.getStatus().equalsIgnoreCase("UnAssigned") && employeeEntity.getStatus().equalsIgnoreCase("Active")) {
                asset.setEmpId(empId);
                asset.setStatus("Assigned");
                asset.setAssignedDate(assignableAssetDto.getAssignedDate());
                asset.setAssignedBy(assignableAssetDto.getAssignedBy());
                AssignedAssetsEntity assignedAssetsEntity = getAssignedAssetsEntity(assignableAssetDto, asset);
                newAssetCount(asset.getType(), asset.getLocation());
                assignedAssetsRepository.save(assignedAssetsEntity);
                assetsRepo.save(asset);
                assetsEntityList.add(asset);
                assetsHistoryServices.saveHistory(assignedAssetsEntity);
            } else if (asset.getStatus().equalsIgnoreCase("Scrap"))
                ignoredAsset.put(assignableAssetDto.getSerialNumber(), "Asset was in Scrap");
            else if (employeeEntity.getStatus().equalsIgnoreCase("InActive"))
                ignoredAsset.put(assignableAssetDto.getSerialNumber(), "Employee Was In InActive");
            else
                ignoredAsset.put(assignableAssetDto.getSerialNumber(), "Asset Already In Assigned");
        }
        sendAcknowledgementMails(assetsEntityList);
        if (ignoredAsset.isEmpty()) {
            return ResponseEntity.ok("Given Assets are Assigned");
        }
        return new ResponseEntity<>(ignoredAsset, HttpStatus.NOT_ACCEPTABLE);
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
        assignedAssetsEntity.setType(asset.getType());
        return assignedAssetsEntity;
    }

    @Override
    @Async
    public List<Map<String, Object>> getRecentAssignedAssets() {
        List<AssignedAssetsEntity> recentAssigned = assignedAssetsRepository.findByEmpIdNotNull()
                .stream()
                .filter(x -> {
                    LocalDate assignedDate = x.getAssignedDate();
                    return assignedDate != null &&
                            assignedDate.isEqual(LocalDate.now());
                })
                .toList();
        return recentAssigned.stream().map(assigned -> {
            EmployeeEntity employee = employeeRepository.findByEmpId(assigned.getEmpId());

            Map<String, Object> map = new HashMap<>();
            map.put("assetId", assigned.getAssignedAssetsId());
            map.put("serialNumber", assigned.getSerialNumber());
            map.put("assignedDate", assigned.getAssignedDate());
            map.put("empId", employee.getEmpId());
            map.put("firstName", employee.getFirstName());
            map.put("lastName", employee.getLastName());

            return map;
        }).toList();

    }


    @Override
    public String unAssignAsset(List<String> serialNumbers) {
        List<String> processedSerials = new ArrayList<>();
        for (String serialNo : serialNumbers) {
            AssignedAssetsEntity assignedAsset = assignedAssetsRepository.findBySerialNumber(serialNo);
            if (assignedAsset == null) {
                continue;
            }

            deleteAssignedAssets(assignedAsset.getAssignedAssetsId());
            assetsRepo.updateUnassignStatus("UnAssigned", null, null, null, List.of(serialNo));

            AssetsEntity asset = assetsRepo.findBySerialNumber(serialNo);
            if (asset != null) {
                unAssignedCountImport(asset.getType(), asset.getLocation());
                assetsHistoryService.updateHistory(assignedAsset, serialNo);
                processedSerials.add(serialNo);
            }
        }

        if (!processedSerials.isEmpty()) {
            return "Assets unassigned for serial numbers: " + processedSerials;
        } else {
            return "No assets were unassigned.";
        }
    }


    public ResponseEntity<?> getAllAssetsAssignedToParticularEmployee(String empId) {
        List<AssignedAssetsEntity> assignedAssetsEntities = assignedAssetsRepository.findByEmpId(empId);
        if (assignedAssetsEntities.isEmpty()) {
            String message = "No assigned assets found for employee ID : " + empId;
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(assignedAssetsEntities);
    }

    public void newAssetCount(String assetType, String location) {
        if (assetType == null || location == null) return;

        Optional<CountOfAssetsEntity> optionalEntity = assetCountRepository
                .findByLocationIgnoreCaseAndTypeIgnoreCase(location.trim(), assetType.trim());

        CountOfAssetsEntity entity = optionalEntity.orElseGet(() -> {
            CountOfAssetsEntity newEntity = new CountOfAssetsEntity();
            newEntity.setLocation(location.trim());
            newEntity.setType(assetType.trim());
            newEntity.setTotal(0);
            newEntity.setAssigned(0);
            newEntity.setUnassigned(0);
            newEntity.setScrapped(0);
            return newEntity;
        });
        entity.setAssigned((entity.getAssigned() != null ? entity.getAssigned() : 0) + 1);
        entity.setUnassigned((entity.getUnassigned() != null ? entity.getUnassigned() : 0) - 1);
        assetCountRepository.save(entity);
    }


    public void unAssignedCountImport(String assetType, String location) {
        if (assetType == null || location == null) return;

        Optional<CountOfAssetsEntity> optionalEntity = assetCountRepository
                .findByLocationIgnoreCaseAndTypeIgnoreCase(location.trim(), assetType.trim());

        CountOfAssetsEntity entity = optionalEntity.orElseGet(() -> {
            CountOfAssetsEntity newEntity = new CountOfAssetsEntity();
            newEntity.setLocation(location.trim());
            newEntity.setType(assetType.trim());
            newEntity.setTotal(1);
            newEntity.setAssigned(0);
            newEntity.setUnassigned(1);
            newEntity.setScrapped(0);
            return newEntity;
        });

        entity.setUnassigned((entity.getUnassigned() != null ? entity.getUnassigned() : 0) + 1);
        entity.setAssigned((entity.getAssigned() != null ? entity.getAssigned() : 0) - 1);
        assetCountRepository.save(entity);
    }


    public void assignedCountImport(String assetType, String location) {
        if (assetType == null || location == null) return;

        Optional<CountOfAssetsEntity> optionalEntity = assetCountRepository
                .findByLocationIgnoreCaseAndTypeIgnoreCase(location.trim(), assetType.trim());

        CountOfAssetsEntity entity = optionalEntity.orElseGet(() -> {
            CountOfAssetsEntity newEntity = new CountOfAssetsEntity();
            newEntity.setLocation(location.trim());
            newEntity.setType(assetType.trim());
            newEntity.setTotal(0);
            newEntity.setAssigned(0);
            newEntity.setUnassigned(0);
            newEntity.setScrapped(0);
            return newEntity;
        });

        entity.setAssigned((entity.getAssigned() != null ? entity.getAssigned() : 0) + 1);
        entity.setTotal((entity.getTotal() != null ? entity.getTotal() : 0) + 1);

        assetCountRepository.save(entity);
    }
}