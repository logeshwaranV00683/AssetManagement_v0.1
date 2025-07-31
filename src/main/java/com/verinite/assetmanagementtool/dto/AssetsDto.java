package com.verinite.assetmanagementtool.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetsDto {

    private int assetId;
    @NotBlank
    @Pattern(regexp = "^[\\w ]+$", message = "Must be alphanumeric, underscores, and spaces only")
    private String assetName;
    @NotBlank
    @Pattern(regexp = "^(\\w+)?$", message = "Must be a Alphanumeric & _")
    private String serialNumber;
    @Pattern(regexp = "^(V\\d{5})?$", message = "Must be V followed by 5 digits or empty")
    private String empId;
    @NotBlank
    private String status;
    @NotBlank
    private String type;
    @NotNull(message = "Purchase Date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate purchaseDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Warranty Date is required")
    private LocalDate warrantyDate;
    @NotBlank
    @Pattern(regexp = "^(?i)[a-z]+(?:[ '-][a-z]+)*$", message = "Location must contain only letters, spaces, or hyphens")
    private String location;
    @NotNull
    @Pattern(regexp = "^(\\w+)?$", message = "ModelName must be alphanumeric & _")
    private String modelName;
    @NotBlank
    @Pattern(regexp = "^(\\w+)?$", message = "operatingSystem must be alphanumeric & _")
    private String operatingSystem;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate returnDate;
    @NotBlank
    @Pattern(regexp = "^(V\\d{5})?$", message = "Must be V followed by 5 digits or empty")
    private String addedBy;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate assignedDate;
    @Pattern(regexp = "^(V\\d{5})?$", message = "Must be V followed by 5 digits or empty")
    private String assignedBy;
    @NotBlank
    private String assetSourcedBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssetsDto e)) return false;
        if (this.serialNumber != null && e.serialNumber != null && this.serialNumber.equals(e.serialNumber) && this.assetId != 0 && e.assetId != 0) {
            return this.assetId == (e.assetId);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return serialNumber != null ? serialNumber.hashCode() + assetId : 0;
    }

    @Override
    public String toString() {
        return "AssetsDto{" +
                "assetId=" + assetId +
                ", assetName='" + assetName + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", empId='" + empId + '\'' +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", purchaseDate='" + purchaseDate + '\'' +
                ", warrantyDate='" + warrantyDate + '\'' +
                ", location='" + location + '\'' +
                ", modelName='" + modelName + '\'' +
                ", operatingSystem='" + operatingSystem + '\'' +
                ", returnDate=" + returnDate +
                ", addedBy='" + addedBy + '\'' +
                ", assignedDate=" + assignedDate +
                ", assignedBy='" + assignedBy + '\'' +
                ", assetSourcedBy='" + assetSourcedBy + '\'' +
                '}';
    }
}