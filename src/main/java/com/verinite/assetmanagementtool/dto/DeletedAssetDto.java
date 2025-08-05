package com.verinite.assetmanagementtool.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeletedAssetDto {

    private String serialNo;
    private String assetName;
    private LocalDate purchaseDate;
    private LocalDate deletedDate;
    private String type;
    private String location;
    private String assetSourcedBy;
    private String deletedBy;

    @Override
    public String toString() {
        return "DeletedAssetDto{" +
                "serialNo='" + serialNo + '\'' +
                ", assetName='" + assetName + '\'' +
                ", purchaseDate=" + purchaseDate +
                ", deletedDate=" + deletedDate +
                ", type='" + type + '\'' +
                ", location='" + location + '\'' +
                ", assetSourcedBy='" + assetSourcedBy + '\'' +
                ", deletedBy='" + deletedBy + '\'' +
                '}';
    }
}
