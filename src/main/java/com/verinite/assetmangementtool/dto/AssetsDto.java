package com.verinite.assetmangementtool.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

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
    private String assetName;
    @NotBlank
    private String serialNumber;
    @Pattern(regexp = "^(V\\d{5})?$", message = "Must be V followed by 5 digits or empty")
    private String empId;
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
    private String location;
    @NotNull
    private Integer locCode;
    @NotBlank
    private String modelName;
    private String operatingSystem;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate returnDate;
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
            return this.assetId==(e.assetId);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return serialNumber != null ? serialNumber.hashCode()+ assetId : 0;
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
                ", locCode=" + locCode +
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