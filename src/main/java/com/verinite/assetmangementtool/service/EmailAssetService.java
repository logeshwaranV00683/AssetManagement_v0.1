package com.verinite.assetmangementtool.service;

import com.verinite.assetmangementtool.dto.AssignableAssetDto;
import com.verinite.assetmangementtool.entity.*;
import com.verinite.assetmangementtool.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmailAssetService {

    @Autowired
    private AssetsRepository assetsRepo;

    @Autowired
    private AssignedAssetsRepository assignedAssetsRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AssetCountRepository assetCountRepository;

    @Autowired
    private AssetsHistoryRepository assetsHistoryRepository;

    @Autowired
    private JavaMailSender mailSender;

    private void updateUnassignedAssetCounts(AssetsEntity asset, CountOfAssets countOfAssets) {
        String assetName = asset.getAssetName();

        switch (assetName.toLowerCase()) {
            case "laptop":
                countOfAssets.setUnAssignedLaptopCount(countOfAssets.getUnAssignedLaptopCount() - 1);
                break;
            case "mouse":
                countOfAssets.setUnAssignedMouseCount(countOfAssets.getUnAssignedMouseCount() - 1);
                break;
            case "laptopcharger":
                countOfAssets.setUnAssignedLaptopChargerCount(countOfAssets.getUnAssignedLaptopChargerCount() - 1);
                break;
            case "headphone":
                countOfAssets.setUnAssignedHeadphonesCount(countOfAssets.getUnAssignedHeadphonesCount() - 1);
                break;
            case "bag":
                countOfAssets.setUnAssignedBagCount(countOfAssets.getUnAssignedBagCount() - 1);
                break;
            case "datacard":
                countOfAssets.setUnAssignedDataCardCount(countOfAssets.getUnAssignedDataCardCount() - 1);
                break;
            case "mobile":
                countOfAssets.setUnAssignedMobileCount(countOfAssets.getUnAssignedMobileCount() - 1);
                break;
            case "camera":
                countOfAssets.setUnAssignedCameraCount(countOfAssets.getUnAssignedCameraCount() - 1);
                break;
            case "projector":
                countOfAssets.setUnAssignedProjectorCount(countOfAssets.getUnAssignedProjectorCount() - 1);
                break;
            case "firewall":
                countOfAssets.setUnAssignedFireWallCount(countOfAssets.getUnAssignedFireWallCount() - 1);
                break;
            case "switch":
                countOfAssets.setUnAssignedSwitchCount(countOfAssets.getUnAssignedSwitchCount() - 1);
                break;
            case "dvr":
                countOfAssets.setUnAssignedDvrCount(countOfAssets.getUnAssignedDvrCount() - 1);
                break;
            case "speaker":
                countOfAssets.setUnAssignedSpeakerCount(countOfAssets.getUnAssignedSpeakerCount() - 1);
                break;
            default:
                break;
        }

        assetCountRepository.save(countOfAssets);
    }

    private void saveHistory(AssetsEntity asset, AssignedAssetsEntity assignedAssetsEntity) {

        AssetsHistoryEntity assetHistory = new AssetsHistoryEntity();
        assetHistory.setSerialNumber(asset.getSerialNumber());
        assetHistory.setEmpId(assignedAssetsEntity.getEmpId());
        assetHistory.setAssignedDate(assignedAssetsEntity.getAssignedDate());
        assetHistory.setAssignedBy(assignedAssetsEntity.getAssignedBy());

        // Save history to repository
        assetsHistoryRepository.save(assetHistory);
    }

    public ResponseEntity<?> save(List<AssignableAssetDto> assignableAssetDtoList) {
        String empId = assignableAssetDtoList.get(0).getEmpId(); // Assuming all assets are for the same employee
        EmployeeEntity employeeEntity = employeeRepository.findByEmpId(empId);

        if (employeeEntity != null) {
            List<AssignedAssetsEntity> assignedAssetsEntities = new ArrayList<>();
            List<String> errors = new ArrayList<>();

            for (AssignableAssetDto assignableAssetDto : assignableAssetDtoList) {
                AssetsEntity asset = assetsRepo.findBySerialNumber(assignableAssetDto.getSerialNumber());
                AssignedAssetsEntity assignedAssetsEntity = new AssignedAssetsEntity();

                try {
                    if (!asset.getStatus().equalsIgnoreCase("scrap")) {
                        assignedAssetsEntity.setAssetId(asset.getAssetId());
                        assignedAssetsEntity.setAssetName(asset.getAssetName());
                        assignedAssetsEntity.setEmpId(assignableAssetDto.getEmpId());
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
                        asset.setEmpId(assignableAssetDto.getEmpId());

                        // Update unassigned asset counts
                        List<CountOfAssets> countOfAssets = assetCountRepository.findAll();
                        for (CountOfAssets count : countOfAssets) {
                            if (asset.getLocation().equalsIgnoreCase(count.getLocation())) {
                                updateUnassignedAssetCounts(asset, count);
                            }
                        }

                        assignedAssetsRepository.save(assignedAssetsEntity);
                        saveHistory(asset, assignedAssetsEntity);
                        assignedAssetsEntities.add(assignedAssetsEntity);
                    } else {
                        errors.add("Asset with serial number " + asset.getSerialNumber() + " is in Scrap.");
                    }
                } catch (Exception e) {
                    errors.add("Error processing asset with serial number: " + assignableAssetDto.getSerialNumber());
                }
            }

            if (!assignedAssetsEntities.isEmpty()) {
                try {
                    sendAssetAssignedEmail(employeeEntity, assignedAssetsEntities);
                    return ResponseEntity.ok("Assets assigned successfully");
                } catch (MessagingException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending email");
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("No assets assigned. " + String.join(", ", errors));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
        }
    }

    private void sendAssetAssignedEmail(EmployeeEntity employeeEntity,
                                        List<AssignedAssetsEntity> assignedAssetsEntities) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(employeeEntity.getMail());
        helper.setSubject("Assets Assigned ( This mail for testing)");

        StringBuilder emailContent = new StringBuilder("<p>Dear " + employeeEntity.getFirstName() + ",</p>");
        emailContent.append("<p>You have been assigned the following assets:</p>");

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

        for (AssignedAssetsEntity assignedAssetsEntity : assignedAssetsEntities) {
            String formattedAssignedDate = dateFormat.format(assignedAssetsEntity.getAssignedDate());

            emailContent.append(
                            "<div style='border: 1px solid #dddddd; padding: 10px; border-radius: 5px; background-color: #f9f9f9;'>")
                    .append("<table style='width: 100%; border-collapse: collapse;'>")
                    .append("<tr><td style='padding: 8px; border: 1px solid #dddddd;'><b>Asset Name:</b></td>")
                    .append("<td style='padding: 8px; border: 1px solid #dddddd;'>")
                    .append(assignedAssetsEntity.getAssetName()).append("</td></tr>")
                    .append("<tr><td style='padding: 8px; border: 1px solid #dddddd;'><b>Serial Number:</b></td>")
                    .append("<td style='padding: 8px; border: 1px solid #dddddd;'>")
                    .append(assignedAssetsEntity.getSerialNumber()).append("</td></tr>")
                    .append("<tr><td style='padding: 8px; border: 1px solid #dddddd;'><b>Model:</b></td>")
                    .append("<td style='padding: 8px; border: 1px solid #dddddd;'>")
                    .append(assignedAssetsEntity.getModelName()).append("</td></tr>")
                    .append("<tr><td style='padding: 8px; border: 1px solid #dddddd;'><b>Location:</b></td>")
                    .append("<td style='padding: 8px; border: 1px solid #dddddd;'>")
                    .append(assignedAssetsEntity.getLocation()).append("</td></tr>")
                    .append("<tr><td style='padding: 8px; border: 1px solid #dddddd;'><b>Assigned Date:</b></td>")
                    .append("<td style='padding: 8px; border: 1px solid #dddddd;'>").append(formattedAssignedDate)
                    .append("</td></tr>").append("</table></div>");
        }

        emailContent.append(
                        "<p>If you encounter any issues with the assets, please contact the IT department immediately.</p>")
                .append("<p>Best regards,</p>").append("<p>IT Support Team</p>");

        helper.setText(emailContent.toString(), true);
        mailSender.send(mimeMessage);
    }

}
