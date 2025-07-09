
//package com.verinite.assetmangementtool.service;
//
//import com.verinite.assetmangementtool.dto.AssignableAssetDto;
//import com.verinite.assetmangementtool.entity.*;
//import com.verinite.assetmangementtool.repository.*;
//import com.verinite.assetmangementtool.service.mailservice.AckMailer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//import javax.mail.MessagingException;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class EmailAssetService {
//
//    @Autowired
//    private AssetsRepository assetsRepo;
//
//    @Autowired
//    private AssignedAssetsRepository assignedAssetsRepository;
//
//    @Autowired
//    private EmployeeRepository employeeRepository;
//
//    @Autowired
//    private AssetCountRepository assetCountRepository;
//
//    @Autowired
//    private AssetsHistoryRepository assetsHistoryRepository;
//
//    @Autowired
//    private JavaMailSender mailSender;
//
//    @Autowired
//    private AckMailer ackMailer;
//
//    private void updateUnassignedAssetCounts(AssetsEntity asset, CountOfAssetsRepository countOfAssets) {
//        String assetName = asset.getAssetName();
//
//        switch (assetName.toLowerCase()) {
//            case "laptop":
//                countOfAssets.setUnAssignedLaptopCount(countOfAssets.getUnAssignedLaptopCount() - 1);
//                break;
//            case "mouse":
//                countOfAssets.setUnAssignedMouseCount(countOfAssets.getUnAssignedMouseCount() - 1);
//                break;
//            case "laptopcharger":
//                countOfAssets.setUnAssignedLaptopChargerCount(countOfAssets.getUnAssignedLaptopChargerCount() - 1);
//                break;
//            case "headphone":
//                countOfAssets.setUnAssignedHeadphonesCount(countOfAssets.getUnAssignedHeadphonesCount() - 1);
//                break;
//            case "bag":
//                countOfAssets.setUnAssignedBagCount(countOfAssets.getUnAssignedBagCount() - 1);
//                break;
//            case "datacard":
//                countOfAssets.setUnAssignedDataCardCount(countOfAssets.getUnAssignedDataCardCount() - 1);
//                break;
//            case "mobile":
//                countOfAssets.setUnAssignedMobileCount(countOfAssets.getUnAssignedMobileCount() - 1);
//                break;
//            case "camera":
//                countOfAssets.setUnAssignedCameraCount(countOfAssets.getUnAssignedCameraCount() - 1);
//                break;
//            case "projector":
//                countOfAssets.setUnAssignedProjectorCount(countOfAssets.getUnAssignedProjectorCount() - 1);
//                break;
//            case "firewall":
//                countOfAssets.setUnAssignedFireWallCount(countOfAssets.getUnAssignedFireWallCount() - 1);
//                break;
//            case "switch":
//                countOfAssets.setUnAssignedSwitchCount(countOfAssets.getUnAssignedSwitchCount() - 1);
//                break;
//            case "dvr":
//                countOfAssets.setUnAssignedDvrCount(countOfAssets.getUnAssignedDvrCount() - 1);
//                break;
//            case "speaker":
//                countOfAssets.setUnAssignedSpeakerCount(countOfAssets.getUnAssignedSpeakerCount() - 1);
//                break;
//            default:
//                break;
//        }
//
//        assetCountRepository.save(countOfAssets);
//    }
//
//    private void saveHistory(AssetsEntity asset, AssignedAssetsEntity assignedAssetsEntity) {
//
//        AssetsHistoryEntity assetHistory = new AssetsHistoryEntity();
//        assetHistory.setAssetId(asset.getAssetId());
//        assetHistory.setAssetName(asset.getAssetName());
//        assetHistory.setSerialNumber(asset.getSerialNumber());
//        assetHistory.setEmpId(assignedAssetsEntity.getEmpId());
//        assetHistory.setStatus("Assigned");
//        assetHistory.setLocation(asset.getLocation());
//        assetHistory.setModelName(asset.getModelName());
//        assetHistory.setOperatingSystem(asset.getOperatingSystem());
//        assetHistory.setAssignedDate(assignedAssetsEntity.getAssignedDate());
//        assetHistory.setAssignedBy(assignedAssetsEntity.getAssignedBy());
//        assetHistory.setPurchaseDate(asset.getPurchaseDate());
//        assetHistory.setWarrantyDate(asset.getWarrantyDate());
//
//        // Save history to repository
//        assetsHistoryRepository.save(assetHistory);
//    }
//
//    public ResponseEntity<?> save(List<AssignableAssetDto> assignableAssetDtoList) {
//        String empId = assignableAssetDtoList.get(0).getEmpId(); // Assuming all assets are for the same employee
//        EmployeeEntity employeeEntity = employeeRepository.findByEmpId(empId);
//
//        if (employeeEntity != null) {
//            List<AssignedAssetsEntity> assignedAssetsEntities = new ArrayList<>();
//            List<String> errors = new ArrayList<>();
//
//            for (AssignableAssetDto assignableAssetDto : assignableAssetDtoList) {
//                AssetsEntity asset = assetsRepo.findBySerialNumber(assignableAssetDto.getSerialNumber());
//                AssignedAssetsEntity assignedAssetsEntity = new AssignedAssetsEntity();
//
//                try {
//                    if (!asset.getStatus().equalsIgnoreCase("scrap")) {
//                        assignedAssetsEntity.setAssetId(asset.getAssetId());
//                        assignedAssetsEntity.setAssetName(asset.getAssetName());
//                        assignedAssetsEntity.setEmpId(assignableAssetDto.getEmpId());
//                        assignedAssetsEntity.setLocation(asset.getLocation());
//                        assignedAssetsEntity.setModelName(asset.getModelName());
//                        assignedAssetsEntity.setOperatingSystem(asset.getOperatingSystem());
//                        assignedAssetsEntity.setPurchaseDate(asset.getPurchaseDate());
//                        assignedAssetsEntity.setWarrantyDate(asset.getWarrantyDate());
//                        assignedAssetsEntity.setAssignedBy(assignableAssetDto.getAssignedBy());
//                        assignedAssetsEntity.setAssignedDate(assignableAssetDto.getAssignedDate());
//                        assignedAssetsEntity.setStatus("Assigned");
//                        assignedAssetsEntity.setType(asset.getType());
//                        assignedAssetsEntity.setSerialNumber(asset.getSerialNumber());
//
//                        asset.setStatus("Assigned");
//                        asset.setAssignedDate(assignableAssetDto.getAssignedDate());
//                        asset.setAssignedBy(assignableAssetDto.getAssignedBy());
//                        asset.setEmpId(assignableAssetDto.getEmpId());
//
//                        // Update unassigned asset counts
//                        List<CountOfAssetsRepository> countOfAssets = assetCountRepository.findAll();
//                        for (CountOfAssetsRepository count : countOfAssets) {
//                            if (asset.getLocation().equalsIgnoreCase(count.getLocation())) {
//                                updateUnassignedAssetCounts(asset, count);
//                            }
//                        }
//
//                        assignedAssetsRepository.save(assignedAssetsEntity);
//                        saveHistory(asset, assignedAssetsEntity);
//                        assignedAssetsEntities.add(assignedAssetsEntity);
//                    } else {
//                        errors.add("Asset with serial number " + asset.getSerialNumber() + " is in Scrap.");
//                    }
//                } catch (Exception e) {
//                    errors.add("Error processing asset with serial number: " + assignableAssetDto.getSerialNumber());
//                }
//            }
//
//            if (!assignedAssetsEntities.isEmpty()) {
//                try {
//                    return ResponseEntity.ok("Assets assigned successfully");
//                } catch (MessagingException e) {
//                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending email");
//                }
//            } else {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                        .body("No assets assigned. " + String.join(", ", errors));
//            }
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
//        }
//    }

//
//}

